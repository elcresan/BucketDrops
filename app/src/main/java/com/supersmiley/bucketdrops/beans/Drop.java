package com.supersmiley.bucketdrops.beans;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Drop extends RealmObject {
    private String what;

    @PrimaryKey
    private long added;
    private long when;
    private boolean complete;

    public Drop() {
    }

    public Drop(String what, long added, long when, boolean complete) {
        this.complete = complete;
        this.what = what;
        this.added = added;
        this.when = when;
    }

    public String getWhat() {
        return what;
    }

    public void setWhat(String what) {
        this.what = what;
    }

    public long getAdded() {
        return added;
    }

    public void setAdded(long added) {
        this.added = added;
    }

    public long getWhen() {
        return when;
    }

    public void setWhen(long when) {
        this.when = when;
    }

    public boolean isComplete() {
        return complete;
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }
}
