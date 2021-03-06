package fr.labri.progess.comet.cron;

import java.util.concurrent.ConcurrentMap;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.labri.progess.comet.model.Content;
import fr.labri.progess.comet.model.ContentWrapper;

public class UpdateCachedContentJob extends FrontalJob implements Job {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(UpdateCachedContentJob.class);

	@Override
	public void execute(JobExecutionContext context)
			throws JobExecutionException {

		String frontalUrl = this.getFrontalConnection(context);
		try {
			@SuppressWarnings("unchecked")
			final ConcurrentMap<String, Content> content = (ConcurrentMap<String, Content>) context
					.getJobDetail().getJobDataMap().get("content-cache");

			WebTarget target = client.target(frontalUrl).path("api")
					.path("content");
			ContentWrapper wrapper = target.request(MediaType.APPLICATION_XML)
					.get(ContentWrapper.class);

			content.clear();
			for (Content con : wrapper.getContents()) {

				content.put(con.getUri(), con);

			}
		} catch (ProcessingException e) {
			LOGGER.warn("failed to access frontend on {}, I will retry later",
					frontalUrl);
		}
	}
}
