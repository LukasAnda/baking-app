package com.lukasanda.bakingapp;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by lukas on 2/27/2018.
 */

public class MyApplication extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(realmConfiguration);
    }
}
