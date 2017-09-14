package com.eteam.dufour.net;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;

import com.eteam.utils.Consts;
import com.eteam.utils.HttpClientHelper;

public class ElahNetHelper {
	// Constants
	// ===========================================================
	
	private static final String SERVER_URL = Consts.SERVER_URL; 
	
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

	
	public static BaseSResponse executeSRequestResponse(BaseSyncRequest request
			,Class<? extends BaseSResponse> responseClass) 
			throws ClientProtocolException, IOException, JSONException{
		
		
		try {
			Class<?> clazz = Class.forName(responseClass.getName());
			Constructor<?> ctor = clazz.getConstructor(JSONObject.class);
			Object object = ctor.newInstance(new Object[] { new JSONObject(executeSIAPost(request))});
			return (BaseSResponse)object;
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		  
	}
	
	public static BaseResponse executeRequestResponse(BaseSyncRequest request
			,Class<? extends BaseResponse> responseClass) 
			throws ClientProtocolException, IOException, JSONException{
		
		
		try {
			Class<?> clazz = Class.forName(responseClass.getName());
			Constructor<?> ctor = clazz.getConstructor(JSONObject.class);
			Object object = ctor.newInstance(new Object[] {
					new JSONObject(executeSIAPost(request))});
			return (BaseResponse)object;
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		  
	}
	
	public static TaskNetHelper executeAsyncRequestResponse(BaseSyncRequest request
			,Class<? extends BaseResponse> responseClass,OnResponseListener listener) {
		return new TaskNetHelper(request, responseClass, listener);
		
		  
	}
	
	public static TaskBaseSNetHelper executeAsyncSRequestResponse(BaseSyncRequest request
			,Class<? extends BaseSResponse> responseClass,OnBaseSResponseListener listener) {
		return new TaskBaseSNetHelper(request, responseClass, listener);
		
		  
	}
	
	public interface OnResponseListener{
		public void onBegin();
		public void onSucess(BaseResponse response);
		public void onFailed();
	}
	
	public interface OnBaseSResponseListener{
		public void onBegin();
		public void onSucess(BaseSResponse response);
		public void onFailed();
	}
	
	// ===========================================================
	// Methods
	// ===========================================================
	
	private static String executeSIAPost(BaseSyncRequest request) 
			throws ClientProtocolException, IOException, JSONException{
		String url = SERVER_URL;
		if(request.isToBeAppendedToUrl()){
			url += "/reqFor"+request.getMethodName();
		}
		return HttpClientHelper.executeHttpPost(url
				, request.createRequest());
		
	}
	
//	private static String executeSIAPost(BaseRequest request) 
//			throws ClientProtocolException, IOException, JSONException{
//		return HttpClientHelper.executeHttpPost(SERVER_URL, request.createRequest());
//		
//	}
	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
	
	public static class TaskNetHelper extends AsyncTask<Void,Void,BaseResponse>{

		
		// ===========================================================
		// Constants
		// ===========================================================
		// ===========================================================
		// Fields
		// ===========================================================
		private BaseSyncRequest request;
		private Class<? extends BaseResponse> responseClass;
		private OnResponseListener listener;
		// ===========================================================
		// Constructors
		// ===========================================================
		
		private TaskNetHelper(){
			
		}
		
		private TaskNetHelper(BaseSyncRequest request
				,Class<? extends BaseResponse> responseClass
				,OnResponseListener listener) {
			this.request = request;
			this.responseClass = responseClass;
			this.listener = listener;
			
		}
		// ===========================================================
		// Getter & Setter
		// ===========================================================

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}
		
		// ===========================================================
		// Methods for/from SuperClass/Interfaces
		// ===========================================================
		@Override
		protected BaseResponse doInBackground(Void... params) {
			
			try {
				return (BaseResponse) executeRequestResponse(request, responseClass);
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
		
		@Override
		protected void onCancelled() {
			// TODO Auto-generated method stub
			super.onCancelled();
			listener.onFailed();
		}
		
		@Override
		protected void onPostExecute(BaseResponse result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if(result!=null){
				listener.onSucess(result);
			}
			else{
				listener.onFailed();
			}
		}
	}
	
	
	public static class TaskBaseSNetHelper extends AsyncTask<Void,Void,BaseSResponse>{

		
		// ===========================================================
		// Constants
		// ===========================================================
		// ===========================================================
		// Fields
		// ===========================================================
		private BaseSyncRequest request;
		private Class<? extends BaseSResponse> responseClass;
		private OnBaseSResponseListener listener;
		// ===========================================================
		// Constructors
		// ===========================================================
		
		private TaskBaseSNetHelper(){
			
		}
		
		private TaskBaseSNetHelper(BaseSyncRequest request
				,Class<? extends BaseSResponse> responseClass
				,OnBaseSResponseListener listener) {
			this.request = request;
			this.responseClass = responseClass;
			this.listener = listener;
			
		}
		// ===========================================================
		// Getter & Setter
		// ===========================================================

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}
		
		// ===========================================================
		// Methods for/from SuperClass/Interfaces
		// ===========================================================
		@Override
		protected BaseSResponse doInBackground(Void... params) {
			
			try {
				return (BaseSResponse) executeSRequestResponse(request, responseClass);
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
		
		@Override
		protected void onCancelled() {
			// TODO Auto-generated method stub
			super.onCancelled();
			listener.onFailed();
		}
		
		@Override
		protected void onPostExecute(BaseSResponse result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if(result!=null){
				listener.onSucess(result);
			}
			else{
				listener.onFailed();
			}
		}
	}
}
