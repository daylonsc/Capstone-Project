package br.android.com.mevenda.Utils;

import android.app.Application;
import com.firebase.client.Firebase;

/**
 * Created by daylo on 26/03/2018.
 */

public class CustomApplication extends Application{

    @Override
    public void onCreate(){
        super.onCreate();
        Firebase.setAndroidContext(this);
    }


}
