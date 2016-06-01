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

/**
 * Created by Administrator on 2015-10-14.
 */
public class ForgotActivity extends AppCompatActivity {
    private Button b_resiter;
    private Button b_login;
    private FloatingActionButton b_find;
    private EditText email;
    private String sUrl_find = "http://"+ Global.ip+"/forget_password.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot_password);
        email = (EditText)findViewById(R.id.forgot_email);

        b_resiter = (Button) findViewById(R.id.button_resister);
        b_resiter.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ForgotActivity.this, ResisterActivity.class);
                startActivity(intent);
                finish();
            }
        });

        b_login = (Button) findViewById(R.id.button_login);
        b_login.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ForgotActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        b_find = (FloatingActionButton) findViewById(R.id.button_find);
        b_find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findPasswordTask ft = new findPasswordTask();
                ft.execute(sUrl_find,email.getText().toString());
            }
        });
    }
    private class findPasswordTask extends AsyncTask<String, Void, String>{
        @Override
        protected String doInBackground(String... params) {
            return submit(params[0], params[1]);
        }
        private String submit(String sUrl, String email){
            URL url = null;
            StringBuffer response = new StringBuffer();
            String body = "user_email="+email;
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
            return response.toString();
        }
        protected void onPostExecute(String s){
            super.onPostExecute(s);
            if(s.equals("success")){
                Toast.makeText(getApplicationContext(), "메일을 발송했습니다.", Toast.LENGTH_SHORT).show();
            }
            else if(s.equals("mail fail")){
                Toast.makeText(getApplicationContext(), "존재하지 않는 메일입니다.", Toast.LENGTH_SHORT).show();
            }
            else if(s.equals("fail"))
            {
                Toast.makeText(getApplicationContext(), "Error!!!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
