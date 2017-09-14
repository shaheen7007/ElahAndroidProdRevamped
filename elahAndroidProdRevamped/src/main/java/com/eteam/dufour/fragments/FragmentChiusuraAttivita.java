package com.eteam.dufour.fragments;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.AsyncTask.Status;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.eteam.dufour.customview.ElahProgress;
import com.eteam.dufour.database.tables.TableCustomers;
import com.eteam.dufour.database.tables.TableCustomers.BaseAddress;
import com.eteam.dufour.database.tables.TableCustomers.BaseCustomer;
import com.eteam.dufour.database.tables.TableLogin;
import com.eteam.dufour.database.tables.TableSurvey;
import com.eteam.dufour.database.tables.TableSurvey.Survey;
import com.eteam.dufour.database.tables.TableSurveyAssortimento;
import com.eteam.dufour.database.tables.TableSurveyCompetitor;
import com.eteam.dufour.database.tables.TableSurveyCustomers;
import com.eteam.dufour.database.tables.TableSurveyPromotions;
import com.eteam.dufour.database.tables.TableSyncInfo;
import com.eteam.dufour.mobile.R;
import com.eteam.utils.Consts;
import com.eteam.utils.ElahHttpClient;
import com.eteam.utils.Util;

public class FragmentChiusuraAttivita extends Fragment implements OnClickListener{
	// ===========================================================
	// Constants
	// ===========================================================
	
	public static final String TAG = "com.eteam.dufour.fragments.FragmentChiusuraArrivita";
	
	private static final String EXTRA_USERNAME = "com.eteam.dufour.fragments.FragmentChiusuraAttivita.EXTRA_USERNAME"; 
	private static final String EXTRA_CUSTOMER_CODE = "com.eteam.dufour.fragments.FragmentChiusuraAttivita.EXTRA_CUSTOMER_CODE";
	private static final String EXTRA_SURVEY_ID = "com.eteam.dufour.fragments.FragmentChiusuraAttivita.EXTRA_SURVEY_ID";
	private static final String EXTRA_CLUSTER_VALUE = "com.eteam.dufour.fragments.FragmentChiusuraAttivita.EXTRA_CLUSTER_VALUE";
	private static final String EXTRA_CLUSTER_VALUE_STAT = "com.eteam.dufour.fragments.FragmentChiusuraAttivita.EXTRA_CLUSTER_VALUE_STAT";
	public static final int CURRENT_SURVEY_SEND_FAILED = 1;
	public static final int SURVEY_SEND_SUCCESS_ONLINE = 2;
	public static final int SURVEY_SEND_SUCCESS_OFFLINE = 4;
	public static final int SURVEY_EMPTY = 3;
	public static final int INVIA_DATI_SURVEY_FAILED = 5;
	
	
	// ===========================================================
	// Fields
	// ===========================================================
	
	private TextView mFieldSync;
	private TextView mFieldCustName;
	private TextView mFieldCustCode;
	private TextView mFieldAddress1;
	private TextView mFieldAddress2;
	
	private View mBtnInviaDati;
	
	private String 		 userName;
	private String 		 passWord;
	private String 		 customerCode;
	private long   		 surveyId;
	private int 		 clusterValue;
	private int 		 clusterStatus;
	private String 		 versionId;
	
	private OnSurveySentListener mSurveySentListener;
	
	private SurveySendTask task;
	
	// ===========================================================
	// Constructors
	// ===========================================================

	public static FragmentChiusuraAttivita getInstance(long surveyId,String userName, String customerCode, int clusterValue, int clusterStatus){
		FragmentChiusuraAttivita fragment = new FragmentChiusuraAttivita();
		Bundle bundle = new Bundle();
		bundle.putString(EXTRA_USERNAME, userName);
		bundle.putString(EXTRA_CUSTOMER_CODE, customerCode);
		bundle.putLong(EXTRA_SURVEY_ID, surveyId);
		bundle.putInt(EXTRA_CLUSTER_VALUE, clusterValue);
		bundle.putInt(EXTRA_CLUSTER_VALUE_STAT, clusterStatus);
		fragment.setArguments(bundle);
		return fragment;
	}
	
