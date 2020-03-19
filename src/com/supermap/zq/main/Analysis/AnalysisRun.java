package com.supermap.zq.main.Analysis;

import com.supermap.data.Point2D;
import com.supermap.data.Workspace;
import com.supermap.data.WorkspaceConnectionInfo;
import com.supermap.data.WorkspaceType;
import com.supermap.zq.main.entitys.KrigingEntity;
import com.supermap.zq.main.utils.AnalysisUtil;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

public class AnalysisRun implements Runnable {
	private AnalysisUtil ku;

	public AnalysisRun() {
	}

	public AnalysisRun(AnalysisUtil ku) {
		this.ku = ku;
	}

	public AnalysisUtil getKu() {
		return this.ku;
	}

	public void setKu(AnalysisUtil ku) {
		this.ku = ku;
	}

	public void init() {
		Properties prop = new Properties();
		try {
			InputStream in = this.getClass().getClassLoader().getResourceAsStream("db.properties");
			prop.load(in);
		} catch (IOException var4) {
			var4.printStackTrace();
		}
		WorkspaceConnectionInfo info = new WorkspaceConnectionInfo();
		info.setType(WorkspaceType.ORACLE);
		info.setServer(prop.getProperty("server"));
		info.setName(prop.getProperty("name"));
		info.setUser(prop.getProperty("user"));
		info.setPassword(prop.getProperty("password"));
		Map<String, Object> pointsMap = (new AnalysisUtil(info, new Workspace())).getPoints(prop.getProperty("dataUrl"));
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

		for(int i = 0; i < zstr.length; ++i) {
			zdouble[i] = Double.parseDouble(zstr[i]);
		}

		AnalysisUtil ku = new AnalysisUtil(info, new Workspace());
		ku.setKn(new KrigingEntity((Point2D[])((Point2D[])pointsMap.get("points")), type, (double[])((double[])pointsMap.get(type)), zdouble));
		return ku;
	}

	public void run() {
		this.ku.doAnalysis(this.ku.getKn());
	}
}
