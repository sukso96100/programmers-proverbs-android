package com.youngbin.programmers.proverbs;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;


/**
 * Implementation of App Widget functionality.
 */
public class ProverbsWidget extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        final int N = appWidgetIds.length;
        for (int i = 0; i < N; i++) {
            updateAppWidget(context, appWidgetManager, appWidgetIds[i]);
        }
    }


    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.proverbs_widget);
        SharedPreferences SP = context.getSharedPreferences("pref", context.MODE_PRIVATE);
        String Proverb = SP.getString("proverb", context.getResources().getString(R.string.placeholder_proverb));
        views.setTextViewText(R.id.appwidget_text, Proverb);



        PendingIntent PI = PendingIntent.getActivity(context, 0, new Intent(context, MainActivity.class), 0);
        views.setOnClickPendingIntent(R.id.appwidget_text, PI);
        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onReceive(Context context, Intent intent) {

// Protect against rogue update broadcasts (not really a security issue,

// just filter bad broacasts out so subclasses are less likely to crash).
        String action = intent.getAction();
        if (AppWidgetManager.ACTION_APPWIDGET_UPDATE == action) {
            Log.d("ProverbWidget", "Received APPWIDGET_UPDATE Action");

            SharedPreferences SP = context.getSharedPreferences("pref", context.MODE_PRIVATE);
            String Proverb = SP.getString("proverb", context.getResources().getString(R.string.placeholder_proverb));
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.proverbs_widget);
            ComponentName thisWidget = new ComponentName(context, ProverbsWidget.class);
            remoteViews.setTextViewText(R.id.appwidget_text, Proverb);
            PendingIntent PI = PendingIntent.getActivity(context,0,new Intent(context,MainActivity.class),0);
            remoteViews.setOnClickPendingIntent(R.id.appwidget_text, PI);
            appWidgetManager.updateAppWidget(thisWidget, remoteViews);


//            Bundle extras = intent.getExtras();
//            if (extras != null) {
//                int[] appWidgetIds = extras.getIntArray(AppWidgetManager.EXTRA_APPWIDGET_IDS);
//                if (appWidgetIds != null && appWidgetIds.length > 0) {
//                    this.onUpdate(context, AppWidgetManager.getInstance(context), appWidgetIds);
//                }
//            }
        }
    }

}


