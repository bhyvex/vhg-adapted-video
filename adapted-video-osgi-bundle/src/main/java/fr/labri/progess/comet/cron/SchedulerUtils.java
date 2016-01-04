package fr.labri.progess.comet.cron;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

import java.util.Date;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;

import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.matchers.GroupMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.labri.progess.comet.model.Content;
import fr.labri.progess.comet.model.FilterConfig;

public abstract class SchedulerUtils {

	private static final Logger LOGGER = LoggerFactory.getLogger(SchedulerUtils.class);
	private static Scheduler scheduler;

	static {
		try {
			StdSchedulerFactory fact = new StdSchedulerFactory();

			Properties schedulerProps = new Properties();
			schedulerProps.setProperty(StdSchedulerFactory.PROP_SCHED_THREAD_NAME, "frontal-config");
			schedulerProps.setProperty("org.quartz.threadPool.threadCount", "1");
			fact.initialize(schedulerProps);
			scheduler = fact.getScheduler();

		} catch (SchedulerException e) {
			LOGGER.error("Failed to fire default scheduler, now configuration will be read from frontal", e);
			throw new RuntimeException(e);
		}
	}

	public static void setupScheduler(ConcurrentMap<String, Content> content, Set<FilterConfig> filterConfigs,
			String frontalHost, Integer frontalPort) {

		try {

			// first kill already existing jobs in case of reconfig
			GroupMatcher<JobKey> matcher = GroupMatcher.groupEquals("frontal");
			if (scheduler.getJobKeys(matcher) != null)
				for (JobKey key : scheduler.getJobKeys(matcher)) {
					scheduler.deleteJob(key);
					LOGGER.info("previous job {} has been deleted", key.getName());
				}

			if (content != null && frontalHost!=null && frontalPort!=null ) {// resource from frontal
				JobDetail job = newJob(UpdateCachedContentJob.class).withIdentity("availableResources", "frontal")
						.build();

				job.getJobDataMap().put("content-cache", content);
				job.getJobDataMap().put("frontalHost", frontalHost);
				job.getJobDataMap().put("frontalPort", frontalPort);

				Trigger trigger = newTrigger().withSchedule(simpleSchedule().withIntervalInSeconds(10).repeatForever())
						.startAt(new Date()).build();

				scheduler.scheduleJob(job, trigger);
			}

			if (filterConfigs != null && frontalHost!=null && frontalPort!=null) {// configuration from frontal
				JobDetail job = newJob(UpdateConfigJob.class).withIdentity("config", "frontal").build();
				job.getJobDataMap().put("filter-config", filterConfigs);
				job.getJobDataMap().put("frontalHost", frontalHost);
				job.getJobDataMap().put("frontalPort", frontalPort);

				Trigger trigger = newTrigger().withSchedule(simpleSchedule().withIntervalInSeconds(10).repeatForever())
						.startAt(new Date()).build();

				scheduler.scheduleJob(job, trigger);
			}

			if (!scheduler.isStarted())
				scheduler.start();
		} catch (SchedulerException e) {
			LOGGER.error("failed to start Scheduler", e);
			throw new RuntimeException(e);

		}
	}
}
