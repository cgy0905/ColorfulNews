package com.cgy.colorfulnews.module.photo.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.cgy.colorfulnews.App;
import com.cgy.colorfulnews.R;
import com.cgy.colorfulnews.entity.PhotoGirl;
import com.cgy.colorfulnews.module.base.BaseRecyclerViewAdapter;
import com.cgy.colorfulnews.utils.DimenUtil;
import com.cgy.colorfulnews.widget.RatioImageView;
import com.socks.library.KLog;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author cgy
 * @desctiption
 * @date 2019/6/14 17:31
 */
public class PhotoListAdapter extends BaseRecyclerViewAdapter<PhotoGirl> {

    private int width = (DimenUtil.getScreenSize() / 2);

    private Map<Integer, Integer> mHeights = new HashMap<>();

    @Inject
    public PhotoListAdapter() {
        super(null);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case TYPE_FOOTER:
                view = getView(parent, R.layout.item_news_footer);
                return new FooterViewHolder(view);
            case TYPE_ITEM:
                view = getView(parent, R.layout.item_photo);
                final ItemViewHolder itemViewHolder = new ItemViewHolder(view);
                setItemOnClickEvent(itemViewHolder);
                return itemViewHolder;
            default:
                throw new RuntimeException("there is no type that matches the type " +
                        viewType + " + make sure your using types correctly");

        }
    }

    private void setItemOnClickEvent(final RecyclerView.ViewHolder holder) {
        if (mOnItemClickListener != null) {
            holder.itemView.setOnClickListener(v -> {
                mOnItemClickListener.onItemClick(v, holder.getLayoutPosition());
            });
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        if (holder instanceof  ItemViewHolder) {
//            ((ItemViewHolder) holder).mPhotoIv.setOriginalSize(width, getHeight(position));

/*            SimpleTarget<Bitmap> simpleTarget = new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                    ((ItemViewHolder) holder).mPhotoIv.setOriginalSize(resource.getWidth(), resource.getHeight());
                    ((ItemViewHolder) holder).mPhotoIv.setImageBitmap(resource);
                }
            };*/  // 加载图片后设置实际图片宽高比，由于加载图片耗时，使用瀑布流比较混乱，容易重叠错位

/*            Glide.with(App.getAppContext())
                    .load(mList.get(position).getUrl())
//                    .asBitmap()*//*.format(DecodeFormat.PREFER_ARGB_8888)*//* // 没有动画
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .crossFade()
                    .placeholder(R.color.image_place_holder)
                    .error(R.drawable.ic_load_fail)
//                    .into(simpleTarget);
                    .into(((ItemViewHolder) holder).mPhotoIv);*/
            Picasso.with(App.getAppContext())
                    .load(mList.get(position).getUrl())
                    .placeholder(R.color.image_place_holder)
                    .error(R.drawable.ic_load_fail)
                    .into(((ItemViewHolder)holder).mIvPhoto);
            // 使用picasso加载图片可以自动计算图片实际宽高比进行设置，刷新也不会出现闪屏现象！
            // 使用动画效果，当滑动过快时会引起item重叠，除了不使用动画，暂还没有想到更好的方法解决此冲突问题！
            // 跳转到图片详情有时共享动画会卡着不动，点一下屏幕才恢复
//        setItemAppearAnimation(holder, position, R.anim.anim_rotate_scale_in);
        }
    }

    private int getHeight(int position) {
        int height;
        try {
            if (position >= mHeights.size()) {
                height = getRandomHeight();
                mHeights.put(position, height);
            } else {
                height = mHeights.get(position);
            }
        } catch (Exception e) {
            KLog.e();
            height = width / 2;
        }
        return height;
    }

    private int getRandomHeight() {
        int height;
        height = (int) (width * (new Random().nextFloat() / 2 + 1));
        return height;
    }

    @Override
    public int getItemViewType(int position) {
        if (mIsShowFooter && isFooterPosition(position)) {
            return TYPE_FOOTER;
        } else {
            return TYPE_ITEM;
        }
    }

    class ItemViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.iv_photo)
        RatioImageView mIvPhoto;

        ItemViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
