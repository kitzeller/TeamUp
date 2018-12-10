package wpi.jtkaplan.teamup;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import wpi.jtkaplan.teamup.model.Class;
import wpi.jtkaplan.teamup.model.Group;
import wpi.jtkaplan.teamup.model.Member;
import wpi.jtkaplan.teamup.model.Skills;
import wpi.jtkaplan.teamup.model.User;

/**
 * Created by mac on 12/8/18.
 */

public class UserPreferences {

    private static Member member;
    private static Skills skillsObj;
    private static Class selectedClass;
    private static User currentUser;
    private static Group currentGroup;


    private static SharedPreferences mSharedPref;
    public static final String UID_VALUE = "wpi.user.uuid";
    public static final String LOC_VALUE = "wpi.user.loc";
    public static final String PROFESSOR = "Professors";
    public static final String STUDENT = "Students";

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

    public static Member getMember() {
        return member;
    }

    public static void setMember(Member member) {
        UserPreferences.member = member;
    }

    public static Skills getSkillsObj() {
        return skillsObj;
    }

    public static void setSkillsObj(Skills skillsObj) {
        UserPreferences.skillsObj = skillsObj;
    }

    public static Class getSelectedClass() {
        return selectedClass;
    }

    public static void setSelectedClass(Class selectedClass) {
        UserPreferences.selectedClass = selectedClass;
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(User currentUser) {
        UserPreferences.currentUser = currentUser;
    }

    public static Group getCurrentGroup() {
        return currentGroup;
    }

    public static void setCurrentGroup(Group currentGroup) {
        UserPreferences.currentGroup = currentGroup;
    }
}
