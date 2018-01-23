package org.fmi.ai.clio.crawler.document;

import java.util.Iterator;
import java.util.function.Supplier;

import org.apache.solr.common.SolrInputDocument;

public interface DocumentSupplier extends Supplier<Iterator<SolrInputDocument>> {

}
