package com.mix;

import android.app.Activity;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;

public class ClientActivity extends TabActivity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        TabHost tabHost = getTabHost();

        TabHost.TabSpec mixer = tabHost.newTabSpec("tab1");
        mixer.setIndicator("Mixer");
        Intent photosIntent = new Intent(this, Mixer.class);
        mixer.setContent(photosIntent);


        TabHost.TabSpec present = tabHost.newTabSpec("tab2");
        present.setIndicator("Present");
        Intent songsIntent = new Intent(this, Presentation.class);
        present.setContent(songsIntent);


        tabHost.addTab(mixer);
        tabHost.addTab(present);

    }

}
