package com.websocket.websocket;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.websocket.websocket.Friend.FriendsActivity;
import com.websocket.websocket.Friend.SearchActivity;
import com.websocket.websocket.Global.Global;
import com.websocket.websocket.Network.NetworkUtil;
import com.websocket.websocket.TTS.TTSActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    Button sttBtn ;
    Button barcodeBtn;
    Button cartBtn;
    Button newBarcodeBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sttBtn = (Button)findViewById(R.id.speak_to_text_search_btn);
        barcodeBtn = (Button)findViewById(R.id.barcode_search_btn);
        cartBtn = (Button)findViewById(R.id.cartBtn);
        newBarcodeBtn = (Button) findViewById(R.id.new_barcode_search_btn);
                //barcode_search_btn cartBtn speak_to_text_search_btn
        sttBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SpeckToText.class);
                startActivity(intent);
            }
        });
        barcodeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, BarcodeActivity.class);
                startActivity(intent);
            }
        });

        cartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CartActivity.class);
                startActivity(intent);
            }
        });
        newBarcodeBtn.setOnClickListener(new View.OnClickListener(

        ) {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Barcode2Activity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem logout = menu.findItem(R.id.logout);
        MenuItem sToText = menu.findItem(R.id.sp_to_text); //
        MenuItem cart = menu.findItem(R.id.cart); //
        MenuItem barcode = menu.findItem(R.id.barcode_menu); //



        logout.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Global.editor.clear();
                Global.editor.commit();

                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                return true;
            }
        });

        sToText.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener(){
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                //Toast.makeText(getApplicationContext(), "SpeakToText", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(MainActivity.this, SpeckToText.class);
                startActivity(intent);
                return true;
            }
        });

        cart.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener(){
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                //Toast.makeText(getApplicationContext(), "Cart", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(MainActivity.this, CartActivity.class);
                startActivity(intent);
                return true;
            }
        });

        barcode.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener(){
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                //Toast.makeText(getApplicationContext(), "Barcode", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(MainActivity.this, BarcodeActivity.class);
                startActivity(intent);
                return true;
            }
        });

////////////////////////////////////////////////////////////////////////////////////////////////////


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    protected void onDestroy(){
        super.onDestroy();
    }

}
