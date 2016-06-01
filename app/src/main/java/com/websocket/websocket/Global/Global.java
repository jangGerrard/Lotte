package com.websocket.websocket.Global;

import android.content.SharedPreferences;
import android.os.Environment;

import com.websocket.websocket.Friend.Friends;
import com.websocket.websocket.MyVideo;
import com.websocket.websocket.product.Product;

import org.java_websocket.client.WebSocketClient;

import java.util.ArrayList;

/**
 * Created by Administrator on 2015-10-16.
 */
public class Global {
    public static ArrayList<MyVideo> myVideo_list = new ArrayList<MyVideo>();
    public static ArrayList<Friends> friends_list = new ArrayList<Friends>();
    public static ArrayList<Product> product_list = new ArrayList<Product>();
    public static String user_name;
    public static String video_name;
    public static SharedPreferences userPref;
    public static String dirPath = Environment.getExternalStorageDirectory()+"/Cox/";
    public static SharedPreferences userIDPref;
    public static SharedPreferences.Editor editor;
    public static WebSocketClient mWebSocketClient = null;
    public static String ip = "192.168.43.47";
}
