package com.cgy.colorfulnews.module.news.adapter;

import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cgy.colorfulnews.App;
import com.cgy.colorfulnews.R;
import com.cgy.colorfulnews.db.NewsChannel;
import com.cgy.colorfulnews.event.ChannelItemMoveEvent;
import com.cgy.colorfulnews.listener.OnItemClickListener;
import com.cgy.colorfulnews.module.base.BaseRecyclerViewAdapter;
import com.cgy.colorfulnews.utils.ClickUtil;
import com.cgy.colorfulnews.utils.MyUtils;
import com.cgy.colorfulnews.utils.RxBus;
import com.cgy.colorfulnews.widget.ItemDragHelperCallback;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author cgy
 * @desctiption
 * @date 2019/6/13 14:06
 */
public class NewsChannelAdapter extends BaseRecyclerViewAdapter<NewsChannel> implements ItemDragHelperCallback.OnItemMoveListener {

    private static final int TYPE_CHANNEL_FIXED = 0;
    private static final int TYPE_CHANNEL_NO_FIXED = 1;

    private ItemDragHelperCallback mItemDragHelperCallback;

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public void setItemDragHelperCallback(ItemDragHelperCallback itemDragHelperCallback) {
        mItemDragHelperCallback = itemDragHelperCallback;
    }


    public NewsChannelAdapter(List<NewsChannel> list) {
        super(list);
    }

    public List<NewsChannel> getData() {
        return mList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_news_channel, parent, false);
        final NewsChannelViewHolder newsChannelViewHolder = new NewsChannelViewHolder(view);
        handleLongPress(newsChannelViewHolder);
        handleOnClick(newsChannelViewHolder);
        return newsChannelViewHolder;
    }

    private void handleLongPress(final NewsChannelViewHolder newsChannelViewHolder) {
        if (mItemDragHelperCallback != null) {
            newsChannelViewHolder.itemView.setOnTouchListener((v, event) -> {
                NewsChannel newsChannel = mList.get(newsChannelViewHolder.getLayoutPosition());
                boolean isChannelFixed = newsChannel.getNewsChannelFixed();
                if (isChannelFixed) {
                    mItemDragHelperCallback.setLongPressEnabled(false);
                } else {
                    mItemDragHelperCallback.setLongPressEnabled(true);
                }
                return false;
            });
        }
    }

    private void handleOnClick(final NewsChannelViewHolder newsChannelViewHolder) {
        if (mOnItemClickListener != null) {
            newsChannelViewHolder.itemView.setOnClickListener(v -> {
                if (!ClickUtil.isFastDoubleClick()) {
                    mOnItemClickListener.onItemClick(v, newsChannelViewHolder.getLayoutPosition());
                }
            });
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final NewsChannel newsChannel = mList.get(position);
        String newsChannelName = newsChannel.getNewsChannelName();
        NewsChannelViewHolder viewHolder = (NewsChannelViewHolder) holder;
        viewHolder.mTvChannel.setText(newsChannelName);

        if (newsChannel.getNewsChannelIndex() == 0) {
            viewHolder.mTvChannel.setTextColor(ContextCompat.getColor(App.getAppContext(), getColorId()));
        }
    }

    private int getColorId() {
        int colorId;
        if (MyUtils.isNightMode()) {
            colorId = R.color.alpha_40_white;
        } else {
            colorId = R.color.alpha_40_black;
        }
        return colorId;
    }

    @Override
    public int getItemViewType(int position) {
        Boolean isFixed = mList.get(position).getNewsChannelFixed();
        if (isFixed) {
            return TYPE_CHANNEL_FIXED;
        } else {
            return TYPE_CHANNEL_NO_FIXED;
        }
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        if (isChannelFixed(fromPosition, toPosition)) {
            return false;
        }
        Collections.swap(mList, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
        RxBus.getInstance().post(new ChannelItemMoveEvent(fromPosition, toPosition));
        return false;
    }

    private boolean isChannelFixed(int fromPosition, int toPosition) {
        return mList.get(fromPosition).getNewsChannelFixed() || mList.get(toPosition).getNewsChannelFixed();
    }

    class NewsChannelViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.tv_channel)
        TextView mTvChannel;

        public NewsChannelViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
