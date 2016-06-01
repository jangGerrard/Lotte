package com.websocket.websocket;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2015-10-14.
 */
public class ResisterActivity extends AppCompatActivity {
    private Button b_login;
    private Button b_forgot;
    private Button b_id_check;
    private Button b_email_check;
    private FloatingActionButton b_save;
    private EditText username;
    private EditText email;
    private EditText password;
    private EditText r_password;

    private Boolean checkId=false, checkPw=false;
    String registUrl = "http://"+ Global.ip+"/app_register.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.resister);
        username = (EditText) findViewById(R.id.resister_username);
        email = (EditText) findViewById(R.id.resister_email);
        password = (EditText) findViewById(R.id.resister_password);
        r_password = (EditText) findViewById(R.id.resister_repeat);

        b_id_check = (Button) findViewById(R.id.button_id_check);
        b_id_check.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Pattern id_pattern = Pattern.compile("^[a-zA-Z0-9]*$");
                Matcher matcher = id_pattern.matcher(username.getText().toString());
                if(!matcher.matches()){
                    Toast.makeText(getApplicationContext(),"ID에는 영어와 숫자만 사용 가능합니다", Toast.LENGTH_SHORT).show();
                }
                if(username.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(),"ID를 입력해주세요", Toast.LENGTH_SHORT).show();
                } else {
                    CheckTast ct = new CheckTast();
                    String[] args = {0+"",
                            username.getText().toString()};
                    ct.execute(args);
                }
            }
        });

        b_email_check = (Button) findViewById(R.id.button_email_check);
        b_email_check.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(email.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "E-mail을 입력해주세요", Toast.LENGTH_SHORT).show();
                } else {
                    CheckTast ct = new CheckTast();
                    String[] args = {1+"",
                            email.getText().toString()};
                    if(ct.execute(args).equals("success")){

                    }
                    else{

                    }

                }
            }
        });

        b_login = (Button) findViewById(R.id.button_login);
        b_login.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ResisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        b_forgot = (Button) findViewById(R.id.button_forgot);
        b_forgot.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ResisterActivity.this, ForgotActivity.class);
                startActivity(intent);
                finish();
            }
        });

        b_save = (FloatingActionButton) findViewById(R.id.button_save);
        b_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(username.getText().toString().equals("") || email.getText().toString().equals("")
                        || password.getText().toString().equals("") || r_password.getText().toString().equals("")){ // 빈칸이 존재할 때
                    Toast.makeText(getApplicationContext(), "빈 칸을 모두 채워주세요", Toast.LENGTH_SHORT).show();
                } else if(!checkPw || !checkId){
                    Toast.makeText(getApplicationContext(), "중복확인을 해주세요", Toast.LENGTH_SHORT).show();
                }
                else if(password.getText().toString().length() >= 6) {
                   if (password.getText().toString().equals(r_password.getText().toString())) { // 두 번 입력한 비밀번호가 같은지 확인
                        RegistTask registTask = new RegistTask();
                        String[] args = {registUrl,
                                username.getText().toString(),
                                password.getText().toString(),
                                r_password.getText().toString(),
                                email.getText().toString()};
                        registTask.execute(args);
                    } else { // 두 번 입력한 비밀번호가 같지 않을 때
                        Toast.makeText(getApplicationContext(), "비밀번호를 정확하게 입력하세요", Toast.LENGTH_SHORT).show();
                    }
                }
                else{ // 비밀번호의 길이가 짧을 때
                    Toast.makeText(getApplicationContext(), "비밀번호는 6글자 이상으로 설정하세요", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private class CheckTast extends AsyncTask<String, Void, String>{
        private int flag;

        @Override
        protected String doInBackground(String... params) {
            return checkIDorEmail(Integer.parseInt(params[0]),params[1]);
        }
        private String checkIDorEmail(int _flag, String sValue){
            String body = "";
            String sUrl = "";
            flag = _flag;
            if(flag == 0) {
                body = "user_name=" + sValue;
                sUrl = "http://"+Global.ip+"/id_check.php";
            }
            else if(flag ==1){
                body = "user_email=" + sValue;
                sUrl = "http://"+Global.ip+"/email_check.php";
            }
            StringBuffer response = new StringBuffer();
            try {
                URL url = new URL(sUrl);
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
                }
                in.close();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return response.toString();
        }

        @Override
        protected void onPostExecute(String response) {
            if(response.toString().equals("success")) {
                if(flag == 0) {
                    Toast.makeText(getApplicationContext(),"사용 가능한 ID 입니다", Toast.LENGTH_SHORT).show();
                    checkId = true;
                } else if(flag == 1){
                    Toast.makeText(getApplicationContext(), "사용 가능한 E-mail 입니다", Toast.LENGTH_SHORT).show();
                    checkPw = true;
                }

            }
            else if(response.toString().equals("exist")) {
                if (flag == 0) {
                    Toast.makeText(getApplicationContext(), "이미 존재하는 ID 입니다", Toast.LENGTH_SHORT).show();
                    username.setText("");
                } else if (flag == 1) {
                    Toast.makeText(getApplicationContext(), "이미 존재하는 E-mail 입니다", Toast.LENGTH_SHORT).show();
                    email.setText("");
                }
            }
            else {
                if(flag == 0) {
                    username.setText("");
                }
                else if(flag == 1){
                    Toast.makeText(getApplicationContext(), "잘못된 형식입니다", Toast.LENGTH_SHORT).show();
                    email.setText("");
                }
            }
        }
    }

    private class RegistTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            return registTask(params);
        }

        @Override
        protected void onPostExecute(String response) {
            Toast.makeText(getApplicationContext(),"회원가입 성공!", Toast.LENGTH_SHORT).show();
        }

        private String registTask(String... args) {
            StringBuffer response = new StringBuffer();
            String sUrl = args[0];
            String body = "register="+"11"+"&user_name=" + args[1] +
                    "&user_password_new=" + args[2]+"&user_password_repeat="+args[3]+
                    "&user_email="+args[4];
            try {
                URL url = new URL(sUrl);
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
            }
                in.close();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if(response.toString().equals("success"))
            {
                Intent intent = new Intent(ResisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
            return response.toString();
        }
    }
}
