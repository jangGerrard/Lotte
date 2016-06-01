package com.websocket.websocket.TTS;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.websocket.websocket.R;

import java.util.ArrayList;

/**
 * Created by Administrator on 2015-11-25.
 */
public class TTSAdapter extends BaseAdapter {
    private ArrayList<String> tts_list; // 리스트를 보관 할 ArrayList
    LinearLayout text_right = null;
    RadioGroup b_radio = null;
    Button b_send = null;
    TextView text_left = null;
    CustomHolder holder = null;

    // 생성자
    public TTSAdapter(){
        tts_list = new ArrayList<String>();
    }

    // 현재 아이템의 수를 리턴
    @Override
    public int getCount(){
        return tts_list.size();
    }

    // 현재 아이템의 Object를 리턴, Object를 상황에 맞게 변경하거나 리턴 받은 Object를 캐스팅해서 사용
    @Override
    public Object getItem(int position) {
        return tts_list.get(position);
    }

    // 아이템 position의  ID 값 리턴
    @Override
    public long getItemId(int position) {
        return position;
    }

    // 출력 될 아이템 관리
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        final Context context = parent.getContext();
        final RadioButton[] radio = new RadioButton[5]; // TTS 결과 (RadioButton의 갯수)

        if (convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.tts_listview, parent, false);

            text_right = (LinearLayout) convertView.findViewById(R.id.tts_text_right);
            b_radio = (RadioGroup) convertView.findViewById(R.id.tts_radio);
            b_send = (Button) convertView.findViewById(R.id.tts_send);
            text_left = (TextView) convertView.findViewById(R.id.tts_text_left);

            b_send.setOnClickListener(new View.OnClickListener() { // tts 결과를 보내는 버튼
                @Override
                public void onClick(View view) {
                    Toast.makeText(context, "send to result", Toast.LENGTH_SHORT).show();
                }
            });

            for(int i=0; i<5; i++){   // TTS 결과 (RadioButton)
                radio[i] = new RadioButton(context);
                b_radio.addView(radio[i]);
                radio[i].setText("radio" + i);
                radio[i].setTextColor(Color.WHITE);
                radio[i].setTextSize(16);
            }

            holder = new CustomHolder();
            holder.textViewRight = text_right;
            holder.b_send = b_send;
            holder.b_radio = b_radio;
            holder.textViewLeft = text_left;
            convertView.setTag(holder);
        } else {
            holder = (CustomHolder) convertView.getTag();
            text_right = holder.textViewRight;
            b_send = holder.b_send;
            b_radio = holder.b_radio;
            text_left = holder.textViewLeft;
        }

        if(position%2 != 0){
            text_left.setVisibility(View.GONE);
            text_right.setVisibility(View.VISIBLE);
        } else if(position%2 == 0){
            text_right.setVisibility(View.GONE);
            text_left.setVisibility(View.VISIBLE);
            text_left.setText(tts_list.get(position));
        }

        return convertView;
    }

    // 리스트의 데이터 변형 방지
    private class CustomHolder{
        LinearLayout textViewRight;
        Button b_send;
        RadioGroup b_radio;
        TextView textViewLeft;
    }

    // 외부에서 데이터를 추가 할 때
    public void add(String message){
        tts_list.add(message);
    }
}
