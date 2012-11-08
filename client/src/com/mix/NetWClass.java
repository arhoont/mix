package com.mix;

import android.os.StrictMode;
import android.widget.TextView;

import java.io.*;
import java.net.Socket;


/**
 * Created with IntelliJ IDEA.
 * User: arhont
 * Date: 11/7/12
 * Time: 6:12 PM
 * To change this template use File | Settings | File Templates.
 */
public class NetWClass {
    private TextView serverTextIp;
    private TextView serverTextPort;
    private TextView serverTextPassw;


    public NetWClass(TextView serverTextIp, TextView serverTextPort, TextView serverTextPassw) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        this.serverTextIp = serverTextIp;
        this.serverTextPort = serverTextPort;
        this.serverTextPassw = serverTextPassw;
    }

    public boolean changeVol(int val, TextView view) {
        try {
            Socket socket = new Socket(serverTextIp.getText().toString(),
                    Integer.valueOf(serverTextPort.getText().toString()));
            OutputStream os = socket.getOutputStream();
            String str = "v1 "+serverTextPassw.getText().toString()+ " volume " + val + "% end ";
            os.write(str.getBytes());
            os.flush();
            socket.close();
        } catch (IOException e) {

            return false;
        }
        return true;
    }
}
