package com.looigi.cambiolacarta.Receivers;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.looigi.cambiolacarta.R;

public class ProviderPhoto extends AppWidgetProvider {
	//  @Override
	// public void onUpdate(Context context, AppWidgetManager appWidgetManager,
	// 		int[] appWidgetIds) {
	// 	 RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget);
	// 	 remoteViews.setOnClickPendingIntent(R.id.imgScatta, buildButtonPendingIntent(context));
//
	// 	 pushWidgetUpdate(context, remoteViews);
	//  }
//
	// public static void pushWidgetUpdate(Context context, RemoteViews remoteViews) {
	// 	ComponentName myWidget = new ComponentName(context, ProviderPhoto.class);
	// 	AppWidgetManager manager = AppWidgetManager.getInstance(context);
	// 	manager.updateAppWidget(myWidget, remoteViews);
	// }
//
	// public static PendingIntent buildButtonPendingIntent(Context context) {
	// 	Intent intent = new Intent();
	// 	intent.setAction("MAKE_CLICK");
//
	// 	return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
	// }

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
						 int[] appWidgetIds) {
		RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget);
		Intent configIntent = new Intent(context, com.looigi.cambiolacarta.Receivers.Photo.class);

		PendingIntent configPendingIntent = PendingIntent.getActivity(context, 0, configIntent, 0);

		remoteViews.setOnClickPendingIntent(R.id.imgScatta, configPendingIntent);
		appWidgetManager.updateAppWidget(appWidgetIds, remoteViews);
	}

}
