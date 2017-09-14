package com.eteam.dufour.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.eteam.dufour.database.tables.TableCompetitors;
import com.eteam.dufour.database.tables.TableCustomers;
import com.eteam.dufour.database.tables.TableItems;
import com.eteam.dufour.database.tables.TableList;
import com.eteam.dufour.database.tables.TableLogin;
import com.eteam.dufour.database.tables.TablePromotions;
import com.eteam.dufour.database.tables.TableSurvey;
import com.eteam.dufour.database.tables.TableSurveyAssortimento;
import com.eteam.dufour.database.tables.TableSurveyCompetitor;
import com.eteam.dufour.database.tables.TableSurveyCustomers;
import com.eteam.dufour.database.tables.TableSurveyPromotions;
import com.eteam.dufour.database.tables.TableSurveyPromotionsItem;
import com.eteam.utils.Consts;

public class ElahDBHelper extends SQLiteOpenHelper{
	
	// ===========================================================
	// Constants
	// ===========================================================
//	public static final String NAME_DB 	= "dufour.db";
	private static final int	VERSION_DB 	= 3;
	
	// ===========================================================
	// Fields
	// ===========================================================
	private static ElahDBHelper mInstance;
	private static SQLiteDatabase elahDB;
	
	// ===========================================================
	// Constructors
	// ===========================================================
	
	private ElahDBHelper(Context context) {
		super(context, Consts.DB_NAME, null, VERSION_DB);
		// TODO Auto-generated constructor stub
	}
	
	// ===========================================================
	// Getter & Setter
	// ===========================================================
   
	public static ElahDBHelper getInstance(Context ctx) {
        /** 
         * use the application context as suggested by CommonsWare.
         * this will ensure that you dont accidentally leak an Activitys
         * context (see this article for more information: 
         * http://developer.android.com/resources/articles/avoiding-memory-leaks.html)
         */
        if (mInstance == null) {
            mInstance = new ElahDBHelper(ctx.getApplicationContext());
        }
        return mInstance;
    }
	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub

		
	}
	
	//On DBVersion 2 MP and OP field where added to login table
	//Has to drop the login table and create new one on update
	
	//On DBVersion 3 taglioPrezzo fields are changed from free number to combo box. 
	//So update existing draft values to combo values and tobesent to oldtobesent
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.d("Log", "Updating");
		if(oldVersion<2){
			if(doesTableExist(db, TableLogin.TABLE_NAME)){
				db.execSQL("DROP TABLE "+TableLogin.TABLE_NAME);
			}
		}
		if(oldVersion<3){
			TableSurvey.convertToOldSurveys(db);
			TableSurveyPromotionsItem.convertDraftsTaglioToCombo(db);
		}
		
	}
	
	@Override
	public synchronized void close() {
		// TODO Auto-generated method stub
		super.close();
		if(elahDB!=null){
			elahDB.close();
			elahDB = null;
		}
	}
	

	
	// ===========================================================
	// Methods
	// ===========================================================
    public static boolean doesTableExist(ElahDBHelper dbHelper, String tblName){ 
        Cursor c = null; 
        try{ 
            c = dbHelper.getReadableDatabase().rawQuery("SELECT * FROM " + tblName , null ); 
            return true; 
        }catch(Exception ex){ 
            return false; 
        }finally{ 
            if (c != null) 
            	c.close(); 
        } 
    }
    
    public static boolean doesTableExist(SQLiteDatabase xdb, String tblName){ 
        Cursor c = null; 
        try{ 
            c = xdb.rawQuery("SELECT * FROM " + tblName , null ); 
            return true; 
        }catch(Exception ex){ 
            return false; 
        }finally{ 
            if (c != null) 
            	c.close(); 
        } 
    }
    
    public SQLiteDatabase getElahWriteAbleDB(){
    	if((elahDB==null)||!elahDB.isOpen()){
    		elahDB = getWritableDatabase();
    	}
    	return elahDB;
    }
    
    public static final void dropTable(ElahDBHelper dbHelper, String tblName){
    	dbHelper.getWritableDatabase().execSQL("drop table "+tblName);
    }
    
    public static final void createAllTables(ElahDBHelper dbHelper){
    	dbHelper.getWritableDatabase().execSQL(TableLogin.CREATE_TABLE);
    	dbHelper.getWritableDatabase().execSQL(TableSurveyAssortimento.CREATE_TABLE);
    	dbHelper.getWritableDatabase().execSQL(TableSurveyPromotions.CREATE_TABLE);
    	dbHelper.getWritableDatabase().execSQL(TableSurvey.CREATE_TABLE);
    	dbHelper.getWritableDatabase().execSQL(TableSurveyCompetitor.CREATE_TABLE);
    	dbHelper.getWritableDatabase().execSQL(TableSurveyPromotionsItem.CREATE_TABLE);
    }
    
    public static final void createAllDataTables(ElahDBHelper dbHelper){
    	SQLiteDatabase db = dbHelper.getWritableDatabase();
    	
    	db.execSQL(TableList.CREATE_TABLE);
    	db.execSQL(TableCustomers.CREATE_TABLE);
    	db.execSQL(TableSurveyCustomers.CREATE_TABLE);
    	db.execSQL(TablePromotions.CREATE_TABLE);
		db.execSQL(TableItems.CREATE_TABLE);
		db.execSQL(TableCompetitors.CREATE_TABLE);
    }
    
    public static final void dropAllDataTables(ElahDBHelper dbHelper){
    	SQLiteDatabase db = dbHelper.getWritableDatabase();
    	db.execSQL("DROP TABLE IF EXISTS "+TableList.TABLE_NAME);
    	db.execSQL("DROP TABLE IF EXISTS "+TableCustomers.TABLE_NAME);
    	db.execSQL("DROP TABLE IF EXISTS "+TableSurveyCustomers.TABLE_NAME);
    	db.execSQL("DROP TABLE IF EXISTS "+TablePromotions.TABLE_NAME);
    	db.execSQL("DROP TABLE IF EXISTS "+TableItems.TABLE_NAME);
    	db.execSQL("DROP TABLE IF EXISTS "+TableCompetitors.TABLE_NAME);
    }
    
//    public static final void deleteAllSurveyTables(ElahDBHelper dbHelper){
//    	dbHelper.getWritableDatabase().execSQL("drop table "+TableSurveyAssortimento.TABLE_NAME);
//    	dbHelper.getWritableDatabase().execSQL("drop table "+TableSurveyPromotions.TABLE_NAME);
//    	dbHelper.getWritableDatabase().execSQL("drop table "+TableSurvey.TABLE_NAME);
//    	dbHelper.getWritableDatabase().execSQL("drop table "+TableSurveyCompetitor.TABLE_NAME);
//    	dbHelper.getWritableDatabase().execSQL("drop table "+TableSurveyPromotionsItem.TABLE_NAME);
//    }
    
	public static final void closeConnections(){
		Log.d("Log", "Closing connections");
		if(mInstance!=null){
			mInstance.close();
		}
	}
   
	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
