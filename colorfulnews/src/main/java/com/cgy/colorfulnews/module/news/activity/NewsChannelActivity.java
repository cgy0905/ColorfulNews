package com.cgy.colorfulnews.module.news.activity;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import com.cgy.colorfulnews.R;
import com.cgy.colorfulnews.db.NewsChannel;
import com.cgy.colorfulnews.event.ChannelItemMoveEvent;
import com.cgy.colorfulnews.listener.OnItemClickListener;
import com.cgy.colorfulnews.module.base.BaseActivity;
import com.cgy.colorfulnews.module.news.adapter.NewsChannelAdapter;
import com.cgy.colorfulnews.module.news.presenter.impl.NewsChannelPresenterImpl;
import com.cgy.colorfulnews.module.news.view.NewsChannelView;
import com.cgy.colorfulnews.utils.RxBus;
import com.cgy.colorfulnews.widget.ItemDragHelperCallback;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

public class NewsChannelActivity extends BaseActivity implements NewsChannelView {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.rv_mine_channel)
    RecyclerView mRvMineChannel;
    @BindView(R.id.rv_more_channel)
    RecyclerView mRvMoreChannel;

    @Inject
    NewsChannelPresenterImpl mNewsChannelPresenter;

    private NewsChannelAdapter mNewsChannelAdapterMine;
    private NewsChannelAdapter mNewsChannelAdapterMore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSubscription = RxBus.getInstance().toObservable(ChannelItemMoveEvent.class)
                .subscribe(channelItemMoveEvent -> {
                    int fromPosition = channelItemMoveEvent.getFromPosition();
                    int toPosition = channelItemMoveEvent.getToPosition();
                    mNewsChannelPresenter.onItemSwap(fromPosition, toPosition);
                });
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_news_channel;
    }

    @Override
    public void initInjector() {
        mActivityComponent.inject(this);
    }

    @Override
    public void initView() {
        mPresenter = mNewsChannelPresenter;
        mPresenter.attachView(this);
    }


    @Override
    public void initRecyclerView(List<NewsChannel> newsChannelsMine, List<NewsChannel> newsChannelsMore) {
        initRecyclerViewMineAndMore(newsChannelsMine, newsChannelsMore);
    }

    private void initRecyclerViewMineAndMore(List<NewsChannel> newsChannelsMine, List<NewsChannel> newsChannelsMore) {
        initRecyclerView(mRvMineChannel, newsChannelsMine, true);
        initRecyclerView(mRvMoreChannel, newsChannelsMore, false);
    }

    private void initRecyclerView(RecyclerView recyclerView, List<NewsChannel> newsChannels, boolean isChannelMine) {

        // FIXME: 2019/6/13 加上这句将不能动态增加列表大小
//        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 4, LinearLayoutManager.VERTICAL, false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        if (isChannelMine) {
            mNewsChannelAdapterMine = new NewsChannelAdapter(newsChannels);
            recyclerView.setAdapter(mNewsChannelAdapterMine);
            setChannelMineOnItemClick();

            initItemDragHelper();
        } else {
            mNewsChannelAdapterMore = new NewsChannelAdapter(newsChannels);
            recyclerView.setAdapter(mNewsChannelAdapterMore);
            setChannelMoreOnItemClick();
        }
    }

    private void setChannelMineOnItemClick() {
        mNewsChannelAdapterMine.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                NewsChannel newsChannel = mNewsChannelAdapterMine.getData().get(position);
                boolean isNewsChannelFixed = newsChannel.getNewsChannelFixed();
                if (!isNewsChannelFixed) {
                    mNewsChannelAdapterMore.add(mNewsChannelAdapterMore.getItemCount(), newsChannel);
                    mNewsChannelAdapterMine.delete(position);

                    mNewsChannelPresenter.onIteAddOrRemove(newsChannel, true);
                }
            }
        });
    }

    private void setChannelMoreOnItemClick() {
        mNewsChannelAdapterMore.setOnItemClickListener((view, position) -> {
            NewsChannel newsChannel = mNewsChannelAdapterMore.getData().get(position);
            mNewsChannelAdapterMine.add(mNewsChannelAdapterMine.getItemCount(), newsChannel);
            mNewsChannelAdapterMore.delete(position);

            mNewsChannelPresenter.onIteAddOrRemove(newsChannel, false);
        });
    }

    private void initItemDragHelper() {
        ItemDragHelperCallback itemDragHelperCallback = new ItemDragHelperCallback(mNewsChannelAdapterMine);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(itemDragHelperCallback);
        itemTouchHelper.attachToRecyclerView(mRvMineChannel);

        mNewsChannelAdapterMine.setItemDragHelperCallback(itemDragHelperCallback);
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void showMsg(String message) {
        Snackbar.make(mRvMoreChannel, message, Snackbar.LENGTH_SHORT).show();
    }
}
