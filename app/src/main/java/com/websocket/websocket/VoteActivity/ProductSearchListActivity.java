package com.websocket.websocket.VoteActivity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.websocket.websocket.R;
import com.websocket.websocket.product.ProductAdapter;

public class ProductSearchListActivity extends AppCompatActivity {


    private ListView searchListView;

    private ProductAdapter productAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_search_list);



    }
}
