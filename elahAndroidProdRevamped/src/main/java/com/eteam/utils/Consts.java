package com.eteam.utils;

import android.os.Environment;
import android.text.InputFilter;

import java.io.File;

public class Consts {
	// ===========================================================
	// Constants
	// ===========================================================

	public static final boolean DEBUGGABLE = true;
	
/*--------------------------------------------------------------------------------------------------------------------------------*/	
	//ELAH PRE PRODUCTION (NEW)

	/*public static final String SERVER_URL = "http://preprodweb.elah-dufour.it/portal/mobile";
	public static final String ELAH_APK_URL = "http://preprodweb.elah-dufour.it/portal/dufourMobile/downloads/ElahAndroid.apk";
	public static final String ERROR_URL = "http://121.241.180.136:8082/elaherror/error.jsp";
	public static final String DB_NAME = "elahPreProd.db";*/
	
/*--------------------------------------------------------------------------------------------------------------------------------*/	
	//ELAH TEST FROM (19-05-2014)
	public static final String SERVER_URL 	= "http://test.elah-dufour.it/portal-test/mobile";
	//public static final String SERVER_URL 	= "http://192.168.0.90:8081/elah/mobile";
	public static final String ELAH_APK_URL = "http://test.elah-dufour.it/portal-test/dufourMobile/downloads/ElahAndroid.apk";
	public static final String ERROR_URL 	= "http://121.241.180.136:8082/elaherror/error.jsp";
	public static final String DB_NAME 		= "elahCustTest.db";
	
/*--------------------------------------------------------------------------------------------------------------------------------*/	
	//ELAH PRODUCTION
	
	/*public static final String SERVER_URL = "http://web.elah-dufour.it/portal/mobile";
	public static final String ELAH_APK_URL = "http://web.elah-dufour.it/portal/dufourMobile/downloads/ElahAndroid.apk";
	public static final String ERROR_URL = "http://121.241.180.136:8082/elaherror/error.jsp";
	public static final String DB_NAME = "elah.db";*/
	

	
	
/*--------------------------------------------------------------------------------------------------------------------------------*/	

	//ELAH ANOOP
//	public static final String SERVER_URL = "http://192.168.0.31:8080/elah/mobile"; 
	
	//ELAH BIJU
//	public static final String SERVER_URL = "http://192.168.1.59:8080/elah/mobile"; 
	
	//ELAH SIBI
//	public static final String SERVER_URL = "http://192.168.1.9:8080/elah/mobile"; 
	
	//ELAH LOCAL
//	public static final String SERVER_URL = "http://192.168.0.1:8080/elah/mobile";
//	public static final String ELAH_APK_URL = "http://192.168.0.1:8082/elah-Real/dufourMobile/downloads/ElahAndroid.apk";
//	public static final String ERROR_URL = "http://192.168.0.1:8082/elaherror/error.jsp";
//	public static final String DB_NAME = "localelah.db";
	
	//ELAH PUBLIC
//	public static final String SERVER_URL = "http://121.241.180.136:8080/elah/mobile";      
//	public static final String ELAH_APK_URL ="http://121.241.180.136:8082/elah-Real/dufourMobile/downloads/ElahAndroid.apk";
//	public static final String ERROR_URL = "http://121.241.180.136:8082/elaherror/error.jsp";
//	public static final String DB_NAME = "localelah.db";
	
/*--------------------------------------------------------------------------------------------------------------------------------*/	
	
	//ELAH CUSTOMER TEST
//	public static final String SERVER_URL = "http://web.elah-dufour.it/portal-test/mobile";      
//	public static final String ELAH_APK_URL = "http://web.elah-dufour.it/portal-test/dufourMobile/downloads/ElahAndroid.apk";
//	public static final String ERROR_URL = "http://121.241.180.136:8082/elaherror/error.jsp";
//	public static final String DB_NAME = "dufour.db";

/*--------------------------------------------------------------------------------------------------------------------------------*/		

