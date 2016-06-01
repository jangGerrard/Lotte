package com.websocket.websocket.TextToMorse;

public class Alphabet{
    private String code, letter;
    private int count;
    Alphabet(String letter, String code){
        this.letter = letter;
        this.code = code;
        this.count = 0;
    }
    Alphabet(String letter, String code, int count){
        this.letter = letter;
        this.code = code;
        this.count = count;
    }
    public String getLetter() {
        return letter;
    }
    public void setCount(int count) {
        this.count = count;
    }
    public String getCode() {
        return code;
    }
    public int getCount() {
        return count;
    }
    public void increaseCount(){
        count++;
    }
    public int compareLetters(Alphabet b) {
        return this.getLetter().compareTo(b.getLetter());
    }
    public int compareCodes(Alphabet b) {
        return this.getCode().compareTo(b.getCode());
    }
    public int compareCounts(Alphabet b) {
        return Integer.valueOf(this.getCount()).compareTo(Integer.valueOf(b.getCount()));
    }
    @Override
    public String toString(){
        return this.getLetter() + "," + this.getCode() + "," + this.getCount();
    }
}