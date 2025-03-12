package com.hill.devlibs.listener;

import android.content.Intent;

/**
 * Created by Hill on 2018/5/21.
 */
public interface BroadcastListener {
    void onCatchReceiver(String action, Intent intent);
}
