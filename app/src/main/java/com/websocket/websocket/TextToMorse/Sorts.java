package com.websocket.websocket.TextToMorse;

public class Sorts {
    private static final int LETTER = 0;
    public static void Sort(Alphabet[] a, int type){
        if(type == LETTER){
            QuickSort(a,0,a.length-1);
        }else{
            InsertionSort(a);
        }

    }
    private static void QuickSort(Alphabet[] a, int lo, int hi) {//used to sort by letter
        if (hi <= lo) return;
        int lt = lo, gt = hi;
        Alphabet v = a[lo];
        int i = lo;
        while (i <= gt) {
            int cmp = a[i].compareLetters(v);
            if(cmp < 0){
                swap(a, lt++, i++);
            }
            else if (cmp > 0){
                swap(a, i, gt--);
            }
            else{
                i++;
            }
        }
        QuickSort(a, lo, lt-1);
        QuickSort(a, gt+1, hi);
    }

    private static void InsertionSort ( Alphabet[] x) {
        for(int i = 0; i < x.length; i++){
            for(int j = i; j > 0 && less(x[j],x[j-1]); j-- ){
                swap(x,j,j-1);
            }
        }
    }

    private static boolean less(Alphabet a, Alphabet b) {
        if(a.compareCodes(b) < 0){
            return true;
        }
        return false;
    }

    private static void swap(Object[] a, int i, int j) {
        Object swap = a[i];
        a[i] = a[j];
        a[j] = swap;
    }
}