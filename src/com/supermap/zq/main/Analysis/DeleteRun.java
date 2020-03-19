package com.supermap.zq.main.Analysis;

import com.supermap.data.Workspace;
import com.supermap.data.WorkspaceConnectionInfo;
import com.supermap.data.WorkspaceType;
import com.supermap.zq.main.utils.AnalysisUtil;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class DeleteRun implements Runnable {
	private AnalysisUtil ku;

	public DeleteRun() {
	}

	public DeleteRun(AnalysisUtil ku) {
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
		Thread delThread = new Thread(new DeleteRun(new AnalysisUtil(info, new Workspace())));
		delThread.start();
	}

	public void run() {
		this.ku.doDelete();
	}

	public static void main(String[] args) {
		(new DeleteRun()).init();
	}
}
