package com.htong.util;

import java.util.List;

import net.sf.json.JSONObject;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public class List2DBObjectUtil<V> {
	public DBObject List2DBObject(List<V> list){  
		DBObject dbObject = new BasicDBObject();
		for(V object:list) {
			DBObject dbo = new BasicDBObject();
			JSONObject json = JSONObject.fromObject(object);
			for(Object key : json.keySet()) {
				if(json.get(key) != null) {
					dbo.put((String) key, json.get(key));
				}
			}
			
		} 
		return null;
	}
}
