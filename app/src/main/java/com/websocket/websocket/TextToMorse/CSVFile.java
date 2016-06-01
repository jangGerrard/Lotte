package com.websocket.websocket.TextToMorse;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by Administrator on 2015-10-21.
 */
public class CSVFile {
    private static final int LETTER = 0;

    public static Alphabet[] readCSV(InputStream s){
        Scanner input = new Scanner(s);
        ArrayList<Alphabet> list = new ArrayList<Alphabet>();
        while (input.hasNextLine()) {
            String line = input.nextLine();
            String[] splits = line.split(",");
            try{
                list.add(new Alphabet(splits[0],splits[1],Integer.parseInt(splits[2])));//loads the count from the file
            }catch(ArrayIndexOutOfBoundsException e){
                list.add(new Alphabet(splits[0],splits[1],0));//defaults to 0 if there is no stored count
            }
        }
        input.close();
        Alphabet[] small = new Alphabet[list.size()];
        list.toArray(small);
        return small;
    }

    public static void WriteCSV(String path, Alphabet[] x){
        Sorts.Sort(x, LETTER);
        PrintWriter writer = null;
        try {
            writer = new PrintWriter (new File(path));
        } catch (FileNotFoundException e) {
            System.exit(0);
        }
        for(int i = 0; i<x.length; i++){
            writer.println(x[i].toString());
        }
        writer.close();
    }
    public static void ResetCountStats(Alphabet[] x){
        for(int i = 0; i<x.length; i++){
            x[i].setCount(0);
        }
    }
}
