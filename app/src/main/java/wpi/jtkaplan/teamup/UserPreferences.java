package wpi.jtkaplan.teamup;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by mac on 12/8/18.
 */

public class UserPreferences {

    private static SharedPreferences mSharedPref;
    public static final String UID_VALUE = "wpi.user.uuid";
    public static final String LOC_VALUE = "wpi.user.loc";

    private UserPreferences() {

    }

    public static void init(Context context) {
        if (mSharedPref == null) {
            mSharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        }
    }

    public static String read(String key, String defValue) {
        return mSharedPref.getString(key, defValue);
    }

    public static void write(String key, String value) {
        SharedPreferences.Editor prefsEditor = mSharedPref.edit();
        prefsEditor.putString(key, value);
        prefsEditor.commit();
    }


}
