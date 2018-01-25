package org.fmi.ai.clio.crawler.bgmama;

public class BgmamaTopicPost {

	private final String topicName;
	private final long postTimestamp;
	private final int thankYouCount;
	private final String postURL;
	private final String postText;

	private final String author;
	private final int authorPosts;

	private BgmamaTopicPost(String topicName, long postTimestamp, int thankYouCount,
	      String postURL, String postText, String author, int authorPosts) {
		this.topicName = topicName;
		this.postTimestamp = postTimestamp;
		this.thankYouCount = thankYouCount;
		this.postURL = postURL;
		this.postText = postText;
		this.author = author;
		this.authorPosts = authorPosts;
	}

	public String getTopicName() {
		return topicName;
	}

	public long getPostTimestamp() {
		return postTimestamp;
	}

	public int getThankYouCount() {
		return thankYouCount;
	}

	public String getPostURL() {
		return postURL;
	}

	public String getPostText() {
		return postText;
	}

	public String getAuthor() {
		return author;
	}

	public int getAuthorPosts() {
		return authorPosts;
	}

	public static class Builder {
		private String postText;

		private String topicName;
		private long postTimestamp;
		private int thankYouCount;
		private String postURL;

		private String author;
		private int authorPosts;

		private Builder(String topicName) {
			this.topicName = topicName;
		}

		public static Builder forTopic(String topicName) {
			return new Builder(topicName);
		}

		public Builder fromAuthor(String author) {
			this.author = author;
			return this;
		}

		public Builder at(long timestamp) {
			this.postTimestamp = timestamp;
			return this;
		}

		public Builder withURL(String postURL) {
			this.postURL = postURL;
			return this;
		}

		public Builder withThankYouCount(int thankYouCount) {
			this.thankYouCount = thankYouCount;
			return this;
		}

		public Builder withAuthorPosts(int authorPostsCount) {
			this.authorPosts = authorPostsCount;
			return this;
		}

		public Builder withText(String text) {
			this.postText = text;
			return this;
		}

		public BgmamaTopicPost build() {
			if (postText == null || postURL == null) {
				throw new IllegalArgumentException(
				      "Missing required parameters. Make sure the the posts has text or URL.");
			}

			return new BgmamaTopicPost(topicName, postTimestamp, thankYouCount, postURL,
			      postText, author, authorPosts);
		}
	}
}
