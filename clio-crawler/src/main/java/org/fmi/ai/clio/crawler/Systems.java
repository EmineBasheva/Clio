package org.fmi.ai.clio;

import org.apache.commons.lang3.StringUtils;

public final class Systems {

	private Systems() {
	}

	public static String getRequiredStringProperty(String propertyName) {
		String propertyValue = getRequired(propertyName);

		return propertyValue;
	}

	public static long getRequiredLongProperty(String propertyName) {
		String propertyValue = getRequired(propertyName);

		return Long.parseLong(propertyValue);
	}

	private static String getRequired(String propertyName) {
		String propertyValue = System.getProperty(propertyName);

		if (StringUtils.isBlank(propertyValue)) {
			throw new IllegalStateException("Missing required property: " + propertyName);
		}

		return propertyValue;
	}
}
