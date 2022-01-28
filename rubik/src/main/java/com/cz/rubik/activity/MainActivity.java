package com.cz.rubik.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.cz.rubik.R;

/*
 * 主类
 */
public class MainActivity extends AppCompatActivity {
    private LinearLayout outer;
    private TextView order_2;
    private TextView order_3;
    private TextView order_4;
    private TextView order_5;
    private TextView order_6;
    private TextView order_n;
    private EditText more_edt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        outer = findViewById(R.id.outer);
        order_2 = findViewById(R.id.order_2);
        order_3 = findViewById(R.id.order_3);
        order_4 = findViewById(R.id.order_4);
        order_5 = findViewById(R.id.order_5);
        order_6 = findViewById(R.id.order_6);
        order_n = findViewById(R.id.order_n);
        more_edt = findViewById(R.id.more);
        outer.setOnClickListener(clickListener);
        order_2.setOnClickListener(clickListener);
        order_3.setOnClickListener(clickListener);
        order_4.setOnClickListener(clickListener);
        order_5.setOnClickListener(clickListener);
        order_6.setOnClickListener(clickListener);
        order_n.setOnClickListener(clickListener);
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int order = 1;
            switch (v.getId()) {
                case R.id.order_2:
                    order = 2;
                    break;
                case R.id.order_3:
                    order = 3;
                    break;
                case R.id.order_4:
                    order = 4;
                    break;
                case R.id.order_5:
                    order = 5;
                    break;
                case R.id.order_6:
                    order = 6;
                    break;
                case R.id.order_n:
                    order = getMoreOrder();
                    if (order > 15 || order < 1) {
                        Toast.makeText(MainActivity.this, "阶数必须是1~15之间的整数", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    hideKeyboard(MainActivity.this);
                    break;
                case R.id.outer:
                    hideKeyboard(MainActivity.this);
                    return;
            }
            Intent intent = new Intent(MainActivity.this, RubikActivity.class);
            intent.putExtra("order", order);
            startActivity(intent);
        }
    };

    private int getMoreOrder() {
        int order = 7;
        String orderStr = String.valueOf(more_edt.getText());
        if (orderStr != null && !orderStr.equals("")) {
            order = Integer.parseInt(orderStr);
        }
        return order;
    }

    // 隐藏软键盘
    private void hideKeyboard(Activity context) {
        more_edt.clearFocus();
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(context.getWindow().getDecorView().getWindowToken(), 0);
    }
}
