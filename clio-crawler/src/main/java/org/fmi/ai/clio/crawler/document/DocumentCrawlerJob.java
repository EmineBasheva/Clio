package org.fmi.ai.clio.crawler.document;

import java.io.IOException;
import java.util.Iterator;

import javax.inject.Inject;
import javax.inject.Provider;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrInputDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DocumentCrawlerJob implements Runnable {

	private final static Logger LOG = LoggerFactory.getLogger(DocumentCrawlerJob.class);

	private final DocumentSupplier documentSupplier;
	private final Provider<SolrClient> sorlClientProvider;

	@Inject
	public DocumentCrawlerJob(DocumentSupplier documentSupplier,
	      Provider<SolrClient> sorlClientProvider) {
		this.documentSupplier = documentSupplier;
		this.sorlClientProvider = sorlClientProvider;
	}

	@Override
	public void run() {
		LOG.info("Crawling...");

		try (SolrClient solr = sorlClientProvider.get()) {
			Iterator<SolrInputDocument> docIterator = documentSupplier.get();
			LOG.info("Obtained Solr documents {}", docIterator);
			solr.add(docIterator);

		} catch (SolrServerException | IOException | RuntimeException e) {
			LOG.error("Could not feed the supplied documents to Solr", e);
		}
	}

}
