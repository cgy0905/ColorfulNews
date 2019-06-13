package com.cgy.colorfulnews.widget;

import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.helper.ItemTouchHelper;

/**
 * @author cgy
 * @desctiption
 * @date 2019/6/13 14:26
 */
public class ItemDragHelperCallback extends ItemTouchHelper.Callback {

    private OnItemMoveListener mOnItemMoveListener;
    private boolean mIsLongPressEnabled;

    public void setLongPressEnabled(boolean longPressEnabled) {
        mIsLongPressEnabled = longPressEnabled;
    }

    public interface OnItemMoveListener {
        boolean onItemMove(int fromPosition, int toPosition);
    }

    public ItemDragHelperCallback(OnItemMoveListener onItemMoveListener) {
        mOnItemMoveListener = onItemMoveListener;
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return mIsLongPressEnabled;
    }

    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        int dragFlags = setDragFlags(recyclerView);
        int swipeFlags = 0;
        return makeMovementFlags(dragFlags, swipeFlags);
    }

    private int setDragFlags(RecyclerView recyclerView) {
        int dragFlags;
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager || layoutManager instanceof StaggeredGridLayoutManager) {
            dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT |ItemTouchHelper.RIGHT;
        } else {
            dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        }
        return dragFlags;
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return !isDifferentItemViewType(viewHolder, target) &&
                mOnItemMoveListener.onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
    }

    private boolean isDifferentItemViewType(RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return viewHolder.getItemViewType() != target.getItemViewType();
    }

    @Override
    public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

    }
}
