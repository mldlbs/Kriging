package com.supermap.zq.main.Analysis;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * 定时任务执行主类
 * @author GZQ
 */
public class AnalysisJob implements Job {
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        new DeleteRun().init();
        new AnalysisRun().init();
    }
}
