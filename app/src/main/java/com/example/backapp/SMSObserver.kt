package com.example.backapp

import android.annotation.SuppressLint
import android.content.Context
import android.database.ContentObserver
import android.database.Cursor
import android.net.Uri
import android.os.Handler
import android.provider.Telephony.TextBasedSmsColumns
import android.provider.Telephony.TextBasedSmsColumns.*
import android.media.MediaPlayer
import android.provider.Settings


class SMSObserver(var handler: Handler?, var adapter:smsAdapter, val context: Context?, val uri: Uri): ContentObserver(handler) {

    private var lastId:Int?=null;
    private var lastType:Int?=null;

    @SuppressLint("Range")
    override fun onChange(selfChange: Boolean) {
        super.onChange(selfChange);

        var resolver = context?.contentResolver;
        var cursor: Cursor? = null ;

        try {
            cursor = resolver?.query(uri, null, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {

                if(cursor.getInt(cursor.getColumnIndex("_ID"))!=lastId && cursor.getInt(cursor.getColumnIndex("TYPE"))!=lastType) {
                    lastId = cursor.getInt(cursor.getColumnIndex("_ID"));
                    lastType = cursor.getInt(cursor.getColumnIndex("TYPE"));

                    val type: Int = cursor.getInt(cursor.getColumnIndex("TYPE"))
                    if (type == MESSAGE_TYPE_INBOX) {
                        adapter.DataSetChanged();
                        println("RECEIVED");
                    } else if (type == MESSAGE_TYPE_SENT) {
                        adapter.DataSetChanged();
                        println("SENT")

                    }
                    cursor.close();
                }
            }
        }
        finally {
            if (cursor != null) cursor?.close();
        }
    }

    override fun deliverSelfNotifications(): Boolean {
        return true;
    }
}