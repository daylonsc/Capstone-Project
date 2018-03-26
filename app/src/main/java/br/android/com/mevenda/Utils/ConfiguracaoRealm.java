package br.android.com.mevenda.Utils;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by daylo on 17/03/2017.
 */

public class ConfiguracaoRealm extends Application {

    @Override
    public void onCreate(){
        super.onCreate();
        Realm.init(getApplicationContext());

        RealmConfiguration config = new RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(config);
    }
}
