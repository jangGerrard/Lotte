package com.websocket.websocket;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class Barcode2Activity extends AppCompatActivity {

    Button zxingBtn;
    private static final String TAG  = "JANG";
    TextView barcodeResult;
    TextView barcodeType;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode2);

        zxingBtn = (Button) findViewById(R.id.zxing);
        barcodeResult = (TextView)findViewById(R.id.barcode_txt_output);
        baracodeType = (TextView) findViewById(R.id.barcode_type_output);

        zxingBtn.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v) {
                new IntentIntegrator(Barcode2Activity.this).initiateScan();
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e(TAG, ">>> requestCode = " + requestCode + ", resultCode = " + resultCode);
        if (requestCode == IntentIntegrator.REQUEST_CODE) {

            IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
            barcodeResult.setText(result.getContents());
            Log.i(TAG, ">>> result.getContents()   :  " + result.getContents());
            Log.i(TAG, ">>> result.getFormatName()   :  " + result.getFormatName());
        }
    }



}
