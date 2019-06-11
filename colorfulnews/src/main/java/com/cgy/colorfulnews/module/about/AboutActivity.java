package com.cgy.colorfulnews.module.about;

import android.os.Bundle;

import com.cgy.colorfulnews.R;
import com.cgy.colorfulnews.module.base.BaseActivity;

public class AboutActivity extends BaseActivity {

    @Override
    public int getLayoutId() {
        return 0;
    }

    @Override
    public void initInjector() {

    }

    @Override
    public void initView() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
    }
}
