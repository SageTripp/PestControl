package com.okq.pestcontrol.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 配置文件
 * Created by Administrator on 2015/12/8.
 */
public class Sharepreference {
    /**
     * 保存在手机里面的文件名
     */
    private static final String FILE_NAME = "pest_control_user_data";

    public static final String REMEMBER_PASSWORD = "isRemember";
    public static final String USER_NAME = "userName";

    /**
     * 手机配置文件保存数据的Key值
     */
    public enum Key {
        /**
         * 是否记住密码
         */
        REMEMBER_PASSWORD("isRemember"),
        /**
         * 用户名
         */
        USER_NAME("userName"),
        /**
         * 用户密码
         */
        PASSWORD("passWord");

        private String key;

        Key(String key) {
            this.key = key;
        }

        public String getKey() {
            return key;
        }
    }

    /**
     * 保存数据的方法，我们需要拿到保存数据的具体类型，然后根据类型调用不同的保存方法
     *
     * @param context 上下文对象
     * @param key     关键词
     * @param object  要保存的对象 (基本数据类型)
     */
    public static void setParam(Context context, Key key, Object object) {
        String type = object.getClass().getSimpleName();
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        String keyValue = key.getKey();

        if ("String".equals(type)) {
            editor.putString(keyValue, (String) object);
        } else if ("Integer".equals(type)) {
            editor.putInt(keyValue, (Integer) object);
        } else if ("Boolean".equals(type)) {
            editor.putBoolean(keyValue, (Boolean) object);
        } else if ("Float".equals(type)) {
            editor.putFloat(keyValue, (Float) object);
        } else if ("Long".equals(type)) {
            editor.putLong(keyValue, (Long) object);
        }

        editor.apply();
    }


    /**
     * 得到保存数据的方法，我们根据默认值得到保存的数据的具体类型，然后调用相对于的方法获取值
     *
     * @param context       上下文对象
     * @param key           关键词
     * @param defaultObject 默认值
     * @return 保存的值
     */
    public static Object getParam(Context context, Key key, Object defaultObject) {
        String type = defaultObject.getClass().getSimpleName();
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);

        String keyValue = key.getKey();

        if ("String".equals(type))
            return sp.getString(keyValue, (String) defaultObject);
        else if ("Integer".equals(type))
            return sp.getInt(keyValue, (Integer) defaultObject);
        else if ("Boolean".equals(type))
            return sp.getBoolean(keyValue, (Boolean) defaultObject);
        else if ("Float".equals(type))
            return sp.getFloat(keyValue, (Float) defaultObject);
        else if ("Long".equals(type))
            return sp.getLong(keyValue, (Long) defaultObject);
        else
            return null;
    }
}
