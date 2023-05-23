package com.gachon.adminapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        textView_RP = findViewById(R.id.textView_RP);
        textView_Count = findViewById(R.id.textView_Count);
        button_BackToMain = findViewById(R.id.button_BackToMain);

        // Get Intent (reference point)
        Intent intent = getIntent();
        String rp = intent.getStringExtra("reference_point");
        textView_RP.setText(rp);

        // DB에서 그 rp에 대해 저장된 데이터 수 가져와서 띄우기
        String num = "5";    // ex
        textView_Count.setText(num);
        
        // Back to main 버튼
        button_BackToMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ResultActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });
    }

    // declaration
    private TextView textView_RP;
    private TextView textView_Count;
    private Button button_BackToMain;
}