	// ===========================================================
	// Getter & Setter
	// ===========================================================

//	public OnSurveySentListener getSurveySentListener() {
//		return mSurveySentListener;
//	}
//
//	public void setSurveySentListener(OnSurveySentListener mSurveySentListener) {
//		this.mSurveySentListener = mSurveySentListener;
//	}
	
	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);		
		versionId = Util.getApplicationVersion(getActivity());
		if(activity instanceof OnSurveySentListener){
			mSurveySentListener = (OnSurveySentListener) activity;
		}
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		if(getArguments()!=null){
			userName 	 = getArguments().getString(EXTRA_USERNAME);
			customerCode = getArguments().getString(EXTRA_CUSTOMER_CODE);
			surveyId     = getArguments().getLong(EXTRA_SURVEY_ID);
			clusterValue = getArguments().getInt(EXTRA_CLUSTER_VALUE);
			clusterStatus =getArguments().getInt(EXTRA_CLUSTER_VALUE_STAT);
		}
	}
	

	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		passWord = TableLogin.getPassword(userName);
		
		View view = inflater.inflate(R.layout.chiusura_attivita, container,false);
		
		mFieldSync = (TextView) view.findViewById(R.id.field_sync_date);
		mFieldCustName = (TextView) view.findViewById(R.id.field_cust_name);
		mFieldCustCode = (TextView) view.findViewById(R.id.field_cust_code);
		mFieldAddress1 = (TextView) view.findViewById(R.id.field_address_1);
		mFieldAddress2 = (TextView) view.findViewById(R.id.field_address_2);
		
		mBtnInviaDati = view.findViewById(R.id.btn_invia_dati);
		
		mFieldSync.setText(TableSyncInfo.getSyncDate());
		BaseCustomer customer = TableCustomers.getBaseCustomer(customerCode);
		BaseAddress		 address  = customer.getAddress();
		mFieldCustName.setText(customer.getName());
		mFieldCustCode.setText(customer.getCode());
		mFieldAddress1.setText(address.getAddress());
		mFieldAddress2.setText(address.getPostalCode()+"-"+address.getCity());
		
		
		
		mBtnInviaDati.setOnClickListener(this);
		
		return view;
	}

	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v==mBtnInviaDati){
			sendCurrentSurvey();			
		}
	}
	
	public void sendCurrentSurvey(){
		if(Util.haveNetworkConnection(getActivity())){
			
			if(task!=null&&task.getStatus()==Status.FINISHED){
				task = new SurveySendTask(getActivity());
				task.execute();
			}
			else if(task==null){
				task = new SurveySendTask(getActivity());
				task.execute();
			}
		}
		else{
			JSONObject currentSurvey;
			
			try {
				
				
				currentSurvey = TableSurvey.getSurveyAsJSON(surveyId,true);
				if(isJSONArrayEmpty(currentSurvey, TableSurveyPromotions.KEY_JSON_ARRAYNAME)
						&&isJSONArrayEmpty(currentSurvey, TableSurveyAssortimento.KEY_JSON_ARRAYNAME)
						&&isJSONArrayEmpty(currentSurvey, TableSurveyCompetitor.KEY_JSON_ARRAYNAME)){
					mSurveySentListener.onSurveySent(SURVEY_EMPTY);	
					return;
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				mSurveySentListener.onSurveySent(CURRENT_SURVEY_SEND_FAILED);
				return;
			}
			TableSurvey.updateSurveySentDate(surveyId, Util.getCurrentDateTime(),clusterValue,clusterStatus);
			TableSurvey.updateFlag(surveyId,TableSurvey.FLAG_TO_BE_SENT);
			
			mSurveySentListener.onSurveySent(SURVEY_SEND_SUCCESS_OFFLINE);
		}
	}
	
	// ===========================================================
	// Methods
	// ===========================================================
	private boolean isJSONArrayEmpty(JSONObject object,String arrayKey) throws JSONException{
		
		if(object==null||object.getJSONArray(arrayKey).length()==0)
			return true;
		else
			return false;
	}

	
	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
	


	private class SurveySendTask extends AsyncTask<Void, Integer, Integer>{
		private static final String KEY_PAGE_NO = "page_no";
		
		private static final String KEY_POST_JSON = "syncdata";
		private static final String KEY_REQ_FOR = "reqfor";
		private static final String KEY_USER_NAME = "user";
		private static final String KEY_PASSWORD = "pwd";
		private static final String KEY_VERSION_ID = "versionId";
		private static final String KEY_CURRENT_TIME = "ct";
		
		private static final String KEY_RESPONSE = "errorcode";
		
		private static final String RESPONSE_SUCCESS = "1";
		
		private static final int SENDING_CURRENT_SURVEY = -4;
		private static final int CURRENT_SURVEY_SUCCESS = -5;
		private static final int SENDING_TO_BE_SENT = -6;
		private static final int TO_BE_SENT_SUCCESS = -7;
		
		private ElahProgress mSyncProgress;
		private Context 	   mContext;
		
	
		public SurveySendTask(Context context) {
			this.mContext = context;
		}
		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			mSyncProgress = new ElahProgress(mContext);
			mSyncProgress.setCanceledOnTouchOutside(false);
			mSyncProgress.setCancelable(false);
			if(mSyncProgress!=null){
	    			 mSyncProgress.show(R.string.senting);
			}
		}
		
		@Override
		protected void onProgressUpdate(Integer... values) {
			// TODO Auto-generated method stub
			super.onProgressUpdate(values);
			if(mSyncProgress!=null){
	    		 if(values[0]==SENDING_CURRENT_SURVEY){
	    			 mSyncProgress.setMessage(R.string.senting);
	    		 }
	    		 else if(values[0]==CURRENT_SURVEY_SUCCESS){
	    			 mSyncProgress.setMessage(R.string.senting);
	    		 }
	    		 else if(values[0]==SENDING_TO_BE_SENT){
	    			 mSyncProgress.setMessage(R.string.senting);
	    		 }
	    		 else if(values[0]==TO_BE_SENT_SUCCESS){
	    			 mSyncProgress.setMessage(R.string.senting_successful);
	    		 }
		    }
		}
		
		@Override
		protected void onPostExecute(Integer result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if(mSyncProgress!=null)
	    		mSyncProgress.dismiss();
			mSurveySentListener.onSurveySent(result);
		}
		
		@Override
		protected Integer doInBackground(Void... params) {
			int count = 0;
//			Cluster cluster = TableCluster.getCluster(customerCode);
//			if(cluster!=null){
//			
//				try {
//					ResSaveCluster response = (ResSaveCluster) ElahNetHelper.executeRequestResponse(
//							new ReqSaveCluster(cluster), ResSaveCluster.class);
//					TableCluster.udpateClusterStatus(customerCode, response.getStatus());
//					
//				} catch (ClientProtocolException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				} catch (JSONException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//					
//				
//			}
			
			
			try {
				publishProgress(SENDING_CURRENT_SURVEY);
				TableSurvey.updateSurveySentDate(surveyId, Util.getCurrentDateTime(),clusterValue,clusterStatus);
				
				JSONObject currentSurvey = TableSurvey.getSurveyAsJSON(surveyId,true);
				JSONObject currentSurveyJSON = null;
				JSONObject response = null;
				
				if(currentSurvey!=null){
					
					if(isJSONArrayEmpty(currentSurvey, TableSurveyPromotions.KEY_JSON_ARRAYNAME)
						&&isJSONArrayEmpty(currentSurvey, TableSurveyAssortimento.KEY_JSON_ARRAYNAME)
						&&isJSONArrayEmpty(currentSurvey, TableSurveyCompetitor.KEY_JSON_ARRAYNAME)){
						return SURVEY_EMPTY;
					}
										
					currentSurveyJSON = getCompleteSurvey(currentSurvey, count);
					if(Consts.DEBUGGABLE)
						Log.d(TAG, "Sending information - "+currentSurvey.toString());
					response = ElahHttpClient.getJSONPost(Consts.SERVER_URL+"/"+"SurveySave",getPostData(currentSurveyJSON,Util.getCurrentDateTime()));
					
					if(response.get(KEY_RESPONSE).equals(RESPONSE_SUCCESS)){
						Survey survey 	= TableSurvey.getSurvey(surveyId);
						
						TableSurveyCustomers.create(Util.convertToCustomer(survey));
						TableSurvey.delete(survey);
						count++;
						
					}
					else{
						if(Consts.DEBUGGABLE)
							Log.e(TAG, "Survey Failure "+response.toString());
						Crashlytics.log(Log.ERROR, FragmentChiusuraAttivita.class.getName()
		            			, "User Name is "+userName+"\n"+"The survey send is "+currentSurveyJSON.toString(4));
						return CURRENT_SURVEY_SEND_FAILED;
						
					}
					
					
				}
				publishProgress(CURRENT_SURVEY_SUCCESS);
				
				
				ArrayList<Survey> list = TableSurvey.getToBeSentSurveys();
				publishProgress(SENDING_TO_BE_SENT);
				for(Survey survey:list){
					JSONObject toBeSentSurvey = null;
					switch(survey.getFlag()){
						case TableSurvey.FLAG_TO_BE_SENT:
							toBeSentSurvey = TableSurvey.getSurveyAsJSON(survey.getId(),true);
							break;
						case TableSurvey.FLAG_TO_BE_SENT_OLD:
							toBeSentSurvey = TableSurvey.getSurveyAsJSON(survey.getId(),false);
							break;
					}
					currentSurveyJSON= getCompleteSurvey(toBeSentSurvey, count);
					response = ElahHttpClient.getJSONPost(Consts.SERVER_URL+"/"+"SurveySave",getPostData(currentSurveyJSON,survey.getSentDateTime()));
					if(response.get(KEY_RESPONSE).equals(RESPONSE_SUCCESS)){
						
						
						TableSurveyCustomers.create(Util.convertToCustomer(survey));
						TableSurvey.delete(survey);
						count++;
					}
					else{
						Crashlytics.log(Log.ERROR, FragmentChiusuraAttivita.class.getName()
		            			, "User Name is "+userName+"\n"+"The survey send is "+currentSurveyJSON.toString(4));
						return INVIA_DATI_SURVEY_FAILED;
					}
					
				}
				publishProgress(TO_BE_SENT_SUCCESS);
				
				
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Crashlytics.log(Log.ERROR, FragmentChiusuraAttivita.class.getName()
            			, "User Name is "+userName+"\n"+"Error is "+e.getMessage());
				if(count>0)
					return INVIA_DATI_SURVEY_FAILED;
				else
					return CURRENT_SURVEY_SEND_FAILED;
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Crashlytics.log(Log.ERROR, FragmentChiusuraAttivita.class.getName()
            			, "User Name is "+userName+"\n"+"Error is "+e.getMessage());
				if(count>0)
					return INVIA_DATI_SURVEY_FAILED;
				else
					return CURRENT_SURVEY_SEND_FAILED;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Crashlytics.log(Log.ERROR, FragmentChiusuraAttivita.class.getName()
            			, "User Name is "+userName+"\n"+"Error is "+e.getMessage());
				if(count>0)
					return INVIA_DATI_SURVEY_FAILED;
				else
					return CURRENT_SURVEY_SEND_FAILED;
			}
			return SURVEY_SEND_SUCCESS_ONLINE;
		}
		

		private JSONObject getCompleteSurvey(JSONObject survey,int page) throws JSONException{
			
			JSONObject object = new JSONObject();
			object.put(Survey.KEY_JSON_ARRAY_NAME, survey);
			object.put(KEY_PAGE_NO, page);
			return object;
		}
		
		private boolean isJSONArrayEmpty(JSONObject object,String arrayKey) throws JSONException{
			if(object.getJSONArray(arrayKey).length()==0)
				return true;
			else
				return false;
		}
		
		private List<NameValuePair> getPostData(JSONObject json,String sentTime){
			return getPostData(json.toString(),sentTime);
		}
		
		private List<NameValuePair> getPostData(String json,String sentTime){
			ArrayList<NameValuePair> list = new ArrayList<NameValuePair>();
			
			BasicNameValuePair request = new BasicNameValuePair(KEY_REQ_FOR, "survey");
			BasicNameValuePair syncdata = new BasicNameValuePair(KEY_POST_JSON, json);
			BasicNameValuePair user = new BasicNameValuePair(KEY_USER_NAME, userName);
			BasicNameValuePair pwd = new BasicNameValuePair(KEY_PASSWORD, passWord);
			BasicNameValuePair version = new BasicNameValuePair(KEY_VERSION_ID,versionId);
			BasicNameValuePair ct = new BasicNameValuePair(KEY_CURRENT_TIME, sentTime);
			
			list.add(request);
			list.add(syncdata);
			list.add(user);
			list.add(pwd);
			list.add(version);
			list.add(ct);
			
			return list;
		}
		
	}
	
	// ===========================================================
	// Interfaces
	// ===========================================================
	
	public interface OnSurveySentListener{
		public void onSurveySent(int status);
	}
	
	
}
