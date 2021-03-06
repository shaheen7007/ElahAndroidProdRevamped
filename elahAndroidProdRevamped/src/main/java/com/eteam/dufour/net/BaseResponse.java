package com.eteam.dufour.net;

import org.json.JSONException;
import org.json.JSONObject;

public class BaseResponse {
	// ===========================================================
	// Constants
	// ===========================================================
	
	// ===========================================================
	// Fields
	// ===========================================================
	protected	JSONObject object;
	// ===========================================================
	// Constructors
	// ===========================================================
	public  BaseResponse(JSONObject object){
		this.object = object;
	}
	// ===========================================================
	// Getter & Setter
	// ===========================================================
		public JSONObject getJSONObject() {
			return object;
		}
	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================
	
	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
