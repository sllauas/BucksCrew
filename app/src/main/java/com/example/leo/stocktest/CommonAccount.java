package com.example.leo.stocktest;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.util.SparseArray;
import android.util.SparseIntArray;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by leo on 28/12/14.
 */
public class CommonAccount implements java.io.Serializable
{
    //private
    static HashMap<Long,CommonAccount> allAccList = null; // for demo only
    static CommonAccount currentAccount = null;

    static boolean hasAccessCopyFromAfterLogin = false;

    long u_id;
    String password;
    String accountEmailAddress;
    String privateName; // shown to you
    String publicName; // shown to public (nick Name)

    double amountTotalMoney;


    //follower/copier feature
    ArrayList<Long> subscribersID;
    ArrayList<Long> copyFromList;

    public void discoveryFeed()
    {
        //should return a feed list
    }

    public boolean subscribe(long id)
    {
        CommonAccount target =  allAccList.get(new Long(id));

        if(target == null) return false;

        target.followersID.add(u_id);
        subscribersID.add(id);

        return true;
    }

    public double getCurrentCash()
    {
        return  amountTotalMoney;
    }

    public boolean copyFrom(long id)
    {
        CommonAccount target =  allAccList.get(id);

        if(target == null) return false;

        target.copyToList.add(u_id);
        copyFromList.add(id);

        return true;
    }


    //Trader feature
    ArrayList<Long> followersID; // myFollowers
    ArrayList<Long> copyToList;

    //trade record (ArrayList<StockExchangeRecord>)
    //currentStockInventory_lot (int [10000])


    ArrayList<StockExchangeRecord> tradeRecord = new ArrayList<StockExchangeRecord>();
    HashMap<Integer, Integer> currentStockInventory = new HashMap<Integer, Integer>();

    String tradeApproach;

    long lastLogoutTime = 0; // ok

    public int getStockInventory(int sNum)
    {
        Integer result = currentStockInventory.get(sNum);

        if(result == null) return 0;

        return result;
    }

    //copier


    //dynamic linking features
    //may be required

    public CommonAccount(long id, String emailAdd)
    {
        u_id = id;
        //allAccList.put(id, this);



        subscribersID = new ArrayList<Long>();
        copyFromList = new ArrayList<Long>();
        followersID = new ArrayList<Long>();
        copyToList = new ArrayList<Long>();

        copyFrom(0); // Always copy the first person, test only!!

        accountEmailAddress = emailAdd;
        amountTotalMoney = 120000;
    }

    private  CommonAccount(){}


    public void setName(String pivName, String pubName)
    {
        privateName = pivName;
        publicName = pubName;
    }

    public String getPrivateName()
    {
        return privateName;
    }

    public String getPublicName()
    {
        return publicName;
    }

    public void setPassword(String pw)
    {
        password = pw;
    }

    public boolean stockBuySell(int sNum, int amount)
    {
        Integer stockNum = currentStockInventory.get(sNum);
        if(stockNum == null) stockNum = 0;

        //buy: amount positive, sell: amount negative
        //WTF for real product, this is only for demo ONLY

        Log.d("StockPurchase", "stockNum=" + sNum + ",amount=" + amount);


        if(amount < 0 && stockNum < (-amount))
        {
            return false;
        }

        StockExchangeRecord tmpRecord = new StockExchangeRecord(sNum, amount, new Date());

        if(tmpRecord.totalAmountOfMoney > amountTotalMoney)
        {
            return false;
        }

        tradeRecord.add(tmpRecord);
        updateInventory(sNum, amount);
        amountTotalMoney -= tmpRecord.totalAmountOfMoney;


        return true;
    }

    void updateInventory(int sNum, int amount)
    {
        Integer currentAmount = currentStockInventory.get(sNum);
        if(currentAmount == null)
            currentAmount = 0;

        currentAmount += amount;
        currentStockInventory.put(sNum, currentAmount);
    }

    <T> String getObjectArrayString(ArrayList<T> a)
    {
        StringBuilder sb = new StringBuilder();
        for (T s : a)
        {
            sb.append(s.toString());
            sb.append("\t");
        }

        return sb.toString();

    }

    ArrayList<Long> stringToLongArrayList(String s)
    {
        String[] strings = s.split("\t");
        ArrayList<Long> list = new ArrayList<Long>();

        for(String ss : strings)
        {
            if(!ss.isEmpty())
                list.add(Long.parseLong(ss));
        }

        return list;
    }

    ArrayList<StockExchangeRecord> stringToStockExchangeRecordList(String s)
    {
        String[] strings = s.split("\t");
        ArrayList<StockExchangeRecord> list = new ArrayList<StockExchangeRecord>();

        for(String ss : strings)
        {
            if(!ss.isEmpty())
                list.add(StockExchangeRecord.createRecordFromString(ss));
        }

        return list;
    }

