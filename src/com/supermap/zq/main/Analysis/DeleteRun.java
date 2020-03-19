package com.supermap.zq.main.Analysis;

import com.supermap.data.Point2D;
import com.supermap.data.Workspace;
import com.supermap.data.WorkspaceConnectionInfo;
import com.supermap.data.WorkspaceType;
import com.supermap.zq.main.entitys.KrigingEntity;
import com.supermap.zq.main.utils.AnalysisUtil;
import com.supermap.zq.main.utils.WorkspaceConnectionInfoUtil;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

/**
 * 插值分析初始化类
 *
 * @author GZQ
 */
public class DeleteRun implements Runnable {
    private AnalysisUtil ku;

    public DeleteRun() {
    }

    public DeleteRun(AnalysisUtil ku) {
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
        Thread delThread = new Thread(new DeleteRun(new AnalysisUtil(info, new Workspace())));
        delThread.start();
    }

    @Override
    public void run() {
        ku.doDelete();
    }

    public static void main(String[] args) {
        new DeleteRun().init();
    }

}

