package com.mobisoft.MbsDemo.LitsViewActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.mobisoft.MbsDemo.R;

import java.util.ArrayList;

public class ListActivity extends AppCompatActivity {

    private ArrayList<MicroAppInfo> mAppList = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private ListAppAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        Res res1 = UrlPath.json2entity(UrlPath.url
                , Res.class);
        ResListMyInstalledMicroApp res = UrlPath.json2entity(res1.getPayload().toString()
                , ResListMyInstalledMicroApp.class);
        mAppList.addAll(res.getMyApps());
        mRecyclerView = (RecyclerView) findViewById(R.id.id_recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter = new ListAppAdapter(mAppList, this));


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            boolean hasPerm = (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
            if (!hasPerm)
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 101);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
