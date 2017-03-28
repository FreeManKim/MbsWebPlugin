# MbsWebPlugin 将web View设置代理，通过proxy读取本地缓存，只限http请求，https还是不能读取缓存的
要使用WebView不造成内存泄漏，首先应该做的就是不能在xml中定义webview节点，而是在需要的时候动态生成。即：可以在使用WebView的地方放置一个LinearLayout类似ViewGroup的节点，然后在要使用WebView的时候，动态生成即：
WebView      mWebView = new WebView(getApplicationgContext());
LinearLayout mll      = findViewById(R.id.xxx);
mll.addView(mWebView);