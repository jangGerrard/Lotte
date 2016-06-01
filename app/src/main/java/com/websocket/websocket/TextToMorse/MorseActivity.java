package com.websocket.websocket.TextToMorse;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.websocket.websocket.MainActivity;
import com.websocket.websocket.R;


import java.io.InputStream;

/**
 * Created by Administrator on 2015-10-20.
 */
public class MorseActivity extends AppCompatActivity {
    private TextView final_text;
    private TextView final_morse;
    private String translateToText;
    private String result_text = "";
    private String result_morse = "";

    private String[] text;
    private String[] view_morse;

    private Vibrator mVibe;
    private long[] pattern;

    private Translator translator;

    private int index_text = 0;
    private int text_cnt=0;
    private int text_pos=0;
    private int pos=0;
    private String cmp = "2300";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.morse);

        final_text = (TextView) findViewById(R.id.final_text);
        final_morse = (TextView) findViewById(R.id.final_morse);

        InputStream morse = getResources().openRawResource(R.raw.morse);

        translator = new Translator(CSVFile.readCSV(morse));
        Intent intent = getIntent();

        translateToText = intent.getStringExtra("text");
        mVibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        mVibe.cancel();
        pattern = translator.CodeToTime(translator.TextToMorseCode(translateToText));
        mVibe.vibrate(pattern, -1);

        text = translateToText.split(" ");
        view_morse = translator.TextToMorseCode(translateToText).split(" ,");

        result_text = "";
        result_morse = "";
        index_text = 0;
        runThread();
    }

    private void runThread(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                long time = 0;

                for(int i=0; i<pattern.length; i++) {
                    time += pattern[i];
                    if (i % 2 != 0) {
                        synchronized (this) {
                            try {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {

                                        if (String.valueOf(view_morse[index_text].charAt(pos)).equals(",")) {
                                            result_morse += "  ";
                                            result_text += text[index_text].charAt(text_pos);
                                            text_pos++;
                                            pos++;
                                        }
                                        result_morse += view_morse[index_text].charAt(pos);
                                        pos++;

                                        final_morse.setText(result_morse);
                                        final_text.setText(result_text);
                                    }
                                });

                                wait(time);
                                time = 0;
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    if (Long.toString(pattern[i]).equals(cmp)) {
                        result_text+=text[index_text].charAt(text_pos);
                        text_pos++;
                        result_text += " ";
                        result_morse += "  ";

                        index_text++;
                        pos=0;
                        text_pos=0;
                        synchronized (this) {
                            try {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        final_text.setText(result_text);
                                    }
                                });
                                wait(time);
                                time = 0;
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }).start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mVibe.cancel();
    }
}
