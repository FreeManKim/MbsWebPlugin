package com.mobisoft.mbswebplugin.proxy.Work;

import com.mobisoft.mbswebplugin.proxy.DB.WebviewCaheDao;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * Author：Created by fan.xd on 2017/3/23.
 * Email：fang.xd@mobisoft.com.cn
 * Description：
 */

public interface CheckFile {
    boolean check(File file,WebviewCaheDao dao,String... args);
    boolean check(File file,File fileTemp) throws IOException;

    boolean check(File file, InputStream inputStream) throws IOException;

    String[] checkMD5(File file, File fileTemp,WebviewCaheDao dao)throws IOException;
}
