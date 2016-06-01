package com.websocket.websocket.Friend;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by busanetri06 on 2016-01-21.
 */
public class FriendsDao {

    private static ArrayList<Friends> friendes = new ArrayList<Friends>();


    public static ArrayList<Friends> getInstance(){

        if(friendes.size() <= 0) {
            for (int i = 0; i < 10; i++) {
                Friends f = new Friends("friend_id" + i);
                friendes.add(f);
            }
        }

        return friendes;
    }
}
