package com.example.mbehzadi.samplesocket;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;


public class Client extends AppCompatActivity implements View.OnClickListener {

    private TextView mTextViewReplyFromServer;
    private EditText mEditTextSendMessage,etip,etport;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.client);

        Button buttonSend = (Button) findViewById(R.id.btn_send);

        mEditTextSendMessage = (EditText) findViewById(R.id.edt_send_message);
        mTextViewReplyFromServer = (TextView) findViewById(R.id.tv_reply_from_server);


        etip = (EditText) findViewById(R.id.et_ip);
        etport = (EditText) findViewById(R.id.et_port2);
        buttonSend.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.btn_send:

                    sendMessage(mEditTextSendMessage.getText().toString());


                break;
        }
    }

    private void sendMessage(final String msg) {

        final Handler handler = new Handler();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    //Replace below IP with the IP of that device in which server socket open.
                    //If you change port then change the port number in the server side code also.
                    Socket s = new Socket(etip.getText().toString(), Integer.valueOf(etport.getText().toString()));

                    OutputStream out = s.getOutputStream();

                    PrintWriter output = new PrintWriter(out);

                    output.println(msg);
                    output.flush();
                    BufferedReader input = new BufferedReader(new InputStreamReader(s.getInputStream()));
                    final String st = input.readLine();

                    handler.post(new Runnable() {
                        @Override
                        public void run() {

                            String s = mTextViewReplyFromServer.getText().toString();
                            if (st.trim().length() != 0)
                                mTextViewReplyFromServer.setText(s + "\nFrom Server : " + st);
                        }
                    });

                    output.close();
                    out.close();
                    s.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
    }
}