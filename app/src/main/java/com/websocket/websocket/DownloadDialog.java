package com.websocket.websocket;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;

import com.websocket.websocket.Global.Global;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

/**
 * Created by Administrator on 2015-11-09.
 */


public class DownloadDialog extends Activity {

    private ProgressDialog dlg;

    private Intent intent;
    private String[] args;

    private ArrayList<String> voiceNames = new ArrayList<>();
    private ArrayList<String> profileNames = new ArrayList<>();

    private ArrayList<Boolean> hasVoiceFiles = new ArrayList<>();
    private ArrayList<Boolean> hasProfileFiles = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.push);
        intent = getIntent();

        Object o1 = intent.getExtras().get("voiceNames");
        Object o2 = intent.getExtras().get("profileNames");
        Object o3 = intent.getExtras().get("hasVoiceFileList");
        Object o4 = intent.getExtras().get("hasProfileFileList");

        voiceNames = (ArrayList<String>) o1;
        profileNames = (ArrayList<String>) o2;

        // 중복 파일 확인
        hasVoiceFiles = (ArrayList<Boolean>) o3;
        hasProfileFiles = (ArrayList<Boolean>) o4;

        args = new String[]{
                intent.getStringExtra("sUrl"),
                intent.getStringExtra("sUserName"),
                intent.getStringExtra("sPath"),
        };


        AlertDialog.Builder dialog = new AlertDialog.Builder(DownloadDialog.this, AlertDialog.THEME_HOLO_LIGHT);
        dialog.setMessage("Interactive 파일을 전송하려고 합니다.\n" + "Download 하시겠습니까?").setCancelable(false)
                .setPositiveButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        finish();
                    }
                }).setNegativeButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // Action for 'NO' Button
                new CallDownTask().execute(args);
                //finish();
            }
        });
        AlertDialog adialog =  dialog.show();
        TextView messageText = (TextView)adialog.findViewById(android.R.id.message);
        messageText.setGravity(Gravity.CENTER);
        adialog.show();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
    private class CallDownTask extends AsyncTask<String, String, Integer> {
        int taskCnt = voiceNames.size() + profileNames.size();

        @Override
        protected void onPreExecute() {
            dlg = new ProgressDialog(DownloadDialog.this);
            dlg.setCancelable(false);
            dlg.setMessage("FileDownLoad...");
            dlg.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            dlg.show();


            super.onPreExecute();
        }

        @Override
        protected Integer doInBackground(String... params) {
            // 파일의 총 개수
            publishProgress("max", Integer.toString(taskCnt));
            for (int k = 0; k < taskCnt; k++) {
                publishProgress("progress", Integer.toString(k), "작업 번호 " + Integer.toString(k+1) + "번 수행중");
                if(k < profileNames.size()){
                    if (!hasProfileFiles.get(k))
                        ImageFileDownload(profileNames.size(), profileNames.get(k), params);
                }
                else{
                    if (!hasVoiceFiles.get(k - profileNames.size()))
                        VoiceFileDownload(voiceNames.size(), voiceNames.get(k-profileNames.size()), params);
                }
                //작업 진행 마다 진행률을 갱신하기 위해 진행된 개수와 설명을 publishProgress() 로 넘겨줌.
            }
            return taskCnt;
        }

        // Image Download
        private void ImageFileDownload(int count, String fileName, String... params) {
            FileOutputStream fos = null;
            InputStream is = null;
            String fileOwner = Global.userPref.getString("user_name", "");
            String fileRootPath = Global.dirPath;
            String video_path = fileRootPath + fileOwner + "/contents/" + params[2]+"/" +fileName;

            // 일단은 두개를 다 보낸다
            String body = "user_name=" + params[1] + "&file_name=" + fileName + "&flag=0&file_path=/var/www/uploads/"+params[1] +"/contents/"+params[2];

            try {
                fos = new FileOutputStream(video_path);
                URL url = new URL(params[0]);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
                wr.writeBytes(body);
                wr.flush();
                wr.close();

                is = conn.getInputStream();

                Bitmap bitmap = BitmapFactory.decodeStream(is);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);

                conn.disconnect();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (fos != null)
                        fos.close();
                    if (is != null)
                        is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        private void VoiceFileDownload(int count, String fileName, String... params) {
            FileOutputStream fos = null;
            InputStream is = null;
            String fileOwner = Global.userPref.getString("user_name", "");
            String fileRootPath = Global.dirPath;
            String video_path = fileRootPath + fileOwner + "/contents/" + params[2]+"/" +fileName;
            // 일단은 두개를 다 보낸다
            String body = "user_name=" + params[1] + "&file_name=" + fileName + "&flag=0&file_path=/var/www/uploads/"+params[1] +"/contents/"+params[2];

            try {
                fos = new FileOutputStream(video_path);
                URL url = new URL(params[0]);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
                wr.writeBytes(body);
                wr.flush();
                wr.close();

                is = conn.getInputStream();

                byte[] buffer = new byte[1024];
                int readBytes;

                while ((readBytes = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, readBytes);
                }
                conn.disconnect();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (fos != null)
                        fos.close();
                    if (is != null)
                        is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        protected void onPostExecute(Integer response) {
            super.onPostExecute(response);

            dlg.dismiss();
            finish();
            ////////////////////////////////////////////////////////////////
            JSONObject main = new JSONObject();
            JSONArray arr = new JSONArray();
            try {
                main.put("call_down","done");
                arr.put(main);
                Global.mWebSocketClient.send("TEXT "+arr.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            ////////////////////////////////////////////////////////////////
        }

        @Override
        protected void onProgressUpdate(String... progress)        {
            if (progress[0].equals("progress")) {
                dlg.setProgress(Integer.parseInt(progress[1]));
                dlg.setMessage(progress[2]);
            }
            else if (progress[0].equals("max"))
            {
                dlg.setMax(Integer.parseInt(progress[1]));
            }

        }


    }
}
