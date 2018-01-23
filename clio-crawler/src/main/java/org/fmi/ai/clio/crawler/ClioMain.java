package org.fmi.ai.clio.crawler;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.fmi.ai.clio.crawler.document.DocumentCrawlerJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class ClioMain {
	private final static Logger LOG = LoggerFactory.getLogger(ClioMain.class);

	private static final String CRAWLER_INITIAL_BACKOFF_SECONDS_PROPERTY = "CRAWLER_INITIAL_BACKOFF_SECONDS";
	private static final String CRAWLER_PERIOD_SECONDS_PROPERTY = "CRAWLER_PERIOD_SECONDS";

	public static void main(String[] args) throws InterruptedException {
		Injector injector = Guice.createInjector(new ClioModule());

		long initialBackoff = Systems
		      .getRequiredLongProperty(CRAWLER_INITIAL_BACKOFF_SECONDS_PROPERTY);
		long period = Systems.getRequiredLongProperty(CRAWLER_PERIOD_SECONDS_PROPERTY);

		DocumentCrawlerJob crawlerJob = injector.getInstance(DocumentCrawlerJob.class);

		ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
		executorService.scheduleAtFixedRate(crawlerJob, initialBackoff, period,
		      TimeUnit.SECONDS);

		while (true) {
			LOG.info("Clio is still running...");
			Thread.sleep(TimeUnit.MINUTES.toMillis(1));
		}
	}

}
