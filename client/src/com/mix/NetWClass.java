package com.mix;

import android.os.StrictMode;
import android.widget.TextView;

import java.io.*;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

/**
 * Created with IntelliJ IDEA.
 * User: arhont
 * Date: 11/7/12
 * Time: 6:12 PM
 * To change this template use File | Settings | File Templates.
 */
public class NetWClass {
    private int serverPort;
    private String serverIp;

    public NetWClass(int serverPort, String serverIp) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        this.serverPort = serverPort;
        this.serverIp = serverIp;

    }

    public boolean changeVol(int val,TextView view) {
        try {
            Socket socket = new Socket (serverIp,serverPort);
            OutputStream os = socket.getOutputStream();
            String str="v1 123aaa volume "+val+"% end ";

            os.write(str.getBytes());
            os.flush();
            socket.close();
        } catch (IOException e) {
            return false;
        }
        return true;
    }
}
