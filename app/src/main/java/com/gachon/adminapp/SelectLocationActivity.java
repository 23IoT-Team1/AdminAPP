package com.gachon.adminapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;


public class SelectLocationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_location);

        spinner_Floor = findViewById(R.id.spinner_Floor);
        spinner_Place = findViewById(R.id.spinner_Place);
        imageView = findViewById(R.id.imageView);
        textView_Place = findViewById(R.id.textView_Place);
        button_ScanWifi = findViewById(R.id.button_ScanWifi);

        // imageViw zoom 기능용 세팅
        matrix = new Matrix();
        savedMatrix = new Matrix();
        imageView.setOnTouchListener(onTouch);
        imageView.setScaleType(ImageView.ScaleType.MATRIX); // 스케일 타입을 matrix로 해줘야 움직인다

        // floor 선택 spinner 세팅 (default: 4F)
        floorAdapter = ArrayAdapter.createFromResource(this, R.array.floors, android.R.layout.simple_spinner_item);
        floorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_Floor.setAdapter(floorAdapter);

        // RP 선택 spinner 세팅 (default: 4F RP array)
        rp4Adapter = ArrayAdapter.createFromResource(this, R.array.place_4F, android.R.layout.simple_spinner_item);
        rp4Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        rp5Adapter = ArrayAdapter.createFromResource(this, R.array.place_5F, android.R.layout.simple_spinner_item);
        rp5Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_Place.setAdapter(rp4Adapter);

        // floor를 선택하면, 옆의 spinner에 그 층의 RP 리스트를 띄움
        spinner_Floor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0) {
                    spinner_Place.setAdapter(rp4Adapter);
                    imageView.setImageResource(R.drawable.floor4);
                }
                else if(position == 1) {
                    spinner_Place.setAdapter(rp5Adapter);
                    imageView.setImageResource(R.drawable.floor5);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        // RP 선택 spinner
        spinner_Place.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // 선택한 string 값 받아오기
                textView_Place.setText(spinner_Place.getSelectedItem().toString());
                // 나중엔 imageView도 변하게 하기
                String[] str = spinner_Place.getSelectedItem().toString().split(" ~ ");
                Log.d("place",str[0]);
                Log.d("rp",str[1]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                textView_Place.setText("");
            }
        });

        // Scan WiFi 버튼
        button_ScanWifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(SelectLocationActivity.this, ScanWifiActivity.class);

                intent.putExtra("current_floor", spinner_Floor.getSelectedItem().toString());
                intent.putExtra("current_place", textView_Place.getText().toString());
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                // matrix 초기화 (imageView 처음 보일 이미지 크기 조정
                Bitmap temp = ((BitmapDrawable)imageView.getDrawable()).getBitmap();
                float density = getResources().getDisplayMetrics().density;
                RectF imageRectF = new RectF(0, 0, temp.getWidth()*density, temp.getHeight()*density);
                RectF viewRectF = new RectF(0, 0, imageView.getWidth(), imageView.getHeight());
                matrix.setRectToRect(imageRectF, viewRectF, Matrix.ScaleToFit.CENTER);
                imageView.setImageMatrix(matrix);

            }
        }, 50);// 0.05초 딜레이를 준 후 시작
        

    }

    // declaration for zoom
    enum TOUCH_MODE {
        NONE,   // 터치 안했을 때
        SINGLE, // 한 손가락 터치
        MULTI   // 두 손가락 터치
    }

    private TOUCH_MODE touchMode;
    private Matrix matrix;      // 기존 매트릭스
    private Matrix savedMatrix; // 작업 후 이미지에 매핑할 매트릭스
    private PointF startPoint;  // 한 손가락 터치 이동 포인트
    private PointF midPoint;    // 두 손가락 터치 시 중심 포인트
    private float oldDistance;  // 터치 시 두 손가락 사이의 거리

    private View.OnTouchListener onTouch = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (v.equals(imageView)) {
                int action = event.getAction();
                switch (action & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_DOWN:
                        touchMode = TOUCH_MODE.SINGLE;
                        downSingleEvent(event);
                        break;
                    case MotionEvent.ACTION_POINTER_DOWN:
                        if (event.getPointerCount() == 2) { // 두 손가락 터치를 했을 때
                            touchMode = TOUCH_MODE.MULTI;
                            downMultiEvent(event);
                        }
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if (touchMode == TOUCH_MODE.SINGLE) {
                            moveSingleEvent(event);
                        } else if (touchMode == TOUCH_MODE.MULTI) {
                            moveMultiEvent(event);
                        }
                        break;

                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_POINTER_UP:
                        touchMode = TOUCH_MODE.NONE;
                        break;
                }
            }
            return true;
        }
    };

    private void downSingleEvent(MotionEvent event) {
        savedMatrix.set(matrix);
        startPoint = new PointF(event.getX(), event.getY());
    }

    private void downMultiEvent(MotionEvent event) {
        oldDistance = getDistance(event);
        if (oldDistance > 5f) {
            savedMatrix.set(matrix);
            midPoint = getMidPoint(event);
        }
    }

    private void moveSingleEvent(MotionEvent event) {
        matrix.set(savedMatrix);
        matrix.postTranslate(event.getX() - startPoint.x, event.getY() - startPoint.y);
        imageView.setImageMatrix(matrix);
    }

    private void moveMultiEvent(MotionEvent event) {
        float newDistance = getDistance(event);
        if (newDistance > 5f) {
            matrix.set(savedMatrix);
            float scale = newDistance / oldDistance;
            matrix.postScale(scale, scale, midPoint.x, midPoint.y);

            imageView.setImageMatrix(matrix);

        }
    }

    private PointF getMidPoint(MotionEvent e) {

        float x = (e.getX(0) + e.getX(1)) / 2;
        float y = (e.getY(0) + e.getY(1)) / 2;

        return new PointF(x, y);
    }

    private float getDistance(MotionEvent e) {
        float x = e.getX(0) - e.getX(1);
        float y = e.getY(0) - e.getY(1);
        return (float) Math.sqrt(x * x + y * y);
    }


    // declaration
    private Spinner spinner_Floor;
    private Spinner spinner_Place;
    private ImageView imageView;
    private TextView textView_Place;
    private Button button_ScanWifi;

    private ArrayAdapter floorAdapter;
    private ArrayAdapter rp4Adapter;
    private ArrayAdapter rp5Adapter;

}