package com.example.administrator.pulldownrefreshlayout;

/**
 * Created by Administrator on 2018/1/9.
 */

public class Content {
    private String mName;
    private int mImageId;
    public Content(String mName,int mImageId){
        this.mName = mName;
        this.mImageId = mImageId;
    }
    public int getImageId(){
        return this.mImageId;
    }
    public String getName(){
        return this.mName;
    }
}
