/*
 * Copyright (C) 2017 Eric Afenyo
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.eric.bakingrecipes;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.example.eric.bakingrecipes.Activities.ShoppingListActivity;
import com.example.eric.bakingrecipes.Utils.L;

/**
 * Implementation of App Widget functionality.
 */
public class RecipesAppWidget extends AppWidgetProvider {


    /**
     * implement intents that launch the ShoppingListActivity and
     * connect the Adapter to a RemoteViewsService
     * @param context
     * @param appWidgetManager
     * @param appWidgetId the ids for the laughed widgets
     */
    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        // Create an Intent to launch the Activity
        Intent intent = new Intent(context, ShoppingListActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.recipes_app_widget);
        views.setOnClickPendingIntent(R.id.text_view_appwidget,pendingIntent);

        //Create an Intent that point to the RemoteViewsService class
        Intent rmsIntent = new Intent(context,ShoppingListRemoteViewsService.class);
        // Set up the RemoteViews object to use a RemoteViews adapter.
        // This adapter connects to a RemoteViewsService through the specified intent.
        views.setRemoteAdapter(R.id.list_view_appwidget,rmsIntent);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);

    }

    /**
     * send a Broadcast to WidgetProvider.
     * @param context context
     */
    public static void sendRefreshBroadcast(Context context) {
        L.toast(context,"sendRefreshBroadcast");
        Intent intent = new Intent(context, RecipesAppWidget.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        intent.setComponent(new ComponentName(context, RecipesAppWidget.class));
        context.sendBroadcast(intent);
}
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    public static void updateListWidgets(Context context, AppWidgetManager appWidgetManager,
                                           int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
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

    @Override
    public void onReceive(Context context, Intent intent) {
        final String action = intent.getAction();
        if (action.equals(AppWidgetManager.ACTION_APPWIDGET_UPDATE)) {
            L.toast(context,"onReceive");

            // refresh all your widgets
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            ComponentName thisWidget = new ComponentName(context, RecipesAppWidget.class);
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
            //Now update all widgets
            updateListWidgets(context,appWidgetManager,appWidgetIds);
//            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.list_view_appwidget);
        }

        super.onReceive(context, intent);
    }
}

