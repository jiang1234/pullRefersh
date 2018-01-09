package com.example.administrator.pulldownrefreshlayout;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.List;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class ContentAdapter extends RecyclerView.Adapter<ContentAdapter.ViewHolder>{
    private List<Content> mContentList;
    static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView contentImage;
        TextView contentName;
        public ViewHolder(View view){
            super(view);
            contentImage = (ImageView)view.findViewById(R.id.content_image);
            contentName = (TextView)view.findViewById(R.id.content_name);
        }
    }
    public ContentAdapter(List<Content> mContentList){
        this.mContentList = mContentList;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_recycler_view_item,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }
    @Override
    public void onBindViewHolder(ViewHolder holder,int position){
        Content mContent = mContentList.get(position);
        holder.contentName.setText(mContent.getName());

    }
    @Override
    public int getItemCount(){
        return mContentList.size();
    }


}


