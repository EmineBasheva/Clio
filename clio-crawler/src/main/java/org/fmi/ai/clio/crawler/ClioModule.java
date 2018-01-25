package org.fmi.ai.clio.crawler;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.apache.solr.client.solrj.SolrClient;
import org.fmi.ai.clio.crawler.bgmama.BgmamaTopicPostCollector;
import org.fmi.ai.clio.crawler.bgmama.BgmamaTopicPostCollectorImpl;
import org.fmi.ai.clio.crawler.bgmama.BgmamaTopicPostProducer;
import org.fmi.ai.clio.crawler.bgmama.BgmamaTopicPostProducerFactory;
import org.fmi.ai.clio.crawler.bgmama.BgmamaWebCrawler;
import org.fmi.ai.clio.crawler.solr.SolrClientProviderImpl;

import com.google.inject.AbstractModule;
import com.google.inject.BindingAnnotation;
import com.google.inject.assistedinject.FactoryModuleBuilder;

public class ClioModule extends AbstractModule {

	private static final String CRAWL_ORIGIN_URL_PROPERTY = "CRAWL_ORIGIN_URL";
	private static final String SOLR_URL_PROPERTY = "SOLR_URL";

	@Override
	protected void configure() {
		bindConstant().annotatedWith(OriginCrawlURL.class)
		      .to(Systems.getRequiredStringProperty(CRAWL_ORIGIN_URL_PROPERTY));
		bindConstant().annotatedWith(SolrURL.class)
		      .to(Systems.getRequiredStringProperty(SOLR_URL_PROPERTY));

		bind(SolrClient.class).toProvider(SolrClientProviderImpl.class);
		bind(BgmamaTopicPostCollector.class).to(BgmamaTopicPostCollectorImpl.class);

		install(new FactoryModuleBuilder()
		      .implement(BgmamaTopicPostProducer.class, BgmamaWebCrawler.class)
		      .build(BgmamaTopicPostProducerFactory.class));
	}

	@Retention(RetentionPolicy.RUNTIME)
	@Target({ ElementType.FIELD, ElementType.PARAMETER, ElementType.METHOD })
	@BindingAnnotation
	public @interface SolrURL {
	}

	@Retention(RetentionPolicy.RUNTIME)
	@Target({ ElementType.FIELD, ElementType.PARAMETER, ElementType.METHOD })
	@BindingAnnotation
	public @interface OriginCrawlURL {
	}
}
