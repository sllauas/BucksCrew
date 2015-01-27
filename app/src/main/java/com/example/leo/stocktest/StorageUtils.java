package com.example.leo.stocktest;


import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;


/**
 * Created by leo on 3/1/15.
 */
public class StorageUtils
{
    //Thx to java, all object which can be serialized can be represent by a string which contains letter only (a~p)
    //consequence: ->2++ x (e.g. 5x)<- times memory
    public static <T>  byte[] objectToByteArray(T obj)
    {
        Log.d("StorgaeUt", "start");
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream out = null;
        byte[] yourBytes = null;
        try {
            out = new ObjectOutputStream(bos);
            out.writeObject(obj);
            yourBytes = bos.toByteArray();
        }
        catch (IOException ex)
        {
            Log.e("IOException", ex.getMessage());
            return  null;
        }
        finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException ex) {
                // ignore close exception
            }

            try {
                bos.close();
            } catch (IOException ex) {
                // ignore close exception
            }
        }

        Log.d("StorgaeUt", yourBytes.toString());

        return yourBytes;
    }

    public static String byteArrayToLetterString(byte[] bytes)
    {
        if(bytes == null) return  null;
        //8 bit-> 2 * 8bit, double space required
        byte[] asciiString = new byte[bytes.length * 2];
//        asciiString[bytes.length * 2] = 0;
//        asciiString[bytes.length * 2 + 1] = 0;

        int tmpInt;
        byte upByte;
        byte boByte;
        int i = 0;
        String resultString;

        for(byte b : bytes)
        {
            tmpInt = b;
            upByte = (byte)(((tmpInt & 0xF0)>> 4) + 0x61); // a = 0x61
            boByte = (byte)((tmpInt & 0x0F)  + 0x61);

            asciiString[i++] = upByte;
            asciiString[i++] = boByte;

        }

        try
        {
//            resultString = new String(asciiString, "US-ASCII");

            resultString = asciiToDefaultEncodingString(asciiString);

//            resultString += "\0";
        }
        catch (Exception e)
        {
            return null;
        }

        return  resultString;
    }

    private static String asciiToDefaultEncodingString(byte[] asciiByte)
    {
        StringBuilder sb = new StringBuilder(asciiByte.length);
        for (int i = 0; i < asciiByte.length; ++ i) {
            if (asciiByte[i] < 0) throw new IllegalArgumentException();
            sb.append((char) asciiByte[i]);
        }
        return sb.toString();
    }


    public static byte[] letterStringToByteArray(String s)
    {
//        if((s.length() % 2) == 1) return null;

        Log.d("StorgaeUt s->b", s);

        byte[] stringBytes;

        byte[] resultBytes = new byte[s.length() / 2];

        int i = 0;
        int j = 0;
        int tmpInt = 0;

        byte b;


//        try
//        {
//            stringBytes = s.getBytes("US-ASCII");
//        }
//        catch (UnsupportedEncodingException e)
//        {
//            return null;
//        }

//
//        for(byte b : stringBytes)
        for(int loopI = 0; loopI < s.length(); loopI++)
        {
            b = (byte) s.charAt(loopI);

            if(i == 0)
            {
                tmpInt = (((int)b) - 0x61) << 4;
                i = 1;
            }
            else
            {
                tmpInt |= ((int)b) - 0x61;
                resultBytes[j++] = (byte) tmpInt;
                i = 0;
            }
        }

        Log.d("StorgaeUt", resultBytes.toString());


        return resultBytes;

    }

    public static <T> T byteArrayToObj(byte[] bytes)
    {
        if(bytes == null) return  null;
        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);

        ObjectInputStream in = null;
        T result;

        try {
            in = new ObjectInputStream(bis);
            Object o = in.readObject();
            result = (T)o;
            return result;
        }
        catch (Exception e)
        {
            return null;
        }
        finally {
            try {
                bis.close();
            } catch (IOException ex) {
                // ignore close exception
            }
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                // ignore close exception
            }
        }
    }

    public static <T> String objectToLetterString(T obj)
    {
        return byteArrayToLetterString(objectToByteArray(obj));
    }

    public  static <T> T letterStringToObj(String s)
    {
        return byteArrayToObj(letterStringToByteArray(s));
    }





}
