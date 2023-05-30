package com.gachon.adminapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private ArrayList<WifiDTO> mData = null;

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtView_num;
        TextView txtView_ssid;
        TextView txtView_bssid;
        TextView txtView_rssi;

        ViewHolder(View itemView) {
            super(itemView);
            txtView_num = itemView.findViewById(R.id.number);
            txtView_ssid = itemView.findViewById(R.id.ssid);
            txtView_bssid = itemView.findViewById(R.id.bssid);
            txtView_rssi = itemView.findViewById(R.id.rssi);
        }
    }

    // 생성자에서 데이터 리스트 객체를 전달받음.
    RecyclerAdapter(ArrayList<WifiDTO> list) {
        mData = list;
    }

    // 아이템 뷰를 위한 뷰홀더 객체를 생성하고 리턴.
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.recyclerview_item, parent, false);
        ViewHolder vh = new ViewHolder(view);


        return vh;
    }

    // position에 해당하는 데이터를 ViewHolder의 아이템뷰에 표시
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        WifiDTO text = mData.get(position);
        holder.txtView_num.setText(String.valueOf(position+1));
        holder.txtView_ssid.setText(text.getSSID());
        holder.txtView_bssid.setText(text.getBSSID());
        holder.txtView_rssi.setText(String.valueOf(text.getRSSI()));
    }

    // return the number of total data
    @Override
    public int getItemCount() {
        return mData.size();
    }

}
