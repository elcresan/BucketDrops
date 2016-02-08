package com.supersmiley.bucketdrops.Adapters;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

public class SimpleTouchCallback extends ItemTouchHelper.Callback {
    private SwipeListener mListener;

    public SimpleTouchCallback(SwipeListener listener) {
        mListener = listener;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        return makeMovementFlags(0, ItemTouchHelper.END | ItemTouchHelper.START);
    }

    @Override
    public boolean isLongPressDragEnabled() {
        // Return always false because we don't want the drag gesture.
        return false;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return true;
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        // Return always false because we don't want the drag gesture.
        return false;
    }

    // Called after the item is swiped.
    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        // We need to create an interface to communicate with the adapter of the recycler view.
        mListener.onSwipe(viewHolder.getAdapterPosition());
    }
}
