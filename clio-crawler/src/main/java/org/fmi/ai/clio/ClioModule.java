package org.fmi.ai.clio;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.apache.solr.client.solrj.SolrClient;
import org.fmi.ai.clio.document.DocumentSupplier;
import org.fmi.ai.clio.document.DocumentSupplierImpl;
import org.fmi.ai.clio.solr.SolrClientProviderImpl;

import com.google.inject.AbstractModule;
import com.google.inject.BindingAnnotation;
import com.google.inject.Provides;

public class ClioModule extends AbstractModule {

	private static final String SOLR_URL_PROPERTY = "SOLR_URL";

	@Override
	protected void configure() {
		bind(SolrClient.class).toProvider(SolrClientProviderImpl.class);
		bind(DocumentSupplier.class).to(DocumentSupplierImpl.class);
	}

	@SolrURL
	@Provides
	public String provideSolrURL() {
		return Systems.getRequiredStringProperty(SOLR_URL_PROPERTY);
	}

	@Retention(RetentionPolicy.RUNTIME)
	@Target({ ElementType.FIELD, ElementType.PARAMETER, ElementType.METHOD })
	@BindingAnnotation
	public @interface SolrURL {
	}
}
