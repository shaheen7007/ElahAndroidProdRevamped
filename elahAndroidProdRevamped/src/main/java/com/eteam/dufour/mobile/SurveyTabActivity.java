package com.eteam.dufour.mobile;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;
import android.widget.Toast;

import com.eteam.dufour.database.tables.TableCustomers;
import com.eteam.dufour.database.tables.TableCustomers.BaseAddress;
import com.eteam.dufour.database.tables.TableCustomers.BaseCustomer;
import com.eteam.dufour.database.tables.TableSurvey;
import com.eteam.dufour.fragments.FragmentChiusuraAttivita;
import com.eteam.dufour.fragments.FragmentChiusuraAttivita.OnSurveySentListener;
import com.eteam.dufour.fragments.FragmentConcorrenza;
import com.eteam.dufour.fragments.FragmentProddoti;
import com.eteam.dufour.fragments.FragmentPromozioni;
import com.eteam.utils.Consts;
import com.eteam.utils.Util;

import java.util.ArrayList;

public class SurveyTabActivity extends FragmentActivity 
			implements OnTabChangeListener, OnSurveySentListener, OnClickListener
{
	// ===========================================================
	// Constants
	// ===========================================================
	
	private static final String TAB_PRODOTTI = "prodotti";
	private static final String TAB_PROMOZIONI = "promozioni";
	private static final String TAB_CONCORRENZA = "concorrenza";
	private static final String TAB_CHIUSURA = "chiusura";
	
	private static final String	INTENT_CUSTOMER_NUMBER = "com.eteam.dufour.mobile.SurveyTabActivity.INTENT_CUSTOMER_NUMBER";
	private static final String INTENT_CUSTOMER_NAME = "com.eteam.dufour.mobile.SurveyTabActivity.INTENT_CUSTOMER_NAME";
	public static final String	INTENT_SURVEY_ID = "com.eteam.dufour.mobile.SurveyTabActivity.INTENT_SURVEY_ID";
	public static final String EXTRA_TAB_SELECTED = "com.eteam.dufour.mobile.SurveyTabActivity.EXTRA_TAB_ACTIVITY";
	
	public static final int DEFAULT_SURVEY_ID = -1;
	
	private static final int CALL_SURVEY_SAVE_ACTIVITY = 101;
	private static final int CALL_SURVEY_FAIL_ACTIVITY = 102;
	
	// ===========================================================
	// Fields
	// ===========================================================
	private TabHost mTabHost;
	private Toast mToast;
	
	public String customer_no;
	public String customer_name;
	public String salesPersonCode;
	public String userName;
	
	private View mBtnEsc;
	
	public long surveyId;
	public int clusterPosition = 1;
	public int clustChangeValue = 0;
	
	private SharedPreferences mPrefs;
	
	private TextView mFieldTabName;
	private TextView mFieldCustDetails;
	private TextView mFieldCustAddress;
	private Spinner cluster;
	
	
	// ===========================================================
	// Constructors
	// ===========================================================
	
	// ===========================================================
	// Initializers
	// ===========================================================

	public static void startActivity(Activity activity,long surveyId
			,String customerName,String customerCode){
		Intent intent = new Intent(activity,SurveyTabActivity.class);
		intent.putExtra(INTENT_SURVEY_ID, surveyId);
		intent.putExtra(INTENT_CUSTOMER_NAME, customerName);
		intent.putExtra(INTENT_CUSTOMER_NUMBER, customerCode);
		activity.startActivity(intent);
	}
	
	// ===========================================================
	// Getter & Setter
	// ===========================================================
	
	
	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	protected void onCreate(Bundle bundle) {
		// TODO Auto-generated method stub
		super.onCreate(bundle);
		setContentView(R.layout.tab_survey);
		
		// Lijo
		cluster = (Spinner)findViewById(R.id.field_cluster);
		final ArrayList<String> clusterList = new ArrayList<String>();
		final BaseCustomer customer = TableCustomers.getBaseCustomer(getIntent().getExtras().getString(INTENT_CUSTOMER_NUMBER));
		System.out.println("Cluster Value : "+customer.getCluster());
		
		clusterList.add("");
		clusterList.add("A");
		clusterList.add("B");
		clusterList.add("C");
		clusterList.add("D");
		clusterList.add("E");
		clusterList.add("F");
		
		ArrayAdapter<String> clusterAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.simple_spinner_item, clusterList);
		clusterAdapter.setDropDownViewResource(R.layout.simple_spinner_item_cluster);
		clusterPosition = clusterList.indexOf(customer.getCluster());
		cluster.setAdapter(clusterAdapter);
		cluster.setSelection(clusterPosition);
		clustChangeValue = 0;
		cluster.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {@Override
		public void onItemSelected(AdapterView<?> parent, View view,
				int position, long id) {
			// TODO Auto-generated method stub
			clusterPosition = position;
			if(position != clusterList.indexOf(customer.getCluster())){
				clustChangeValue = 1;
			}
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {
			// TODO Auto-generated method stub
			
		}
		});
		mTabHost = (TabHost) findViewById(android.R.id.tabhost);
		getIntentAndSetValues(bundle);
		setupTabs();
		initialize();
//		setUpClusterTab();
		
	}
	


	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
	}
	
	@Override
	protected void onPostResume() {
		// TODO Auto-generated method stub
		super.onPostResume();
		onTabChanged(TAB_PRODOTTI);
		mTabHost.setOnTabChangedListener(this);
	}
	
	
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		
		super.onRestoreInstanceState(savedInstanceState);
		if(savedInstanceState!=null){
			mTabHost.setCurrentTab(savedInstanceState.getInt(EXTRA_TAB_SELECTED, 0));
		}
	}
	
	@Override
	protected void onResumeFragments() {
		// TODO Auto-generated method stub
		super.onResumeFragments();
		
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
	
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		if (outState.isEmpty()) {
			outState.putString(INTENT_CUSTOMER_NAME, customer_name);
			outState.putString(INTENT_CUSTOMER_NUMBER, customer_no);
			outState.putLong(INTENT_SURVEY_ID, surveyId);
			outState.putInt(EXTRA_TAB_SELECTED, mTabHost.getCurrentTab());
		}
		super.onSaveInstanceState(outState);
	}

	@Override
	public void onTabChanged(String tabId) {
		if(tabId.equals(TAB_PRODOTTI)){
			mFieldTabName.setText(R.string.prodotti);
			
			FragmentProddoti fragmentProdotti = FragmentProddoti.getInstance(surveyId, 
												customer_no, salesPersonCode);
			updateTab(TAB_PRODOTTI,fragmentProdotti, R.id.tab_prodotti);
			
		}
		if(tabId.equals(TAB_PROMOZIONI)){
			mFieldTabName.setText(R.string.promozioni);
			
			resetProdottiFragment();
			FragmentPromozioni fragmentProdotti = FragmentPromozioni.getInstance(surveyId,
					customer_no, salesPersonCode);
			updateTab(TAB_PROMOZIONI, fragmentProdotti, R.id.tab_promozioni);
		}
		if(tabId.equals(TAB_CONCORRENZA)){
			mFieldTabName.setText(R.string.concorrenza);
			
			resetProdottiFragment();
			FragmentConcorrenza fragment = FragmentConcorrenza.getInstance(surveyId, salesPersonCode);
			updateTab(TAB_CONCORRENZA, fragment, R.id.tab_concorrenza);
		}
		if(tabId.equals(TAB_CHIUSURA)){
			mFieldTabName.setText(R.string.chiusura_attivita);
			
			resetProdottiFragment();
			
			FragmentChiusuraAttivita fragment = FragmentChiusuraAttivita.getInstance(surveyId,
					userName, customer_no,clusterPosition,clustChangeValue);
			
			updateTab(TAB_CHIUSURA, fragment, R.id.tab_chiusura_attivita);
		}
	}
	