    String sparseIntArrayToString(SparseIntArray array)
    {
        StringBuilder sb = new StringBuilder();
        int endIndex = array.size();
        for(int i = 0; i < endIndex; i++)
        {
            sb.append(array.keyAt(i)).append(',').append(array.valueAt(i)).append(';');
        }

        return sb.toString();
    }

    SparseIntArray stringToSparseIntArray(String s)
    {
        SparseIntArray newA = new SparseIntArray();
        String[] strings = s.split("\t");
        String[] elementSs;

        for(String ss : strings)
        {
            if(!ss.isEmpty()) {
                elementSs = ss.split(",");
                newA.put(Integer.parseInt(elementSs[0]), Integer.parseInt(elementSs[1]));
            }
        }

        return newA;
    }

    public ArrayList<StockExchangeRecord> getCopyFromNewestRecord()
    {
        //Get the newest record history from "copyFrom" list
        if(hasAccessCopyFromAfterLogin) return null;

        hasAccessCopyFromAfterLogin = true;

        CommonAccount tmpAcc;
        StockExchangeRecord rec;
        long timeDiff;
        long curTime = new Date().getTime();
        ArrayList<StockExchangeRecord> result = new ArrayList<StockExchangeRecord>();

        long expectedLookThroughTime = 1200000;

//        if((curTime - lastLogoutTime) < expectedLookThroughTime)
//        {
//            expectedLookThroughTime = curTime - lastLogoutTime;
//        }

        for(Long id : copyFromList)
        {
            tmpAcc = allAccList.get(id);
            if(tmpAcc != null)
            {
               for(int i = tmpAcc.tradeRecord.size() - 1; i >= 0; i--)
               {
                   rec = tmpAcc.tradeRecord.get(i);

                   timeDiff = curTime - rec.bTime.getTime();

                   if(timeDiff < expectedLookThroughTime)
                   {
                       result.add(rec);
                   }

               }
            }
        }

        if(result.size() < 1) return null;

        return result;
    }


    public String createStringData()
    {
//        StringBuilder builder = new StringBuilder();

        String s;
        try
        {
//            Log.d("CommonAccSaveStart", "Hii");
            s = StorageUtils.objectToLetterString(this);

//            builder.append(u_id).append('\n');
//            builder.append(password).append('\n');
//            builder.append(accountEmailAddress).append('\n');
//            builder.append(privateName).append('\n');
//            builder.append(publicName).append('\n');
//            builder.append(amountTotalMoney).append('\n');
//            builder.append(tradeApproach).append('\n');
//
//
//            builder.append(getObjectArrayString(subscribersID)).append('\n');
//            builder.append(getObjectArrayString(copyFromList)).append('\n');
//            builder.append(getObjectArrayString(followersID)).append('\n');
//            builder.append(getObjectArrayString(copyToList)).append('\n');
//            builder.append(getObjectArrayString(tradeRecord)).append('\n');
//            builder.append(sparseIntArrayToString(currentStockInventory));

//            s = builder.toString();
            Log.d("CommonAccSave", s);

            //Array store

        }
        catch (Exception e)
        {
            return null;
        }

//        return builder.toString();

        return s;
    }

    public static CommonAccount createAccountFromString(String s)
    {
//        String[] strings = s.split("\n", -2);
        CommonAccount acc; //= new CommonAccount();

        int i = 0;

        try
        {
            Log.d("CommonAccLoadStart1", s);

//            acc = new CommonAccount();
            acc = StorageUtils.<CommonAccount>letterStringToObj(s);


//            acc.u_id = Long.parseLong(strings[i++]);
//            acc.password = strings[i++];
//            acc.accountEmailAddress = strings[i++];
//            acc.privateName = strings[i++]; // shown to you
//            acc.publicName = strings[i++];
//            acc.amountTotalMoney = Double.parseDouble(strings[i++]);
//            acc.tradeApproach = strings[i++];
//
//            acc.subscribersID = acc.stringToLongArrayList(strings[i++]);
//            acc.copyFromList = acc.stringToLongArrayList(strings[i++]);
//            acc.followersID = acc.stringToLongArrayList(strings[i++]);
//            acc.copyToList = acc.stringToLongArrayList(strings[i++]);
//
//            acc.tradeRecord = acc.stringToStockExchangeRecordList(strings[i++]);
//            acc.currentStockInventory = acc.stringToSparseIntArray(strings[i++]);


            Log.d("CommonAccLoadEnd", "Hii");
//            builder.append(getObjectArrayString(subscribersID)).append('\n');
//            builder.append(getObjectArrayString(copyFromList)).append('\n');
//            builder.append(getObjectArrayString(followersID)).append('\n');
//            builder.append(getObjectArrayString(copyToList)).append('\n');
//            builder.append(getObjectArrayString(tradeRecord)).append('\n');
//            builder.append(sparseIntArrayToString(currentStockInventory)).append('\n');

        }
        catch ( Exception e)
        {
            return null;
        }

        return acc;

    }


