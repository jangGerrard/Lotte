package com.websocket.websocket;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import android.os.Bundle;
import android.os.Environment;
import android.preference.DialogPreference;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;


import com.websocket.websocket.Global.Global;
import com.websocket.websocket.ReceiveCall.ReceiveCallActivity;
import com.websocket.websocket.TextToMorse.MorseActivity;
import com.websocket.websocket.VoteActivity.VoteActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;


/**
 * Created by Administrator on 2015-10-12.
 */
public class TestReceiver extends BroadcastReceiver {
    Context mContext;
    private NotificationManager mNotificationManager;
    private String sJsonText;


    private String mode;

    // Interactive Push variable
    private String text, url="";

    // Interactive Call variable
    private String phoneName,phoneNum, voiceName, profile;
    private String call_down_path;
    private ArrayList<String> voiceNames= new ArrayList<>(), profileNames=new ArrayList<>();
    private ArrayList<Boolean> hasVoiceFiles = new ArrayList<>();
    private ArrayList<Boolean> hasProfileFiles = new ArrayList<>();

    // Interactive Branch variable
    private String answerChoice1, answerChoice2;    // 선택지 1, 2
    private String branchTime1, branchTime2;        // 분기할 시간(초)
    private String totalTime;                         // 영상의 끝부분

    // 서버로부터 오는 JSONArray -  OpCode, phoneNum, phoneName, voiceName, profile, text
    // Example) []가 JSON Array, {}가 JSON Object
    // [{"mode":"call","time":"1","phoneNum":"1","phoneName":"1","voiceName":"voice.mp3","profile":"Chrysanthemum.jpg"}]
    // [{"mode":"push","time":"2","text":"push test"}]
    private JSONArray ja;
    private JSONObject mainObject;

