package com.websocket.websocket;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

public class SpeckToText extends AppCompatActivity {

    private final int SPEECH_RECOGNITION_CODE = 1;
    private TextView textOutput;
    private ImageButton btnMicroPhone;
    private Button findWebBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speck_to_text);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        textOutput = (TextView)findViewById(R.id.txt_output);
        btnMicroPhone = (ImageButton) findViewById(R.id.btn_mic);
        btnMicroPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSpeechToText();
            }
        });
        findWebBtn = (Button)findViewById(R.id.find_web);
        findWebBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(textOutput.getText().toString() != null && !(textOutput.getText().toString().equals(""))){
                    Toast.makeText(getApplicationContext(), textOutput.getText().toString(), Toast.LENGTH_LONG).show();

                    Intent i = new Intent(SpeckToText.this, SearchActivity.class);
                    i.putExtra("query", textOutput.getText().toString());
                    startActivity(i);
                } else {
                    Toast.makeText(getApplicationContext(), "음성 데이터가 없습니다.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void startSpeechToText(){
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                "Speak Something....");

        try {
            startActivityForResult(intent,SPEECH_RECOGNITION_CODE);
        }catch(ActivityNotFoundException e){
            Toast.makeText(getApplicationContext(), "Sorry...",Toast.LENGTH_LONG).show();
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        Toast.makeText(getApplicationContext(), "OnActivityResult", Toast.LENGTH_SHORT).show();

        switch(requestCode){
            case SPEECH_RECOGNITION_CODE:
                if(resultCode == RESULT_OK && data != null){
                    ArrayList<String> result =data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    String text = result.get(0);
                    Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
                    Log.d("TTSActivity", text);
                    textOutput.setText(text);
                }
                break;
        }
    }

}
