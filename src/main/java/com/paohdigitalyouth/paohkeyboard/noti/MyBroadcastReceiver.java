package com.paohdigitalyouth.paohkeyboard.noti;

/**
 * Created by HtetzNaing on 4/3/2018.
 */

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;
import android.widget.Toast;

import com.paohdigitalyouth.paohkeyboard.News;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class MyBroadcastReceiver extends WakefulBroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        Intent intent1 = new Intent(context,News.class);
        intent1.putExtra("receiver","yes");
        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent1);
    }
}