package com.websocket.websocket.VoteActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.websocket.websocket.Global.Global;
import com.websocket.websocket.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class VoteActivity extends Activity {

    protected AlertDialog.Builder dialog;
    private String mode;
    private String text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vote);

        ///intent unpacking
        Intent intent = getIntent();
        mode = intent.getStringExtra("mode");
        text = intent.getStringExtra("text");


        //Toast.makeText(getApplicationContext(), "mode : " + mode + ", text : " + text, Toast.LENGTH_LONG).show();

        ///여기 까지 data잘 넘어 온다.
        //그렇다면 이제 dialog를 추가 해야 한다.

        if(mode.equals("vote")){
                ///mode:vote ,
                ///selctionCount : 3,
                ///String array : problems count;
                ////몇개의 문제를 보낼 것인지.
                ////그만큼의 문자 array를 보내면 될것 같다.

                String[] arr  = {"쪼금", "전이랑 완전 똑같아", "어 찐 것 같아."};
                AlertDialog alert = new AlertDialog.Builder(VoteActivity.this )
                        .setTitle("남친에게 듣고 싶어하는 말은?") ///문제 problem || question
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                JSONObject main = new JSONObject();
                                JSONArray arr = new JSONArray();
                                try {
                                    main.put("branch","done");
                                    main.put("branch_start",47);
                                    main.put("branch_end",82.2);
                                    arr.put(main);
                                    Global.mWebSocketClient.send("TEXT "+arr.toString());  ////어쨋든 send 하는데 정보가 done branch_start, branch_end 이라는 것
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                //dialog.dismiss();
                                finish();
                            }
                        })
//                    .setNeutralButton("힌트 보기", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            Toast.makeText(getApplicationContext(), "hint : ", Toast.LENGTH_LONG).show();
//                        }
//                    })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(getApplicationContext(), "취소를 선택하셧습니다. 자동으로 선택됨 ㅡㅡ^", Toast.LENGTH_LONG).show();
                                finish();
                            }
                        })
                        .setSingleChoiceItems(arr, -1, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ///which 가 어떤 것인지 알려주는 것
                            }
                        })
                    .show();
                alert.show();

        } ///if vote end loop

    }
}
