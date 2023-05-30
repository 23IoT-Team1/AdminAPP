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



        private static ArrayList<node> nodeList;
        public SenderToServer(String floor, String place, ArrayList arrayList){
                
                // rp는 아래 함수에 매칭 후 넣기
                String rp = setRP(floor, place);
                
                //빈 arraylist에 집어넣어줌
                //constructor에서 받아온 arraylist는
                // scanWifiActivity에서 저장한 5개의 AP 정보를 WifiDTO 형식으로 담은 Arraylist이다
                nodeList = new ArrayList<>();
                nodeList.add(new node(floor, rp, place, arrayList));

        }
        public static void send() {

                Gson gson = new Gson();
                String json = gson.toJson(nodeList);

                Log.d(TAG, json);


                OkHttpClient client = new OkHttpClient();

                Log.e("테스트","testsets");

                Request request = new Request.Builder()
                        .url("http://localhost:8080/rp")
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

        // place를 받으면 그에 맞는 rp 이름을 반환
        private String setRP(String floor, String place)
        {
                String rp = "";

                // TODO: rp랑 place 정보 넣기. place 정보는 string.xml이랑 맞추기
                if (floor == "4F") {
                        switch(place) {
                                case "4층 아르테크네":
                                        rp = "4-1";
                                        break;
                                case "401호 앞":
                                        rp = "4-2";
                                        break;
                        }

                }
                else if (floor == "5F") {
                        switch(place) {
                                case "5층 아르테크네":
                                        rp = "5-1";
                                        break;
                                case "501호 앞":
                                        rp = "5-2";
                                        break;
                        }
                }


                return rp;
        }


}

