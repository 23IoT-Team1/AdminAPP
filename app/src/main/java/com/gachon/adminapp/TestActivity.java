package com.gachon.adminapp;

import androidx.annotation.IdRes;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        imageView = findViewById(R.id.imageView);
        textView_RP = findViewById(R.id.textView_RP);
        button_Test = findViewById(R.id.button_Test);
        button_BackToMain = findViewById(R.id.button_BackToMain);

        radioButton_4F = (RadioButton) findViewById(R.id.radioButton_4F);
        radioButton_5F = (RadioButton) findViewById(R.id.radioButton_5F);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        radioButton_4F.setChecked(true);   // 4층으로 selected 초기화

        // imageViw zoom 기능용 세팅
        matrix = new Matrix();
        savedMatrix = new Matrix();
        imageView.setOnTouchListener(onTouch);
        imageView.setScaleType(ImageView.ScaleType.MATRIX); // 스케일 타입을 matrix로 해줘야 움직인다

        //라디오 그룹 클릭 리스너
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                if(i == R.id.radioButton_4F){
                    imageView.setImageResource(R.drawable.floor4);
                }
                else if(i == R.id.radioButton_5F){
                    imageView.setImageResource(R.drawable.floor5);
                }
            }
        });

        // 현위치 인식 테스트 버튼
        button_Test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: wifi scan 후 현위치 결과 뱉기

                // 이후 RP textView 바꾸기
                String RP = "~~";    // ex
                textView_RP.setText(RP);
                // 나중에 가능하면 imageView도
            }
        });

        button_BackToMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // main activity로 돌아가기
                Intent intent = new Intent(TestActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();   // To prevent confusing
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

    private SelectLocationActivity.TOUCH_MODE touchMode;
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
                        touchMode = SelectLocationActivity.TOUCH_MODE.SINGLE;
                        downSingleEvent(event);
                        break;
                    case MotionEvent.ACTION_POINTER_DOWN:
                        if (event.getPointerCount() == 2) { // 두 손가락 터치를 했을 때
                            touchMode = SelectLocationActivity.TOUCH_MODE.MULTI;
                            downMultiEvent(event);
                        }
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if (touchMode == SelectLocationActivity.TOUCH_MODE.SINGLE) {
                            moveSingleEvent(event);
                        } else if (touchMode == SelectLocationActivity.TOUCH_MODE.MULTI) {
                            moveMultiEvent(event);
                        }
                        break;

                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_POINTER_UP:
                        touchMode = SelectLocationActivity.TOUCH_MODE.NONE;
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
    private ImageView imageView;
    private TextView textView_RP;
    private Button button_Test;
    private Button button_BackToMain;
    private RadioGroup radioGroup;
    private RadioButton radioButton_4F;
    private RadioButton radioButton_5F;

}