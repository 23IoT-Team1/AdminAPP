//Getting Wifi RSSI Values continuously Android Application Doesn't start
//출처 : https://stackoverflow.com/questions/41583416/getting-wifi-rssi-values-continuously-android-application-doesnt-start
package com.gachon.adminapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ScanWifiActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_wifi);

        textView_RP = findViewById(R.id.textView_RP);
        recyclerView = findViewById(R.id.recycler1);
        button_StoreInDB = findViewById(R.id.button_StoreInDB);
        button_BackToMain = findViewById(R.id.button_BackToMain);

        // Get Intent (reference point)
        Intent intent = getIntent();
        String rp = intent.getStringExtra("reference_point");
        textView_RP.setText(rp);

        // Get Wi-Fi data
        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        registerReceiver(wifiReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        requestPermissions();
        scanWifi();

        // recyclerView setting
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RecyclerAdapter(arrayList);
        recyclerView.setAdapter(adapter);

        // Store in DataBase 버튼
        button_StoreInDB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO : DB에 RP, WiFi Data 업로드
                // Send the JSON data to the Spring server
                SenderToServer sender = new SenderToServer(arrayList);
                sender.send();
            }
        });

        // Back to main 버튼
        button_BackToMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // main activity로 돌아가기
                //finish를 통해 activity 종료하여 돌아감
                finish();
            }
        });

    }

    private void scanWifi() {
        if (start) {
            if (!wifiManager.isWifiEnabled()) {
                Toast.makeText(this, "WIFI DISABLED", Toast.LENGTH_LONG).show();
                wifiManager.setWifiEnabled(true);
            }
            wifiManager.startScan();
            Toast.makeText(this, "SCANNING", Toast.LENGTH_LONG).show();
        } else {
            requestPermissions();
        }
    }

    private void requestPermissions() {
        if (ContextCompat.checkSelfPermission(this, permissions[0]) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, permissions[1]) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, permissions[2]) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, permissions[3]) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, permissions, REQUEST_PERMISSION_CODE);

        } else {
            Toast.makeText(this, "Permissions already granted", Toast.LENGTH_SHORT).show();
            start = true;
        }
    }

    BroadcastReceiver wifiReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("check","onReceive");
            scanResults = wifiManager.getScanResults();
            Log.d("check","OK");
            Collections.sort(scanResults, new Comparator<ScanResult>() {
                @Override
                public int compare(ScanResult result1, ScanResult result2) {
                    return Integer.compare(result2.level, result1.level);
                }
            });

            arrayList.clear();

            int count = 0; // Variable to keep track of the number of items added
            for (ScanResult res : scanResults) {
                if (count < 5) { // Add only the top 5 values
                    WifiDTO temp = new WifiDTO(res.SSID, res.BSSID, String.valueOf(res.level));
                    arrayList.add(temp);
                    count++;
                } else {
                    break; // Break the loop once 5 values have been added
                }
            }
            adapter.notifyDataSetChanged();
        }
    };

    // declaration


    private TextView textView_RP;
    private RecyclerView recyclerView;
    private Button button_StoreInDB;
    private Button button_BackToMain;
    private ArrayList<WifiDTO> arrayList = new ArrayList<>();
    private WifiManager wifiManager;
    private List<ScanResult> scanResults;
    private RecyclerAdapter adapter;
    private int REQUEST_PERMISSION_CODE = 1;
    private String[] permissions = {
            android.Manifest.permission.ACCESS_COARSE_LOCATION,
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.ACCESS_WIFI_STATE,
            android.Manifest.permission.CHANGE_WIFI_STATE };
    private boolean start;
}
