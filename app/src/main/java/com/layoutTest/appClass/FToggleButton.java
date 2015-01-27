package com.layoutTest.appClass;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

import info.hoang8f.widget.FButton;

/**
 * Created by leo on 14/1/15.
 */
public class FToggleButton extends FButton
{
    boolean m_IsChecked = true;

    SingleSelectSet singleSelectSet = null;

    public FToggleButton(Context context) {
        super(context);
    }

    public FToggleButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FToggleButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (motionEvent.getAction()) {
//                case MotionEvent.ACTION_DOWN:
//                    setDrawable(pressedDrawable);
//                    this.setPadding(mPaddingLeft, mPaddingTop + mShadowHeight, mPaddingRight, mPaddingBottom);
//                    break;
//                case MotionEvent.ACTION_MOVE:
//                    Rect r = new Rect();
//                    view.getLocalVisibleRect(r);
//                    if (!r.contains((int) motionEvent.getX(), (int) motionEvent.getY() + 3 * mShadowHeight) &&
//                            !r.contains((int) motionEvent.getX(), (int) motionEvent.getY() - 3 * mShadowHeight)) {
//                        setDrawable(unpressedDrawable);
//                        this.setPadding(mPaddingLeft, mPaddingTop + mShadowHeight, mPaddingRight, mPaddingBottom + mShadowHeight);
//                    }
//                    break;
            case MotionEvent.ACTION_OUTSIDE:
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                m_IsChecked = !m_IsChecked;
                if(m_IsChecked) {
                    setReleasedDrawable();
                }
                else
                {
                    setPressedDrawable();

                    if(singleSelectSet != null)
                    {
                        for(FToggleButton b : singleSelectSet.list)
                        {
                            if(b != this)
                                b.setReleasedDrawable();
                        }
                    }
                }
                break;
        }
        return false;
    }

    public void setPressedDrawable()
    {
        m_IsChecked = false;
        setDrawable(pressedDrawable);
        this.setPadding(mPaddingLeft, mPaddingTop + mShadowHeight, mPaddingRight, mPaddingBottom);
    }

    public void setReleasedDrawable()
    {
        m_IsChecked = true;
        setDrawable(unpressedDrawable);
        this.setPadding(mPaddingLeft, mPaddingTop + mShadowHeight, mPaddingRight, mPaddingBottom + mShadowHeight);
    }

    public void setDrawable(Drawable background)
    {
        if (Build.VERSION.SDK_INT >= 16) {
            setBackground(background);
        } else {
            setBackgroundDrawable(background);
        }
    }


    public static class SingleSelectSet
    {
        ArrayList<FToggleButton> list = new ArrayList<FToggleButton>();

        public void add(FToggleButton but)
        {
            but.singleSelectSet = this;
            list.add(but);
        }

        public FToggleButton getCurrentSelected()
        {
            for(FToggleButton b : list)
            {
                if(!b.m_IsChecked)
                   return  b;
            }

            return null;
        }
    }


}