//	private void setUpClusterTab() {
//		TextView fieldCluster = (TextView) findViewById(R.id.field_cluster);
//		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1
//				,android.R.id.text1,getResources().getStringArray(R.array.clusters));
//		setListPopUpWindow(fieldCluster, adapter);
//		
//	}
//	
//	private void setListPopUpWindow(final TextView field
//			,ArrayAdapter< ? extends Object> adapter){
//		final ListPopupWindow popup = new ListPopupWindow(this);
//		popup.setAdapter(adapter);
//		popup.setAnchorView(field);
//		field.setOnClickListener(new View.OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				popup.show();
//			}
//		});
//		
//		popup.setOnItemClickListener(new AdapterView.OnItemClickListener(){
//
//			@Override
//			public void onItemClick(AdapterView<?> parent, View view,
//					int position, long id) {
//				String cluster = parent.getAdapter()
//									.getItem(position)
//									.toString();
//				field.setText(cluster);
//			}
//			
//		});
//	}
	
	public void onSurveySent(int status) {
		
		if(status==FragmentChiusuraAttivita.SURVEY_SEND_SUCCESS_ONLINE){
			Intent intent = new Intent(this,SurveySuccessActivity.class);
			intent.putExtra(SurveySuccessActivity.INTENT_SENT_STATUS, SurveySuccessActivity.STATUS_ONLINE);
			startActivity(intent);
			finish();
		}
		else if(status==FragmentChiusuraAttivita.SURVEY_EMPTY){
			Util.showToast(mToast, R.string.msg_empty_survey);
		}
		else if(status==FragmentChiusuraAttivita.CURRENT_SURVEY_SEND_FAILED){
//			Util.showToast(mToast, R.string.error_sending);
			Intent intent = new Intent(this,SurveyFailActivity.class);
			startActivityForResult(intent, CALL_SURVEY_FAIL_ACTIVITY);
		}
		else if(status==FragmentChiusuraAttivita.SURVEY_SEND_SUCCESS_OFFLINE){
			Intent intent = new Intent(this,SurveySuccessActivity.class);
			intent.putExtra(SurveySuccessActivity.INTENT_SENT_STATUS, SurveySuccessActivity.STATUS_OFFLINE);
			startActivity(intent);
			finish();
		}
		else if(status==FragmentChiusuraAttivita.INVIA_DATI_SURVEY_FAILED){
			Intent intent = new Intent(this,SurveySuccessActivity.class);
			intent.putExtra(SurveySuccessActivity.INTENT_SENT_STATUS, SurveySuccessActivity.STATUS_DA_INVIARE_SURVEY_SENDING_FAILED);
			startActivity(intent);
			finish();
		}
		
	}
	
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v==mBtnEsc){
			Intent intent = new Intent(this, SurveySaveActivity.class);
			intent.putExtra(SurveySaveActivity.INTENT_SURVEY_ID, surveyId);
			intent.putExtra(SurveySaveActivity.INTENT_CLUSTER_VALUE, (cluster.getSelectedItemPosition())+1);
			startActivityForResult(intent, CALL_SURVEY_SAVE_ACTIVITY);
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, intent);
		if(requestCode==CALL_SURVEY_SAVE_ACTIVITY){
			if(resultCode==RESULT_OK){
				finish();
			}
		}
		if(requestCode==CALL_SURVEY_FAIL_ACTIVITY){
			if(resultCode==RESULT_OK){
				respondToSurveyFailActivity(intent);
			}
		}
	}
	


	@Override
	public void onBackPressed() {
		if(surveyId!=DEFAULT_SURVEY_ID){
			Intent intent = new Intent(this, SurveySaveActivity.class);
			intent.putExtra(SurveySaveActivity.INTENT_SURVEY_ID, surveyId);
			startActivityForResult(intent, CALL_SURVEY_SAVE_ACTIVITY);
		}
		else{
			finish();
		}
	}
	
	// ===========================================================
	// Methods
	// ===========================================================
	
	@SuppressLint("ShowToast")
	private void initialize() {
		// TODO Auto-generated method stub
		mFieldTabName = (TextView) findViewById(R.id.field_title);
		mFieldCustAddress = (TextView) findViewById(R.id.field_cust_address);
		mFieldCustDetails = (TextView) findViewById(R.id.field_cust_details);
		mBtnEsc = findViewById(R.id.btn_esci);
		
		mBtnEsc.setOnClickListener(this);

		mPrefs = getSharedPreferences(Consts.PREF_ELAH, MODE_PRIVATE);
		salesPersonCode = mPrefs.getString(LoginActivity.PREF_SALESPERSON, "");
		userName = mPrefs.getString(LoginActivity.PREF_USER_NAME, "");
		
				
		BaseAddress address = TableCustomers.getAddress(customer_no);
		
		mFieldCustAddress.setText(address.getAddress()+"-"+address.getCity());
		mFieldCustDetails.setText(customer_no+" - "+customer_name);
		
		mToast = Toast.makeText(this, "Sample", Toast.LENGTH_SHORT);
		Util.setUpToast(mToast);
		
		
	}
	
	private void setupTabs() {
	        mTabHost.setup(); // you must call this before adding your tabs!
	        
	        mTabHost.addTab(newTab(TAB_PRODOTTI, R.string.prodotti,
	        		R.drawable.icon_tab_prodotti, R.id.tab_prodotti));
	        
	        mTabHost.addTab(newTab(TAB_PROMOZIONI, R.string.promozioni,
	        		R.drawable.icon_tab_promizioni, R.id.tab_promozioni));
	        
	        mTabHost.addTab(newTab(TAB_CONCORRENZA, R.string.concorrenza,
	        		R.drawable.icon_tab_concorenzza, R.id.tab_concorrenza));
	        
	        mTabHost.addTab(newTab(TAB_CHIUSURA, R.string.chiusura_attivita,
	        		R.drawable.icon_tab_chisuura, R.id.tab_chiusura_attivita));
	 }
	 
	 
	 private TabSpec newTab(String tag, int labelId,int drawableId, int tabContentId) {
	        Log.d("Log", "buildTab(): tag=" + tag);
	        LayoutInflater inflater = LayoutInflater.from(this);
	        View indicator = inflater.inflate(R.layout.tab_icon, null);
	                
	        ((ImageView) indicator.findViewById(R.id.icon)).setImageResource(drawableId);
	        ((TextView) indicator.findViewById(R.id.label)).setText(labelId);
	 
	        TabSpec tabSpec = mTabHost.newTabSpec(tag);
	        tabSpec.setIndicator(indicator);
	        tabSpec.setContent(tabContentId);
	        return tabSpec;
	    }
	 
	    private void updateTab(String tabId,Fragment fragment,int placeholder) {
	        FragmentManager fm = getSupportFragmentManager();
	        if (fm.findFragmentByTag(tabId) == null) {
	            fm.beginTransaction()
	                    .replace(placeholder,fragment ,tabId)
	                    .commit();
	            
	        }
	    }
	    
	    private void resetProdottiFragment(){
	    	 FragmentManager fm = getSupportFragmentManager();
	    	 FragmentProddoti fragment = (FragmentProddoti) fm.findFragmentByTag(TAB_PRODOTTI);
	        if (fragment != null) {
//	            fragment.resetContentChanged();         
	        }
	    }
	    

		private void getIntentAndSetValues(Bundle bundle) {
			// TODO Auto-generated method stub
			if(bundle==null){
				customer_name = getIntent().getStringExtra(INTENT_CUSTOMER_NAME);
				customer_no = getIntent().getStringExtra(INTENT_CUSTOMER_NUMBER);
				surveyId = getIntent().getLongExtra(INTENT_SURVEY_ID, DEFAULT_SURVEY_ID);
			}
			else{
				customer_name = bundle.getString(INTENT_CUSTOMER_NAME);
				customer_no = bundle.getString(INTENT_CUSTOMER_NUMBER);
				surveyId = bundle.getLong(INTENT_SURVEY_ID, DEFAULT_SURVEY_ID);
			}
		}

		private void respondToSurveyFailActivity(Intent intent) {
			// TODO Auto-generated method stub
			FragmentChiusuraAttivita fragment = (FragmentChiusuraAttivita) getSupportFragmentManager().findFragmentByTag(TAB_CHIUSURA);
			if(fragment!=null){
				if(intent.getAction().equals(SurveyFailActivity.ACTION_RETRY)){
					fragment.sendCurrentSurvey();
				}
				if(intent.getAction().equals(SurveyFailActivity.ACTION_SEND_LATER)){
					TableSurvey.updateSurveySentDate(surveyId, Util.getCurrentDateTime(),clusterPosition,clustChangeValue);
					TableSurvey.updateFlag(surveyId,TableSurvey.FLAG_TO_BE_SENT);
					Intent action = new Intent(this,SurveySuccessActivity.class);
					action.putExtra(SurveySuccessActivity.INTENT_SENT_STATUS, SurveySuccessActivity.STATUS_SAVED_TO_DA_INVIARE);
					startActivity(action);
					finish();
				}
			}
		}
		

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
	 
}
