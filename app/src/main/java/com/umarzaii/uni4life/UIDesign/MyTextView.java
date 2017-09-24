package com.umarzaii.uni4life.UIDesign;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

public class MyTextView extends android.support.v7.widget.AppCompatTextView {

    GradientDrawable gradientDrawable;

    public MyTextView(Context context) {
        super(context);
        setBackground();
        this.setBackgroundDrawable(gradientDrawable);
    }

    public MyTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setBackground();
        this.setBackgroundDrawable(gradientDrawable);
    }

    public MyTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setBackground();
        this.setBackgroundDrawable(gradientDrawable);
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    protected void setBackground() {
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setColor(0xFF00FF00);
        gradientDrawable.setCornerRadius(5);
        gradientDrawable.setStroke(1, 0xFF000000);
        this.gradientDrawable = gradientDrawable;
    }

}
