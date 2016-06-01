package com.websocket.websocket;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.websocket.websocket.Global.Global;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by Administrator on 2015-10-12.
 */
public class SocketService extends Service {
    private WebSocketClient mWebSocketClient;
    String message;
    String[] opCode = new String[2];

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(mWebSocketClient!=null)
            mWebSocketClient.close();
        connectWebSocket();

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mWebSocketClient.close();
        Log.i("service","destroied");
    }

    public IBinder onBind(Intent intent) {
        return null;
    }

    private void connectWebSocket() {
        URI uri;
        try {
            uri = new URI("ws://"+Global.ip+":1238/websocket/server.php");
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return;
        }

        mWebSocketClient = new WebSocketClient(uri) {
            @Override
            public void onOpen(ServerHandshake serverHandshake) {
                Log.i("Websocket", "Opened");
                Global.userPref = getSharedPreferences("userPref", Activity.MODE_PRIVATE);
                Global.editor = Global.userPref.edit();
                mWebSocketClient.send("CHECKUSERS ");
            }

            @Override
            public void onMessage(String s) {
                message = "";

                String[] args;
                args = s.split(" ");
                opCode[0] = args[0];
                Log.i("opCODE", s);
                // ONTEXT user_name WebSocket_Message
                if(opCode[0].equals("CHECKUSERS")){
                    for(int i=1;i<args.length; i++){
                        if(args[i].equals("android_" + String.valueOf(Global.userPref.getString("user_name", "")))){
                            mWebSocketClient.send("SERVERRE android_" + String.valueOf(Global.userPref.getString("user_name", "")));
                        }
                    }
                    if (Global.userPref.getString("user_name", "").length() > 1) {
                        mWebSocketClient.send("JOIN android_" + String.valueOf(Global.userPref.getString("user_name", "")));
                    }
                }
                else if (opCode[0].equals("ONTEXT") && args[1].equals(String.valueOf(Global.userPref.getString("user_name", "")))) {
                    for (int i = 2; i < args.length; i++) {
                        message += args[i] + " ";
                    }
                    Intent intent = new Intent("com.websocket.websocket.send");
                    intent.putExtra("title", "Cox Studio");
                    intent.putExtra("message", message);
                    intent.putExtra("Operation Code", opCode[0]);
                    sendBroadcast(intent);
                }
                else if (opCode[0].equals("SERVER") && args[1].equals("refresh")) {
                    Global.myVideo_list.clear();
                    Global.editor.clear();
                    Global.editor.commit();
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    stopService(new Intent(getApplicationContext(), SocketService.class));
                    startActivity(intent);
                }
            }

            @Override
            public void onClose(int i, String s, boolean b) {
                Log.i("Websocket", "Closed " + s);
            }

            @Override
            public void onError(Exception e) {
                Log.i("Websocket", "Error " + e.getMessage());
            }
        };
        mWebSocketClient.connect();
        // WebSocket 통신하기 위한 변수를 Global로 지정
        Global.mWebSocketClient = mWebSocketClient;
    }
}
