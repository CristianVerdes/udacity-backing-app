package com.example.cristianverdes.bakingapp.widgets;

import android.content.Intent;
import android.widget.RemoteViewsService;

/**
 * Created by cristian.verdes on 20.03.2018.
 */

public class IngredientsWidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new IngredientsRemoteViewFactory(this.getApplicationContext(), Integer.valueOf(intent.getData().getSchemeSpecificPart()));
    }
}
