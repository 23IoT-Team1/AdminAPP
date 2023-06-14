//arrayList->gson->json 데이터 변환과 okhttp3를 이용한 데이터 전송
//출처 : https://snowdeer.github.io/android/2017/03/03/get-and-post-and-put-using-okhttp/
package com.gachon.adminapp;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SenderToServer {
        // Convert the measured values to JSON

        private static node request_body;
        public SenderToServer(String floor, String place, ArrayList arrayList){
                
                // rp랑 place 저장
                String[] str = place.split(" ~ ");      // 0은 place, 1은 rp

                request_body = new node(floor, str[1], str[0], arrayList);

        }
        public static void send() {

                Gson gson = new Gson();
                String json = gson.toJson(request_body);

                OkHttpClient client = new OkHttpClient();


                Request request = new Request.Builder()
                        .url("http://172.16.63.238:8080/rp")
                        .post(RequestBody.create(MediaType.parse("application/json"), json))
                        .build();

                client.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                                // Handle the server response if needed
                        }

                        @Override
                        public void onFailure(@NonNull Call call, @NonNull IOException e) {
                                e.printStackTrace();
                        }
                });


        }

}

