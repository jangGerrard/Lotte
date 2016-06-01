package com.websocket.websocket.ReceiveCall;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.os.Vibrator;
import android.speech.SpeechRecognizer;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.melnykov.fab.FloatingActionButton;
import com.websocket.websocket.Global.Global;
import com.websocket.websocket.MainActivity;
import com.websocket.websocket.R;
import com.websocket.websocket.Util.CropBitmap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Administrator on 2015-10-14.
 */
public class ReceiveCallActivity extends AppCompatActivity implements View.OnClickListener {
    private final int MY_UI=1001;			                              //ui 기본 값
    private FloatingActionButton b_receive_call;
    private FloatingActionButton b_exit_call_1;
    private Button b_exit_call_2;
//    private Button b_tts;
    private Button b_speaker;
    private TextView timer;

    private AudioManager am = null;
    private MediaPlayer mAudio = null;
    private Vibrator mVibe = null;

    private int seconds = 0;
    private int speaker_flag = -1;

    private ArrayList<String> mResult;									//음성인식 결과 저장할 list
    private String mSelectedString;										//결과 list 중 사용자가 선택한 텍스트

    private CropBitmap cropBitmap;
    private boolean running = true;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                mAudio.stop();
                mVibe.cancel();
                finish();
                break;
        }
        return super.onKeyDown(keyCode, event);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.receive_call);
        cropBitmap = new CropBitmap();
        String fileOwner = Global.userPref.getString("user_name", "");
        String fileRootPath =Global.dirPath;
        String profile="";
        if(getIntent().getStringExtra("profile")!=null)
            profile = getIntent().getStringExtra("profile");


        VolumeControl(0); // 전화가 왔을 때 flag = '0'
        ((TextView)findViewById(R.id.phoneNum)).setText(this.getIntent().getStringExtra("phoneNum"));
        ((TextView)findViewById(R.id.phoneName)).setText(this.getIntent().getStringExtra("phoneName"));
        // user.jpg파일을 Bitmap으로 디코딩한다.

        if(profile !="") {
            Bitmap selectedImage = BitmapFactory.decodeFile(Global.dirPath + "/contents/"+Global.video_name+"/" + profile);
            ((ImageView) findViewById(R.id.profile)).setImageBitmap(cropBitmap.getCroppedBitmap(BitmapFactory.decodeFile(fileRootPath + fileOwner + "/contents/"+Global.video_name+"/" + profile)));
        }
        else {
            Log.d("ReceiveCallActivity", fileRootPath + fileOwner + "/contents/"+Global.video_name+"/" + profile);
        }


//        sttIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
//        sttIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getPackageName());
//        sttIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR");
//
//        mRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
//        mRecognizer.setRecognitionListener(listener);
//        mRecognizer.startListening(sttIntent);

//        ((ImageView)findViewById(R.id.profile)).setImageURI(new URI(Global.dirPath+this.getIntent().getStringExtra("profile")));

        timer = (TextView) findViewById(R.id.time);
        b_receive_call = (FloatingActionButton) findViewById(R.id.button_receive_call);
        b_exit_call_1 = (FloatingActionButton) findViewById(R.id.button_exit_call_1);
        b_speaker = (Button) findViewById(R.id.button_speaker);
        b_exit_call_2 = (Button) findViewById(R.id.button_exit_call_2);
//        b_tts = (Button) findViewById(R.id.button_tts);

        b_receive_call.setOnClickListener(this);
        b_exit_call_1.setOnClickListener(this);
        b_speaker.setOnClickListener(this);
        b_exit_call_2.setOnClickListener(this);
