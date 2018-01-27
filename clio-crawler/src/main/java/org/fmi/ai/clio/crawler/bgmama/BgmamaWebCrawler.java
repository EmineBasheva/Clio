package org.fmi.ai.clio.crawler.bgmama;

import java.io.IOException;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.fmi.ai.clio.crawler.ClioModule.OriginCrawlURL;
import org.fmi.ai.clio.crawler.bgmama.BgmamaTopicPost.Builder;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.assistedinject.Assisted;

public class BgmamaWebCrawler implements BgmamaTopicPostProducer {

	private final static Logger LOG = LoggerFactory.getLogger(BgmamaWebCrawler.class);

	private static final String HTML_BOARD_TOPIC_CLASS = "board-topic";
	private static final String HTML_TOPIC_TITLE_CLASS = "topic-title";

	private static final String HTML_ABSOLUTE_HREF_ATTR = "abs:href";

	private final BgmamaTopicPostCollector collector;
	private final String originURL;

	@Inject
	public BgmamaWebCrawler(@OriginCrawlURL String originURL,
	      @Assisted BgmamaTopicPostCollector collector) {
		this.originURL = originURL;
		this.collector = collector;
	}

	@Override
	public void start() throws BgmamaTopicPostProducerException {
		try {
			Document originDoc = connecTo(originURL);
			List<BoardTopicMetadata> boardTopics = getBoardTopics(originDoc);

			for (BoardTopicMetadata boardTopicMetadata : boardTopics) {
				processTopicPosts(boardTopicMetadata);
			}
		} finally {
			collector.close();
		}
	}

	private void processTopicPosts(BoardTopicMetadata boardTopicMetadata) {
		Document topicDoc = connecToSafe(boardTopicMetadata.getUrl());
		if (topicDoc == null) {
			return;
		}

		Elements postsElements = topicDoc.getElementsByClass("topic-post");
		for (Element postElement : postsElements) {
			processTopicPost(boardTopicMetadata.getTitle(), postElement);
		}
	}

	private void processTopicPost(String topicTitle, Element postElement) {
		String postAuthor = postElement.getElementsByClass("user-name").text();

		String postURL = postElement.getElementsByClass("post-link")
		      .attr(HTML_ABSOLUTE_HREF_ATTR);

		if (StringUtils.isBlank(postURL)) {
			return;
		}
		
		long postTimestamp = parsePostTimestamp(
		      postElement.getElementsByClass("post-date").text());

		int authorPostsCount = Integer.parseInt(
		      postElement.getElementsByClass("posts-count").text().replaceAll("\\D+", ""));

		String postThankYouCount = postElement.getElementsByClass("thank-you-count").text();

		Elements postContentElement = postElement.getElementsByClass("post-content-inner");
		postContentElement.select("div").remove();

		Builder builder = BgmamaTopicPost.Builder.forTopic(topicTitle)
		      .withText(postContentElement.text()).fromAuthor(postAuthor).withURL(postURL)
		      .at(postTimestamp).withAuthorPosts(authorPostsCount);

		if (StringUtils.isNotBlank(postThankYouCount)) {
			builder.withThankYouCount(Integer.parseInt(postThankYouCount));
		}

		LOG.info("{} - Processed a post by {} in topic {} at {}", LocalDateTime.now(), postAuthor, topicTitle,
		      postURL);
		collector.add(builder.build());
	}

	private static List<BoardTopicMetadata> getBoardTopics(Document doc) {
		List<BoardTopicMetadata> topicsMetadata = new ArrayList<>();

		Elements topicElements = doc.getElementsByClass(HTML_BOARD_TOPIC_CLASS);
		for (Element topicElement : topicElements) {
			Elements topicTitleElement = topicElement
			      .getElementsByClass(HTML_TOPIC_TITLE_CLASS);

			String topicTitle = topicTitleElement.text();
			String topicURL = topicTitleElement.select("a").attr(HTML_ABSOLUTE_HREF_ATTR);

			topicsMetadata.add(new BoardTopicMetadata(topicURL, topicTitle));
		}

		return topicsMetadata;
	}

	private Document connecToSafe(String url) {
		try {
			return connecTo(url);
		} catch (BgmamaTopicPostProducerException e) {
			LOG.warn("Could not connect to {}", url);
			return null;
		}
	}

	private static Document connecTo(String url) throws BgmamaTopicPostProducerException {
		try {
			return Jsoup.connect(url).get();
		} catch (IOException | IllegalArgumentException e) {
			throw new BgmamaTopicPostProducerException("Could not connect to  " + url, e);
		}
	}

	private static class BoardTopicMetadata {

		private final String url;
		private final String title;

		public BoardTopicMetadata(String url, String title) {
			this.url = url;
			this.title = title;
		}

		public String getUrl() {
			return url;
		}

		public String getTitle() {
			return title;
		}
	}

	static long parsePostTimestamp(String date) {
		date = date.replaceAll("\\.", "");

		String[] months = { "ян", "фев", "мар", "апр", "май", "юни", "юли", "авг", "септ",
		      "окт", "ноем", "дек" };
		Locale bg = new Locale("bg");

		DateFormatSymbols symbols = DateFormatSymbols.getInstance(bg);
		symbols.setShortMonths(months);

		SimpleDateFormat format = new SimpleDateFormat("dd MMM yyyy',' HH:mm 'ч'", bg);
		format.setDateFormatSymbols(symbols);

		try {
			return format.parse(date).getTime();
		} catch (ParseException e) {
			LOG.warn("Could not parse post date {}!", date, e);
		}

		return 0L;
	}
}