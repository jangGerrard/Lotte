package com.websocket.websocket;

import android.app.WallpaperInfo;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.service.wallpaper.WallpaperService;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.websocket.websocket.Global.Global;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

/**
 * Created by Administrator on 2015-10-27.
 */
public class ListViewAdapter extends ArrayAdapter<MyVideo> {
    private Context context;

    CustomHolder holder = null;
    ImageView image = null;
    TextView title = null;
    TextView writer = null;

    private class CustomHolder {
        ImageView image;
        TextView title;
        TextView writer;
    }

    public long getItemId(int position){
        return position;
    }

    public ListViewAdapter(Context context, int layoutViewResourceId){
        super(context, layoutViewResourceId, Global.myVideo_list);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        final int pos = position;

        if (convertView == null){
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.custom_listview, parent, false);

            image = (ImageView) convertView.findViewById(R.id.list_image);
            title = (TextView) convertView.findViewById(R.id.list_title);
            writer = (TextView) convertView.findViewById(R.id.list_writer);

            holder = new CustomHolder();
            holder.image = image;
            holder.title = title;
            holder.writer = writer;

            convertView.setTag(holder);
        } else {
            holder = (CustomHolder) convertView.getTag();
            image = holder.image;
            title = holder.title;
            writer = holder.writer;
        }

        image.setImageBitmap(Global.myVideo_list.get(position).image);
        title.setText(Global.myVideo_list.get(position).title);
        writer.setText(Global.myVideo_list.get(position).writer);

        convertView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v){
                String s_position = String.valueOf(pos);
                //Toast.makeText(context, s_position, Toast.LENGTH_SHORT).show();
            }
        });

        return convertView;
    }
}
