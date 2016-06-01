package com.websocket.websocket.test;

import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.websocket.websocket.Friend.Friends;
import com.websocket.websocket.Friend.FriendsDao;
import com.websocket.websocket.Global.Global;
import com.websocket.websocket.R;

import java.util.ArrayList;

public class ForSearchViewActivity extends ListActivity {

//    private ArrayAdapter<Friends> friendsAdapter = new ArrayAdapter<Friends>(this, android.R.layout.simple_list_item_1, FriendsDao.getInstance());
    private MyCustomAdapter mAdapter ;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAdapter = new MyCustomAdapter();
        for (int i = 0; i < 50; i++) {
            mAdapter.addItem("item " + i);
        }
        setListAdapter(mAdapter);
    }

    private class MyCustomAdapter extends BaseAdapter {

        private ArrayList mData = new ArrayList();
        private LayoutInflater mInflater;

        public MyCustomAdapter() {
            mInflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        public void addItem(final String item) {
            mData.add(item);
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return mData.size();
        }

        @Override
        public String getItem(int position) {
            return (String)mData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            System.out.println("getView " + position + " " + convertView);
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.item_friends, null);
                holder = new ViewHolder();
                holder.textView = (TextView)convertView.findViewById(R.id.friends_id);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder)convertView.getTag();
            }
            holder.textView.setText(mData.get(position)+"");
            return convertView;
        }

    }

    public static class ViewHolder {
        public TextView textView;
    }

}
