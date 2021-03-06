package com.baseproject.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import com.baseproject.R;

/**
 * 依据高或者宽设置正方形的LinearLayout。默认依据宽
 * Created by yuanxiang on 2019/1/8.
 */
public class SquareLinearLayout extends LinearLayout {
    private boolean basis = true;

    public SquareLinearLayout(Context context) {
        super(context);
    }

    public SquareLinearLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initTypedArray(context, attrs);
    }

    public SquareLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initTypedArray(context, attrs);
    }

    private void initTypedArray(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SquareLinearLayout);
        basis = typedArray.getBoolean(R.styleable.SquareLinearLayout_basis_linearlayout, true);
        typedArray.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(getDefaultSize(0, widthMeasureSpec), getDefaultSize(0, heightMeasureSpec));

        if (basis) {
            int childWidthSize = getMeasuredWidth();
            //高度和宽度一样
            heightMeasureSpec = widthMeasureSpec = MeasureSpec.makeMeasureSpec(childWidthSize, MeasureSpec.EXACTLY);
        } else {
            int childHeightSize = getMeasuredHeight();
            widthMeasureSpec = heightMeasureSpec = MeasureSpec.makeMeasureSpec(childHeightSize, MeasureSpec.EXACTLY);
        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void setBasis(boolean basis) {
        this.basis = basis;
    }
}