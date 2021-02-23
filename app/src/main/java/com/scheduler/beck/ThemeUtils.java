package com.scheduler.beck;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

public class ThemeUtils
{
    public static final int THEME_5 = 4;
    public static final int THEME_6 = 5;
    public static final int THEME_4 = 3;
    public static final int THEME_7 = 7;
    private static int sTheme;
    public final static int THEME_DEFAULT = 0;
    public final static int THEME_WHITE = 1;
    public final static int THEME_BLUE = 2;
    private static SharedPreferences.Editor editor;
    private static SharedPreferences sharedPref;
    private static boolean isStart = true;

    /**
     * Set the theme of the Activity, and restart it by creating a new Activity of the same type.
     */
    public static void changeToTheme(Activity activity, int theme)
    {
        sTheme = theme;
        activity.finish();
        activity.startActivity(new Intent(activity, activity.getClass()));
        saveTheme(sTheme, activity);
    }

    private static void saveTheme(int sTheme, Activity activity) {
        sharedPref = activity.getSharedPreferences("prefs", Context.MODE_PRIVATE);
        editor = sharedPref.edit();
        editor.putInt("theme", sTheme);
        editor.apply();
    }

    /** Set the theme of the activity, according to the configuration. */
    public static void onActivityCreateSetTheme(Activity activity)
    {
        if(isStart){
            sTheme = getTheme(activity);
            isStart = false;
        }
        switch (sTheme)
        {
            default:
            case THEME_DEFAULT:
                activity.setTheme(R.style.FirstTheme);
                break;
            case THEME_WHITE:
                activity.setTheme(R.style.SecondTheme);
                break;
            case THEME_BLUE:
                activity.setTheme(R.style.Thirdheme);
                break;
            case THEME_4:
                activity.setTheme(R.style.FourthTheme);
                break;
            case THEME_5:
                activity.setTheme(R.style.FifthTheme);
                break;
            case THEME_6:
                activity.setTheme(R.style.Sixthheme);
                break;
            case THEME_7:
                activity.setTheme(R.style.Seventhheme);
                break;
        }
    }

    private static int getTheme(Activity activity) {
        sharedPref = activity.getSharedPreferences("prefs", Context.MODE_PRIVATE);
        return sharedPref.getInt("theme",0 );
    }
}
