package com.francony.romain.outerspacemanager.helpers;


import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.accessibility.AccessibilityManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.francony.romain.outerspacemanager.R;

import org.json.JSONObject;

import java.lang.reflect.Field;
import java.text.DecimalFormat;

import retrofit2.Response;

public abstract class Helpers {

    /**
     * Fix bug on some device where an accessibility setting is enabled and remove Snackbar default animation
     * @param snackbar
     */
    public static void showSnackbarWithAnimation(Snackbar snackbar) {
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

    /**
     * Transform second to a more readable text (ex: 2h 25m 30s)
     * @param second
     * @return
     */
    public static String secondToHumanReadableTime(int second) {
        int h = second/ 3600;
        int m = (second % 3600) / 60;
        int s = second % 60;
        String sh = (h > 0 ? String.valueOf(h) + "h" : "");
        String sm = (m < 10 && m > 0 && h > 0 ? "0" : "") + (m > 0 ? (h > 0 && s == 0 ? String.valueOf(m) : String.valueOf(m) + "m") : "");
        String ss = (s == 0 && (h > 0 || m > 0) ? "" : (s < 10 && (h > 0 || m > 0) ? "0" : "") + String.valueOf(s) + "s");
        return sh + (h > 0 ? " " : "") + sm + (m > 0 ? " " : "") + ss;
    }

    /**
     * Load image with Glide
     * @param view
     * @param url
     */
    public static void loadExternalImageWithAnimation(ImageView view, String url){
        Context context = view.getContext();
        Glide.with(context).load(url)
                .apply(new RequestOptions()
                        .placeholder(new ColorDrawable(ContextCompat.getColor(context, R.color.colorGrayTransparent))).centerCrop())
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(view);
    }


    /**
     * Get error message from JSON string response
     * @param response
     * @return
     */
    public static String getResponseErrorMessage(Response response){
        try {
            JSONObject jObjError = new JSONObject(response.errorBody().string());
            return jObjError.getString("message");
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    /**
     * Transform number to a more readable text (ex: 1 520 320,50)
     * @param number
     * @return
     */
    public static String numberToHumanReadableNumber(float number) {
        return new DecimalFormat("##,###.##").format(number);
    }
}
