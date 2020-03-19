package com.supermap.zq.main.utils;

import com.supermap.data.DatasetVector;
import com.supermap.data.Datasource;
import com.supermap.data.Workspace;
import com.supermap.data.WorkspaceConnectionInfo;

/**
 * 单实例打开工作空间
 *
 */
public class WorkSpaceUtil {

    //工作空间
    private Workspace workspace;
    //数据源
    private Datasource datasource;
    //分析空间数据
    private DatasetVector analysisDataset;

    public Workspace getWorkspace() {
        return workspace;
    }

    public void setWorkspace(Workspace workspace) {
        this.workspace = workspace;
    }

    public Datasource getDatasource() {
        return datasource;
    }

    public void setDatasource(Datasource datasource) {
        this.datasource = datasource;
    }

    public DatasetVector getAnalysisDataset() {
        return analysisDataset;
    }

    public void setAnalysisDataset(DatasetVector analysisDataset) {
        this.analysisDataset = analysisDataset;
    }

    public DatasetVector getClipDataset() {
        return clipDataset;
    }

    public void setClipDataset(DatasetVector clipDataset) {
        this.clipDataset = clipDataset;
    }

    //裁剪空间数据
    private DatasetVector clipDataset;

    private WorkSpaceUtil(WorkspaceConnectionInfo info, Workspace workspace) {
        this.workspace = workspace;
        this.workspace.open(info);
        this.datasource = workspace.getDatasources().get(0);
        this.analysisDataset = (DatasetVector) datasource.getDatasets().get("方格点面");
        this.clipDataset = (DatasetVector) datasource.getDatasets().get("石嘴山市");
    }

    private WorkSpaceUtil() {
    }

    private static volatile WorkSpaceUtil instance;

    /**
     * 获取 WorkSpaceUtil对象
     *
     * @return
     */
    public static WorkSpaceUtil getInstance(WorkspaceConnectionInfo info, Workspace workspace) {
        if (instance == null) {
            synchronized (WorkSpaceUtil.class) {
                if (instance == null) {
                    instance = new WorkSpaceUtil(info, workspace);
                }
            }
        }
        return instance;
    }

}
