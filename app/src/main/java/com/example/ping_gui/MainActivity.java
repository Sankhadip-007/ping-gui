package com.example.ping_gui;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    private EditText serverIpEditText, serverPortEditText, serverMsgEditText;
    private EditText clientIpEditText, clientPortEditText, clientMsgEditText;
    private Button startServerButton, connectClientButton;
    private TextView serverResponseTextView, clientResponseTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        serverIpEditText = findViewById(R.id.serverIpEditText);
        serverPortEditText = findViewById(R.id.serverPortEditText);
        serverMsgEditText = findViewById(R.id.serverMsgEditText);
        startServerButton = findViewById(R.id.startServerButton);
        serverResponseTextView = findViewById(R.id.serverResponseTextView);

        clientIpEditText = findViewById(R.id.clientIpEditText);
        clientPortEditText = findViewById(R.id.clientPortEditText);
        clientMsgEditText = findViewById(R.id.clientMsgEditText);
        connectClientButton = findViewById(R.id.connectClientButton);
        clientResponseTextView = findViewById(R.id.clientResponseTextView);

        startServerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startServer();
            }
        });

        connectClientButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                connectClient();
            }
        });
    }

    private void startServer() {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                /*ServerSocket serverSocket = new ServerSocket(Integer.parseInt(serverPortEditText.getText().toString()));
                Socket clientSocket = serverSocket.accept();
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                String message = serverMsgEditText.getText().toString();
                out.println(message);

                final String response = in.readLine();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        serverResponseTextView.setText(response);
                    }
                });

                in.close();
                out.close();
                clientSocket.close();
                serverSocket.close(); */
                PacketSender packetSender=new PacketSender();
                packetSender.sendPacket();

            }
        });
    }

    private void connectClient() {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                /*Socket socket = new Socket(clientIpEditText.getText().toString(), Integer.parseInt(clientPortEditText.getText().toString()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                String message = clientMsgEditText.getText().toString();
                out.println(message);

                final String response = in.readLine();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        clientResponseTextView.setText(response);
                    }
                });

                in.close();
                out.close();
                socket.close(); */
                PacketSniffer packetSniffer=new PacketSniffer();
                packetSniffer.startSniffing1();

            }
        });
    }
}

