package com.example.backapp

import android.content.Context
import android.database.Cursor
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class VM: ViewModel() {

    var currentChat:String = "";
    var myPhone:String = "";
    var hashState:Boolean = false;

    fun getSMS(context: Context?, person:String="null"):LiveData<List<sms>>{
        val ret = mutableListOf<sms>();
        val uriSMSURIin = Uri.parse("content://sms/inbox")
        val cur: Cursor? = context?.contentResolver?.query(uriSMSURIin, null, null, null, null)
        while (cur != null && cur.moveToNext()) {
            val time = cur.getString(cur.getColumnIndexOrThrow("date"))
            val address = cur.getString(cur.getColumnIndexOrThrow("address"))
            val body = cur.getString(cur.getColumnIndexOrThrow("body"))
            if(person=="null" || address==person)
                ret.add(sms(time, body, address, false));
        }
        if (cur != null) {
            cur.close()
        }
        val uriSMSURIout = Uri.parse("content://sms/sent")
        val cur2: Cursor? = context?.contentResolver?.query(uriSMSURIout, null, null, null, null)
        while (cur2 != null && cur2.moveToNext()) {
            val time = cur2.getString(cur2.getColumnIndexOrThrow("DATE"))
            val address = cur2.getString(cur2.getColumnIndexOrThrow("address"))
            val body = cur2.getString(cur2.getColumnIndexOrThrow("body"))
            if(person=="" || address==person)
                ret.add(sms(time, body, address, true));
        }
        if (cur2 != null) {
            cur2.close()
        }

        var retu = ret.sortedByDescending { it.time };

        var mld = MutableLiveData<List<sms>>();
        mld.value=retu;

        return mld as LiveData<List<sms>>;
    }

    fun getContacts(context: Context?):List<String>{
        val ret = mutableListOf<String>();
        val uriSMSURIin = Uri.parse("content://sms/inbox")
        val cur: Cursor? = context?.contentResolver?.query(uriSMSURIin, null, null, null, null)
        while (cur != null && cur.moveToNext()) {
            val address = cur.getString(cur.getColumnIndexOrThrow("address"))
            if(!ret.contains(address))
                ret.add(address);
        }
        if (cur != null) {
            cur.close()
        }
        val uriSMSURIout = Uri.parse("content://sms/sent")
        val cur2: Cursor? = context?.contentResolver?.query(uriSMSURIout, null, null, null, null)
        while (cur2 != null && cur2.moveToNext()) {
            val address = cur2.getString(cur2.getColumnIndexOrThrow("address"))
            if(!ret.contains(address))
                ret.add(address);
        }
        if (cur2 != null) {
            cur2.close()
        }
        return ret as List<String>;
    }
}