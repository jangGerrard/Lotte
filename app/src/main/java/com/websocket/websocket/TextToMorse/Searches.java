package com.websocket.websocket.TextToMorse;

public class Searches {
    private static final int CODE = 1;

    public static int BinarySearch(String key, Alphabet[] a, int type) {
        int low = 0;
        int high = a.length - 1;
        int mid;

        while( low <= high )
        {
            mid = ( low + high ) / 2;
            int cmp = Compare(key, a[mid], type);
            if(cmp < 0 ){
                low = mid + 1;
            }else if(cmp > 0 ){
                high = mid - 1;
            }else{
                return mid;
            }
        }
        return -1;
    }
    private static int Compare(String s, Alphabet a, int type){
        if(type == CODE){
            return a.getCode().compareTo(s);
        }else{
            return a.getLetter().compareTo(s);
        }
    }
}
