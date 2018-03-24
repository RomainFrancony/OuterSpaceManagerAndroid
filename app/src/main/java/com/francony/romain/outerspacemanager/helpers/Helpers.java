package com.francony.romain.outerspacemanager.helpers;


import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.accessibility.AccessibilityManager;

import java.lang.reflect.Field;

public abstract class Helpers {

    /**
     * Show snackbar with animation
     * @param snackbar
     */
    public static void showSnackbarWithAnimation(Snackbar snackbar){
        try {
            Field mAccessibilityManagerField = BaseTransientBottomBar.class.getDeclaredField("mAccessibilityManager");
            mAccessibilityManagerField.setAccessible(true);
            AccessibilityManager accessibilityManager = (AccessibilityManager) mAccessibilityManagerField.get(snackbar);
            Field mIsEnabledField = AccessibilityManager.class.getDeclaredField("mIsEnabled");
            mIsEnabledField.setAccessible(true);
            mIsEnabledField.setBoolean(accessibilityManager, false);
            mAccessibilityManagerField.set(snackbar, accessibilityManager);
            snackbar.show();
        } catch (Exception e) {
            Log.d("Snackbar", "Reflection error: " + e.toString());
        }
    }

}
