package com.github.hsg.circleprogressbar;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class ListViewDemoActivity extends AppCompatActivity {

    private ListView listView;
    private ListViewAdapter adapter;
    private List<Float> dataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_list_view_demo);
        init();
    }

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, ListViewDemoActivity.class);
        context.startActivity(intent);
    }

    private void init(){
        dataList = getDataSource();
        listView = (ListView) findViewById(R.id.list_view);
        adapter = new ListViewAdapter(this, R.layout.list_view_item, dataList);
        listView.setAdapter(adapter);
    }

    private List<Float> getDataSource() {
        List<Float> dataSource = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            //0.0 ~ 1.0
            dataSource.add(new Float(Math.random() * 1));
        }

        return dataSource;
    }
}
