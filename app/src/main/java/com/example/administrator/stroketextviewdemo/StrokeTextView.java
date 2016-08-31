package com.example.administrator.stroketextviewdemo;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.widget.TextView;

import java.lang.reflect.Field;

/**
 * Created by Administrator on 2016-08-31.
 */
public class StrokeTextView extends TextView {

    TextPaint mTextPaint;
    int mInnerColor;
    int mOuterColor;

    public StrokeTextView(Context context, int outerColor, int mInnerColor) {
        super(context);
        mTextPaint = this.getPaint();
        this.mInnerColor = mInnerColor;
        this.mOuterColor = outerColor;
    }


    public StrokeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

        mTextPaint = this.getPaint();
        //获取自定义的xml属性名称
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.StrokeTextView);
        //获取对应的属性值
        this.mInnerColor = a.getColor(R.styleable.StrokeTextView_innnerColor, 0xffffff);
        this.mOuterColor = a.getColor(R.styleable.StrokeTextView_outerColor, 0xffffff);
    }

    public StrokeTextView(Context context, AttributeSet attrs, int defStyleAttr,int outerColor,int innnerColor) {
        super(context, attrs, defStyleAttr);
        mTextPaint = this.getPaint();
        this.mInnerColor = innnerColor;
        this.mOuterColor = outerColor;
    }

    private boolean m_bDrawSideLine = true;//默认采用描边


    @Override
    protected void onDraw(Canvas canvas) {
        if (m_bDrawSideLine) {
            //描外层
            // super.setTextColor(Color.BLUE); // 不能直接这么设，如此会导致递归
            setTextColorUseReflection(mOuterColor);
            mTextPaint.setStrokeWidth(5);//描边宽度
            mTextPaint.setStyle(Paint.Style.FILL_AND_STROKE);//描边种类
            mTextPaint.setFakeBoldText(true);//外层text采用粗体
            mTextPaint.setShadowLayer(1, 0, 0, 0);//字体的阴影效果
            super.onDraw(canvas);

            //描内层，恢复原先的画笔
            // super.setTextColor(Color.BLUE); // 不能直接这么设，如此会导致递归
            setTextColorUseReflection(mInnerColor);
            mTextPaint.setStrokeWidth(0);
            mTextPaint.setStyle(Paint.Style.FILL_AND_STROKE);
            mTextPaint.setFakeBoldText(false);
            mTextPaint.setShadowLayer(0, 0, 0, 0);
        }
        super.onDraw(canvas);
    }

    /**
     * 使用反射的方法进行字体颜色的设置
     *
     * @param color
     */
    private void setTextColorUseReflection(int color) {
        Field textColorField;

        try {
            textColorField = TextView.class.getDeclaredField("mCurTextColor");
            textColorField.setAccessible(true);
            textColorField.set(this,color);
            textColorField.setAccessible(false);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        mTextPaint.setColor(color);
    }
}
