package com.websocket.websocket;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.melnykov.fab.FloatingActionButton;
import com.websocket.websocket.Global.Global;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;


public class LoginActivity extends AppCompatActivity {
    private Button b_resiter;
    private Button b_forgot;
    private FloatingActionButton b_next;
    private EditText username;
    private EditText password;
    String userUrl = "http://"+Global.ip+":11000/LotteSpec"+"/android/login.lotte";

    private String user_name;
    private String user_password;
//    SharedPreferences userPref;
//    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        //Toast.makeText(getApplicationContext(), "testtest", Toast.LENGTH_LONG).show();

        Global.userPref = getSharedPreferences("userPref", Activity.MODE_PRIVATE);

//        if(String.valueOf(Global.userPref.getString("user_name", "")) != ""){
//            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//            startActivity(intent);
//            finish();
//        }

        b_resiter = (Button) findViewById(R.id.button_resister);
        b_resiter.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, ResisterActivity.class);
                startActivity(intent);
                finish();
            }
        });

        b_forgot = (Button) findViewById(R.id.button_forgot);
        b_forgot.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, ForgotActivity.class);
                startActivity(intent);
                finish();
            }
        });


        b_next = (FloatingActionButton) findViewById(R.id.button_next);
        b_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginTask logintask = new LoginTask();
                username = (EditText) findViewById(R.id.login_username);
                password = (EditText) findViewById(R.id.login_password);
                //Toast.makeText(getApplicationContext(), "login Btn", Toast.LENGTH_LONG).show();
                logintask.execute(userUrl, username.getText().toString(), password.getText().toString()).toString();
            }
        });
    }

    private class LoginTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            return login(params[0],params[1],params[2]);
        }

        private String login(String sUrl, String user_name, String user_password) {
            URL url = null;
            StringBuffer response = new StringBuffer();
            String body = "user_name=" + user_name + "&user_password=" + user_password;
            Log.d("aaa", "login");
            try {
                url = new URL(sUrl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
                wr.writeBytes(body);
                wr.flush();
                wr.close();

                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                    // DB에 있으면 Login되도록
                }
                in.close();
                conn.disconnect();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(response.toString().equals("success"))
            {
                Log.d("aaa", "login" + "success");
                Global.userPref = getSharedPreferences("userPref", Activity.MODE_PRIVATE);
                Global.user_name = user_name;
                Global.editor = Global.userPref.edit();
                Global.editor.putString("user_name",user_name);
                Global.editor.commit();

                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
            return response.toString();
        }

        @Override
        protected void onPostExecute(String s){
            super.onPostExecute(s);
            if(username.getText().toString().equals("") || password.getText().toString().equals("")){
                Toast.makeText(getApplicationContext(), "빈 칸을 채워주세요", Toast.LENGTH_SHORT).show();
            }else if(s.equals("id fail")){
                Toast.makeText(getApplicationContext(), "아이디가 틀렸습니다", Toast.LENGTH_SHORT).show();
            }else if(s.equals("password fail")){
                Toast.makeText(getApplicationContext(), "비밀번호가 틀렸습니다", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
