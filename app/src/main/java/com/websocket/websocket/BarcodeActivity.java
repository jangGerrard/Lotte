package com.websocket.websocket;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.websocket.websocket.Global.Global;
import com.websocket.websocket.product.Product;
import com.websocket.websocket.product.ProductAdapter;

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

public class BarcodeActivity extends AppCompatActivity {

    private EditText barcodeEdit;
    private Button btn;

    private ProductAdapter productAdapter;
    private ListView custom_listview;
    private String param;
    private String query;
    String sUrl = "http://"+ Global.ip+":11000/LotteSpec"+"/android/barcode.lotte";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode);

        barcodeEdit = (EditText)findViewById(R.id.barcode_edit);
        btn = (Button) findViewById(R.id.barcode_search);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (barcodeEdit.getText().toString() != "") {
                    query = barcodeEdit.getText().toString();
                    BarcodeSearch barcodeSearch = new BarcodeSearch();
                    String result = barcodeSearch.execute(sUrl, query).toString();

                }
            }
        });

        custom_listview = (ListView)findViewById(R.id.barcode_listView);

    }

    private class BarcodeSearch extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            return login(params[0], params[1]);
        }

        private String login(String sUrl, String query) {
            URL url = null;
            StringBuffer response = new StringBuffer();
            String body = "test=" + query ;

            try {
                Log.d("query", query);
                url = new URL(sUrl + "?barcode="+query);
                Log.d("query", url.toString());
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