//        b_tts.setOnClickListener(this);
    }

    public void VolumeControl(int flag){
        try{
            mAudio = new MediaPlayer();
            am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            mVibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

            if(flag == 0){ // 통화가 왔을 때
                switch (am.getRingerMode()){
                    case AudioManager.RINGER_MODE_NORMAL: // 소리모드
                        Uri alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE); // 설정 된 벨소리 파일path
                        mAudio.setDataSource(alert.toString());
                        mAudio.setAudioStreamType(AudioManager.STREAM_RING); // 벨소리
                        break;
                    case AudioManager.RINGER_MODE_SILENT: // 무음모드
                        break;
                    case AudioManager.RINGER_MODE_VIBRATE: // 진동모드
                        long[] pattern = {100, 500, 100, 300, 100, 500};
                        mVibe.vibrate(pattern, 1);
                        break;
                }
                mAudio.setLooping(true);
                mAudio.prepare();
            } else if(flag == 1){ // 통화 중 일 때
                if(am.getRingerMode() == AudioManager.RINGER_MODE_VIBRATE) mVibe.cancel();
                String fileOwner = Global.userPref.getString("user_name", "");
                String path = Global.dirPath +fileOwner+"/contents/"+Global.video_name+"/" + getIntent().getStringExtra("voiceName");
                mAudio.setDataSource(path);
                mAudio.setAudioStreamType(AudioManager.STREAM_VOICE_CALL); // 전화(수신스피커)
                mAudio.prepare();
                /*
                mAudio.setOnCompletionListener(new MediaPlayer.OnCompletionListener() { // 음성 파일 재생완료가 되면 sttulactivity로 넘어간다.
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        mAudio.stop();
                        finish();
                    }
                });
                */
            }
        }catch(Exception e){}

        mAudio.start();
    }

    @Override
    protected void onStop(){
        super.onStop();

        mAudio.stop();
        mVibe.cancel();
        am.setSpeakerphoneOn(false);
    }

    @Override
    public void onClick(View arg0){
        switch (arg0.getId()){
            case R.id.button_receive_call: // 통화 버튼
                mAudio.stop();
                VolumeControl(1); // 통화 중 일 때 flag = '1'
                RelativeLayout layout_call_1 = (RelativeLayout) findViewById(R.id.layout_call_1);
                LinearLayout layout_call_2 = (LinearLayout) findViewById(R.id.layout_call_2);
                layout_call_1.setVisibility(View.GONE);
                layout_call_2.setVisibility(View.VISIBLE);
                timer.setVisibility(View.VISIBLE);
                //////////////////////////////////////////////////////////////////////////////////////////////////////
                if(Global.mWebSocketClient != null){
                    JSONObject main = new JSONObject();
                    JSONArray arr = new JSONArray();
                    try {
                        main.put("call","accept");
                        arr.put(main);
                        Global.mWebSocketClient.send("TEXT "+arr.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                    //Global.mWebSocketClient.send("TEXT accept_call");
                //////////////////////////////////////////////////////////////////////////////////////////////////////
                runThread();
                break;
            case R.id.button_exit_call_1: // 통화 거절 버튼(전화가 왔을 때)
                Toast.makeText(ReceiveCallActivity.this, "Reject call", Toast.LENGTH_SHORT).show();
                if(Global.mWebSocketClient != null){
                    JSONObject main = new JSONObject();
                    JSONArray arr = new JSONArray();
                    try {
                        main.put("call","reject");
                        arr.put(main);
                        Global.mWebSocketClient.send("TEXT "+arr.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                mAudio.stop();
                mVibe.cancel();
                finish();
                break;
            case R.id.button_speaker: // 통화 중 일 때 Speaker 버튼
                speaker_flag *= (-1);

                if(speaker_flag == 1){
                    am.setSpeakerphoneOn(true);
                    Toast.makeText(ReceiveCallActivity.this, "on speaker", Toast.LENGTH_SHORT).show();
                } else if(speaker_flag == -1) {
                    am.setSpeakerphoneOn(false);
                    Toast.makeText(ReceiveCallActivity.this, "off speaker", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.button_exit_call_2: // 통화 종료 버튼(통화 중 일 때)
                Toast.makeText(ReceiveCallActivity.this, "Exit call", Toast.LENGTH_SHORT).show();
                mAudio.stop();
                finish();
                break;
//            case R.id.button_tts: // 음성인식을 위한 tts 버튼(interactive 기능)
//                mAudio.stop();
//                //mVibe.cancel();
//                // 현재 Activity에서 SttUIActivity를 호출하고 결과값을 받아온다
//                startActivityForResult(new Intent(ReceiveCallActivity.this, SttUIActivity.class), MY_UI);
//                break;
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if( resultCode == RESULT_OK  && (requestCode == MY_UI) ){		//결과가 있으면
            showSelectDialog(requestCode, data);				//결과를 다이얼로그로 출력.
        }
        else{															//결과가 없으면 에러 메시지 출력
            String msg = null;
            if(msg != null)		//오류 메시지가 null이 아니면 메시지 출력
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
        }
    }


    /* 통화가 시작되었을 때 부터의 time(통화시간) */
    private void runThread(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(running){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            seconds++;
                            timer.setText(milliSecondsToTimer(seconds));
                            if(!mAudio.isPlaying()){
                                running = false;
                                finish();
                            }
                        }
                    });
                    SystemClock.sleep(1000);
                }
            }
        }).start();
    }

    /* 통화시간을 띄워주기 위해 분, 초를 계산해서 String으로 반환 */
    private String milliSecondsToTimer(int secondsReceives) {
        String finalTimerString = "";
        String secondsStringSeconds;
        String secondsStringMinutes;

        int seconds = (secondsReceives % 60);
        int minutes = (secondsReceives / 60);

        if (seconds < 10) {
            secondsStringSeconds = "0" + seconds;
        } else {
            secondsStringSeconds = "" + seconds;
        }

        if (minutes < 10) {
            secondsStringMinutes = "0" + minutes;
        } else {
            secondsStringMinutes = "" + minutes;
        }

        finalTimerString = finalTimerString + secondsStringMinutes + ":" + secondsStringSeconds;
        return finalTimerString;
    }


    private void showSelectDialog(int requestCode, Intent data){
        String key = "";
        key = SpeechRecognizer.RESULTS_RECOGNITION;	//키값 설정
        mResult = data.getStringArrayListExtra(key);		//인식된 데이터 list 받아옴.
        String[] result = new String[mResult.size()];			//배열생성. 다이얼로그에서 출력하기 위해
        mResult.toArray(result);									//	list 배열로 변환

        //1개 선택하는 다이얼로그 생성
        AlertDialog ad = new AlertDialog.
                Builder(this).setTitle("선택하세요.")
                .setSingleChoiceItems(result, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mSelectedString = mResult.get(which);        //선택하면 해당 글자 저장
                    }
                })
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        answer_set(mSelectedString);
                    }
                })
                .setNegativeButton("다시하기", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mAudio.start(); // 다시 질문을 하고
                        // STT 다시 시작
                        startActivityForResult(new Intent(ReceiveCallActivity.this, SttUIActivity.class), MY_UI);
                    }
                }).create();
        ad.show();

    }


    public void answer_set(String answer) { // Stt 음성에 저장된 문자 전송
        //////////////////////////////////////////////////////////////////////////////////////////////////////
        Global.mWebSocketClient.send("TEXT "+ answer);
        //////////////////////////////////////////////////////////////////////////////////////////////////////
        Toast.makeText(getBaseContext(), "선택한 내용 '"+answer+"'이 전송되었습니다.", Toast.LENGTH_SHORT).show();
        finish();
    }

}
