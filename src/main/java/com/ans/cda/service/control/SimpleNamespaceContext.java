package com.ans.cda.service.control;

/**
 * SimpleNamespaceContext
 * 
 * @author Nizar Ben Salem
 */
public class SimpleNamespaceContext implements javax.xml.namespace.NamespaceContext {

	/**
	 * prefixToUri
	 */
	private final java.util.Map<String, String> prefixToUri = new java.util.HashMap<>();

	/**
	 * addNamespace
	 * 
	 * @param prefix
	 * @param uri
	 */
	public void addNamespace(String prefix, String uri) {
		prefixToUri.put(prefix, uri);
	}

	/**
	 * getNamespaceURI
	 */
	public String getNamespaceURI(String prefix) {
		return prefixToUri.getOrDefault(prefix, javax.xml.XMLConstants.NULL_NS_URI);
	}

	/**
	 * getPrefix
	 */
	public String getPrefix(String uri) {
		for (java.util.Map.Entry<String, String> entry : prefixToUri.entrySet()) {
			if (entry.getValue().equals(uri)) {
				return entry.getKey();
			}
		}
		return null;
	}

	/**
	 * getPrefixes
	 */
	public java.util.Iterator<String> getPrefixes(String uri) {
		return prefixToUri.keySet().iterator();
	}
}
