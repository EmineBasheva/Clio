package org.fmi.ai.clio.crawler.bgmama;

import java.util.Iterator;

public interface BgmamaTopicPostCollector extends Iterator<BgmamaTopicPost> {

	public void add(BgmamaTopicPost post);

	public void close();

	public boolean hasAny();
}
