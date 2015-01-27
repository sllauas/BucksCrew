package com.example.leo.stocktest;

import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by leo on 3/1/15.
 */
public class PlainTextPost implements BasicPost.PostContent
{
    String plainContentText;

    public void displayInView(ViewGroup viewgroup, BasicPost basicPost)
    {
        TextView tv = new TextView(viewgroup.getContext());
        tv.setText(plainContentText);
        viewgroup.addView(tv);
    }

    public String storeUsingString()
    {
        return StorageUtils.objectToLetterString(plainContentText);
    }

    public void setPostUsingString(String s)
    {
        plainContentText = StorageUtils.<String>letterStringToObj(s);
    }

    public class PlainTextPostFactory implements BasicPost.PostContentFactory
    {
        public BasicPost.PostContent createPostContent()
        {
            return new PlainTextPost();
        }
    }

}
