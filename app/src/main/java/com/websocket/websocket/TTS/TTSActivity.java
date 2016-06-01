package com.websocket.websocket.TTS;

import android.content.Intent;
import android.os.Bundle;
import android.renderscript.Sampler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.websocket.websocket.R;

/**
 * Created by Administrator on 2015-11-25.
 */
public class TTSActivity extends AppCompatActivity {
    private Button b_tts;
    private ListView tts_listview;
    private TTSAdapter tts_adapter;
    private View tts_footer;
    private int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tts);

        tts_adapter = new TTSAdapter();
        tts_listview = (ListView) findViewById(R.id.list_tts);
        tts_listview.setAdapter(tts_adapter);

        tts_footer = LayoutInflater.from(getApplicationContext()).inflate(R.layout.tts_footer, null);

        b_tts = (Button) findViewById(R.id.button_voice);
        b_tts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tts_listview.removeFooterView(tts_footer);
                String str = String.valueOf(i);
                tts_adapter.add("HELLO HELLO " + str);
                tts_listview.setAdapter(tts_adapter);
                tts_adapter.notifyDataSetChanged();
                i++;
                tts_listview.addFooterView(tts_footer);
            }
        });
    }
}
