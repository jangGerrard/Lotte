package com.websocket.websocket.Friend;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.websocket.websocket.Global.Global;
import com.websocket.websocket.MyVideo;
import com.websocket.websocket.R;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity
        implements SearchView.OnQueryTextListener,
                    SearchView.OnCloseListener{

    private ListView m_ListView;

    private ArrayAdapter<Friends> friendsArrayAdapter;
    private ArrayAdapter<Friends> queryArrayAdapter;

    private  View footerView_loading;
    private  View footerView_reloading;

    private boolean lastItemVisibleFlag;

    private SearchView searchFriend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_friend);

        m_ListView = (ListView) findViewById(R.id.friends_list_view);
        friendsArrayAdapter = new FriendsAdapter(getApplicationContext(), R.layout.item_friends );
        friendsArrayAdapter.addAll(FriendsDao.getInstance());
        footerView_loading = LayoutInflater.from(getApplicationContext()).inflate(R.layout.loding, null); // 스크롤 하단의 progress bar
        footerView_reloading = LayoutInflater.from(getApplicationContext()).inflate(R.layout.reloading, null); // 네트워크가 끊겼을 때 하단의 ui

        searchFriend = (SearchView)findViewById(R.id.search_friend);

        m_ListView.setAdapter(friendsArrayAdapter);
        //friendsArrayAdapter.clear();

        searchFriend.setOnQueryTextListener(this);
        searchFriend.setOnCloseListener(this);
        searchFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchFriend.setActivated(true);
            }
        });
        searchFriend.setActivated(true);

    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        Log.i("SearchActivity", "onQueryTextSubmit");
        displayResult(query);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String query) {
        Log.i("SearchActivity", "onQueryTextChange");
        displayResult(query);
        return true;
    }

    @Override
    public boolean onClose() {
        m_ListView.setAdapter(friendsArrayAdapter);
        return false;
    }

    public void displayResult(String query){
        SearchHelper searchHelper = new SearchHelper();
        ArrayList<Friends> fs = searchHelper.searchByInputText(query);
        for (Friends f:
             fs) {
            Log.i("SearchActivity", "friends :  " + f.f_id);
        }
        Log.i("SearchActivity", "fs.size() : " + fs.size());

        queryArrayAdapter = new FriendsAdapter(getApplicationContext(), R.layout.item_friends);
        queryArrayAdapter.addAll(fs);

        m_ListView.setAdapter(queryArrayAdapter);

    }



}