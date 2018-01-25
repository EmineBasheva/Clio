package org.fmi.ai.clio.crawler.solr;

import java.util.Iterator;

import javax.inject.Inject;
import javax.inject.Provider;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.common.SolrInputDocument;
import org.fmi.ai.clio.crawler.bgmama.BgmamaTopicPost;
import org.fmi.ai.clio.crawler.bgmama.BgmamaTopicPostCollector;
import org.fmi.ai.clio.crawler.bgmama.BgmamaTopicPostProducer;
import org.fmi.ai.clio.crawler.bgmama.BgmamaTopicPostProducerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SolrTopicPostSupplyJob implements Runnable {

	private final static Logger LOG = LoggerFactory.getLogger(SolrTopicPostSupplyJob.class);

	private final Provider<SolrClient> sorlClientProvider;

	private final BgmamaTopicPostProducerFactory producerFactory;
	private final Provider<BgmamaTopicPostCollector> consumerProvider;

	@Inject
	public SolrTopicPostSupplyJob(Provider<SolrClient> sorlClientProvider,
	      BgmamaTopicPostProducerFactory producerFactory,
	      Provider<BgmamaTopicPostCollector> consumerProvider) {
		this.sorlClientProvider = sorlClientProvider;
		this.producerFactory = producerFactory;
		this.consumerProvider = consumerProvider;
	}

	@Override
	public void run() {
		LOG.info("Will be supplying topic posts...");

		BgmamaTopicPostCollector consumer = consumerProvider.get();
		BgmamaTopicPostProducer producer = producerFactory.create(consumer);

		try (SolrClient solr = sorlClientProvider.get()) {
			producer.start();

			solr.add(new Iterator<SolrInputDocument>() {
				@Override
				public SolrInputDocument next() {
					BgmamaTopicPost post = consumer.next();

					SolrInputDocument sid = new SolrInputDocument();
					sid.addField("url", post.getPostURL());
					sid.addField("text", post.getPostText());
					sid.addField("author", post.getAuthor());
					sid.addField("topic_name", post.getTopicName());
					sid.addField("author_posts", post.getAuthorPosts());
					sid.addField("thank_you_count", post.getThankYouCount());
					sid.addField("timestamp", post.getPostTimestamp());

					return sid;
				}

				@Override
				public boolean hasNext() {
					return consumer.hasNext();
				}
			});
			solr.commit();
		} catch (Exception e) {
			LOG.error("Could not feed the supplied documents to Solr", e);
			consumer.close();
		}
	}

}
