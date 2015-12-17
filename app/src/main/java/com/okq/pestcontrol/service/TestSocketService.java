package com.okq.pestcontrol.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Administrator on 2015/12/17.
 */
public class TestSocketService extends Service {

    private SimpleBinder binder;

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
                    ServerSocket serverSocket = new ServerSocket(5970);
                    while (isSocket) {
                        Socket accept = serverSocket.accept();
                        //获取输入流
                        BufferedReader in = new BufferedReader(
                                new InputStreamReader(accept.getInputStream()));
                        //获取从客户端发来的信息
                        String str = in.readLine();
                        binder.message = str;
//                        return binder;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        return binder;
    }


    public class SimpleBinder extends Binder {
        public String message;

        public TestSocketService getTestService() {
            return TestSocketService.this;
        }
    }
}
