package com.websocket.websocket.TextToMorse;

/**
 * Created by Josh Voskamp.
 */

import com.websocket.websocket.TextToMorse.Alphabet;

import java.util.ArrayList;

public class Translator {
    private Alphabet[] language;
    private static final int LETTER = 0, UNIT = 100;//By moding the unit size you can increase or decrease the length of a dot/dash


    Translator(Alphabet[] list){
        this.language = list;
    }
    public String TextToMorseCode(String message){
        message = message.toUpperCase();
        String code = "";
        for(int i = 0; i<message.length(); i++){
            code = code + CharToCode(message.charAt(i));
        }
        return code;
    }

    public long[] CodeToTime(String code){
        ArrayList<Integer> list = new ArrayList<Integer>();
        int k = 0;//Counter
        list.add(0);
        k++;
        for(int i = 0; i<code.length(); i++){
            String s  = String.valueOf(code.charAt(i));
            if(s.equals("-")){
                list.add(3*UNIT);//Dash
                k++;
                list.add(UNIT);//Pause
                k++;
            }else if(s.equals(".")){
                list.add(UNIT);//Dot
                k++;
                list.add(UNIT);//Pause
                k++;
            }else if(s.equals(",")){
                int old = list.get(k-1);
                list.set(k-1, old+2*UNIT);//Add 2 UNITS for each comma
            }else if(s.equals(" ")){
                int old = list.get(k-1);
                list.set(k-1, old+4*500);//Add just 4 UNITS for the space because of the comma and the default pause before it total is 7 UNITS
                i++;//To skip over the following comma
            }else if(s.equals("/")){
                int old = list.get(k-1);
                list.set(k-1, old+7*UNIT);//Add 7 UNIT for period, because of the comma and the default pause before it total is 10 UNITS
                i = i+3;//To skip over the following commas
            }
        }
        long[] pattern = new long[list.size()];
        for(int i = 0; i<list.size();i++){
            pattern[i] = list.get(i);
        }
        return pattern;
    }
    private String CharToCode(char t){
        String s = String.valueOf(t);
        if(s.equals(" ")){//returns a space if the given char is a space
            return " ,";
        }
        int i = 0;
        Sorts.Sort(language, LETTER);
        i = Searches.BinarySearch(s,language,LETTER);
        if(i != -1){
            return language[i].getCode() + ",";
        }
        if(s.equals(".") || s.equals("?") || s.equals("!")){
            return "/,";//return a / followed by a comma
        }
        return "*,";//unrecognized char returns * followed by a comma
    }
}