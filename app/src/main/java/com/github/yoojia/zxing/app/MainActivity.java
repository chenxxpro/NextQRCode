package com.github.yoojia.zxing.app;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.github.yoojia.zxing.R;

/**
 * @author :   Yoojia.Chen (yoojia.chen@gmail.com)
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Show QRCode
        findViewById(R.id.qrcode_show).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, QRCodeShowActivity.class);
                startActivity(intent);
            }
        });

        // Generate QRCode
        findViewById(R.id.qrcode_scan).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, QRCodeScanActivity.class);
                startActivity(intent);
            }
        });

    }

}