    public static CommonAccount createNewAccount(Activity context, String pw,String realName, String nickName, String email)
    {
        Set<Map.Entry<Long,CommonAccount>> set = allAccList.entrySet();
        for(Map.Entry<Long,CommonAccount> e : set)
        {
            if(e.getValue().privateName.equals(realName))
            {
                return null;
            }
        }

        CommonAccount acc = new CommonAccount(allAccList.size(), email);
        acc.setName(realName, nickName);
        acc.setPassword(pw);

        allAccList.put(acc.u_id, acc);

        SharedPreferences dataBase = context.getSharedPreferences("accDateBase",0);
        String listString = dataBase.getString("accList", "");
        if(listString != "")
            listString += (",acc_" + Long.toString(acc.u_id));
        else
            listString = "acc_" + Long.toString(acc.u_id);
        dataBase.edit().putString("accList", listString).commit();


        currentAccount = acc;

        updateCurrentAccount(context);

        return acc;
    }

    public static ArrayList<CommonAccount> getAccountList()
    {
        return new ArrayList<CommonAccount>(allAccList.values());
    }

    public static void storeAllAccount(Context context)
    {
        SharedPreferences.Editor dataBase = context.getSharedPreferences("accDateBase",0).edit();

//        StringBuilder sb = new StringBuilder();
        String accDatabaseID;


        Set<Map.Entry<Long,CommonAccount>> set = allAccList.entrySet();
        for(HashMap.Entry<Long,CommonAccount> entry : set)
        {
            accDatabaseID = "acc_" + entry.getKey().toString();
//            sb.append(accDatabaseID).append(',');
            dataBase.putString(accDatabaseID, entry.getValue().createStringData());
        }

//        sb.deleteCharAt(sb.length() - 1);
//        dataBase.putString("accList", sb.toString());

        if(currentAccount == null)
            dataBase.putLong("curAccountID", -1);
        else
            dataBase.putLong("curAccountID", currentAccount.u_id);

        dataBase.commit();

    }

//    public static void updateCurrentAccount(Activity act)
//    {
//        updateCurrentAccount()
//    }

    public static void updateCurrentAccount(Activity act)
    {
        Context context = act.getApplicationContext();

        if(currentAccount == null) return;

        SharedPreferences.Editor dataBase = context.getSharedPreferences("accDateBase",0).edit();

//        StringBuilder sb = new StringBuilder();
        String accDatabaseID;

        accDatabaseID = "acc_" + Long.toString(currentAccount.u_id);
//        sb.append(accDatabaseID).append(',');
        dataBase.putString(accDatabaseID, currentAccount.createStringData());

        dataBase.putLong("curAccountID", currentAccount.u_id);

        dataBase.commit();
    }

    public static void getAllAccounts(Context context)
    {
        if(allAccList != null) return;
        //Will clear all previous data first
        //allAccList.clear();

        allAccList = new HashMap<Long,CommonAccount>();
        CommonAccount tmpAcc;

        SharedPreferences dataBase = context.getSharedPreferences("accDateBase", 0);
        String accList = dataBase.getString("accList", "");

        if(accList == "") return;

        String[] accIds = accList.split(",");
        String dataString;

        for(String s : accIds)
        {
            dataString = dataBase.getString(s, "");
            tmpAcc = CommonAccount.createAccountFromString(dataString);
            if(tmpAcc != null)
            {
                allAccList.put(tmpAcc.u_id, tmpAcc);
            }
            else {
                Log.e("Account loading", "Fail to load accounts!!");
                return;
            }
        }

        long curAccId = dataBase.getLong("curAccountID", -1);

        if(curAccId != -1)
        {
            currentAccount = allAccList.get(curAccId);
        }

    }

    public static void logout()
    {
        if(currentAccount == null) return;
        currentAccount.lastLogoutTime = new Date().getTime();
        currentAccount = null;
    }

    public static CommonAccount loginIn(Activity act,String realName, String pw)
    {
//        if(currentAccount != null)
//        {
//            return null;
//        }

//        String allNames = realName + "," + pw + ";";
        String tmpID;

        Set<Map.Entry<Long,CommonAccount>> set = allAccList.entrySet();
        for(Map.Entry<Long,CommonAccount> e : set)
        {
            tmpID =  e.getValue().privateName;
//            allNames += tmpID;

//            Log.e("HiiasTest", "(" + tmpID + "," + realName + ": " + tmpID.equals(realName));

            if(tmpID.equals(realName))
            {
//                Log.e("HiiasTest2", "(" + pw + "," + e.getValue().password + ": " + pw.equals(e.getValue().password));

                if(e.getValue().password.equals(pw))
                {
                    currentAccount = e.getValue();
                    hasAccessCopyFromAfterLogin = false;

                    updateCurrentAccount(act);
                    return currentAccount;
                }
                else
                {
//                    Log.e("Hiias", allNames + "," + e.getValue().password);
                    return null;
                }
            }
        }

//        Log.e("HiiasBad", allNames);


        return null;
    }

    public static CommonAccount getCurrentAccount()
    {
        return currentAccount;
    }

}


