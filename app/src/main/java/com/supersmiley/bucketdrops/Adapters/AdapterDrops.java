package com.supersmiley.bucketdrops.Adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.supersmiley.bucketdrops.AppBucketDrops;
import com.supersmiley.bucketdrops.R;
import com.supersmiley.bucketdrops.beans.Drop;
import com.supersmiley.bucketdrops.extras.Util;

import io.realm.Realm;
import io.realm.RealmResults;

public class AdapterDrops extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements SwipeListener{
    public static final int COUNT_FOOTER = 1;
    public static final int COUNT_NO_ITEMS = 1;
    public static final int ITEM = 0;
    public static final int NO_ITEM = 1;
    public static final int FOOTER = 2;
    private final ResetListener mResetListener;
    private LayoutInflater mInflater;
    private RealmResults<Drop> mResults;
    private AddListener mAddListener;
    private MarkListener mMarkListener;
    private Realm mRealm;
    private int mFilterOption;
    private Context mContext;

    public AdapterDrops(Context context, Realm realm, RealmResults<Drop> results, AddListener addListener, MarkListener markListener, ResetListener resetListener){
        mInflater = LayoutInflater.from(context);
        mAddListener = addListener;
        mMarkListener = markListener;
        mRealm = realm;
        mContext = context;
        mResetListener = resetListener;
        update(results);
    }

    public void update(RealmResults<Drop> results){
        mResults = results;
        mFilterOption = AppBucketDrops.load(mContext);
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == FOOTER){
            View view = mInflater.inflate(R.layout.footer, parent, false);

            return new FooterHolder(view, mAddListener);
        } else if(viewType == NO_ITEM){
            View view = mInflater.inflate(R.layout.no_item, parent, false);

            return new DropHolder.NoItemsHolder(view);
        } else {
            View view = mInflater.inflate(R.layout.row_drop, parent, false);

            return new DropHolder(view, mMarkListener);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof DropHolder){
            DropHolder dropHolder = (DropHolder) holder;
            Drop drop = mResults.get(position);
            dropHolder.setWhat(drop.getWhat());
            dropHolder.setWhen(drop.getWhen());
            dropHolder.setBackground(drop.isCompleted());
        }
    }

    @Override
    public long getItemId(int position) {
        if(position < mResults.size()) {

           return mResults.get(position).getAdded();
        }

        return RecyclerView.NO_ID;
    }

    @Override
    public int getItemCount() {
        if(!mResults.isEmpty()){
            return mResults.size() + COUNT_FOOTER;
        } else if (mFilterOption == Filter.LEAST_TIME_LEFT
                || mFilterOption == Filter.MOST_TIME_LEFT
                || mFilterOption == Filter.NONE){
                return 0;
            } else {
                return COUNT_NO_ITEMS + COUNT_FOOTER;
            }
    }

    @Override
    public int getItemViewType(int position) {
        if(!mResults.isEmpty()){
            if(position < mResults.size()){
                return ITEM;
            } else {
                return FOOTER;
            }
        } else {
            if (mFilterOption == Filter.COMPLETED
                    || mFilterOption == Filter.UNCOMPLETED){
                if(position == 0){
                    return NO_ITEM;
                } else {
                    return  FOOTER;
                }
            } else {
                return ITEM;
            }
        }
    }

    @Override
    public void onSwipe(int position) {
        if(position < mResults.size()) {
            mRealm.beginTransaction();
            // Remove from database.
            mResults.get(position).removeFromRealm();
            mRealm.commitTransaction();
            // Notify to the adapter.
            notifyItemRemoved(position);
        }

        resetFilterIfEmpty();
    }

    private void resetFilterIfEmpty() {
        if(mResults.isEmpty() && (mFilterOption == Filter.COMPLETED || mFilterOption == Filter.UNCOMPLETED)){
            mResetListener.onReset();
        }
    }

    public void markComplete(int position) {
        if(position < mResults.size()) {
            mRealm.beginTransaction();
            // The object is updated in realm.
            mResults.get(position).setCompleted(true);
            mRealm.commitTransaction();

            // Notify changes to the adapter.
            notifyItemChanged(position);
        }
    }

    public static class DropHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView mTextWhat;
        TextView mTextWhen;
        MarkListener mMarkListener;
        Context mContext;
        View mItemView;

        public DropHolder(View itemView, MarkListener markListener) {
            super(itemView);

            mItemView = itemView;
            mContext = itemView.getContext();
            itemView.setOnClickListener(this);
            mTextWhat = (TextView) itemView.findViewById(R.id.tv_what);
            mTextWhen = (TextView) itemView.findViewById(R.id.tv_when);
            AppBucketDrops.setRalewayRegular(mContext, mTextWhat, mTextWhen);
            mMarkListener = markListener;
        }

        public void setWhat(String what){
            mTextWhat.setText(what);
        }

        public void setWhen(long when) {
            mTextWhen.setText(DateUtils.getRelativeTimeSpanString(when, System.currentTimeMillis(), DateUtils.DAY_IN_MILLIS, DateUtils.FORMAT_ABBREV_ALL));
        }

        public static class NoItemsHolder extends RecyclerView.ViewHolder {

            public NoItemsHolder(View itemView) {
                super(itemView);
            }
        }

        @Override
        public void onClick(View v) {
            mMarkListener.onMark(getAdapterPosition());
        }

        public void setBackground(boolean complete) {
            Drawable drawable;

            if(complete) {
                drawable = ContextCompat.getDrawable(mContext, R.color.bg_drop_complete);
            } else {
                drawable = ContextCompat.getDrawable(mContext, R.drawable.bg_row_drop);
            }

            Util.setBackground(mItemView, drawable);
        }
    }

    public static class FooterHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        Context mContext;
        Button mBtnAdd;
        AddListener mListener;

        public FooterHolder(View itemView) {
            super(itemView);

            mContext = itemView.getContext();
            mBtnAdd = (Button) itemView.findViewById(R.id.btn_footer);
            AppBucketDrops.setRalewayRegular(mContext, mBtnAdd);
            mBtnAdd.setOnClickListener(this);
        }

        public FooterHolder(View itemView, AddListener listener) {
            super(itemView);

            mBtnAdd = (Button) itemView.findViewById(R.id.btn_footer);
            mBtnAdd.setOnClickListener(this);
            mListener = listener;
        }

        @Override
        public void onClick(View v) {
            mListener.add();
        }
    }
}
