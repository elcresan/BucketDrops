package com.supersmiley.bucketdrops.widgets;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

import com.supersmiley.bucketdrops.extras.Util;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

// Custom recycler view to have a particular behaviour when there are no items left.
public class BucketRecyclerView extends RecyclerView {

    private List<View> mNonEmptyViews = Collections.emptyList();
    private List<View> mEmptyViews = Collections.emptyList();

    private AdapterDataObserver mObserver = new AdapterDataObserver() {
        @Override
        public void onChanged() {
            toggleViews();
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            toggleViews();
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
            toggleViews();
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            toggleViews();
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            toggleViews();
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            toggleViews();
        }
    };

    private void toggleViews() {
        if(getAdapter() != null && !mEmptyViews.isEmpty() && !mNonEmptyViews.isEmpty()){

            // There are no items.
            if (getAdapter().getItemCount() == 0){
                // Show all the views for the empty state.
                Util.showViews(mEmptyViews);

                // Hide the recycler view.
                setVisibility(View.GONE);

                // Hide all the views for the non empty state.
                Util.hideViews(mNonEmptyViews);
            } else {
                // Show all the views for the non empty state.
                Util.showViews(mNonEmptyViews);

                // Show the recycler view.
                setVisibility(View.VISIBLE);

                // Hide all the views for the empty state.
                Util.hideViews(mEmptyViews);
            }
        }
    }

    // Initialize recycler view from code.
    public BucketRecyclerView(Context context) {
        super(context);
    }

    // Initialize recycler view from xml.
    public BucketRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    // Initialize recycler view from xml.
    public BucketRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void setAdapter(Adapter adapter) {
        super.setAdapter(adapter);
        if(adapter != null){
            adapter.registerAdapterDataObserver(mObserver);
        }

        mObserver.onChanged();
    }

    public void hideIfEmpty(View ...views) {
        mNonEmptyViews = Arrays.asList(views);
    }

    public void showIfEmpty(View ...emptyViews) {
        mEmptyViews = Arrays.asList(emptyViews);
    }
}
