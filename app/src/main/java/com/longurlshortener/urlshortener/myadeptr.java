package com.longurlshortener.urlshortener;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by dipanker on 30/05/17.
 */

public class myadeptr extends RecyclerView.Adapter<myadeptr.MyViewHolder> {

    ArrayList<stringItem> arrayList;
    Context context;

    public myadeptr(ArrayList<stringItem> arrayList,Context context) {
        this.arrayList = arrayList;
        this.context=context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.row,parent,false);
        MyViewHolder myViewHolder=new MyViewHolder(view,context,arrayList);
        return myViewHolder;
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.textView.setText(arrayList.get(position).getIp());

    }


    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            TextView textView;
        ArrayList<stringItem> arrayList=new ArrayList<>();
        Context context;

        public MyViewHolder(View itemView,Context context,ArrayList<stringItem> arrayList) {
            super(itemView);
            this.context=context;
            this.arrayList=arrayList;
            itemView.setOnClickListener(this);
            textView= (TextView) itemView.findViewById(R.id.txtip);



        }

        @Override
        public void onClick(View v) {
            int pos=getAdapterPosition();
            Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.iptrackeronline.com/index.php?ip_address="+arrayList.get(pos).getIp()));
            v.getContext().startActivity(intent);

        }
    }
}