    private void interactiveAction(String _mode, JSONObject jsonObject){
        try {
            if (mode.equals("push")){
                text = mainObject.getString("text");
            }
            else if(mode.equals("pushlink")){
                text = mainObject.getString("text");
                url = mainObject.getString("url");
            }
            else if(mode.equals("morse")){
                text = mainObject.getString("text")+" ";
            }
            else if(mode.equals("vote")){ ///추가하는거
                //Log.i("vote","branch test");
                text = mainObject.getString("text");
            }
            else if(mode.equals("call"))
            {
                phoneName = mainObject.getString("phoneName");
                phoneNum = mainObject.getString("phoneNum");
                voiceName = mainObject.getString("voiceName");
                profile = mainObject.getString("profileName");
            }
            else if(mode.equals("call_down"))
            {
                // [{"mode":"call_down"},{"voiceName":"soundTest.mp3","profileName":"clipping.jpg"},{"voiceName":"soundTest.mp3","profileName":"clipping.jpg"}]
                call_down_path = mainObject.getString("path");
                for(int i= 1, j=0; i<ja.length(); i++,j++) {
                    voiceNames.add(ja.getJSONObject(i).getString("voiceName"));
                    profileNames.add(ja.getJSONObject(i).getString("profileName"));

                    String fileOwner = Global.userPref.getString("user_name", "");
                    String fileRootPath = Global.dirPath;

                    File voiceFile = new File(fileRootPath + fileOwner + "/contents/" + voiceNames.get(j));
                    File profileFile = new File(fileRootPath + fileOwner + "/contents/" + profileNames.get(j));

                    if (!voiceFile.exists()) {
                        hasVoiceFiles.add(voiceFile.exists());
                        Log.i("TestReceiver", fileRootPath + fileOwner + " !voiceFile.exists");
                    } else if(voiceFile.exists()){
                        hasVoiceFiles.add(voiceFile.exists());
                        Log.i("TestReceiver", fileRootPath + fileOwner + " voiceFile.exists");
                    }
                    if (!profileFile.exists()) {
                        hasProfileFiles.add(profileFile.exists());
                        Log.i("TestReceiver", fileRootPath + fileOwner + " !profileFile.exists");
                    } else if(profileFile.exists()){
                        hasProfileFiles.add(profileFile.exists());
                        Log.i("TestReceiver", fileRootPath + fileOwner + " profileFile.exists");
                    }
                }
            }
            else if(mode.equals("branch")){
             // [{"mode":"branch","time":"1","text":"1","choice1":"1","choice1_time":"1","choice2":"1","choice2_time":"1"}]
                text = mainObject.getString("text");

                answerChoice1 = mainObject.getString("choice1");
                answerChoice2 = mainObject.getString("choice2");

                branchTime1 = mainObject.getString("choice1_time");
                branchTime2 = mainObject.getString("choice2_time");

                totalTime = mainObject.getString("total_time");
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        mContext = context;
        sJsonText = intent.getStringExtra("message").toString();
        String title = intent.getStringExtra("title").toString();
        String name = intent.getAction();

        if(sJsonText!=null) {
            try {
                ja = new JSONArray(sJsonText);
                mainObject = ja.getJSONObject(0);
                mode = mainObject.getString("mode");
                interactiveAction(mode, mainObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (name.equals("com.websocket.websocket.send") && sJsonText !=null) {
            if (mode.equals("call_down")) {
                // Find File path php,
                Bundle extras = new Bundle();

                Object[] args = {
                        "http://"+Global.ip+"/android_FileDownload.php",
                        String.valueOf(Global.userPref.getString("user_name", "")),
                        voiceNames,
                        profileNames,
                        hasVoiceFiles,
                        hasProfileFiles,
                        call_down_path
                };

                String fileOwner = Global.userPref.getString("user_name", "");
                String fileRootPath = Global.dirPath;
                File dir = new File(fileRootPath+fileOwner + "/contents/" + call_down_path);
                Global.video_name = call_down_path;
                if (!dir.exists())     {
                    dir.mkdirs();
                }
                boolean check_exist = true;
                try {
                    for(int i=1; i<ja.length(); i++) {
                        String voice_check = fileRootPath + fileOwner + "/contents/" + call_down_path + "/" + ja.getJSONObject(i).getString("voiceName");
                        String profile_check = fileRootPath + fileOwner + "/contents/" + call_down_path + "/" + ja.getJSONObject(i).getString("profileName");
                        File voice = new File(voice_check);
                        File profile = new File(profile_check);
                        if(voice.exists()==false || profile.exists()==false){
                            check_exist=false;
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if(check_exist==false) {
                    Intent dialogIntent = new Intent(mContext, DownloadDialog.class);

                    dialogIntent.putExtra("sUrl", (String) args[0]);
                    dialogIntent.putExtra("sUserName", (String) args[1]);
                    extras.putSerializable("voiceNames", (Serializable) args[2]);
                    extras.putSerializable("profileNames", (Serializable) args[3]);
                    extras.putSerializable("hasVoiceFileList", (Serializable) args[4]);
                    extras.putSerializable("hasProfileFileList", (Serializable) args[5]);
                    dialogIntent.putExtra("sPath", (String) args[6]);

                    dialogIntent.putExtras(extras);

                    PendingIntent pi = PendingIntent.getActivity(mContext, 0, dialogIntent, PendingIntent.FLAG_ONE_SHOT);
                    try {
                        pi.send();
                    } catch (PendingIntent.CanceledException e) {
                        e.printStackTrace();
                    }
                }
                else{
                    JSONObject main = new JSONObject();
                    JSONArray arr = new JSONArray();
                    try {
                        main.put("call_down","done");
                        arr.put(main);
                        Global.mWebSocketClient.send("TEXT "+arr.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            } else if (mode.equals("push") || mode.equals("pushlink") || mode.equals("branch") || mode.equals("vote") ) {
                mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                //PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, new Intent(mContext, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
                NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext);
                builder.setSmallIcon(R.mipmap.ic_launcher);
                builder.setTicker("미리보기 입니다.");
                builder.setWhen(System.currentTimeMillis());
                builder.setContentTitle(title);
                builder.setContentText(text);
                builder.setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_LIGHTS);
                //builder.setContentIntent(pendingIntent);
                builder.setAutoCancel(true);
                builder.setPriority(Notification.PRIORITY_HIGH);
                mNotificationManager.notify(123456, builder.build());
                Intent i = new Intent(mContext, PushActivity.class); //push Activity로 간다
                i.putExtra("mode",mode);
                i.putExtra("text", text);

                if(mode.equals("pushlink")){
                    i.putExtra("url", url);
                }
                else if(mode.equals("branch")) {
                    i.putExtra("answerChoice1", answerChoice1);
                    i.putExtra("answerChoice2", answerChoice2);
                    i.putExtra("branchTime1", branchTime1);
                    i.putExtra("branchTime2", branchTime2);
                    i.putExtra("totalTime",totalTime);
                }
                else if(mode.equals("vote")){ ////vote 추가 어기서 위에처럼 더 추가해야 할듯하다.
                    Toast.makeText(context,"vote",Toast.LENGTH_LONG ).show();

                    i.putExtra("","");
                }

                i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                PendingIntent pi = PendingIntent.getActivity(mContext, 0, i, PendingIntent.FLAG_ONE_SHOT);
                try {
                    pi.send();
                } catch (PendingIntent.CanceledException e) {
                    e.printStackTrace();
                }
            } else if (mode.equals("morse")) {
                Intent i = new Intent(mContext, MorseActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                i.putExtra("text", text);
                PendingIntent pi = PendingIntent.getActivity(mContext, 0, i, PendingIntent.FLAG_ONE_SHOT);
                try {
                    pi.send();
                } catch (PendingIntent.CanceledException e) {
                    e.printStackTrace();
                }
            } else if (mode.equals("call")) {
                Intent i = new Intent(mContext, ReceiveCallActivity.class);
                i.putExtra("phoneNum", phoneNum);
                i.putExtra("phoneName", phoneName);
                i.putExtra("voiceName", voiceName);
                i.putExtra("profile", profile);
                i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                PendingIntent pi = PendingIntent.getActivity(mContext, 0, i, PendingIntent.FLAG_ONE_SHOT);
                try {
                    pi.send();
                } catch (PendingIntent.CanceledException e) {
                    e.printStackTrace();
                }
            }
//            else if(mode.equals("vote")){
//
//            }
        }
    }
}
