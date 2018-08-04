package com.github.neone35.enalyzer.scan;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.github.neone35.enalyzer.R;

public class ScanShortcutWidgetProvider extends AppWidgetProvider {
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);

        for (int scanShorcutWidgetId : appWidgetIds) {
            // Create an Intent to launch ScanActivity
            Intent intent = new Intent(context, ScanActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
            // Get the layout for the App Widget and attach an on-click listener to the button
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_scan_shortcut_layout);
            views.setOnClickPendingIntent(R.id.ib_scan_shortcut_widget_button, pendingIntent);
            // Tell the AppWidgetManager to perform an update on the current app widget
            appWidgetManager.updateAppWidget(scanShorcutWidgetId, views);
        }
    }
}
