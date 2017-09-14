package com.eteam.dufour.mobile;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.AsyncTask.Status;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.eteam.dufour.customview.ElahProgress;
import com.eteam.dufour.database.ElahDBHelper;
import com.eteam.dufour.database.tables.TableLogin;
import com.eteam.dufour.database.tables.TableLogin.Credentials;
import com.eteam.dufour.database.tables.TableLogin.UserInfo;
import com.eteam.dufour.service.UpdateService;
import com.eteam.utils.Consts;
import com.eteam.utils.ElahHttpClient;
import com.eteam.utils.Util;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;

public class LoginActivity extends Activity implements OnClickListener{
	// ===========================================================
	// Constants
	// ===========================================================
	
	public enum LoginStatus{FAILED, SUCCESS, NO_PERMISSION, SERVER_ERROR};
	
	private final static String PARAM_USER_NAME = "user";
	private final static String PARAM_PASSWORD = "pw";
	private final static String PARAM_VERSION = "versionId";
	private final static String PARAM_CURRENT_TIME = "ct";
	
	public static final String PREF_SALESPERSON = "com.eteam.dufour.mobile.LoginActivity.PREF_SALESPERSON";
	public static final String PREF_USER_NAME = "com.eteam.dufour.mobile.LoginActivity.PREF_USER_NAME";
	public static final String PREF_VERSION_ID = "com.eteam.dufour.mobile.LoginActivity.PREF_VERSION_ID";
	
	public static final String INTENT_ERROR_MSG = "com.eteam.dufour.release.backup.LoginActivity.INTENT_ERROR_MSG";
	public static final String INTENT_ERROR_MSG_ID = "com.eteam.dufour.release.backup.LoginActivity.INTENT_ERROR_MSG_ID";
	// ===========================================================
	// Fields
	// ===========================================================
	private Button btnAccept;
	private EditText mFielduName,mFieldPwd;
	private TextView mLblVersion;
    private LoginTask mLoginTask;
    
    private Toast mStatToast;
    
    private ElahDBHelper dbHelper;
    
    private ArrayList<NameValuePair> mLoginParams;
    
    private SharedPreferences mPrefs;
    
    private Button btnImport;
    
    // ===========================================================
	// Constructors
	// ===========================================================

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

    @SuppressLint("ShowToast")
	@Override
    protected void onCreate(Bundle savedInstanceState) {
    	// TODO Auto-generated method stub
    	super.onCreate(savedInstanceState);
    	Crashlytics.start(this);
		setContentView(R.layout.main);
    	mFielduName = (EditText)findViewById(R.id.et_un);
    	mFieldPwd = (EditText)findViewById(R.id.et_pw);
    	btnAccept = (Button)findViewById(R.id.btn_login);
    	btnImport = (Button) findViewById(R.id.btn_import);
    	mLblVersion = (TextView) findViewById(R.id.lbl_version);

        btnAccept.setOnClickListener(this);
        btnImport.setOnClickListener(this);
        
        dbHelper = ElahDBHelper.getInstance(this);
        
        mLblVersion.setText(Util.getAppFullVersionName(this));
        
        ElahDBHelper.createAllTables(dbHelper);
       
        mStatToast = Toast.makeText(this, "sample",Toast.LENGTH_SHORT );
        Util.setUpToast(mStatToast);
	    mPrefs = getSharedPreferences(Consts.PREF_ELAH, MODE_PRIVATE);
        
        mLoginParams = new ArrayList<NameValuePair>();
        showTimeOutAlert(getIntent());
        Util.checkAndPromptUpdate(this, mPrefs);
		Intent intent = new Intent(this,UpdateService.class);
		startService(intent);
        
    }
    
    @Override
    protected void onRestart() {
    	// TODO Auto-generated method stub
    	super.onRestart();
    	Util.checkAndPromptUpdate(this, mPrefs);
    }
    
