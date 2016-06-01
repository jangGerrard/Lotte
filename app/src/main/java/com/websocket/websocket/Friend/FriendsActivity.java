package com.websocket.websocket.Friend;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
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

import com.websocket.websocket.Global.Global;
import com.websocket.websocket.ListViewAdapter;
import com.websocket.websocket.LoginActivity;
import com.websocket.websocket.MainActivity;
import com.websocket.websocket.MyVideo;
import com.websocket.websocket.Network.NetworkUtil;
import com.websocket.websocket.R;
import com.websocket.websocket.SocketService;

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

public class FriendsActivity extends AppCompatActivity {
    private ArrayAdapter<Friends> friends_listView_adapter;
    private ListView custom_listview;
    private Button b_load;
    private Intent serviceIntent;
    private LinearLayout main_progress;
    private View footerView_loading;
    private  View footerView_reloading;
    private int temp = 0;

    private String searchUrl = "http://"+ Global.ip+"/video_search.php";

    private SwipeRefreshLayout swipeContainer;

    private String scrollFlag = "";
    private boolean lastItemVisibleFlag;
    private boolean isInternetPresent = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

        Log.d("test", "s");

        stopService(new Intent(FriendsActivity.this, SocketService.class));
        serviceIntent = new Intent(this.getBaseContext(), SocketService.class);
        startService(serviceIntent);
        Global.userPref =getSharedPreferences("userPref", Activity.MODE_PRIVATE);
        Global.editor = Global.userPref.edit();

        VideoSearch videoSearch = new VideoSearch();
        videoSearch.execute(searchUrl, Global.userPref.getString("user_name", ""));

        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkReceiver, filter);

        main_progress = (LinearLayout) findViewById(R.id.friends_progress);
        custom_listview = (ListView) findViewById(R.id.friends_list_view);

        friends_listView_adapter = new FriendsAdapter(FriendsActivity.this, R.layout.custom_listview);



        footerView_loading = LayoutInflater.from(getApplicationContext()).inflate(R.layout.loding, null); // 스크롤 하단의 progress bar
        footerView_reloading = LayoutInflater.from(getApplicationContext()).inflate(R.layout.reloading, null); // 네트워크가 끊겼을 때 하단의 ui

        custom_listview.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
            }

            @Override
            public void onScroll(AbsListView absListView, int firstItem, int ItemCount, int totalItemCount) {
                int count = totalItemCount - ItemCount;

                if (firstItem >= count && totalItemCount != 0 && lastItemVisibleFlag == false && scrollFlag.equals("end") == false) {
                    custom_listview.addFooterView(footerView_loading);
                    lastItemVisibleFlag = true;
                    VideoSearch videoSearch = new VideoSearch();
                    videoSearch.execute(searchUrl, Global.userPref.getString("user_name", ""));
                }
            }
        });

        b_load = (Button) footerView_reloading.findViewById(R.id.button_load);
        b_load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isInternetPresent){
                    Toast.makeText(getApplicationContext(), "인터넷 연결을 다시 확인해주세요", Toast.LENGTH_SHORT).show();
                } else {
                    custom_listview.removeFooterView(footerView_reloading);
                    VideoSearch videoSearch = new VideoSearch();
                    videoSearch.execute(searchUrl, Global.userPref.getString("user_name", ""));
                }
            }
        });
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        Global.myVideo_list.clear();
        unregisterReceiver(networkReceiver);
    }

    private BroadcastReceiver networkReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String status = NetworkUtil.getConnectivityStatusString(context);
            if(status.equals("Wifi enabled") || status.equals("Mobile data enabled")){
                isInternetPresent = true;
            } else if(status.equals("Not connected to Internet")){
                isInternetPresent = false;
                scrollFlag = "end";
            }


            showNetworkState();
        }
    };

    public void showNetworkState(){
        if(!isInternetPresent) {
            custom_listview.removeFooterView(footerView_loading);
            custom_listview.addFooterView(footerView_reloading);
        }
    }
////////////////////////////////////////////////////////////////////////////////////////////////////

    private class VideoSearch extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            return search(params[0], params[1]);
        }

        private String search(String sUrl, String user_name) {
            StringBuilder jsonHtml = new StringBuilder();
            try {
                String body = "user_name=" + user_name + "&temp=" + String.valueOf(temp);

                URL url = new URL(sUrl);

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
                wr.writeBytes(body);
                wr.flush();
                wr.close();

                InputStreamReader rd = new InputStreamReader(conn.getInputStream());

                BufferedReader in = new BufferedReader(rd);
                String inputLine = "";

                while ((inputLine = in.readLine()) != null) {
                    jsonHtml.append(inputLine);
                }

                in.close();
            }catch (IOException e) {
                e.printStackTrace();
            }


            try {
                int size = Global.myVideo_list.size();

                if (jsonHtml.toString().equals("end")) {
                    scrollFlag = "end";
                    return "end";
                } else {
                    JSONArray ja = new JSONArray(jsonHtml.toString());
                    for (int i = 0; i < ja.length(); i++) {
                        JSONObject jo = ja.getJSONObject(i);
                        MyVideo myVideo = new MyVideo(null, jo.getString("video_title"), jo.getString("video_writer"), jo.getString("video_path"), jo.getString("video_thumbnail"));
                        Global.myVideo_list.add(myVideo);
                        temp++;

                        if (Global.myVideo_list.get(size + i).thumbnail_path != "null") { // Thumbnail 받아오는 부분
                            InputStream is = null;

                            String q = "user_name=" + user_name + "&file_name=" + Global.myVideo_list.get(size + i).thumbnail_path + "&file_path=/var/www/uploads/"+user_name +"/contents/"+Global.myVideo_list.get(size+i).title;
                            Log.d("post",q);
                            URL img_url = new URL("http://"+Global.ip+"/android_FileDownload.php");
                            HttpURLConnection con = (HttpURLConnection) img_url.openConnection();
                            con.setRequestMethod("POST");
                            con.setDoInput(true);
                            con.setDoOutput(true);

                            DataOutputStream w = new DataOutputStream(con.getOutputStream());
                            w.writeBytes(q);
                            w.flush();
                            w.close();

                            is = con.getInputStream();

                            Global.myVideo_list.get(size + i).image = BitmapFactory.decodeStream(is);
                        }
                    }
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            catch(JSONException e) {
                e.printStackTrace();
            }
            return "success";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                if(!s.equals("end") && isInternetPresent) scrollFlag = "";
                custom_listview.setVisibility(View.VISIBLE);
                main_progress.setVisibility(View.GONE);
                int position = custom_listview.getLastVisiblePosition();
                custom_listview.removeFooterView(footerView_loading);
                custom_listview.setAdapter(friends_listView_adapter);
                friends_listView_adapter.notifyDataSetChanged();


                custom_listview.setSelectionFromTop(position, 0);
                lastItemVisibleFlag = false;
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
/////