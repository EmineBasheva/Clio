package org.fmi.ai.clio.crawler.bgmama;

import org.junit.Assert;
import org.junit.Test;

public class BgmamaWebCrawlerTest {

	@Test
	public void testParseDate() {
		Assert.assertEquals(1516839600000L,
		      BgmamaWebCrawler.parsePostTimestamp("25 ян 2018, 02:20 ч."));
		Assert.assertEquals(1516839600000L,
		      BgmamaWebCrawler.parsePostTimestamp("25 ян. 2018, 02:20 ч."));
		Assert.assertEquals(1487693220000L,
		      BgmamaWebCrawler.parsePostTimestamp("21 фев. 2017, 18:07 ч."));
		Assert.assertEquals(1501350300000L,
		      BgmamaWebCrawler.parsePostTimestamp("29 юли 2017, 20:45 ч."));
		Assert.assertEquals(1410951600000L,
		      BgmamaWebCrawler.parsePostTimestamp("17 септ 2014, 14:00 ч"));
		Assert.assertEquals(1415790180000L,
		      BgmamaWebCrawler.parsePostTimestamp("12 ноем. 2014, 13:03 ч."));
	}
}
