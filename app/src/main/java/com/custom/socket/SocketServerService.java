package com.custom.socket;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.Nullable;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/***************************************************************************************************
 *                                  Copyright (C), Nexgo Inc.                                      *
 *                                    http://www.nexgo.cn                                          *
 ***************************************************************************************************
 * date: 2020/5/28
 * author: bruse
 * description: the server for talk
 ***************************************************************************************************/

public class SocketServerService extends Service {
    private boolean isServiceDestroyed = false;

    @Override
    public void onCreate() {
        new Thread(new TcpServer()).start();
        super.onCreate();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private class TcpServer implements  Runnable {

        @Override
        public void run() {
            ServerSocket serverSocket;
            try {
                //监听8688端口
                serverSocket = new ServerSocket(8688);
            } catch (IOException e){
                return;
            }
            while (!isServiceDestroyed){
                try {
                    //接受客户端请求，并且阻塞直到接收到消息
                    final Socket client = serverSocket.accept();
                    new Thread(){
                        @Override
                        public void run() {
                            try{
                                responseClient(client);
                            }catch (IOException e){
                                e.printStackTrace();
                            }
                        }
                    }.start();
                }catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    private void responseClient(Socket client) throws IOException {
        //用于接收客户端信息
        BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
        //用于向客户端发送信息
        PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(client.getOutputStream())),true);
        out.println("hello,I am server");
        while (!isServiceDestroyed){
            String str = in.readLine();
            Log.i("server","收到客户端的信息 "+str);
            if (TextUtils.isEmpty(str)){
                //客户端断开了连接
                Log.i("server","客户端断开连接");
                break;
            }
            String message = "receive the message from client: "+ str;
            //从客户端收到的消息加工再发给客户端
            out.println(message);

        }
        out.close();
        in.close();
        client.close();
    }

    @Override
    public void onDestroy() {
        isServiceDestroyed = true;
        super.onDestroy();
    }
}
