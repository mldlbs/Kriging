/* 
 * All content copyright Terracotta, Inc., unless otherwise indicated. All rights reserved. 
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not 
 * use this file except in compliance with the License. You may obtain a copy 
 * of the License at 
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0 
 *   
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT 
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the 
 * License for the specific language governing permissions and limitations 
 * under the License.
 * 
 */
 
package com.supermap.zq.main.Analysis;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

import static org.quartz.DateBuilder.evenMinuteDate;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

/**
 * Quartz.
 * @author GZQ
 */
public class AnalystTask {

  public AnalystTask() {
  }

  public void run() throws Exception {
    Logger log = LoggerFactory.getLogger(AnalystTask.class);
    log.info("------- 初始化....-------------");
    SchedulerFactory sf = new StdSchedulerFactory();
    Scheduler sched = sf.getScheduler();
    log.info("------- 初始化完成 -----------");
    Date runTime = DateBuilder.evenMinuteDate(new Date());
    log.info("------- 调度作业  --------");
    JobDetail job = JobBuilder.newJob(AnalysisJob.class).withIdentity("job1", "group1").build();
    Trigger trigger = TriggerBuilder.newTrigger().withIdentity("trigger1", "group1").withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInMinutes(60).repeatForever()).build();
    sched.scheduleJob(job, trigger);
    log.info(job.getKey() + " will run at: " + runTime);
    sched.start();
    log.info("------- 开始任务调度  --------");
  }

  public static void main(String[] args) throws Exception {
    AnalystTask task = new AnalystTask();
    task.run();
  }
}
