package com.mix;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

/**
 * Created with IntelliJ IDEA.
 * User: arhont
 * Date: 12/5/12
 * Time: 7:54 PM
 * To change this template use File | Settings | File Templates.
 */
public class Mixer extends Activity{

    SeekBar volumeSB;
    TextView textViewIp;
    TextView textViewPort;
    TextView textViewPassw;
    TextView testView;
    NetWClass netWClass;
    SharedPreferences activityPreferences;
    SharedPreferences.Editor editor;
    Context context;
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.player);
        context=this;
        volumeSB=(SeekBar) findViewById(R.id.volumeSekBar);
        Button volumeOk=(Button) findViewById(R.id.VolumeOk);
        textViewIp = (TextView) findViewById(R.id.texIp);
        textViewPort = (TextView) findViewById(R.id.texPort);
        textViewPassw = (TextView) findViewById(R.id.texPassw);

        activityPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = activityPreferences.edit();

        textViewIp.setText(activityPreferences.getString("servIp", "no"));
        textViewPort.setText(activityPreferences.getString("servPort", "no"));
        textViewPassw.setText(activityPreferences.getString("servPasswd", "no"));

        netWClass = new NetWClass(textViewIp, textViewPort, textViewPassw);
        testView = (TextView) findViewById(R.id.test);
        volumeOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean b=netWClass.changeVol(volumeSB.getProgress(),testView);
                if (!b){
                    AlertDialog.Builder al = new AlertDialog.Builder(context);
                    al.setTitle("Not connected to server");
                    al.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    al.show();
                }
                editor.putString("servIp", textViewIp.getText().toString());
                editor.putString("servPort", textViewPort.getText().toString());
                editor.putString("servPasswd", textViewPassw.getText().toString());
                editor.commit();
            }
        });

        Button play = (Button)findViewById(R.id.Play);
        play.setOnClickListener(new Listner("play"));

        Button pause = (Button)findViewById(R.id.Pause);
        pause.setOnClickListener(new Listner("pause"));

        Button next = (Button)findViewById(R.id.Next);
        next.setOnClickListener(new Listner("next"));

        Button prev = (Button)findViewById(R.id.Prev);
        prev.setOnClickListener(new Listner("prev"));



    }
    class Listner implements View.OnClickListener {
        String val;

        Listner(String val) {
            this.val = val;
        }

        @Override
        public void onClick(View view) {
            boolean b=netWClass.changeTrack(val);
            if (!b){
                AlertDialog.Builder al = new AlertDialog.Builder(context);
                al.setTitle("Not connected to server");
                al.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                al.show();
            }
            editor.putString("servIp", textViewIp.getText().toString());
            editor.putString("servPort", textViewPort.getText().toString());
            editor.putString("servPasswd", textViewPassw.getText().toString());
            editor.commit();
        }
    }
}
