package com.supersmiley.bucketdrops;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.supersmiley.bucketdrops.Adapters.AdapterDrops;
import com.supersmiley.bucketdrops.Adapters.AddListener;
import com.supersmiley.bucketdrops.Adapters.CompleteListener;
import com.supersmiley.bucketdrops.Adapters.Divider;
import com.supersmiley.bucketdrops.Adapters.MarkListener;
import com.supersmiley.bucketdrops.Adapters.SimpleTouchCallback;
import com.supersmiley.bucketdrops.beans.Drop;
import com.supersmiley.bucketdrops.widgets.BucketRecyclerView;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import io.realm.Sort;

public class ActivityMain extends AppCompatActivity{

    Toolbar mToolbar;
    Button mBtnAdd;
    BucketRecyclerView mRecycler;
    AdapterDrops mAdapter;
    Realm mRealm;
    RealmResults<Drop> mResults;
    View mEmptyView;

    private View.OnClickListener mBtnAddListener = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            showDialogAdd();
        }
    };

    private AddListener mAddListener = new AddListener() {
        @Override
        public void add() {
            showDialogAdd();
        }
    };

    private RealmChangeListener mChangeListener = new RealmChangeListener() {
        @Override
        public void onChange() {
            mAdapter.update(mResults);
        }
    };

    private MarkListener mMarkListener = new MarkListener() {
        @Override
        public void onMark(int position) {
            showDialogMark(position);
        }
    };

    private CompleteListener mCompleteListener = new CompleteListener() {
        @Override
        public void onComplete(int position) {
            mAdapter.markComplete(position);
        }
    };

    private void showDialogAdd() {
        DialogAdd dialog = new DialogAdd();
        dialog.show(getSupportFragmentManager(), "Add");
    }

    private void showDialogMark(int position) {
        DialogMark dialog = new DialogMark();
        Bundle bundle = new Bundle();
        bundle.putInt("POSITION", position);
        dialog.setArguments(bundle);
        dialog.setCompleteListener(mCompleteListener);
        dialog.show(getSupportFragmentManager(), "Mark");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        mRealm = Realm.getDefaultInstance();
        mResults = mRealm.where(Drop.class).findAllAsync();
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mEmptyView = findViewById(R.id.empty_drops);
        mBtnAdd = (Button) findViewById(R.id.btn_add);        
        mBtnAdd.setOnClickListener(mBtnAddListener);
        mRecycler = (BucketRecyclerView) findViewById(R.id.rv_drops);
        mRecycler.addItemDecoration(new Divider(this, LinearLayoutManager.VERTICAL));
        mRecycler.hideIfEmpty(mToolbar);
        mRecycler.showIfEmpty(mEmptyView);
        mAdapter = new AdapterDrops(this, mRealm, mResults, mAddListener, mMarkListener);
        mRecycler.setAdapter(mAdapter);
        setSupportActionBar(mToolbar);

        // Handle swipe.
        SimpleTouchCallback callback = new SimpleTouchCallback(mAdapter);
        ItemTouchHelper helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(mRecycler);

        initBackgroundImage();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mResults.addChangeListener(mChangeListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mResults.removeChangeListener(mChangeListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id){
            case R.id.action_add:
                showDialogAdd();

                return true;
            case R.id.action_sort_descending_date:
                mResults = mRealm.where(Drop.class).findAllSortedAsync("when");
                mResults.addChangeListener(mChangeListener);
                return true;
            case R.id.action_sort_ascending_date:
                mResults = mRealm.where(Drop.class).findAllSortedAsync("when", Sort.DESCENDING);
                mResults.addChangeListener(mChangeListener);
                return true;
            case R.id.action_show_complete:
                mResults = mRealm.where(Drop.class).equalTo("completed", true).findAllAsync();
                mResults.addChangeListener(mChangeListener);
                return true;
            case R.id.action_show_incomplete:
                mResults = mRealm.where(Drop.class).equalTo("completed", false).findAllAsync();
                mResults.addChangeListener(mChangeListener);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void initBackgroundImage(){
        ImageView background = (ImageView) findViewById(R.id.iv_background);
        Glide.with(this)
                .load(R.drawable.background)
                .centerCrop()
                .into(background);
    }
}
