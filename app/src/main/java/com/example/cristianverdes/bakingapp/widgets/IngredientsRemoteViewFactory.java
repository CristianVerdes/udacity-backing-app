package com.example.cristianverdes.bakingapp.widgets;

import android.content.Context;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

/**
 * Created by cristian.verdes on 20.03.2018.
 */

class IngredientsRemoteViewFactor implements RemoteViewsService.RemoteViewsFactory {
    Context context;

    public IngredientsRemoteViewFactor(Context context) {
        this.context = context;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public RemoteViews getViewAt(int position) {
        return null;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 0;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}
