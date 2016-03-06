package com.supersmiley.bucketdrops.services;

import android.app.IntentService;
import android.app.Notification;
import android.content.Intent;

import com.supersmiley.bucketdrops.ActivityMain;
import com.supersmiley.bucketdrops.R;
import com.supersmiley.bucketdrops.beans.Drop;

import br.com.goncalves.pugnotification.notification.PugNotification;
import io.realm.Realm;
import io.realm.RealmResults;

public class NotificationService extends IntentService {

    public NotificationService() {
        super("NotificationService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Realm realm = null;

        try {
            realm = Realm.getDefaultInstance();
            RealmResults<Drop> results = realm.where(Drop.class).equalTo("completed", false).findAll();

            for(Drop drop : results){
                if(isNotificationNeeded(drop.getAdded(), drop.getWhen())) {
                    fireNotification(drop);
                }
            }

        } finally {
            if(realm != null){
                realm.close();
            }
        }
    }

    private void fireNotification(Drop drop) {
        String message = getString(R.string.notif_message) + "\"" + drop.getWhat() + "\"";

        PugNotification.with(this)
                .load()
                .title(R.string.notif_title)
                .message(message)
                .bigTextStyle(R.string.notif_long_message)
                .smallIcon(R.drawable.ic_drop)
                .flags(Notification.DEFAULT_ALL)
                .autoCancel(true)
                .click(ActivityMain.class)
                .simple()
                .build();
    }

    private boolean isNotificationNeeded(long added, long when){
        long now = System.currentTimeMillis();

        // Don't fire a notification if the drop is old.
        if(now > when){
            return false;
        } else {
            // Fire a notification if the 90% of the time has already passed.
            long difference90 = (long)(0.9 * (when - added));

            return now > (added+difference90);
        }
    }
}
