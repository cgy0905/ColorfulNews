package com.cgy.colorfulnews.module.about;

import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.widget.TextView;

import com.cgy.colorfulnews.R;
import com.cgy.colorfulnews.module.base.BaseActivity;

import butterknife.BindView;

public class AboutActivity extends BaseActivity {

    @BindView(R.id.tv_msg)
    TextView mTvMsg;

    @Override
    public int getLayoutId() {
        return R.layout.activity_about;
    }

    @Override
    public void initInjector() {

    }

    @Override
    public void initView() {
        mTvMsg.setAutoLinkMask(Linkify.ALL);
        mTvMsg.setMovementMethod(LinkMovementMethod.getInstance());
    }

}
