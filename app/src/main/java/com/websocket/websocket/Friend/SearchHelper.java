package com.websocket.websocket.Friend;

import android.util.Log;

import java.util.ArrayList;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created by busanetri06 on 2016-01-21.
 */
public class SearchHelper {
    private ArrayList<Friends> friendes;

    public Map<String, Friends> maps;

    public SearchHelper(){
        friendes = FriendsDao.getInstance();
        //mapSetting();
    }

    public void mapSetting(){
        for(Friends f : friendes){
            maps.put(f.f_id, f);
        }
    }

    public ArrayList<Friends> searchByInputText(String inputText){  //db에서 가져올때 이부분을 수정하면 됩니다.
        ArrayList<Friends> temp = new ArrayList<Friends>();
        for(int i = 0 ; i < friendes.size(); i++){
            if(Pattern.matches(".*"+inputText+".*", friendes.get(i).f_id)) {
                    temp.add(friendes.get(i));
            }
        }
        Log.i("teeeeeeeeeeest", "temp size : " + temp.size());
        return temp;
    }

}
