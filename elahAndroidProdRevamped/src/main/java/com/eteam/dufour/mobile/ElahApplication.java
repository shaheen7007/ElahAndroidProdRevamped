package com.eteam.dufour.mobile;

import android.database.sqlite.SQLiteOpenHelper;

import com.eteam.db.DBApp;
import com.eteam.dufour.database.ElahDBHelper;
import com.eteam.utils.ElahHttpClient;

public class ElahApplication extends DBApp{
	// ===========================================================
	// Constants
	// ===========================================================
	// ===========================================================
	// Fields
	// ===========================================================
	// ===========================================================
	// Constructors
	// ===========================================================

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		ElahHttpClient.enableCookie();
//		ElahHttpClient.enableHttpResponseCache(this);
		ElahHttpClient.disableConnectionReuseIfNecessary();
	}
	
	
	
	@Override
	public void onTerminate() {
		// TODO Auto-generated method stub
		super.onTerminate();
		ElahDBHelper.closeConnections();
		
	}



	@Override
	public SQLiteOpenHelper getDBHelper() {
		// TODO Auto-generated method stub
		return ElahDBHelper.getInstance(this);
	}
	
	
	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
