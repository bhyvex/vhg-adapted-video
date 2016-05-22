package fr.labri.progess.comet.app;

import java.awt.image.Kernel;
import java.io.IOException;
import java.io.StringWriter;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadFactory;

import javax.inject.Singleton;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.strategies.SameThreadIOStrategy;
import org.glassfish.grizzly.threadpool.FixedThreadPool;
import org.glassfish.grizzly.threadpool.ThreadPoolConfig;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Throwables;
import com.google.common.collect.Lists;

import fr.labri.progess.comet.cron.SchedulerUtils;
import fr.labri.progess.comet.model.Content;
import fr.labri.progess.comet.model.ContentWrapper;

public class ConfigManager {

	private static Logger LOGGER = LoggerFactory.getLogger(ConfigManager.class);

	public ConfigManager() {

		// WEB APP SETUP

		// create a resource config that scans for JAX-RS resources and
		// providers
		// in nicolas package
		final ResourceConfig rc = new ResourceConfig().packages("fr.labri.progess.comet.endpoint");

		rc.register(new AbstractBinder() {
			@Override
			protected void configure() {
				bindFactory(ContextProvider.class).to(Context.class).in(Singleton.class);
			}
		});

		// create and start a new instance of grizzly http server
		// exposing the Jersey application at BASE_URI

		HttpServer server = GrizzlyHttpServerFactory.createHttpServer(URI.create("http://localhost:8888/"), rc, false);
		LOGGER.info("DEBUG API STARTED ON PORT 8888");
		ThreadPoolConfig poolConfig = ThreadPoolConfig.defaultConfig();
		poolConfig.setMaxPoolSize(1);
		poolConfig.setDaemon(false);
		poolConfig.setPoolName("config");
		poolConfig.setCorePoolSize(1);

		server.getListener("grizzly").getTransport().setWorkerThreadPoolConfig(poolConfig);

		ThreadPoolConfig kernelPoolConfig = ThreadPoolConfig.defaultConfig();
		poolConfig.setMaxPoolSize(1);
		poolConfig.setDaemon(false);
		poolConfig.setPoolName("kernel-config");
		poolConfig.setCorePoolSize(1);

		server.getListener("grizzly").getTransport().setKernelThreadPoolConfig(kernelPoolConfig);

		server.getListener("grizzly").getTransport().setIOStrategy(SameThreadIOStrategy.getInstance());
		server.getListener("grizzly").getTransport().setSelectorRunnersCount(1);

		// examples
		Content content = new Content();
		content.setCached(true);
		content.setCreated(new Date());
		content.setId(UUID.randomUUID().toString());

		content.setQualities(Arrays.asList("high", "low"));
		content.setUri("http://google.fr");
		content.setTargetUri("http://bing.fr");

		StringWriter strWriter = new StringWriter();
		try {
			JAXBContext.newInstance(ContentWrapper.class, Content.class).createMarshaller()
					.marshal(ContentWrapper.wraps(Arrays.asList(content)), strWriter);
		} catch (JAXBException e1) {

			e1.printStackTrace();
			throw Throwables.propagate(e1);
		}

		LOGGER.info("a content looks like this: {}", strWriter.getBuffer().toString());

		try {
			server.start();
		} catch (IOException e) {
			LOGGER.warn(
					"failed to launch config http server, remote configuration is not available and default value will be used");
			throw Throwables.propagate(e);
		}

		ContextProvider provider = new ContextProvider();
		SchedulerUtils.setupScheduler(provider.provide().getContent(), provider.provide().getConfigs(),
				provider.provide().getFrontalHostName(), provider.provide().getFrontalPort());

	}

}
