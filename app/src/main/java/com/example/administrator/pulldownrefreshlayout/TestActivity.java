package com.example.administrator.pulldownrefreshlayout;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        ContentAdapter appItemAadpter = new ContentAdapter(getcontentList());
        recyclerView.setAdapter(appItemAadpter);
    }
    private List<Content> getcontentList(){
        List<Content> contentList = new ArrayList<>();
        for(int i = 0;i<20;i++){
            Content contentItem = new Content("aaaa"+i,0);
            contentList.add(contentItem);
        }
        return contentList;
    }
}
