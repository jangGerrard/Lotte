package com.websocket.websocket.Network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.websocket.websocket.SocketService;

/**
 * Created by Administrator on 2015-10-12.
 */
public class NetworkChangeReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(final Context context, final Intent intent) {
        String status = NetworkUtil.getConnectivityStatusString(context);
        if(status.equals("Wifi enabled")) {
            context.startService(new Intent(context, SocketService.class));
        }else if(status.equals("Mobile data enabled")){
            context.startService(new Intent(context, SocketService.class));
        }
        else if(status.equals("Not connected to Internet")) {
            context.stopService(new Intent(context, SocketService.class));
        }
    }
}
