package org.fmi.ai.clio.crawler.bgmama;

public interface BgmamaTopicPostProducerFactory {

	BgmamaTopicPostProducer create(BgmamaTopicPostCollector collector);
}
