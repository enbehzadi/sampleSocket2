package com.example.mbehzadi.samplesocket;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mbehzadi.samplesocket.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Girish Bhalerao on 5/4/2017.
 */
public class Server extends AppCompatActivity implements View.OnClickListener {

    final Handler handler = new Handler();

    private Button buttonStartReceiving;
    private Button buttonStopReceiving;
    private TextView textViewDataFromClient;
    EditText etport;
    private boolean end = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.server);

        buttonStartReceiving = (Button) findViewById(R.id.btn_start_receiving);
        buttonStopReceiving = (Button) findViewById(R.id.btn_stop_receiving);
        textViewDataFromClient = (TextView) findViewById(R.id.tv_data_from_client);

        buttonStartReceiving.setOnClickListener(this);
        buttonStopReceiving.setOnClickListener(this);
        etport=(EditText) findViewById(R.id.et_port);

    }

    private void startServerSocket() {

        Thread thread = new Thread(new Runnable() {

            private String stringData = null;

            @Override
            public void run() {

                try {

                    if(etport.getText().toString().length()==4)
                    {


                    ServerSocket ss = new ServerSocket(Integer.valueOf(etport.getText().toString()));

                    while (!end) {
                        //Server is waiting for client here, if needed
                        Socket s = ss.accept();
                        BufferedReader input = new BufferedReader(new InputStreamReader(s.getInputStream()));
                        PrintWriter output = new PrintWriter(s.getOutputStream());

                        stringData = input.readLine();
                        output.println("FROM SERVER - " + stringData.toUpperCase());
                        output.flush();

                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        updateUI(stringData);
                        if (stringData.equalsIgnoreCase("STOP")) {
                            end = true;
                            output.close();
                            s.close();
                            break;
                        }

                        output.close();
                        s.close();
                    }

                    ss.close();
                    }
                    else {
                        Toast.makeText(getBaseContext(),"Enter port please",Toast.LENGTH_SHORT).show();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        });
        thread.start();
    }

    private void updateUI(final String stringData) {

        handler.post(new Runnable() {
            @Override
            public void run() {

                String s = textViewDataFromClient.getText().toString();
                if (stringData.trim().length() != 0)
                    textViewDataFromClient.setText(s + "\n" + "From Client : " + stringData);
            }
        });
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_start_receiving:

                startServerSocket();
                buttonStartReceiving.setEnabled(false);
                buttonStopReceiving.setEnabled(true);
                break;

            case R.id.btn_stop_receiving:

                //stopping server socket logic you can add yourself
                buttonStartReceiving.setEnabled(true);
                buttonStopReceiving.setEnabled(false);
                break;
        }
    }
}
