package com.github.hsg.circleprogressbar;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private Button btnListviewDemo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        this.btnListviewDemo = (Button) findViewById(R.id.btn_list_view_demo);
        this.btnListviewDemo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListViewDemoActivity.actionStart(MainActivity.this);
            }
        });
    }
}
