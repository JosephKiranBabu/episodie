package pl.hypeapp.episoder.util;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.view.View;

public class StartActivityUtil {
    private static StartActivityUtil ourInstance = new StartActivityUtil();

    private static Activity context;

    public static StartActivityUtil getInstance(Activity activity) {
        context = activity;
        return ourInstance;
    }

    private StartActivityUtil() {
    }

    public void runActivityWithTransition(Class startActivityClass, Pair<View, String>... sharedElements) {
        ActivityOptionsCompat transitionActivityOptions = null;
        if (BuildUtils.isMinApi21()) {
            transitionActivityOptions = ActivityOptionsCompat.
                    makeSceneTransitionAnimation(context, sharedElements);
        }
        if (transitionActivityOptions != null) {
            context.startActivity(new Intent(context, startActivityClass),
                    transitionActivityOptions.toBundle());
        }
    }
}