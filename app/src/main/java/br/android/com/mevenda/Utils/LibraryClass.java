package br.android.com.mevenda.Utils;

import com.firebase.client.Firebase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by daylo on 26/03/2018.
 */

public class LibraryClass {
    private static DatabaseReference refenciaFirebase;

    public static DatabaseReference getFirebase() {
        if (refenciaFirebase == null) {
            refenciaFirebase = FirebaseDatabase.getInstance().getReference();
        }
        return refenciaFirebase;
    }
}
