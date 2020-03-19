package com.supermap.zq.main.utils;

import com.supermap.data.Workspace;
import com.supermap.data.WorkspaceConnectionInfo;
import com.supermap.data.WorkspaceType;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class WorkspaceConnectionInfoUtil {
	private static volatile WorkspaceConnectionInfo instance;
	public static WorkspaceConnectionInfo getInstance() {
		if (instance == null) {
			synchronized (WorkspaceConnectionInfoUtil.class) {
				if (instance == null) {
					Properties prop = new Properties();
					try {
						InputStream in = WorkspaceConnectionInfoUtil.class.getClassLoader().getResourceAsStream("db.properties");
						prop.load(in);
					} catch (IOException e) {
						e.printStackTrace();
					}
					instance = new WorkspaceConnectionInfo();
					instance.setType(WorkspaceType.ORACLE);
					instance.setServer(prop.getProperty("server"));
					instance.setName(prop.getProperty("name"));
					instance.setUser(prop.getProperty("user"));
					instance.setPassword(prop.getProperty("password"));
				}
			}
		}
		return instance;
	}
}
