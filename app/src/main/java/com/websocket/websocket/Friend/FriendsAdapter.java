package com.websocket.websocket.Friend;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.websocket.websocket.Global.Global;
import com.websocket.websocket.R;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016-01-21.
 */
public class FriendsAdapter extends ArrayAdapter<Friends>{

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

    public FriendsAdapter(Context context, int layoutViewResourceId) {
        super(context,layoutViewResourceId);
        this.context = context;
    }

    public long getItemId(int position){
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        final int pos = position;

        if (convertView == null){
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_friends, parent, false);

            title = (TextView) convertView.findViewById(R.id.friends_id);

            holder = new CustomHolder();
            holder.title = title;

            convertView.setTag(holder);
        } else {
            holder = (CustomHolder) convertView.getTag();
            title = holder.title;
        }

        title.setText(getItem(position).f_id);



        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                String s_position = String.valueOf(pos);
                Toast.makeText(context, s_position, Toast.LENGTH_SHORT).show();

            }
        });

        return convertView;
    }
}
