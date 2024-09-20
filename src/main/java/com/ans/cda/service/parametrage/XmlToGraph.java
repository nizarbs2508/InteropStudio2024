package com.ans.cda.service.parametrage;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.scene.control.MultipleSelectionModel;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

/**
 * XmlToGraph
 * 
 * @author Nizar Ben Salem
 */
public class XmlToGraph {
	/**
	 * Logger
	 */
	private static final Logger LOG = Logger.getLogger(XmlToGraph.class);

	/**
	 * loadXmlDocument
	 * 
	 * @param filePath
	 * @return
	 */
	public static Document loadXmlDocument(final String filePath) {
		try {
			final File file = new File(filePath);
			final DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			final DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			final Document document = dBuilder.parse(file);
			document.getDocumentElement().normalize();
			return document;
		} catch (final Exception e) {
			if (LOG.isInfoEnabled()) {
				final String error = e.getMessage();
				LOG.error(error);
			}
			return null;
		}
	}

	/**
	 * expandAll
	 * 
	 * @param item
	 */
	public static void expandAll(final TreeItem<String> item) {
		if (item == null)
			return;
		item.setExpanded(true);
		for (final TreeItem<String> child : item.getChildren()) {
			if (child.getValue().contains("ClinicalDocument") || child.getValue().contains("ns5:SubmitObjectsRequest")
					|| child.getValue().contains("RegistryObjectList")) {
				expandAll(child);
			}
		}
	}

	/**
	 * parseXmlToTreeItem
	 * 
	 * @param filePath
	 * @return
	 */
	public static TreeItem<String> parseXmlToTreeItem(final String filePath, final TreeView<String> treeView) {
		try {
			// Initialize XML document builder
			final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			final DocumentBuilder builder = factory.newDocumentBuilder();
			final Document document = builder.parse(filePath);
			document.getDocumentElement().normalize();
			// Get the root element
			final Element rootElement = document.getDocumentElement();
			return createTreeItemFromElement(rootElement);
		} catch (final Exception e) {
			if (LOG.isInfoEnabled()) {
				final String error = e.getMessage();
				LOG.error(error);
			}
			return new TreeItem<>("Error loading XML");
		}
	}

	/**
	 * selectedItemProperty
	 * 
	 * @param tvSelModel
	 * @return
	 */
	public static ReadOnlyObjectProperty<TreeItem<String>> selectedItemProperty(
			final MultipleSelectionModel<TreeItem<String>> tvSelModel) {
		return tvSelModel.selectedItemProperty();
	}

	/**
	 * getSelectionModelTV
	 * 
	 * @param treeView
	 * @return
	 */
	public static MultipleSelectionModel<TreeItem<String>> getSelectionModelTV(final TreeView<String> treeView) {
		return treeView.getSelectionModel();
	}

	/**
	 * createTreeItemFromElement
	 * 
	 * @param element
	 * @return
	 */
	public static TreeItem<String> createTreeItemFromElement(final Element element) {
		final String elementText = element.getTagName();
		final TreeItem<String> treeItem = new TreeItem<>(elementText);
		element.getAttributes().getLength();
		if (element.hasAttributes()) {
			for (int i = 0; i < element.getAttributes().getLength(); i++) {
				final Node attr = element.getAttributes().item(i);
				if (!attr.getNodeName().equals("lineNumAttribName")) {
					treeItem.getChildren().add(new TreeItem<>(attr.getNodeName() + " = " + attr.getNodeValue()));
				}
			}
		}
		final NodeList nodeList = element.getChildNodes();
		for (int i = 0; i < nodeList.getLength(); i++) {
			final Node node = nodeList.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				final TreeItem<String> childItem = createTreeItemFromElement((Element) node);
				treeItem.getChildren().add(childItem);
			} else if (node.getNodeType() == Node.TEXT_NODE) {
				final String textContent = node.getTextContent().trim();
				if (!textContent.isEmpty()) {
					treeItem.getChildren().add(new TreeItem<>(textContent));
				}
			}
		}
		return treeItem;
	}
}
