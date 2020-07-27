package com.sewage.springboot.dao;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Repository;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * 根据json文件获取站点信息
 *
 */
@Repository
public class SiteDetailDao {

	protected final String FILE_PATH = "siteDetails/siteDetails.json";

	public JSONArray readSiteDetailJsonFile() {
		File siteFile = new File(FILE_PATH);
		String input = "[]";
		try {
			input = FileUtils.readFileToString(siteFile, "UTF-8");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return JSONArray.parseArray(input);
	}

	public JSONObject getSiteDetailById(String siteID){
		JSONArray jsonArray = readSiteDetailJsonFile();
		JSONObject site = new JSONObject();
		for (int i = 0; i < jsonArray.size(); i++) {
			String id = jsonArray.getJSONObject(i).getString("id");
			if(id==null || id.trim().isEmpty()) continue;
			if (id.equals(siteID)) {
				site = jsonArray.getJSONObject(i);
			}
		}
		return site;
	}
}
