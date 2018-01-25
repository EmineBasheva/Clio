package org.fmi.ai.clio.crawler.bgmama;

import java.util.NoSuchElementException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class BgmamaTopicPostCollectorImpl implements BgmamaTopicPostCollector {

	private final BlockingQueue<BgmamaTopicPost> posts;
	private final AtomicBoolean isClosed;

	public BgmamaTopicPostCollectorImpl() {
		this.posts = new LinkedBlockingQueue<>();
		this.isClosed = new AtomicBoolean(false);
	}

	@Override
	public void add(BgmamaTopicPost post) {
		posts.add(post);
	}

	@Override
	public void close() {
		this.isClosed.compareAndSet(false, true);
	}

	@Override
	public boolean hasNext() {
		return !isClosed.get();
	}

	@Override
	public synchronized BgmamaTopicPost next() {
		if (isClosed.get()) {
			throw new NoSuchElementException("All posts were consumed");
		}

		try {
			BgmamaTopicPost post = posts.poll(30, TimeUnit.SECONDS);

			return post;
		} catch (InterruptedException e) {
			throw new IllegalStateException(
			      "Collector was not closed but no more posts are available", e);
		}
	}
}
