package com.softdesign.devintensive.ui.view;

/** Overrides fonts in TextView to Roboto Regular, compatible with API 15 */
import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class TextView_Roboto extends TextView {
    public TextView_Roboto(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        createFont();
    }

    public TextView_Roboto(Context context, AttributeSet attrs) {
        super(context, attrs);
        createFont();
    }

    public TextView_Roboto(Context context) {
        super(context);
        createFont();
    }

    public void createFont() {
        Typeface font = Typeface.createFromAsset(getContext().getAssets(), "fonts/roboto_condensed.ttf");
        setTypeface(font);
    }
}
