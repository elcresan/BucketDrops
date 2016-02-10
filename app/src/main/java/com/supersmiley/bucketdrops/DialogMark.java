package com.supersmiley.bucketdrops;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.supersmiley.bucketdrops.Adapters.CompleteListener;

public class DialogMark extends DialogFragment {

    private ImageButton mBtnclose;
    private Button mBtnCompleted;
    private CompleteListener mListener;

    private View.OnClickListener mBtnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btn_completed:
                    // TODO: handle the action to mark the item as completed.
                    markAsComplete();
                    break;
            }

            // Dismiss the dialog.
            dismiss();
        }
    };

    private void markAsComplete() {
        Bundle arguments = getArguments();

        if(mListener != null && arguments != null){
            mListener.onComplete(arguments.getInt("POSITION"));
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_mark, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mBtnclose = (ImageButton) view.findViewById(R.id.btn_close);
        mBtnCompleted = (Button) view.findViewById(R.id.btn_completed);
        mBtnclose.setOnClickListener(mBtnClickListener);
        mBtnCompleted.setOnClickListener(mBtnClickListener);
    }

    public void setCompleteListener(CompleteListener completeListener) {
        mListener = completeListener;
    }
}
