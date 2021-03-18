package com.reemsservices.helper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

/**
 * Created by shubham on 2/8/17.
 */

@SuppressLint("AppCompatCustomView")
public class CustomEditTextMontserratRegular extends EditText {

    public CustomEditTextMontserratRegular(Context context) {
        super(context);
        applyCustomFont(context);

    }

    public CustomEditTextMontserratRegular(Context context, AttributeSet attrs) {
        super(context, attrs); applyCustomFont(context);
    }

    public CustomEditTextMontserratRegular(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        applyCustomFont(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CustomEditTextMontserratRegular(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        applyCustomFont(context);
    }

    private void applyCustomFont(Context context) {
        Typeface customFont = FontCache.getTypeface("font/Montserrat-Regular.ttf", context);
        setTypeface(customFont);

    }
}
