package com.josephus;

import android.app.Dialog;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AbsoluteLayout;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    AbsoluteLayout layout;
    Button btnClick;
    EditText totalView,keyView;
    TextView  showMsg;

    int WIDTH, HEIGHT, N = 10, M = 6;

    LinkedList<TextView> list = new LinkedList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //2、通过Resources获取
//        DisplayMetrics dm = getResources().getDisplayMetrics();
//        HEIGHT = dm.heightPixels;
//        WIDTH = dm.widthPixels;
        WindowManager manager = getWindowManager();
        DisplayMetrics metrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(metrics);
        WIDTH = metrics.widthPixels;  //以要素为单位
        HEIGHT = metrics.heightPixels;


        layout = findViewById(R.id.layout);
        layout.setMinimumHeight(WIDTH);

        btnClick = findViewById(R.id.btnClick);
        btnClick.setOnClickListener(this);

        showMsg = findViewById(R.id.showMsg);
        totalView = findViewById(R.id.totalView);
        keyView = findViewById(R.id.keyView);

    }

    @Override
    public void onClick(View v) {

        btnClick.setEnabled(false);

        Toast.makeText(this, "开始", Toast.LENGTH_SHORT).show();

        N=Integer.parseInt(totalView.getText().toString());
        M=Integer.parseInt(keyView.getText().toString());

        for (int i = 0; i < N; i++) {
            int x = (int) (Math.sin(Math.toRadians(i * 360 / N)) * WIDTH / 3 + WIDTH / 2 - 50);
            int y = (int) (Math.cos(Math.toRadians(i * 360 / N)) * WIDTH / 3 + WIDTH / 2 - 50);
            addCard(i + 1, x, y);
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                cal(N, M);
            }
        }).start();


    }


    void addCard(int num, int x, int y) {
        TextView textView = new TextView(this);
        textView.setWidth(20);
        textView.setBackgroundResource(R.drawable.circle_2);
        textView.setText(String.valueOf(num));
        textView.setGravity(Gravity.CENTER);

        layout.addView(textView, new AbsoluteLayout.LayoutParams(100, 100, x, y));

        list.add(textView);

    }

    void setAnimation(final TextView textView) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textView.setBackgroundResource(R.drawable.circle_1);
            }
        });

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textView.setBackgroundResource(R.drawable.circle_2);
            }
        });

    }

    void setKilled(final TextView textView) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textView.setBackgroundResource(R.drawable.circle_3);
            }
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public void cal(int total, int keyNumber) {

        int index = -1;
        while (list.size() > 1) {
            for (int i = 0; i < keyNumber; i++) {

                index++;
                if (index> list.size() - 1) {
                    index = 0;
                }
                if (i==keyNumber-1){
                    setKilled(list.get(index));
                    list.remove(index);
                    index--;
                }else {
                    setAnimation(list.get(index));
                }

            }


        }

        final String str = total + "个人围成一圈数数，数到" + keyNumber + "的被淘汰，最后剩下的是第" + list.get(0).getText() + "个人。";
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showMsg.setText(str);
            }
        });
    }


}
