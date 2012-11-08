package com.mix;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

public class ClientActivity extends Activity {
    /**
     * Called when the activity is first created.
     */
    SeekBar volumeSB;
    TextView testViewIp;
    TextView testViewPort;
    TextView testViewPassw;
    TextView testView;
    NetWClass netWClass;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        volumeSB=(SeekBar) findViewById(R.id.volumeSekBar);
        Button volumeOk=(Button) findViewById(R.id.VolumeOk);
        testViewIp = (TextView) findViewById(R.id.texIp);
        testViewPort = (TextView) findViewById(R.id.texPort);
        testViewPassw = (TextView) findViewById(R.id.texPassw);
        netWClass = new NetWClass(testViewIp,testViewPort,testViewPassw);

        volumeOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //testView.setText(Integer.toString(volumeSB.getProgress()));
                netWClass.changeVol(volumeSB.getProgress(),testView);
            }
        });
        volumeSB.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //To change body of implemented methods use File | Settings | File Templates.
            }
        });
    }
}
