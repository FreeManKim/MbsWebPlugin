/**
 * Copyright (c) 2013, The Android Open Source Project
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.mobisoft.mbswebplugin.proxy.server;


import android.content.Context;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;

import com.mobisoft.mbswebplugin.IProxyPortListener;
import com.mobisoft.mbswebplugin.proxy.tool.FileCache;
import com.mobisoft.mbswebplugin.proxy.DB.WebviewCaheDao;
import com.mobisoft.mbswebplugin.proxy.Setting.ProxyConfig;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.ProxySelector;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 */
public class ProxyServer extends Thread {

    private static final String CONNECT = "CONNECT";
    private static final String HTTP_OK = "HTTP/1.1 200 OK\n";

    private static final String TAG = "ProxyServer";

    // HTTP Headers
    private static final String HEADER_CONNECTION = "connection";
    private static final String HEADER_PROXY_CONNECTION = "proxy-connection";
    public static final int TIMEOUT = 3 * 60 * 1000;
    /**
     * 缓存路径
     */
    private final String cacheDir;
    /**
     *
     */
    private final Context mContext;
    /**
     * 数据库
     */
    private final WebviewCaheDao DBdao;

    private ExecutorService threadExecutor;

    public boolean mIsRunning = false;

    private ServerSocket serverSocket;
    private int mPort;
    private IProxyPortListener mCallback;

    private class ProxyConnection implements Runnable {
        private Socket connection;

        private ProxyConnection(Socket connection) {
            this.connection = connection;
        }

