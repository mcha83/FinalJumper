package com.bradleywilcox.finaljumper;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.content.IntentCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;



public class Start extends AppCompatActivity implements View.OnClickListener {

    public TextView txt1;
    public Button butt1, butt2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start);

        txt1 = (TextView) findViewById(R.id.textView);
        butt1 = (Button) findViewById(R.id.buttonStart);
        butt2 = (Button) findViewById(R.id.buttonAbout);

        butt1.setOnClickListener(this);
        butt2.setOnClickListener(this);

    }

    @Override
    public void onClick(View v)
    {
      
        if(v==butt1) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_CLEAR_TOP
                    | IntentCompat.FLAG_ACTIVITY_CLEAR_TASK);

            startActivity(intent);
        }

        if(v==butt2) {
            Intent intent = new Intent(this, About.class);
            startActivity(intent);
        }


    }



}
