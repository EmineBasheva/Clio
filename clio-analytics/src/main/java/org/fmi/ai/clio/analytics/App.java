package org.fmi.ai.clio.analytics;

import java.io.IOException;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
//import org.apache.solr.client.solrj.impl.CommonsHttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocumentList;


public class App 
{
    public static void main( String[] args )
    {
//    	CommonsHttpSolrServer solr = new CommonsHttpSolrServer(
//			"http://localhost:8983/solr/person");
//
//		SolrQuery query = new SolrQuery();
//		query.setQuery("*:*");
//		query.addFilterQuery("name : tester1");
//
//		QueryResponse response = solr.query(query);
//		SolrDocumentList results = response.getResults();
//		
//		//iterate the results
//		for (int i = 0; i < results.size(); ++i) {
//			System.out.println(results.get(i));
//		}
        System.out.println( "Hello World!" );
    }
}
