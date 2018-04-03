package com.example.kb_jay.smsdemo;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * *
 * content://sms/inbox    收件箱
 * <p>
 * content://sms/sent      已发送
 * <p>
 * content://sms/draft    草稿
 * <p>
 * content://sms/outbox    发件箱  (正在发送的信息)
 * <p>
 * content://sms/failed    发送失败
 * <p>
 * content://sms/queued    待发送列表  (比如开启飞行模式后，该短信就在待发送列表里)
 *
 * @author kb_jay
 *         created at 2018/4/3 下午1:41
 */

public class SmsObserver extends ContentObserver {

    public static final String SMS_URI_INBOX = "content://sms/inbox";
    private final Activity mAct;
    private final SmsListener mListener;

    /**
     * Creates a content observer.
     *
     * @param handler The handler to run {@link #onChange} on, or null if none.
     */
    public SmsObserver(Activity act, Handler handler, SmsListener listener) {
        super(handler);
        mAct = act;
        mListener = listener;
    }

    @Override
    public void onChange(boolean selfChange) {
        super.onChange(selfChange);
        Cursor cursor = null;
        ContentResolver contentResolver = mAct.getContentResolver();
        cursor = contentResolver.query(Uri.parse(SMS_URI_INBOX),
                new String[]{"_id", "address", "body", "read"},
                "body like ? and read=?",
                new String[]{"%作业本%","0"},
                "date desc"
                );
        if(cursor!=null){
            if(cursor.moveToFirst()){
                String smsBody = cursor.getString(cursor.getColumnIndex("body"));
                String regEx="[^0-9]";//正则表达式：表示不包括0-9的所有字符串。
                Pattern p=Pattern.compile(regEx);
                Matcher m = p.matcher(smsBody);
                String content = m.replaceAll("").trim();//使用""替换掉所有的匹配到的字符串
                if(mListener!=null&&!TextUtils.isEmpty(content)){
                    mListener.onResult(content);
                }
            }
        }
    }

    public interface SmsListener {
        void onResult(String msg);
    }
}
