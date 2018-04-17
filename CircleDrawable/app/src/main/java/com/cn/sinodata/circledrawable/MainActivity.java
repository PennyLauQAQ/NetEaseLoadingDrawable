package com.cn.sinodata.circledrawable;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private CircleDrawable circleDrawable;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = findViewById(R.id.img);

        circleDrawable = new CircleDrawable(new int[]{getResources().getColor(android.R.color.holo_blue_dark), getResources().getColor(android.R.color.holo_green_dark), getResources().getColor(android.R.color.holo_orange_dark)
                , getResources().getColor(android.R.color.holo_red_dark), getResources().getColor(android.R.color.holo_blue_bright)});
        imageView.setImageDrawable(circleDrawable);

        findViewById(R.id.size_btn).setOnClickListener(this);
        findViewById(R.id.color_btn).setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        circleDrawable.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        circleDrawable.stop();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.size_btn:
                ViewGroup.LayoutParams layoutParams = imageView.getLayoutParams();
                layoutParams.width = layoutParams.height = 200;
                imageView.setLayoutParams(layoutParams);
                break;
            case R.id.color_btn:
                circleDrawable.setColors(new int[]{getResources().getColor(android.R.color.holo_blue_dark), getResources().getColor(android.R.color.holo_green_dark)});
                break;
        }
    }
}
