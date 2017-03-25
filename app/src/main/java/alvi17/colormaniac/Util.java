package alvi17.colormaniac;

import android.content.Context;
import android.preference.PreferenceManager;
import android.util.Log;

/**
 * Created by User on 3/25/2017.
 */

public class Util {
    static String TAG="Util";
    public static void saveInfo(Context context, String key, String value )
    {
        try
        {
            PreferenceManager.getDefaultSharedPreferences(context).edit().putString(key, value).apply();
        }
        catch (Exception ex)
        {
            Log.e(TAG," saveInfo "+ex+"");
        }
    }
    public static String getInfo(Context context, String key)
    {
        try {
            return PreferenceManager.getDefaultSharedPreferences(context).getString(key,"");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return "";
    }

}
