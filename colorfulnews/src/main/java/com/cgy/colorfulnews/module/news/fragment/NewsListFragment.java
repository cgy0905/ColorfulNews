package com.cgy.colorfulnews.module.news.fragment;

import android.app.Activity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.cgy.colorfulnews.R;
import com.cgy.colorfulnews.entity.NewsSummary;
import com.cgy.colorfulnews.module.base.BaseFragment;
import com.cgy.colorfulnews.module.news.adapter.NewsListAdapter;
import com.cgy.colorfulnews.module.news.presenter.impl.NewsListPresenterImpl;
import com.cgy.colorfulnews.module.news.view.NewsListView;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * @author cgy
 * @desctiption
 * @date 2019/6/12 13:58
 */
public class NewsListFragment extends BaseFragment implements NewsListView, SwipeRefreshLayout.OnRefreshListener{

    @BindView(R.id.news_rv)
    RecyclerView mNewsRv;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.empty_view)
    TextView mEmptyView;
    @BindView(R.id.progress_bar)
    ProgressBar mProgressBar;

    @Inject
    NewsListAdapter mNewsListAdapter;
    @Inject
    NewsListPresenterImpl mNewsListPresenter;
    @Inject
    Activity mActivity;

    private String mNewsId;
    private String mNewsType;

    private boolean mIsAllLoaded;

    @Override
    public void initInjector() {
        mFragmentComponent.inject(this);
    }

    @Override
    public void initView(View view) {
        initSwipeRefreshLayout();
        initRecyclerView();
        initPresenter();
        registerScrollToTopEvent();
    }

    private void initSwipeRefreshLayout() {
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeColors(getActivity().getResources().getIntArray(R.array.gplus_colors));
    }

    private void initPresenter() {
        mNewsListPresenter.setNewsTypeAndId(mNewsType, mNewsId);
        mPresenter = mNewsListPresenter;
        mPresenter.attachView(this);
        mPresenter.onCreate();
    }

    private void registerScrollToTopEvent() {

    }

    private void initRecyclerView() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_news;
    }

    @Override
    public void setNewsList(List<NewsSummary> newsSummary, int loadType) {

    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void showMsg(String message) {

    }

    @Override
    public void onRefresh() {

    }
}
