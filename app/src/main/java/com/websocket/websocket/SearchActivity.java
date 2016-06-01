package com.websocket.websocket;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.websocket.websocket.Friend.Friends;
import com.websocket.websocket.Friend.FriendsActivity;
import com.websocket.websocket.Friend.FriendsAdapter;
import com.websocket.websocket.Global.Global;
import com.websocket.websocket.product.Product;
import com.websocket.websocket.product.ProductAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class SearchActivity extends AppCompatActivity {

    private ProductAdapter productAdapter;
    private ListView custom_listview;
    private String param;
    String sUrl = "http://"+Global.ip+":11000/LotteSpec"+"/android/search.lotte";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Intent i = getIntent();
        param = i.getStringExtra("query");

        Toast.makeText(getApplicationContext(), " " + param , Toast.LENGTH_LONG).show();

        custom_listview = (ListView) findViewById(R.id.product_list_view);

        productAdapter = new ProductAdapter(SearchActivity.this, R.layout.product_item);
        Global.product_list.add(new Product("name1" , "content", 1234));
        Global.product_list.add(new Product("name2", "content", 1234));
        Global.product_list.add(new Product("name3" , "content", 1234));
        Global.product_list.add(new Product("name4" , "content", 1234))        ;

        productAdapter = new ProductAdapter(getApplicationContext(), R.layout.product_item);
        productAdapter.addAll(Global.product_list);

        custom_listview.setAdapter(productAdapter);
        productAdapter.notifyDataSetChanged();
        Log.d("ProdSearch", "postExecute");

        ProductSearch productSearch = new ProductSearch();
        productSearch.execute(sUrl,param).toString();

    }

    private class ProductSearch extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            return login(params[0], params[1]);
        }

        private String login(String sUrl, String query) {
            URL url = null;
            StringBuffer response = new StringBuffer();
            String body = "prod_name=" + query ;

            Log.d("aaa", "prod");
            try {
                url = new URL(sUrl + "?prodName="+query);
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

            JSONArray ja = null;

            try {
                Global.product_list.clear();
                ja = new JSONArray(response.toString());

                for (int i = 0; i < ja.length(); i++) {
                    JSONObject jo = ja.getJSONObject(i);
                    Product product = new Product();
                    product.setId(jo.getInt("id"));
                    product.setProdName(jo.getString("prodName"));
                    product.setPrice(jo.getInt("price"));
                    product.setContent(jo.getString("content"));
                    product.setBarcode(jo.getString("barcode"));
                    Global.product_list.add(product);
                    Log.d("product : ",product.toString());
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return response.toString();
        }

        @Override
        protected void onPostExecute(String s){
            productAdapter = new ProductAdapter(getApplicationContext(), R.layout.product_item);
            productAdapter.addAll(Global.product_list);
            custom_listview.setAdapter(productAdapter);
            productAdapter.notifyDataSetChanged();
            super.onPostExecute(s);
        }
    }
}
