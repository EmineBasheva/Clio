package org.fmi.ai.clio.crawler.document;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.SolrInputField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DocumentSupplierImpl implements DocumentSupplier {
	private final static Logger LOG = LoggerFactory.getLogger(DocumentSupplierImpl.class);

	@Override
	public Iterator<SolrInputDocument> get() {
		LOG.info("Retrieving Solr documents...");
		Map<String, SolrInputField> solrDocumentFields = new HashMap<>();

		return Collections.singletonList(new SolrInputDocument(solrDocumentFields))
		      .iterator();
	}

}
