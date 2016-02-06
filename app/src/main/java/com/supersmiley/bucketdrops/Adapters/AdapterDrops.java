package com.supersmiley.bucketdrops.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.supersmiley.bucketdrops.R;

public class AdapterDrops extends RecyclerView.Adapter<AdapterDrops.DropHolder> {

    private LayoutInflater mInflater;

    public AdapterDrops(Context context){
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public DropHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.row_drop, parent, false);
        return new DropHolder(view);
    }


    @Override
    public void onBindViewHolder(DropHolder holder, int position) {
        holder.mTextWhat.setText("Item " + position);
        holder.mTextWhen.setText("Today!");
    }


    @Override
    public int getItemCount() {
        return 100;
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
}
