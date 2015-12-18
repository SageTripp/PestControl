package com.okq.pestcontrol.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import org.xutils.common.util.LogUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Administrator on 2015/12/17.
 */
public class TestSocketService extends Service {

    private SimpleBinder binder;
    private OnMsgBackListener listener;
    private OutputStream outputStream;

    @Override
    public void onCreate() {
        super.onCreate();
        binder = new SimpleBinder();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        final boolean isSocket = true;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (isSocket) {
                        int temp;
                        ServerSocket serverSocket = new ServerSocket(5970);
                        Socket accept = serverSocket.accept();
                        String address = accept.getRemoteSocketAddress().toString();
                        LogUtil.d(address);
                        //获取输入流
                        InputStream in = accept.getInputStream();
                        outputStream = accept.getOutputStream();
                        byte[] buffer = new byte[512];
                        while ((temp = in.read(buffer)) != -1) {
                            String msg = new String(buffer, 0, temp);
                            LogUtil.d(msg);
                            listener.onMsgBack(address + ":\r\n" + msg);
                        }
                    }
//                        return binder;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        return binder;
    }

    public void senMsg(final String msg) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (null != outputStream) {
                    byte[] buffer;
                    try {
                        buffer = msg.getBytes();
                        outputStream.write(buffer);
                        outputStream.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    public void setOnMsgBackListener(OnMsgBackListener listener) {
        this.listener = listener;
    }

    public interface OnMsgBackListener {
        void onMsgBack(String msg);
    }

    public class SimpleBinder extends Binder {
        public TestSocketService getTestService() {
            return TestSocketService.this;
        }
    }
}
