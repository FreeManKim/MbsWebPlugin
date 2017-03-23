/**
 * Copyright (C) 2013-2014 EaseMob Technologies. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.mobisoft.mbswebplugin.proxy.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbOpenHelper extends SQLiteOpenHelper {

	private static final int DATABASE_VERSION = 4;
	private static DbOpenHelper instance;

	/**
	 * 创建webview的表
	 */
	private static final String CREAT_ARTE_TABLE = "CREATE TABLE "
			+ WebviewCaheDao.TABLE_NAME + " ("
			+ WebviewCaheDao.COLUMN_KEY + " TEXT PRIMARY KEY,"
			+ WebviewCaheDao.COLUMN_ACCOUNT + " TEXT,"
			+ WebviewCaheDao.COLUMN_JSON + " TEXT); ";
	
	private DbOpenHelper(Context context) {
		super(context, getUserDatabaseName(), null, DATABASE_VERSION);
	}

	private static String getUserDatabaseName() {
//		return SyncStateContract.Constants.ACCOUNT_NAME + "_demo.db";
		return  "web_cache.db";

	}

	public static DbOpenHelper getInstance(Context context) {
		if (instance == null) {
			instance = new DbOpenHelper(context.getApplicationContext());
		}
		return instance;
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREAT_ARTE_TABLE);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//		if(oldVersion < 2){
//		    db.execSQL("ALTER TABLE "+ UserDao.TABLE_NAME +" ADD COLUMN "+
//		            UserDao.COLUMN_NAME_AVATAR + " TEXT ;");
//		}
//
//		if(oldVersion < 3){
//		    db.execSQL(CREATE_PREF_TABLE);
//        }
//		if(oldVersion < 4){
//			db.execSQL(ROBOT_TABLE_CREATE);
//		}
	}
	
	public void closeDB() {
	    if (instance != null) {
	        try {
	            SQLiteDatabase db = instance.getWritableDatabase();
	            db.close();
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        instance = null;
	    }
	}
	
}
