package com.cgy.colorfulnews.module.news.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.cgy.colorfulnews.App;
import com.cgy.colorfulnews.R;
import com.cgy.colorfulnews.entity.NewsSummary;
import com.cgy.colorfulnews.listener.OnItemClickListener;
import com.cgy.colorfulnews.module.base.BaseRecyclerViewAdapter;
import com.cgy.colorfulnews.utils.DimenUtil;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author cgy
 * @desctiption
 * @date 2019/6/12 14:19
 */
public class NewsListAdapter extends BaseRecyclerViewAdapter<NewsSummary> {

    public static final int TYPE_PHOTO_ITEM = 2;

    public interface OnNewsListItemClickListener extends OnItemClickListener {
        void onItemClick(View view, int position, boolean isPhoto);
    }

    @Inject
    public NewsListAdapter() {
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
                view = getView(parent, R.layout.item_news);
                final ItemViewHolder itemViewHolder = new ItemViewHolder(view);
                setItemOnClickEvent(itemViewHolder, false);
                return itemViewHolder;
            case TYPE_PHOTO_ITEM:
                view = getView(parent, R.layout.item_news_photo);
                final PhotoViewHolder photoViewHolder = new PhotoViewHolder(view);
                setItemOnClickEvent(photoViewHolder, true);
                return photoViewHolder;
            default:
                throw new RuntimeException("there is no type that matches the type " +
                        viewType + " + make sure your using types correctly");
        }
    }

    private void setItemOnClickEvent(final RecyclerView.ViewHolder holder, final boolean isPhoto) {
        if (mOnItemClickListener != null) {
            holder.itemView.setOnClickListener(v -> ((OnNewsListItemClickListener) mOnItemClickListener).onItemClick(v, holder.getLayoutPosition(), isPhoto));
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (mIsShowFooter && isFooterPosition(position)) {
            return TYPE_FOOTER;
        } else if (!TextUtils.isEmpty(mList.get(position).getDigest())) {
            return TYPE_ITEM;
        } else {
            return TYPE_PHOTO_ITEM;
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        setValues(holder, position);
        setItemAppearAnimation(holder, position, R.anim.anim_bottom_in);
    }

    private void setValues(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder) {
            setItemValues((ItemViewHolder) holder, position);
        } else if (holder instanceof PhotoViewHolder) {
            setPhotoItemValues((PhotoViewHolder) holder, position);
        }
    }

    private void setItemValues(ItemViewHolder holder, int position) {
        NewsSummary newsSummary = mList.get(position);
        String title = newsSummary.getLtitle();
        if (title == null) {
            title = newsSummary.getTitle();
        }
        String pTime = newsSummary.getPtime();
        String digest = newsSummary.getDigest();
        String imgSrc = newsSummary.getImgsrc();

        holder.mNtvTitle.setText(title);
        holder.mTvTime.setText(pTime);
        holder.mTvDigest.setText(digest);

        Glide.with(App.getAppContext()).load(imgSrc).asBitmap()// gif格式有时会导致整体图片不显示,貌似有冲突
                .format(DecodeFormat.PREFER_ARGB_8888)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.color.image_place_holder)
                .error(R.drawable.ic_load_fail)
                .into(holder.mIvPhoto);
    }

    private void setPhotoItemValues(PhotoViewHolder holder, int position) {
        NewsSummary newsSummary = mList.get(position);
        setTextView(holder, newsSummary);
        setImageView(holder, newsSummary);
    }

    private void setTextView(PhotoViewHolder holder, NewsSummary newsSummary) {
        String title = newsSummary.getTitle();
        String pTime = newsSummary.getPtime();

        holder.mTvTitle.setText(title);
        holder.mTvTime.setText(pTime);
    }

    private void setImageView(PhotoViewHolder holder, NewsSummary newsSummary) {
        int photoThreeHeight = (int) DimenUtil.dp2px(90);
        int photoTwoHeight = (int) DimenUtil.dp2px(120);
        int photoOneHeight = (int) DimenUtil.dp2px(150);

        String imgSrcLeft = null;
        String imgSrcMiddle = null;
        String imgSrcRight = null;

        ViewGroup.LayoutParams layoutParams = holder.mLlPhoto.getLayoutParams();

        if (newsSummary.getAds() != null) {
            List<NewsSummary.AdsBean> adsBeanList = newsSummary.getAds();
            int size = adsBeanList.size();
            if (size >= 3) {
                imgSrcLeft = adsBeanList.get(0).getImgsrc();
                imgSrcMiddle = adsBeanList.get(1).getImgsrc();
                imgSrcRight = adsBeanList.get(2).getImgsrc();

                layoutParams.height = photoThreeHeight;

                holder.mTvTitle.setText(App.getAppContext().
                        getString(R.string.photo_collections, adsBeanList.get(0).getTitle()));
            } else if (size >= 2) {
                imgSrcLeft = adsBeanList.get(0).getImgsrc();
                imgSrcMiddle = adsBeanList.get(1).getImgsrc();

                layoutParams.height = photoTwoHeight;
            } else if (size >= 1) {
                imgSrcLeft  = adsBeanList.get(0).getImgsrc();

                layoutParams.height = photoOneHeight;
            }
        } else if (newsSummary.getImgextra() != null) {
            int size = newsSummary.getImgextra().size();
            if (size >= 3) {
                imgSrcLeft = newsSummary.getImgextra().get(0).getImgsrc();
                imgSrcMiddle = newsSummary.getImgextra().get(1).getImgsrc();
                imgSrcRight = newsSummary.getImgextra().get(2).getImgsrc();

                layoutParams.height = photoThreeHeight;
            } else if (size >= 2) {
                imgSrcLeft = newsSummary.getImgextra().get(0).getImgsrc();
                imgSrcMiddle = newsSummary.getImgextra().get(1).getImgsrc();

                layoutParams.height = photoTwoHeight;
            } else if (size >= 1) {
                imgSrcLeft = newsSummary.getImgextra().get(0).getImgsrc();

                layoutParams.height = photoOneHeight;
            }
        } else {
            imgSrcLeft = newsSummary.getImgsrc();

            layoutParams.height = photoOneHeight;
        }

        setPhotoImageView(holder, imgSrcLeft, imgSrcMiddle, imgSrcRight);
        holder.mLlPhoto.setLayoutParams(layoutParams);
    }

    private void setPhotoImageView(PhotoViewHolder holder, String imgSrcLeft, String imgSrcMiddle, String imgSrcRight) {
        if (imgSrcLeft != null) {
            showAndSetPhoto(holder.mIvLeftPhoto, imgSrcLeft);
        } else {
            hidePhoto(holder.mIvLeftPhoto);
        }

        if (imgSrcMiddle != null) {
            showAndSetPhoto(holder.mIvMiddlePhoto, imgSrcMiddle);
        } else {
            hidePhoto(holder.mIvMiddlePhoto);
        }

        if (imgSrcRight != null) {
            showAndSetPhoto(holder.mIvRightPhoto, imgSrcRight);
        } else {
            hidePhoto(holder.mIvRightPhoto);
        }
    }

    private void showAndSetPhoto(ImageView imageView, String imgSrc) {
        imageView.setVisibility(View.VISIBLE);
        Glide.with(App.getAppContext())
                .load(imgSrc)
                .asBitmap()
                .format(DecodeFormat.PREFER_ARGB_8888)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.color.image_place_holder)
                .error(R.drawable.ic_load_fail)
                .into(imageView);
    }

    private void hidePhoto(ImageView imageView) {
        imageView.setVisibility(View.GONE);
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull RecyclerView.ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        if (isShowingAnimation(holder)) {
            holder.itemView.clearAnimation();
        }
    }

    private boolean isShowingAnimation(RecyclerView.ViewHolder holder) {
        return holder.itemView.getAnimation() != null && holder.itemView.getAnimation().hasStarted();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_photo)
        ImageView mIvPhoto;
        @BindView(R.id.ntv_title)
        TextView mNtvTitle;
        @BindView(R.id.tv_digest)
        TextView mTvDigest;
        @BindView(R.id.tv_time)
        TextView mTvTime;

        public ItemViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    class PhotoViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_title)
        TextView mTvTitle;
        @BindView(R.id.iv_left_photo)
        ImageView mIvLeftPhoto;
        @BindView(R.id.iv_middle_photo)
        ImageView mIvMiddlePhoto;
        @BindView(R.id.iv_right_photo)
        ImageView mIvRightPhoto;
        @BindView(R.id.ll_photo)
        LinearLayout mLlPhoto;
        @BindView(R.id.tv_time)
        TextView mTvTime;

        public PhotoViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
