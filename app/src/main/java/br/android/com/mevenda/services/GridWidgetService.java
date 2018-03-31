package br.android.com.mevenda.services;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import br.android.com.mevenda.Utils.Constantes;
import br.android.com.mevenda.Utils.LibraryClass;
import br.android.com.mevenda.bean.Pedido;

import br.android.com.mevenda.R;


public class GridWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new GridRemoteViewsFactory(this.getApplicationContext());
    }
}

class GridRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    Context mContext;
    List<Pedido> mPedido;
    DatabaseReference firebase;
    ValueEventListener valueEventListener;
    boolean getPedidos;

    public GridRemoteViewsFactory(Context applicationContext) {
        mContext = applicationContext;
        mPedido = new ArrayList<Pedido>();

        firebase = LibraryClass.getFirebase().child(Constantes.PARAMETER_FIREBASE_PEDIDO);
        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                getPedidos = true;
                mPedido.clear();

                for (DataSnapshot dados : dataSnapshot.getChildren()) {
                    Pedido pedido = dados.getValue(Pedido.class);
                    mPedido.add(pedido);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
    }

    @Override
    public void onCreate() {
        getPedidos = false;
        firebase.addValueEventListener(valueEventListener);
    }

    @Override
    public void onDataSetChanged() {
        while (!getPedidos) {
            Log.d("Pedido", "Processando Pedido");
        }
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        if (mPedido == null) return 0;
        return mPedido.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        if (mPedido == null || mPedido.size() == 0) return null;
        Pedido pedido = mPedido.get(position);

        RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.pedido_widget);
        views.setImageViewResource(R.id.widget_pedido_image, R.mipmap.cliente);
        views.setTextViewText(R.id.widget_pedido_name, pedido.getCliente().getNome());
        Bundle extras = new Bundle();
        extras.putParcelable(Constantes.PARAMETER_WIDGET_PEDIDO, pedido);
        Intent fillInIntent = new Intent();
        fillInIntent.putExtras(extras);
        views.setOnClickFillInIntent(R.id.widget_pedido_image, fillInIntent);

        return views;

    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1; // Treat all items in the GridView the same
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}

