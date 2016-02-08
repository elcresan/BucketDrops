package com.supersmiley.bucketdrops.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.supersmiley.bucketdrops.R;
import com.supersmiley.bucketdrops.beans.Drop;

import io.realm.Realm;
import io.realm.RealmResults;

public class AdapterDrops extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements SwipeListener{
    public static final int ITEM = 0;
    public static final int FOOTER = 1;
    private LayoutInflater mInflater;
    private RealmResults<Drop> mResults;
    private AddListener mAddListener;
    private Realm mRealm;

    public AdapterDrops(Context context, Realm realm, RealmResults<Drop> results){
        mInflater = LayoutInflater.from(context);
        mRealm = realm;
        update(results);
    }

    public AdapterDrops(Context context, Realm realm, RealmResults<Drop> results, AddListener listener){
        mInflater = LayoutInflater.from(context);
        mAddListener = listener;
        mRealm = realm;
        update(results);
    }

    public void update(RealmResults<Drop> results){
        mResults = results;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        // We return an item if results are null or if the position is withing the bounds of the results
        if(mResults == null || position < mResults.size()){
            return ITEM;
        } else{
            return FOOTER;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == FOOTER){
            View view = mInflater.inflate(R.layout.footer, parent, false);
            return new FooterHolder(view, mAddListener);
        } else{
            View view = mInflater.inflate(R.layout.row_drop, parent, false);
           return new DropHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof DropHolder){
            Drop drop = mResults.get(position);
            ((DropHolder)holder).mTextWhat.setText(drop.getWhat());
            ((DropHolder)holder).mTextWhen.setText("Today!");
        }
    }

    @Override
    public int getItemCount() {
        // We need this check because of the footer.
        return (mResults == null || mResults.isEmpty()) ? 0 : mResults.size() + 1;
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
    }

    public static class DropHolder extends RecyclerView.ViewHolder{
        TextView mTextWhat;
        TextView mTextWhen;

        public DropHolder(View itemView) {
            super(itemView);

            mTextWhat = (TextView) itemView.findViewById(R.id.tv_what);
            mTextWhen = (TextView) itemView.findViewById(R.id.tv_when);
        }
    }

    public static class FooterHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        Button mBtnAdd;
        AddListener mListener;

        public FooterHolder(View itemView) {
            super(itemView);

            mBtnAdd = (Button) itemView.findViewById(R.id.btn_footer);
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
