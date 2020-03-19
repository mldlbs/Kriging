package com.supermap.zq.main.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 解析从后得到的hdfs数据
 *
 * @author GZQ
 */
public class AnalysisDataUtil {

	public static Map<String, JSONArray> getGridData(String res) {
		Map<String, JSONArray> map = new HashMap<>();
		JSONObject job = JSON.parseObject(res);
		JSONArray temArray = JSON.parseArray(job.getString("temperature"));
		map.put("temp", temArray);
		JSONArray rfArray = JSON.parseArray(job.getString("rainfall"));
		map.put("water", rfArray);
		JSONArray hdArray = JSON.parseArray(job.getString("humidity"));
		map.put("hum", hdArray);
		JSONArray wsArray = JSON.parseArray(job.getString("windSpeed"));
		map.put("windy", wsArray);
		JSONArray apArray = JSON.parseArray(job.getString("airPressure"));
		map.put("press", apArray);
		return map;
	}

	public static Map<String, JSONArray> getTempGridData() {
		Map<String, JSONArray> map = new HashMap<>();
		//JSONArray temArray = JSON.parseArray(job.getString("temperature"));
		List<Double> micaps4Data = (List<Double>) FileUtil.Micaps4("micaps/1a2e42814bdd4b98b76281105b0df42e2019022710.000").get("Data");
		JSONArray temArray = JSONArray.parseArray(JSON.toJSONString(micaps4Data));
		map.put("temp", temArray);
		return map;
	}
}
