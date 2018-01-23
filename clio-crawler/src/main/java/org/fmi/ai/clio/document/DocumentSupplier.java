package org.fmi.ai.clio.document;

import java.util.Iterator;
import java.util.function.Supplier;

import org.apache.solr.common.SolrInputDocument;

public interface DocumentSupplier extends Supplier<Iterator<SolrInputDocument>> {

}
