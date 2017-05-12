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

public class WebviewCaheDao {
	/**
	 * webview的表名
	 */
	public static final String TABLE_NAME = "cachedao";
	/**
	 *  主键 id
	 */
	public static final String COLUMN_ACCOUNT = "account";
	/**
	 * webview 网络数据的key
	 */
	public static final String COLUMN_KEY = "key";
	/**
	 * webview 网络数据的json
	 */
	public static final String COLUMN_JSON = "value";

	public WebviewCaheDao(Context context) {
	    DemoDBManager.getInstance().onInit(context);
	}

	/**
	 * 保存 webview 到 数据库
	 * @param account  传入唯一 webview的标识
	 * @param key  传入webview 的key
	 * @param json  传入webview的json
	 */
	public void saveUrlPath(String account, String key, String json) {
	    DemoDBManager.getInstance().saveUrlPath(account,key, json);

	}

	public void deleteWebviewList(String account, String key){
		DemoDBManager.getInstance().deletWebviewList(account,key);
	}


	public void deletKey(String column_key){
		DemoDBManager.getInstance().deletKey(column_key);
	}

	public int  getCount(){
		 return DemoDBManager.getInstance().getCount();
	}
	/**
	 *获取缓存
	 * @param account
	 * @param key
     * @return
     */
	public String getUrlPath(String account, String key) {
	   return DemoDBManager.getInstance().getUrlPath(account,key);
	}

	/**
	 * 获取缓存
	 ** @param key
	 * @return
	 */
	public String getUrlPath(String key) {
		return DemoDBManager.getInstance().getUrlPath(key);
	}

}
