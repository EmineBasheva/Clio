package org.fmi.ai.clio.crawler.solr;

import javax.inject.Inject;
import javax.inject.Provider;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.fmi.ai.clio.crawler.ClioModule.SolrURL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SolrClientProviderImpl implements Provider<SolrClient> {

	private final static Logger LOG = LoggerFactory.getLogger(SolrTopicPostSupplyJob.class);

	private final HttpSolrClient.Builder sorlClientBuilder;
	private final String solrUrl;

	@Inject
	public SolrClientProviderImpl(@SolrURL String sorlUrl) {
		this.sorlClientBuilder = new HttpSolrClient.Builder(sorlUrl);
		this.solrUrl = sorlUrl;
	}

	@Override
	public SolrClient get() {
		LOG.info("Building Solr client at {}", solrUrl);
		return sorlClientBuilder.build();
	}

}
