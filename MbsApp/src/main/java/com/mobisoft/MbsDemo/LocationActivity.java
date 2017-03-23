package com.mobisoft.MbsDemo;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import com.mobisoft.MbsDemo.Loaction.LocationListener;
import com.mobisoft.amaplibrary.AmapLocation;

public class LocationActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btn_destroy;
    private Button btn_start;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        initView();
        initEvents();
    }

    private void initEvents() {
        btn_destroy.setOnClickListener(this);
        btn_start.setOnClickListener(this);

    }

    private void initView() {
        btn_destroy = (Button) findViewById(R.id.btn_destroy);
        btn_start = (Button) findViewById(R.id.btn_start);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_start://开始定位
                if (TextUtils.equals(btn_start.getText(), "开始定位")) {
                    btn_start.setText("定位已经启动");
                    btn_start.setBackgroundResource(R.color.actionsheet_gray);
                    btn_start.setTextColor(Color.parseColor("#ffffff"));
                    AmapLocation.getInstance()
                            .setContext(LocationActivity.this.getApplicationContext())
                            .setmLocationListener(new LocationListener(this))
                            .init()
                            .startLocation();
                }
                break;
            case R.id.btn_destroy:
                AmapLocation.getInstance().destroy();
                btn_start.setText("开始定位");
                btn_start.setBackgroundResource(R.color.gray);
                btn_start.setTextColor(Color.parseColor("#000000"));

                break;
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
