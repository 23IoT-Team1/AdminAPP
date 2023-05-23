package com.gachon.adminapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        button_CollectData = findViewById(R.id.button_CollectData);
        button_Test = findViewById(R.id.button_Test);

        button_CollectData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, SelectLocationActivity.class);
                startActivity(intent);
            }
        });

        button_Test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, TestActivity.class);
                startActivity(intent);
            }
        });
    }

    // declaration
    private Button button_CollectData;
    private Button button_Test;
}