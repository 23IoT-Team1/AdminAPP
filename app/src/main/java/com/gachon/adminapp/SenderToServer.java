package com.gachon.adminapp;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.util.Log;

import com.google.gson.Gson;

import java.util.ArrayList;

public class SenderToServer {

        // Convert the measured values to JSON



        private static ArrayList<node> nodeList;
        public SenderToServer(ArrayList arrayList){
                //selectLocationActivity에서 spinner에서 고른 값을 저장 후 가져옴
                String floor = SelectLocationActivity.Floor;
                String rp = SelectLocationActivity.RP;
                String name = "임시 이름";
                
                //빈 arraylist에 집어넣어줌
                //constructor에서 받아온 arraylist는
                // scanWifiActivity에서 저장한 5개의 AP 정보를 WifiDTO 형식으로 담은 Arraylist이다
                nodeList = new ArrayList<>();
                nodeList.add(new node(floor, rp, name, arrayList));

        }
        public static void send() {

                Gson gson = new Gson();
                String json = gson.toJson(nodeList);

                Log.d(TAG, json);

                /*
                OkHttpClient client = new OkHttpClient();

                Request request = new Request.Builder()
                        .url("http://localhost:8080/save")
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

                */
        }

}