        @Override
        public void run() {
            try {
                String requestLine = getLine(connection.getInputStream());
                String[] splitLine = requestLine.split(" ");
                if (splitLine.length < 3) {
                    connection.close();
                    return;
                }
                Log.i(TAG, " -> REQUEST: " + requestLine);

                String requestType = splitLine[0];
                String urlString = splitLine[1];

                String httpVersion = splitLine[2];

                if (getCacheSrc(urlString)) {
                    return;
                }
                URI url = null;
                String host;
                int port;

                if (requestType.equals(CONNECT)) {
                    String[] hostPortSplit = urlString.split(":");
                    host = hostPortSplit[0];
                    // Use default SSL port if not specified. Parse it otherwise
                    if (hostPortSplit.length < 2) {
                        port = 443;
                    } else {
                        try {
                            port = Integer.parseInt(hostPortSplit[1]);
                        } catch (NumberFormatException nfe) {
                            connection.close();
                            return;
                        }
                    }
                    urlString = "Https://" + host + ":" + port;
                    Log.i(TAG, " -> urlString: " + urlString);
                } else {
                    try {
                        url = new URI(urlString);
                        host = url.getHost();
                        port = url.getPort();
                        if (port < 0) {
                            port = 80;
                        }
                    } catch (URISyntaxException e) {
                        connection.close();
                        return;
                    }
                }


//                Log.v(TAG, " -> urlString: " + urlString);
                List<Proxy> list = new ArrayList<>();
                try {
                    list = ProxySelector.getDefault().select(new URI(urlString));
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
//                Log.v(TAG, " -> list.size(): " + list.size());
                Socket server = null;
                for (Proxy proxy : list) {
                    try {
                        boolean forward = false;
                        if (!proxy.equals(Proxy.NO_PROXY)) {
                            // Only Inets created by PacProxySelector.
                            InetSocketAddress inetSocketAddress =
                                    (InetSocketAddress) proxy.address();
                            server = new Socket(inetSocketAddress.getHostName(),
                                    inetSocketAddress.getPort());

//                            Log.v(TAG, " -> proxy.toString(): " + proxy.toString());
                            if ("127.0.0.1".equals(InetAddress.getByName(
                                    inetSocketAddress.getHostName()).getHostAddress()) != true ||
                                    inetSocketAddress.getPort() != ProxyService.PORT) {
//                                Log.v(TAG, " -> proxy.toString(): " + "本地");

                                server = new Socket(inetSocketAddress.getHostName(),
                                        inetSocketAddress.getPort());
                                sendLine(server, requestLine);
                                forward = true;
                            }
                        }
                        if (forward != true) {
                            server = new Socket(host, port);
                            if (requestType.equals(CONNECT)) {
                                Log.v(TAG, " -> CONNECT: " + host + ":" + port);
                                skipToRequestBody(connection);
                                // No proxy to respond so we must.
                                sendLine(connection, HTTP_OK);
                            } else {
                                // Proxying the request directly to the origin server.
                                Log.v(TAG, " -> DIRECT: " + host + ":" + port);
                                sendAugmentedRequestToHost(connection, server,
                                        requestType, url, httpVersion);
                            }
                        }
                    } catch (IOException ioe) {
                        if (Log.isLoggable(TAG, Log.VERBOSE)) {
                            Log.v(TAG, "Unable to connect to proxy " + proxy, ioe);
                        }
                    }
                    if (server != null) {
                        break;
                    }
                }
                if (list.isEmpty()) {
                    server = new Socket(host, port);
                    if (requestType.equals(CONNECT)) {
                        Log.v(TAG, " -> CONNECT:2  " + host + ":" + port);

                        skipToRequestBody(connection);
                        // No proxy to respond so we must.
                        sendLine(connection, HTTP_OK);
                    } else {
                        Log.v(TAG, " -> DIRECT:2 " + host + ":" + port);

                        // Proxying the request directly to the origin server.
                        sendAugmentedRequestToHost(connection, server,
                                requestType, url, httpVersion);
                    }
                }

                // Pass data back and forth until complete.
                if (server != null) {
                    SocketConnect.connect(connection, server, urlString, cacheDir, mContext, DBdao);
                }


            } catch (Exception e) {
                Log.d(TAG, "Problem Proxying", e);
                e.printStackTrace();
            }
            try {
                connection.close();
            } catch (IOException ioe) {
                // Do nothing
                ioe.printStackTrace();
            }
        }

        /**
         * 获取 缓存资源
         *
         * @param url
         * @return
         */
        private synchronized boolean getCacheSrc(String url) {
            String urlString = "";
            if (url.startsWith("http:") && ProxyConfig.getConfig().isChangeHttps()) {
                urlString = url.replace("http:", "https:");
            } else {
                urlString = url;
            }
            String catchPath = DBdao.getUrlPath(urlString);
            try {
                if (!TextUtils.isEmpty(catchPath) && !catchPath.endsWith("cache.manifest")) {
                    InputStream inputStream1 = connection.getInputStream();
                    String ling = getLine(inputStream1);
                    connection.shutdownInput();
//                    inputStream1.close();
                    Log.i(TAG, " -> catchPath: " + catchPath + "-->" + ling);
//                    connection.setSoTimeout(10 * 1000);
                    // 缓存测试
                    InputStream inputStream = FileCache.getInstance().getCache(urlString, catchPath, mContext);
                    if (inputStream != null) {
                        OutputStream out = connection.getOutputStream();
                        final byte[] buffer = new byte[512];
                        while (true) {
                            int r = inputStream.read(buffer);

                            if (r < 0) {
                                break;
                            }
                            if (out != null) {
                                out.write(buffer, 0, r);
                                out.flush();
                            }
                        }

                        connection.shutdownOutput();
//                        out.close();
                        connection.close();
                        inputStream.close();
                        Log.w(TAG, " -> catchPath: //  " + catchPath + "/-close--/ ");

                        return true;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        }

        /**
         * Sends HTTP request-line (i.e. the first line in the request)
         * that contains absolute path of a given absolute URI.
         *
         * @param server      server to send the request to.
         * @param requestType type of the request, a.k.a. HTTP method.
         * @param absoluteUri absolute URI which absolute path should be extracted.
         * @param httpVersion version of HTTP, e.g. HTTP/1.1.
         * @throws IOException if the request-line cannot be sent.
         */
        private void sendRequestLineWithPath(Socket server, String requestType,
                                             URI absoluteUri, String httpVersion) throws IOException {

            String absolutePath = getAbsolutePathFromAbsoluteURI(absoluteUri);
            String outgoingRequestLine = String.format("%s %s %s",
                    requestType, absolutePath, httpVersion);
            sendLine(server, outgoingRequestLine);
        }

        /**
         * Extracts absolute path form a given URI. E.g., passing
         * <code>http://google.com:80/execute?query=cat#top</code>
         * will result in <code>/execute?query=cat#top</code>.
         *
         * @param uri URI which absolute path has to be extracted,
         * @return the absolute path of the URI,
         */
        private String getAbsolutePathFromAbsoluteURI(URI uri) {
            String rawPath = uri.getRawPath();
            String rawQuery = uri.getRawQuery();
            String rawFragment = uri.getRawFragment();
            StringBuilder absolutePath = new StringBuilder();

            if (rawPath != null) {
                absolutePath.append(rawPath);
            } else {
                absolutePath.append("/");
            }
            if (rawQuery != null) {
                absolutePath.append("?").append(rawQuery);
            }
            if (rawFragment != null) {
                absolutePath.append("#").append(rawFragment);
            }
            Log.i(TAG, "absolutePath:" + absolutePath.toString());

            return absolutePath.toString();
        }

        private String getLine(InputStream inputStream) throws IOException {
            StringBuilder buffer = new StringBuilder();
            int byteBuffer = inputStream.read();
            if (byteBuffer < 0) return "";
            do {
                if (byteBuffer != '\r') {
                    buffer.append((char) byteBuffer);
                }
                byteBuffer = inputStream.read();
            } while ((byteBuffer != '\n') && (byteBuffer >= 0));
//            Log.v(TAG, " -> getLine: " + buffer.toString());

            return buffer.toString();
        }

        private void sendLine(Socket socket, String line) throws IOException {
            OutputStream os = socket.getOutputStream();
            os.write(line.getBytes());
            os.write('\r');
            os.write('\n');
            os.flush();
//            Log.v(TAG, " -> sendLine: " + line);
//            Log.v(TAG, " -> getAddress: " + socket.getInetAddress().getHostAddress());
        }


        /**
         * Reads from socket until an empty line is read which indicates the end of HTTP headers.
         *
         * @param socket socket to read from.
         * @throws IOException if an exception took place during the socket read.
         */
        private void skipToRequestBody(Socket socket) throws IOException {
            while (getLine(socket.getInputStream()).length() != 0) ;
        }

        /**
         * Sends an augmented request to the final host (DIRECT connection).
         *
         * @param src         socket to read HTTP headers from.The socket current position should point
         *                    to the beginning of the HTTP header section.
         * @param dst         socket to write the augmented request to.
         * @param httpMethod  original request http method.
         * @param uri         original request absolute URI.
         * @param httpVersion original request http version.
         * @throws IOException if an exception took place during socket reads or writes.
         */
        private void sendAugmentedRequestToHost(Socket src, Socket dst,
                                                String httpMethod, URI uri, String httpVersion) throws IOException {
//            Log.v(TAG, " -> sendAugmentedRequestToHost: " + httpMethod + ":" + uri.toString() + ":  " + httpVersion);
            sendRequestLineWithPath(dst, httpMethod, uri, httpVersion);
            filterAndForwardRequestHeaders(src, dst);

            // Currently the proxy does not support keep-alive connections; therefore,
            // the proxy has to request the destination server to close the connection
            // after the destination server sent the response.
            sendLine(dst, "Connection: close");

            // Sends and empty line that indicates termination of the header section.
            sendLine(dst, "");
        }

        /**
         * Forwards original request headers filtering out the ones that have to be removed.
         *
         * @param src source socket that contains original request headers.
         * @param dst destination socket to send the filtered headers to.
         * @throws IOException if the data cannot be read from or written to the sockets.
         */
        private void filterAndForwardRequestHeaders(Socket src, Socket dst) throws IOException {
            String line;
            do {
                line = getLine(src.getInputStream());
                if (line.length() > 0 && !shouldRemoveHeaderLine(line)) {
                    sendLine(dst, line);
//                    Log.e(TAG,"Headers:"+line);
                }
            } while (line.length() > 0);
        }

        /**
         * Returns true if a given header line has to be removed from the original request.
         *
         * @param line header line that should be analysed.
         * @return true if the header line should be removed and not forwarded to the destination.
         */
        private boolean shouldRemoveHeaderLine(String line) {
            int colIndex = line.indexOf(":");
            if (colIndex != -1) {
                String headerName = line.substring(0, colIndex).trim();
                if (headerName.regionMatches(true, 0, HEADER_CONNECTION, 0,
                        HEADER_CONNECTION.length())
                        || headerName.regionMatches(true, 0, HEADER_PROXY_CONNECTION,
                        0, HEADER_PROXY_CONNECTION.length())) {
                    return true;
                }
            }
            return false;
        }
    }

    /***
     * @param cacheDir 缓存路径
     * @param context
     * @param dao
     */
    public ProxyServer(String cacheDir, Context context, WebviewCaheDao dao) {
        threadExecutor = Executors.newCachedThreadPool();
        mPort = -1;
        mCallback = null;
        this.cacheDir = cacheDir;
        this.mContext = context;
        this.DBdao = dao;
    }

    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(ProxyService.PORT);
//            serverSocket.setSoTimeout(TIMEOUT);
            setPort(serverSocket.getLocalPort());

            while (mIsRunning) {
                try {
                    Socket socket = serverSocket.accept();
                    socket.setSoTimeout(60 * 1000);


                    //输出缓冲区大小
//                    Log.e(TAG, " socket:设置前" +socket.toString()+""+socket.getSendBufferSize());
                    Log.e(TAG, " socket:" + socket.toString() + "" + socket.getReceiveBufferSize());

                    //重置缓冲区大小
//                    socket.setSendBufferSize(1024*1024);
//                    socket.setReceiveBufferSize(1024*1024);
//                    Log.e(TAG, " socket:设置后" + socket.toString()+""+socket.getSendBufferSize());
//                    Log.e(TAG, " socket:设置后" + socket.toString()+""+socket.getReceiveBufferSize());

                    // Only receive local connections.
                    Log.i(TAG, " socket:" + socket.getInetAddress());
                    Log.i(TAG, " socket:" + socket.getPort());
                    Log.i(TAG, " socket:" + socket.getLocalAddress());
                    Log.i(TAG, " socket:" + socket.getLocalPort());

                    if (socket.getInetAddress().isLoopbackAddress()) {
                        ProxyConnection parser = new ProxyConnection(socket);

                        threadExecutor.execute(parser);
                    } else {
                        socket.close();
                        Log.e(TAG, "  socket.close()");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (SocketException e) {
            Log.e(TAG, "Failed to start proxy server", e);
        } catch (IOException e1) {
            Log.e(TAG, "Failed to start proxy server", e1);
        }

        mIsRunning = false;
    }

    public synchronized void setPort(int port) {
        if (mCallback != null) {
            try {
                mCallback.setProxyPort(port);
            } catch (RemoteException e) {
                Log.w(TAG, "Proxy failed to report port to PacManager", e);
            }
        }
        mPort = port;
    }

    public synchronized void setCallback(IProxyPortListener callback) {
        if (mPort != -1) {
            try {
                callback.setProxyPort(mPort);
            } catch (RemoteException e) {
                Log.w(TAG, "Proxy failed to report port to PacManager", e);
            }
        }
        mCallback = callback;
    }

    public synchronized void startServer() {
        mIsRunning = true;
        start();
    }

    public synchronized void stopServer() {
        mIsRunning = false;
        if (serverSocket != null) {
            try {
                serverSocket.close();
                serverSocket = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean isBound() {
        return (mPort != -1);
    }

    public int getPort() {
        return mPort;
    }
}
