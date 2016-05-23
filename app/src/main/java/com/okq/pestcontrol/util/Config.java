package com.okq.pestcontrol.util;

import java.io.File;

/**
 * Created by zst on 2016/2/23.
 */
public class Config {
    //    public static final String URL = "http://clever-argi.com:4050/Web/APP/";
    public static final String URL = "http://clever-argi.com:3050/Pest/APP/";
    public static final String DOWNLOAD_PATH = android.os.Environment.getExternalStorageDirectory() + File.separator + "PestControl" + File.separator + "apk" + File.separator;
    public static final String CACHE_DIR = android.os.Environment.getExternalStorageDirectory() + File.separator + "PestControl" + File.separator + "cache" + File.separator + "http" + File.separator;
    public static final int HTTP_TIME_OUT = 10 * 1000; //10s
}
