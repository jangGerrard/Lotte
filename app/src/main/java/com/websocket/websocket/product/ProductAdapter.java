package com.websocket.websocket.product;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.websocket.websocket.Global.Global;
import com.websocket.websocket.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

/**
 * Created by Jang on 2016-06-01.
 */
public class ProductAdapter extends ArrayAdapter<Product> {
    private Context context;

    private String sUrl = "http://"+Global.ip+":11000/LotteSpec"+"/android/addcart.lotte";

    CustomHolder holder = null;
    TextView name= null;
    TextView price= null;
    TextView content = null;
    TextView barcode= null;

    private class CustomHolder {
        TextView name= null;
        TextView price= null;
        TextView content = null;
        TextView barcode= null;
    }

    public ProductAdapter(Context context, int layoutViewResourceId) {
        super(context,layoutViewResourceId);
        this.context = context;
    }

    public long getItemId(int position){
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent){
        final int pos = position;

        if (convertView == null){
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.product_item, parent, false);

            name= (TextView) convertView.findViewById(R.id.product_name);
            price = (TextView)convertView.findViewById(R.id.product_price);
            content = (TextView) convertView.findViewById(R.id.product_content);
            barcode = (TextView) convertView.findViewById(R.id.product_barcode);

            holder = new CustomHolder();
            holder.name = name;
            holder.content = content;
            holder.barcode = barcode;
            holder.price = price;
            convertView.setTag(holder);
        } else {
            holder = (CustomHolder) convertView.getTag();
             name = holder.name;
             price = holder.price;
            content = holder.content;
            barcode = holder.barcode;
        }
        Log.d("name", getItem(position).getProdName());
        name.setText(getItem(position).getProdName());
        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        price.setText(getItem(position).getPrice()+"");
        content.setText(getItem(position).getContent());
        barcode.setText(getItem(position).getBarcode());


        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s_position = String.valueOf(pos);
                Product p = getItem(position);
                Toast.makeText(context, p.getId() + p.getProdName(), Toast.LENGTH_SHORT).show();

                CartAdd cartAdd = new CartAdd();
                cartAdd.execute(sUrl, Global.userPref.getString("user_name", ""), p.getId()+"").toString();

            }
        });



        return convertView;
    }


    private class CartAdd extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            return login(params[0], params[1], params[2] );
        }

        private String login(String sUrl, String user_name, String prod_id) {
            URL url = null;
            StringBuffer response = new StringBuffer();
            String body = "prod_name=" + user_name ;

            Log.d("aaa", "prod");
            try {
                url = new URL(sUrl + "?user_name="+user_name + "&prod_id=" + prod_id);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
                wr.writeBytes(body);
                wr.flush();
                wr.close();

                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                    // DB에 있으면 Login되도록
                }
                in.close();
                conn.disconnect();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.d("response", response.toString());



            return response.toString();
        }

        @Override
        protected void onPostExecute(String s){
            super.onPostExecute(s);
        }
    }
}
