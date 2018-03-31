package br.android.com.mevenda.services;


import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import br.android.com.mevenda.PedidoWidgetProvider;
import br.android.com.mevenda.R;

public class PedidoWidgetService extends IntentService {

    public PedidoWidgetService() {
        super("PedidoWidgetService");
    }

    public static void startActionUpdatePedidoWidgets(Context context) {
        Intent intent = new Intent(context, PedidoWidgetService.class);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        handleActionUpdatePedidoWidgets();
    }

    private void handleActionUpdatePedidoWidgets() {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, PedidoWidgetProvider.class));
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_grid_view);
        PedidoWidgetProvider.updatePedidoWidgets(this, appWidgetManager, appWidgetIds);
    }
}