	@Override
    protected void onNewIntent(Intent intent) {
    	// TODO Auto-generated method stub
    	super.onNewIntent(intent);
    	showTimeOutAlert(intent);
    	
    }
    
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v==btnAccept){
			
			if(mFielduName.getText().toString().trim().equals("")){
				Util.showToast(mStatToast, R.string.username);
			}
			else if(mFieldPwd.getText().toString().trim().equals("")){
				Util.showToast(mStatToast, R.string.password);
			}
			else if(Util.haveNetworkConnection(this)){
				if(mLoginTask!=null){
					if(mLoginTask.getStatus()==Status.FINISHED){
						mLoginTask = new LoginTask();
						populateLoginParams();
						mLoginTask.execute(mFielduName.getText().toString()
								, mFieldPwd.getText().toString(),Util.getApplicationVersion(this)
								,Util.getCurrentTimeInMilliSeconds());
					}
				}
				else{
					mLoginTask = new LoginTask();
					populateLoginParams();
					mLoginTask.execute(mFielduName.getText().toString()
							, mFieldPwd.getText().toString(),Util.getApplicationVersion(this)
							,Util.getCurrentTimeInMilliSeconds());
				}
			}
			else{
				offlineLogin(mFielduName.getText().toString(),mFieldPwd.getText().toString());
			}
		}
		if(v==btnImport){
			FileChannel src = null;
            FileChannel dst = null;
			try {
                File sd = Environment.getExternalStorageDirectory();
                File data = Environment.getDataDirectory();
                
                if (sd.canWrite()) {
                    String currentDBPath = "//data//"+getPackageName()+"//databases//"+Consts.DB_NAME;
                    String backupDBPath = Consts.DB_NAME;
                    File currentDB = new File(data, currentDBPath);
                    File backupDB = new File(sd, backupDBPath);

                    if (currentDB.exists()) {
                        src = new FileInputStream(backupDB).getChannel();
                        dst = new FileOutputStream(currentDB).getChannel();
                        dst.transferFrom(src, 0, src.size());
                        src.close();
                        dst.close();
                    }
                }
            } catch (Exception e) {
            	e.printStackTrace();
            }
		}
	}
    
	// ===========================================================
	// Methods
	// ===========================================================

	private void offlineLogin(String username,String password) {
		// TODO Auto-generated method stub
		if(TableLogin.isOfflineCredentialsValid(username, password)){
			setUserNameToPref(username,mPrefs);
	    	setSalesPersonToPref(TableLogin.getSalesPersonCode(username),mPrefs);
			Intent i = new Intent(LoginActivity.this, ElahSyncPage.class);
    	    startActivity(i);
    	  
		}
		else{
			Util.showToast(mStatToast, R.string.login_failed);
		}
		
	}

	private void populateLoginParams(){
		mLoginParams.clear();
		mLoginParams.add(new BasicNameValuePair(PARAM_USER_NAME, mFielduName.getText().toString()));
		mLoginParams.add(new BasicNameValuePair(PARAM_PASSWORD, mFielduName.getText().toString()));
		mLoginParams.add(new BasicNameValuePair(PARAM_VERSION, Util.getApplicationVersion(this)));
		mLoginParams.add(new BasicNameValuePair(PARAM_CURRENT_TIME, Util.getCurrentTimeInMilliSeconds()));
	}
	
	public static void setUserNameToPref(String userName,SharedPreferences mPrefs){
		Editor editor =	mPrefs.edit();
	    editor.putString(PREF_USER_NAME, userName);
	    editor.commit();
	}
	
	public static void setSalesPersonToPref(String salesPerson,SharedPreferences mPrefs){
		Editor editor =	mPrefs.edit();
		editor.putString(PREF_SALESPERSON, salesPerson);
		editor.commit();
	}
	
	public static void setVersionId(String version,SharedPreferences mPrefs){
		Editor editor =	mPrefs.edit();
	    editor.putString(PREF_VERSION_ID, version);
	    editor.commit();
	}
	
	private void showTimeOutAlert(Intent intent){
		if(intent!=null){
			String msg = intent.getStringExtra(INTENT_ERROR_MSG);
    		int msgId = intent.getIntExtra(INTENT_ERROR_MSG_ID, -1);
    		if(msgId!=-1){
    			Util.showToast(mStatToast,msgId);
    		}
    		else if(msg!=null){
    			Util.showToast(mStatToast, msg);
    		}
    	}
	}
	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
    
    private class LoginTask extends AsyncTask<String, Void, LoginStatus>
    {
    	private ElahProgress mSyncProgress;
    	
         @Override
         protected void onPostExecute(LoginStatus status) {
             super.onPostExecute(status);
             if(mSyncProgress!=null){
 	        	try{
 	        		mSyncProgress.dismiss();
 	        	}
 	        	catch(Exception e){
 	        		e.printStackTrace();
 	        	}
 	        }
             switch(status){
             	case SUCCESS:
             		Util.showToast(mStatToast, R.string.login_success);
             		Intent i = new Intent(LoginActivity.this, ElahSyncPage.class);
            	    startActivity(i);
             		break;
             	case FAILED:
             		Util.showToast(mStatToast, R.string.login_failed);
             		break;
             	case NO_PERMISSION:
             		Util.showToast(mStatToast, R.string.login_no_permission);
             		break;
             	case SERVER_ERROR:
             		Util.showToast(mStatToast, R.string.error_server);
             		break;
             }
         }

         @Override
         protected void onPreExecute() {
        	 super.onPreExecute();
        	mSyncProgress = new ElahProgress(LoginActivity.this);
 			mSyncProgress.setCanceledOnTouchOutside(false);
 			mSyncProgress.setCancelable(false);
 			if(mSyncProgress!=null){
 	    			 mSyncProgress.show(R.string.logging_in);
 			}
         }
         
         @Override
        protected void onCancelled() {
        	// TODO Auto-generated method stub
        	super.onCancelled();
        	if(mSyncProgress!=null)
	    		mSyncProgress.dismiss();
        }
        
         @Override
         protected LoginStatus doInBackground(String... params) {
            
           	LoginStatus status = LoginStatus.FAILED;
           	if(params.length!=4){
           		throw new IllegalArgumentException("params should have user name and password passed in order");
           	}
           	try {
           	    status = login(dbHelper,params[0], params[1], params[2],mPrefs);
           	    
           	}
           	catch(Exception e)
           	{	e.printStackTrace();
           		status = LoginStatus.SERVER_ERROR;
           	}
            return status;
         }
     }
    
    public static final LoginStatus login(ElahDBHelper dbHelper,String username,String password,String versionId,
    		SharedPreferences mPrefs) throws ClientProtocolException, IllegalStateException, URISyntaxException, IOException, JSONException{
   	    JSONObject response = ElahHttpClient.executeGetAsJSON(Consts.SERVER_URL+"/login?user=" + username +
   	    		"&pwd=" + password+"&versionId="+ versionId+"&ct="+Util.getCurrentTimeInMilliSeconds() );
//   		String response = ElahHttpClient.sendPost(Consts.SERVER_URL+"/login", mLoginParams);
   	    System.out.println("Url login : "+Consts.SERVER_URL+"/login?user=" + username +
   	    		"&pwd=" + password+"&versionId="+ versionId+"&ct="+Util.getCurrentTimeInMilliSeconds() );
   	    if(Consts.DEBUGGABLE)
	   	    Log.d("Log", "Url - "+Consts.SERVER_URL+"/login?user=" + username +
	   	    		"&pwd=" + password+"&versionId="+ versionId+"&ct="+Util.getCurrentTimeInMilliSeconds());
   	    
   	    JSONObject loginInfo = response.getJSONObject("login");
   	    String	   status 	 = loginInfo.getString("errorcode");
   	    if(status.equals("0")) {
   	    	UserInfo userinfo = new UserInfo(loginInfo, new Credentials(username, password));
   	    	TableLogin.addLogin(userinfo);
   	    	setUserNameToPref(username,mPrefs);
   	    	setSalesPersonToPref(userinfo.getSalesPersonCode(),mPrefs);
   	    	setVersionId(versionId, mPrefs);
   	    	return LoginStatus.SUCCESS;
   	    }
   	    else if(status.equals("2")){
   	    	return LoginStatus.NO_PERMISSION;
   	    }
   	    else{
   	    	return LoginStatus.FAILED;
   	    }
    }
    
    public static final LoginStatus login(ElahDBHelper dbHelper,SharedPreferences mPrefs) throws ClientProtocolException, IllegalStateException, URISyntaxException, IOException, JSONException{
   	    String userName = mPrefs.getString(PREF_USER_NAME, "");
   	    String password = TableLogin.getPassword(userName);
   	    String versionId = mPrefs.getString(PREF_VERSION_ID, "");
   	    return login(dbHelper, userName, password, versionId, mPrefs);
    }

	public static void copyAppDbToDownloadFolder(Activity ctx) throws IOException {
		File backupDB = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
				Consts.DB_NAME); // for example "my_data_backup.db"
		File currentDB = ctx.getDatabasePath(Consts.DB_NAME);
		if (currentDB.exists()) {
			FileChannel src = new FileInputStream(currentDB).getChannel();
			FileChannel dst = new FileOutputStream(backupDB).getChannel();
			dst.transferFrom(src, 0, src.size());
			src.close();
			dst.close();
		}
	}

}
