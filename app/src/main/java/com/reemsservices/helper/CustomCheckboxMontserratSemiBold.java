package com.reemsservices.helper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.CheckBox;

import androidx.annotation.RequiresApi;

/**
 * Created by shubham on 2/8/17.
 */

@SuppressLint("AppCompatCustomView")
public class CustomCheckboxMontserratSemiBold extends CheckBox {

    public CustomCheckboxMontserratSemiBold(Context context) {
        super(context);
        applyCustomFont(context);

    }

    public CustomCheckboxMontserratSemiBold(Context context, AttributeSet attrs) {
        super(context, attrs); applyCustomFont(context);
    }

    public CustomCheckboxMontserratSemiBold(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        applyCustomFont(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CustomCheckboxMontserratSemiBold(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        applyCustomFont(context);
    }

    private void applyCustomFont(Context context) {
        Typeface customFont = FontCache.getTypeface("font/Montserrat-SemiBold.ttf", context);
        setTypeface(customFont);

    }
}
