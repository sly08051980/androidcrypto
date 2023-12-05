package com.example.crypto;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.view.View;
import android.widget.ListView;
import android.widget.RemoteViews;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of App Widget functionality.
 */
public class CryptoWidget extends AppWidgetProvider {

    private ListView listView;
    private DBHelper dbHelper;

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        CharSequence widgetText = context.getString(R.string.appwidget_text);
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.crypto_widget);


        Intent intent = new Intent(context, CryptoWidget.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        views.setOnClickPendingIntent(R.id.widget_layout, pendingIntent);



        appWidgetManager.updateAppWidget(appWidgetId, views);
    }


    @Override

    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {


        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.crypto_widget);



        BroadcastReceiver wifiStateReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                if (wifiManager.isWifiEnabled()) {
                    views.setTextViewText(R.id.widget_textview, "WiFi activé");
                    views.setViewVisibility(R.id.actualisé, View.GONE);
                } else {
                    views.setTextViewText(R.id.widget_textview, "WiFi désactivé");
                    views.setViewVisibility(R.id.actualisé, View.VISIBLE);
                }
                ComponentName thisWidget = new ComponentName(context, CryptoWidget.class);
                AppWidgetManager manager = AppWidgetManager.getInstance(context);
                manager.updateAppWidget(thisWidget, views);
            }
        };


        IntentFilter filter = new IntentFilter();
        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        context.getApplicationContext().registerReceiver(wifiStateReceiver, filter);


        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        views.setOnClickPendingIntent(R.id.widget_layout, pendingIntent);




        List<GridViewItem> items = new ArrayList<>();




        GridViewAdapter adapter = new GridViewAdapter(context, items);


        views.setRemoteAdapter(R.id.grid_view, new Intent(context, GridViewService.class));
        views.setEmptyView(R.id.grid_view, R.id.empty_view);



        appWidgetManager.updateAppWidget(appWidgetIds, views);
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

}