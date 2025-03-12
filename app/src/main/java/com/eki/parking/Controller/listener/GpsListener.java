package com.eki.parking.Controller.listener;

import android.location.Location;

public interface GpsListener {
//    void displayGPSSettingsDialog();
	void locationChanged(Location loc, double longitude, double latitude);
	void onGpsDisable();
	void onGpsEnable();

}
