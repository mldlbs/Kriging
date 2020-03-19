package com.supermap.zq.main.Analysis;

import com.google.gson.JsonArray;
import com.supermap.data.*;
import com.supermap.zq.main.entitys.KrigingEntity;
import com.supermap.zq.main.utils.AnalysisDataUtil;
import com.supermap.zq.main.utils.AnalysisUtil;
import com.supermap.zq.main.utils.WorkspaceConnectionInfoUtil;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * 插值分析初始化类
 *
 * @author GZQ
 */
public class AnalysisRun implements Runnable {
	Properties prop = new Properties();
	private AnalysisUtil ku;

	{
		try {
			InputStream in = this.getClass().getClassLoader().getResourceAsStream("db.properties");
			prop.load(in);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public AnalysisRun() {
	}

	public AnalysisRun(AnalysisUtil ku) {
		this.ku = ku;
	}

	public AnalysisUtil getKu() {
		return ku;
	}

	public void setKu(AnalysisUtil ku) {
		this.ku = ku;
	}

	public void init() {

		WorkspaceConnectionInfo info = WorkspaceConnectionInfoUtil.getInstance();
		Map<String, Object> pointsMap = new AnalysisUtil(info, new Workspace()).getPoints(prop.getProperty("dataUrl"));
		//解析本地文件
		//Map<String, Object> pointsMap = new AnalysisUtil(info, new Workspace()).getTempPoints();
		this.initThread(this.auInit(info, "temp", pointsMap, prop));
		this.initThread(this.auInit(info, "water", pointsMap, prop));
		this.initThread(this.auInit(info, "hum", pointsMap, prop));
		this.initThread(this.auInit(info, "windy", pointsMap, prop));
		this.initThread(this.auInit(info, "press", pointsMap, prop));
	}

	public void initThread(AnalysisUtil au) {
		Thread thread = new Thread(new AnalysisRun(au));
		thread.start();
	}


	public AnalysisUtil auInit(WorkspaceConnectionInfo info, String type, Map<String, Object> pointsMap, Properties prop) {
		String[] zstr = prop.getProperty(type + "ZValues").split(",");
		double[] zdouble = new double[zstr.length];
		for (int i = 0; i < zstr.length; i++) {
			zdouble[i] = Double.parseDouble(zstr[i]);
		}
		AnalysisUtil ku = new AnalysisUtil(info, new Workspace());
		ku.setKn(new KrigingEntity((Point2D[]) pointsMap.get("points"), type, (double[]) pointsMap.get(type), zdouble));
		return ku;
	}

	@Override
	public void run() {
		ku.doAnalysis(ku.getKn());
	}

}

