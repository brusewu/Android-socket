package com.custom.socket;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

/***************************************************************************************************
 *                                  Copyright (C), Nexgo Inc.                                      *
 *                                    http://www.nexgo.cn                                          *
 ***************************************************************************************************
 * date: 2020/5/28
 * author: bruse
 * description:
 ***************************************************************************************************/

public class SocketClientActivity extends AppCompatActivity {

    private Button button;
    private EditText editText;
    private Socket clientSocket;
    private PrintWriter printWriter;
    private TextView message;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_socket);
        initView();
        Intent service = new Intent(this,SocketServerService.class);
        startService(service);
        new Thread() {
            @Override
            public void run() {
                connectSocketServer();
            }
        }.start();

    }

    private void initView(){
        editText = (EditText) findViewById(R.id.et_receive);
        button = (Button) findViewById(R.id.bt_send);
        message = (TextView) this.findViewById(R.id.tv_message);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String msg = editText.getText().toString();
                Log.d("client","start...");
                //向服务器发送消息
                if (!TextUtils.isEmpty(msg)&& null != printWriter) {
                    Log.i("client","start...message");
                    printWriter.println(msg);
                    message.setText(message.getText() + "\n" + "client: " + msg);
                    editText.setText("");
                }
            }
        });
    }

    private void connectSocketServer() {
        Socket socket = null;
        while (socket == null) {
            try {
                //选择和服务器一样的端口8688
                socket = new Socket("localhost",8688);
                clientSocket = socket;
                printWriter = new PrintWriter(new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream())),true);

            }catch (IOException e){
                SystemClock.sleep(1000);
            }
        }

        try {
            //接收服务器的消息
            BufferedReader br = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            while (!isFinishing()){
                final String msg = br.readLine();
                if (msg != null) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            message.setText(message.getText() + "\n" + "service: " + msg);
                        }
                    });
                }
            }
            printWriter.close();
            br.close();
            socket.close();
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
