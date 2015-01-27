package com.example.leo.stocktest;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by leo on 28/12/14.
 */
public class BasicPost
{
    //Post only, no content included

    public interface PostContent
    {
        public void displayInView(ViewGroup viewgroup, BasicPost basicPost);
        public String storeUsingString();
        public void setPostUsingString(String s);
    }

    public interface PostContentFactory
    {
        public PostContent createPostContent();
    }

//    public interface

    private long postID; //for demo, this is totally enough
    // i don't think this id is required for apps...this should be on the server

    private long senderID; // who send this

    PostContent content;

    private int contentType;

    static ArrayList<BasicPost> allPostList = new ArrayList<BasicPost>();

    static HashMap<Integer, PostContentFactory> handlerMap = new HashMap<Integer, PostContentFactory>();


    public BasicPost(long senderId)
    {
        senderID = senderId;
    }

    public static void registerFactories()
    {

    }

}
