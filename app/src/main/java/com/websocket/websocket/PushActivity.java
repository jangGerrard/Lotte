package com.websocket.websocket;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import com.websocket.websocket.Global.Global;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * Created by Administrator on 2015-11-04.
 */
public class PushActivity extends Activity {
    protected AlertDialog.Builder dialog;
    private Intent intent;
    private WebView mWebView;
    private String mode;

    private ArrayList<Double> branchTimes;

    static String push_Url, push_message;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.push);

        //Toast.makeText(getApplicationContext(), "PushActivity", Toast.LENGTH_LONG  ).show();

        intent = getIntent();
        push_message = intent.getStringExtra("text");   //text를 받고
        mode = intent.getStringExtra("mode");           //mode를 받고



        dialog = new AlertDialog.Builder(PushActivity.this, AlertDialog.THEME_HOLO_LIGHT);
        dialog.setCancelable(false);
        if(!mode.equals("vote")) {
            dialog.setMessage(push_message);
        }
        if(mode.equals("push")) {
            // Push Message 생성
            dialog.setTitle("Message");
            dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    finish();
                }
            });

        }else if(mode.equals("pushlink")){
            push_Url = intent.getStringExtra("url"); // Url을 담는다.
            mWebView = (WebView) findViewById(R.id.WebView1);

            // Url로 이동할 수 있는 Dialog 생성 - Webview 사용

            dialog.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(push_Url)); //암시적 인텐트
                    startActivity(intent);
//                        mWebView.getSettings().setJavaScriptEnabled(true);
//                        mWebView.loadUrl(push_Url);
//                        mWebView.setWebViewClient(new AdWebViewClient());
//                        mWebView.setVisibility(View.VISIBLE);
                }
            });
            dialog.setPositiveButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
        }
        else if(mode.equals("branch")){
            branchTimes = new ArrayList<>();
            branchTimes.add(Double.parseDouble(intent.getStringExtra("branchTime1")));
            branchTimes.add(Double.parseDouble(intent.getStringExtra("branchTime2")));
            branchTimes.add(Double.parseDouble(intent.getStringExtra("totalTime")));

            Collections.sort(branchTimes);  // 오름차순   ///branch될때 어떤부분으로 갈지를 정보를 가지고 있고

            dialog.setNegativeButton(intent.getStringExtra("answerChoice1"), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    int i = branchTimes.indexOf(Double.parseDouble(intent.getStringExtra("branchTime1")));
                    JSONObject main = new JSONObject();
                    JSONArray arr = new JSONArray();
                    try {
                        main.put("branch","done");
                        main.put("branch_start",branchTimes.get(i));
                        main.put("branch_end",branchTimes.get(i+1));
                        arr.put(main);
                        Global.mWebSocketClient.send("TEXT "+arr.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    //Global.mWebSocketClient.send("TEXT branch " + branchTimes.get(i) + " " + branchTimes.get(i+1));
                    //Global.mWebSocketClient.send("TEXT [{'mode':'branch', 'starttime':" + branchTimes.get(i) + ", 'endtime':" + branchTimes.get(i+1)+"}]");
                    finish();
                }
            });
            dialog.setPositiveButton(intent.getStringExtra("answerChoice2"), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            int i = branchTimes.indexOf(Double.parseDouble(intent.getStringExtra("branchTime2")));
                            //Global.mWebSocketClient.send("TEXT branch " + branchTimes.get(i) + " " + branchTimes.get(i+1));
                            JSONObject main = new JSONObject();
                            JSONArray arr = new JSONArray();
                            try {
                                main.put("branch","done");
                                main.put("branch_start",branchTimes.get(i));
                                main.put("branch_end",branchTimes.get(i+1));
                                arr.put(main);
                                Global.mWebSocketClient.send("TEXT "+arr.toString());  ////어쨋든 send 하는데 정보가 done branch_start, branch_end 이라는 것
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            //Global.mWebSocketClient.send("TEXT [{'mode':'branch', 'starttime':" + branchTimes.get(i) + ", 'endtime':" + branchTimes.get(i+1)+"}]");
                            finish();
                }
            });

        } else if(mode.equals("vote")){

            //여기서 받을 것들 싼다.

            dialog.setTitle(push_message);
            String[] arr = new String[] {"aaaa", "bbbb", "cccc", "dddd"};


            //감싸서 보낼꺼는 보내고,
            dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            dialog.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            dialog.setSingleChoiceItems(arr, -1, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(getApplicationContext(), "onClick singleChioceITems", Toast.LENGTH_LONG).show();
                }
            });

        }
        AlertDialog adialog =  dialog.show();
        if(!mode.equals("vote")) {
            TextView messageText = (TextView) adialog.findViewById(android.R.id.message);    //message 가 중앙으로 갑니다.
            messageText.setGravity(Gravity.CENTER);
        }
        adialog.show();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if((keyCode == KeyEvent.KEYCODE_BACK)&& mWebView.canGoBack()){
            mWebView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode,event);
    }
    private class AdWebViewClient extends WebViewClient{

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

}