	//ELAH NEW CUSTOMER TEST PREPROD (NOT USED ANYMORE)
	
//	public static final String SERVER_URL = "http://portalnovi.preprod.gruppozenit.com/portal-test/mobile";      
//	public static final String ELAH_APK_URL = "http://portalnovi.preprod.gruppozenit.com/portal-test/dufourMobile/downloads/ElahAndroid.apk";
//	public static final String ERROR_URL = "http://121.241.180.136:8082/elaherror/error.jsp";
//	public static final String DB_NAME = "testelah.db";

	
/*--------------------------------------------------------------------------------------------------------------------------------*/	
	
	//ELAH NEW CUSTOMER TEST 14-11-2013
	
//	public static final String SERVER_URL = "http://newweb.elah-dufour.it/portal-test/mobile";      
//	public static final String ELAH_APK_URL = "http://newweb.elah-dufour.it/portal-test/dufourMobile/downloads/ElahAndroid.apk";
//	public static final String ERROR_URL = "http://121.241.180.136:8082/elaherror/error.jsp";
//	public static final String DB_NAME = "newtestelah.db";

	
/*--------------------------------------------------------------------------------------------------------------------------------*/
	//ELAH REAL LOCAL

//	public static final String SERVER_URL = "http://192.168.0.1:8082/elah-Real/mobile";
//	public static final String ELAH_APK_URL = "http://192.168.0.1:8082/elah-Real/dufourMobile/downloads/ElahAndroid.apk";
//	public static final String ERROR_URL = "http://192.168.0.1:8082/elaherror/error.jsp";
//	public static final String DB_NAME = "realelah.db";
	
	
	//ELAH-REAL PUBLIC
//	public static final String SERVER_URL = "http://121.241.180.136:8082/elah-Real/mobile";
//	public static final String ELAH_APK_URL = "http://121.241.180.136:8082/elah-Real/dufourMobile/downloads/ElahAndroid.apk";
//	public static final String ERROR_URL = "http://121.241.180.136:8082/elaherror/error.jsp";
//	public static final String DB_NAME 	 = "realelah.db";

/*--------------------------------------------------------------------------------------------------------------------------------*/		
	
	public static final String PREF_ELAH = "com.eteam.utils.AppPreferences.elah_pref";
	

/*--------------------------------------------------------------------------------------------------------------------------------*/			

	//Static Arrays
	public static final String[] ARRAY_SI_NO = new String[]{"","Si","No"};
	public static final String[] ARRAY_VISIBILITY = new String[]{"","Isola","Testata","Fuori banco"};
	public static final String[] ARRAY_FACING = new String[]{"","1","2","3","4","5"};
	public static final String[] ARRAY_POSLNONE = new String[]{"","1","2","3","4","5","6","7","8","V"};
	public static final String[] ARRAY_POSLNTWO = new String[]{"","1","2","3","4","5","6","7","8"};
	public static final String[] ARRAY_PREZZO_PROMO = new String[]{"","10","20","30","40","50"};
	public static final String[] ARRAY_ASSORT = new String[]{"","Assortito","NonAssortito","Rottura di stock","Sospensione"};

	
/*--------------------------------------------------------------------------------------------------------------------------------*/			
	
	public static final String JSON_KEY_ID = "ID";
	
	public static final String JSON_KEY_SESSION_TIME_OUT = "conStatus";
	public static final String RESPONSE_SESSION_TIMED_OUT = "1";

/*--------------------------------------------------------------------------------------------------------------------------------*/			
	
	public static final String ELAH_APK_FILENAME = "ElahAndroid.apk";
	public static final String ELAH_DOWNLOAD_DIRECTORY = Environment.getExternalStorageDirectory().getAbsolutePath()
			+File.separator+"ELAH_DOWNLOADS";

/*--------------------------------------------------------------------------------------------------------------------------------*/		

	public static final ElahKeyListener ELAH_KEY_LISTENER = new ElahKeyListener(false,true);
	public static final InputFilter[] COMMA_FILTER = new InputFilter[]{new CommaInputFilter(),new InputFilter.LengthFilter(15)};
/*--------------------------------------------------------------------------------------------------------------------------------*/	

}
