package com.mobisoft.mbswebplugin.proxy.Work;

import android.text.TextUtils;
import android.util.Log;

import com.mobisoft.mbswebplugin.proxy.DB.WebviewCaheDao;
import com.mobisoft.mbswebplugin.utils.MD5Util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * Author：Created by fan.xd on 2017/3/23.
 * Email：fang.xd@mobisoft.com.cn
 * Description：
 */

public class DefaultCheck implements CheckFile {
    private static DefaultCheck defaultCheck;

    public static DefaultCheck getInstance() {
        if (defaultCheck == null)
            defaultCheck = new DefaultCheck();
        return defaultCheck;
    }


    @Override
    public boolean check(File file, WebviewCaheDao dao, String... args) {
        boolean isSave = !TextUtils.isEmpty(dao.getUrlPath(args[0], args[1]));
        return isSave && file.exists();
    }

    @Override
    public String[] checkMD5(File file, File fileTemp,WebviewCaheDao dao) throws IOException {
        String[] args = new String[2];
        String f2 = MD5Util.getFileMD5String(fileTemp);
        args[1] = f2;
        if (file.exists()) {
            String f1 = MD5Util.getFileMD5String(file);
            Log.e("check", "f2!:" + f2);
            Log.e("check", "f1!:" + f1);
            String isFinish = dao.getUrlPath(f2,"checkMD5");

            if (TextUtils.equals(f1, f2) && TextUtils.equals("true",isFinish)) {
                boolean flag = fileTemp.delete();
                Log.i("check", "delete!:" + flag);
                args[0] = String.valueOf(true);
                return args;
            } else {
                file.delete();
                boolean flag = fileTemp.renameTo(file);
                Log.i("check", "renameTo:" + flag);
                args[0] = String.valueOf(false);
            }
            return args;

        }
        args[0] = String.valueOf(false);
        boolean flag = fileTemp.renameTo(file);
        Log.e("check", "renameTo2:" + flag);
        return args;
    }

    @Override
    public boolean check(File file, File fileTemp) throws IOException {
        if (file.exists()) {
            String f1 = MD5Util.getFileMD5String(file);
            String f2 = MD5Util.getFileMD5String(fileTemp);
            Log.e("check", "f2!:" + f2);
            Log.e("check", "f1!:" + f1);

            if (TextUtils.equals(f1, f2)) {
                boolean flag = fileTemp.delete();

                Log.e("check", "delete!:" + flag);
                return true;
            } else {
                file.delete();
                boolean flag = fileTemp.renameTo(file);
                Log.e("check", "renameTo:" + flag);
            }
            return false;

        }
        boolean flag = fileTemp.renameTo(file);
        Log.e("check", "renameTo2:" + flag);
        return false;
    }

    @Override
    public boolean check(File file1, InputStream inputStream) throws IOException {
        if (file1.exists()) {
            String f1 = MD5Util.getFileMD5String(file1);
            String f2 = MD5Util.getFileMD5String(inputStream);
            return TextUtils.equals(f1, f2);

        }
        return false;
    }

}
