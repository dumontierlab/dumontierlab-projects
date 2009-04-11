package com.dumontierlab.ontocreator.ui.client;

import java.util.List;

import com.dumontierlab.ontocreator.ui.client.model.ColumnMappingBean;
import com.dumontierlab.ontocreator.ui.client.model.OWLClassBean;
import com.dumontierlab.ontocreator.ui.client.model.ColumnMappingBean.ColumnMappingType;
import com.dumontierlab.ontocreator.ui.client.rpc.OntologyService;
import com.dumontierlab.ontocreator.ui.client.util.CollectionsUtil;
import com.dumontierlab.ontocreator.ui.client.util.Constants;
import com.dumontierlab.ontocreator.ui.client.util.OnRequestRpcCommand;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class ColumnDefition extends Composite {

	private static final String NULL_VALUE = "nil";

	private static final String STYLE_NAME = "columnDefinition";
	private static final String HEADER_STYLE_NAME = "columnDefinition-header";
	private static final String URI_TEXTBOX_STYLE_NAME = "uriTextbox";

	private final int columnIndex;
	private final String columnName;
	private final int mappingIndex;
	private final SimplePanel content;

	private final ListBox mappingTypeList;

	private LiteralMappingSelectors literalSelectors;
	private OWLIndividualMappingSelectors individualSelectors;
	private OWLClassMappingSelectors classSelectors;

	public ColumnDefition(int columnIndex, String columnName, int mappingIndex) {
		this.columnIndex = columnIndex;
		this.columnName = columnName;
		this.mappingIndex = mappingIndex;
		mappingTypeList = createMappingTypeSelector();
		content = new SimplePanel();
		initWidget(createUi());
	}

	public ColumnMappingBean getColumnMappingBean() {
		ColumnMappingType mappingType = getSelectedMappingType();
		ColumnMappingBean bean = new ColumnMappingBean(getMappingName(), columnIndex, mappingType);

		if (mappingType == ColumnMappingType.OWL_CLASS) {
			if (classSelectors.populateMappingBean(bean)) {
				return bean;
			}
		} else if (mappingType == ColumnMappingType.OWL_INDIVIDUAL) {
			if (individualSelectors.populateMappingBean(bean)) {
				return bean;
			}
		} else if (mappingType == ColumnMappingType.LITERAL) {
			if (literalSelectors.populateMappingBean(bean)) {
				return bean;
			}
		}

		return null;

	}

	private ColumnMappingType getSelectedMappingType() {
		return ColumnMappingType.values()[mappingTypeList.getSelectedIndex()];
	}

	private Widget createUi() {
		FlexTable table = new FlexTable();
		table.setStyleName(STYLE_NAME);
		table.setWidget(0, 0, new Label(getMappingName()));

		table.setWidget(0, 1, mappingTypeList);
		table.getCellFormatter().setHorizontalAlignment(0, 1, HasHorizontalAlignment.ALIGN_RIGHT);

		table.getRowFormatter().addStyleName(0, HEADER_STYLE_NAME);

		table.setWidget(1, 0, content);
		table.getFlexCellFormatter().setColSpan(1, 0, 2);

		useClassSelectors();

		return table;
	}

	private String getMappingName() {
		return columnName + (mappingIndex > 0 ? "." + mappingIndex : "");
	}

	private ListBox createMappingTypeSelector() {
		final ListBox list = new ListBox();
		for (ColumnMappingType mappingType : ColumnMappingType.values()) {
			list.addItem(mappingType.toString());
		}

		list.addChangeListener(new ChangeListener() {
			public void onChange(Widget sender) {
				int selectedIndex = list.getSelectedIndex();
				if (selectedIndex == ColumnMappingType.OWL_CLASS.ordinal()) {
					useClassSelectors();
				} else if (selectedIndex == ColumnMappingType.OWL_INDIVIDUAL.ordinal()) {
					useIndividualSelectors();
				} else if (selectedIndex == ColumnMappingType.LITERAL.ordinal()) {
					useLiteralSelectors();
				}

			}
		});
		return list;
	}

	private void useLiteralSelectors() {
		if (literalSelectors == null) {

			literalSelectors = new LiteralMappingSelectors();
		}
		content.setWidget(literalSelectors);

	}

	private void useIndividualSelectors() {
		if (individualSelectors == null) {

			individualSelectors = new OWLIndividualMappingSelectors();

		}
		content.setWidget(individualSelectors);
	}

	private ListBox createClassesList() {
		return createClassesList(false);
	}

	private ListBox createClassesList(boolean allowNoSelection) {
		final ListBox list = new ListBox();
		if (allowNoSelection) {
			list.addItem("Not specified", NULL_VALUE);
		}
		list.addItem("owl:Thing", NULL_VALUE);
		return list;
	}

	private void initializeClassLists(final ListBox... lists) {
		new OnRequestRpcCommand<List<OWLClassBean>>() {

			@Override
			protected void rpcCall(AsyncCallback<List<OWLClassBean>> callback) {
				OntologyService.Util.getInstace().getInputClasses(callback);
			}

			@Override
			protected void rpcFail(Throwable caught) {
				UserMessage.serverError(caught.getMessage(), caught);
			}

			@Override
			protected void rpcReturn(List<OWLClassBean> result) {
				CollectionsUtil.sortAlphabetical(result);
				for (OWLClassBean concept : result) {
					for (ListBox list : lists) {
						list.addItem(concept.getLabel(), concept.getUri());
					}
				}
			}

		}.call();
	}

	private String createDefaultUri() {
		return "${ontologyUri}#${columnValue}";
	}

	private void useClassSelectors() {
		if (classSelectors == null) {
			classSelectors = new OWLClassMappingSelectors();
		}
		content.setWidget(classSelectors);
	}

	private TextBox createUriTextBox() {
		TextBox uriTextbox = new TextBox();
		uriTextbox.addStyleName(URI_TEXTBOX_STYLE_NAME);
		uriTextbox.setText(createDefaultUri());

		return uriTextbox;
	}

	private class OWLClassMappingSelectors extends Composite {

		private final TextBox uriTextBox;
		private final ListBox subclassOfList;
		private final ListBox equivalentToList;
		private final ListBox superclassOfList;

		public OWLClassMappingSelectors() {
			uriTextBox = createUriTextBox();
			subclassOfList = createClassesList();
			equivalentToList = createClassesList(true);
			superclassOfList = createClassesList(true);
			initWidget(createUi());
		}

		public boolean populateMappingBean(ColumnMappingBean bean) {
			String uri = uriTextBox.getText();
			if (uri == null || uri.length() == 0) {
				return false;
			}
			bean.setUri(uri);
			String subClassOf = subclassOfList.getValue(subclassOfList.getSelectedIndex());
			if (!subClassOf.equals(NULL_VALUE)) {
				bean.setSubclassOf(subClassOf);
			}
			String equivalentTo = equivalentToList.getValue(equivalentToList.getSelectedIndex());
			if (!equivalentTo.equals(NULL_VALUE)) {
				bean.setEquivalentTo(equivalentTo);
			}
			String superClassOf = superclassOfList.getValue(superclassOfList.getSelectedIndex());
			if (!superClassOf.equals(NULL_VALUE)) {
				bean.setSuperclassOf(superClassOf);
			}
			return true;
		}

		private Widget createUi() {
			FlowPanel panel = new FlowPanel();
			HorizontalPanel uriPanel = new HorizontalPanel();
			uriPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
			uriPanel.setSpacing(5);
			uriPanel.add(new Label("URI:"));
			uriPanel.add(uriTextBox);
			panel.add(uriPanel);

			HorizontalPanel subclassOfPanel = new HorizontalPanel();
			subclassOfPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
			subclassOfPanel.setSpacing(5);
			subclassOfPanel.add(new Label("Subclass of:"));
			subclassOfPanel.add(subclassOfList);

			HorizontalPanel equivalentToPanel = new HorizontalPanel();
			equivalentToPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
			equivalentToPanel.setSpacing(5);
			equivalentToPanel.add(new Label("Equivalent to:"));
			equivalentToPanel.add(equivalentToList);

			HorizontalPanel superclassOfPanel = new HorizontalPanel();
			superclassOfPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
			superclassOfPanel.setSpacing(5);
			superclassOfPanel.add(new Label("Superclass of:"));
			superclassOfPanel.add(superclassOfList);

			panel.add(subclassOfPanel);
			panel.add(equivalentToPanel);
			panel.add(superclassOfPanel);

			initializeClassLists(subclassOfList, equivalentToList, superclassOfList);

			return panel;
		}

	}

	private class OWLIndividualMappingSelectors extends Composite {

		private final TextBox uriTextBox;
		private final ListBox classes;

		public OWLIndividualMappingSelectors() {
			uriTextBox = createUriTextBox();
			classes = createClassesList();

			initWidget(createUi());
		}

		public boolean populateMappingBean(ColumnMappingBean bean) {
			String uri = uriTextBox.getText();
			if (uri == null || uri.length() == 0) {
				return false;
			}
			bean.setUri(uri);

			String instanceOf = classes.getValue(classes.getSelectedIndex());
			if (!instanceOf.equals(NULL_VALUE)) {
				bean.setInstanceOf(instanceOf);
			}

			return true;
		}

		private Widget createUi() {
			FlowPanel panel = new FlowPanel();
			HorizontalPanel uriPanel = new HorizontalPanel();
			uriPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
			uriPanel.setSpacing(5);
			uriPanel.add(new Label("URI:"));
			uriPanel.add(uriTextBox);
			panel.add(uriPanel);

			HorizontalPanel typePanel = new HorizontalPanel();
			typePanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
			typePanel.setSpacing(5);
			typePanel.add(new Label("Instance of:"));
			initializeClassLists(classes);
			typePanel.add(classes);

			panel.add(typePanel);
			return panel;
		}
	}

	private class LiteralMappingSelectors extends Composite {
		private final ListBox datatypesList;

		public LiteralMappingSelectors() {
			datatypesList = createDatatypesList();
			initWidget(createUi());
		}

		public boolean populateMappingBean(ColumnMappingBean bean) {
			bean.setDatatype(datatypesList.getValue(datatypesList.getSelectedIndex()));
			return true;
		}

		private Widget createUi() {
			HorizontalPanel panel = new HorizontalPanel();
			panel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
			panel.setSpacing(5);
			panel.add(new Label("Datatype:"));
			panel.add(datatypesList);
			return panel;
		}

		private ListBox createDatatypesList() {
			ListBox list = new ListBox();

			list.addItem("xsd:anyURI", Constants.XSD_NAMESPACE + "anyURI");
			list.addItem("xsd:base64Binary", Constants.XSD_NAMESPACE + "base64Binary");
			list.addItem("xsd:boolean", Constants.XSD_NAMESPACE + "boolean");
			list.addItem("xsd:byte", Constants.XSD_NAMESPACE + "byte");
			list.addItem("xsd:date", Constants.XSD_NAMESPACE + "date");
			list.addItem("xsd:dateTime", Constants.XSD_NAMESPACE + "dateTime");
			list.addItem("xsd:double", Constants.XSD_NAMESPACE + "double");
			list.addItem("xsd:decimal", Constants.XSD_NAMESPACE + "decimal");
			list.addItem("xsd:float", Constants.XSD_NAMESPACE + "float");
			list.addItem("xsd:gDay", Constants.XSD_NAMESPACE + "gDay");
			list.addItem("xsd:gMonth", Constants.XSD_NAMESPACE + "gMonth");
			list.addItem("xsd:gMonthDay", Constants.XSD_NAMESPACE + "gMonthDay");
			list.addItem("xsd:gYear", Constants.XSD_NAMESPACE + "gYear");
			list.addItem("xsd:gYearMonth", Constants.XSD_NAMESPACE + "gYearMonth");
			list.addItem("xsd:hexBinary", Constants.XSD_NAMESPACE + "hexBinary");
			list.addItem("xsd:int", Constants.XSD_NAMESPACE + "int");
			list.addItem("xsd:integer", Constants.XSD_NAMESPACE + "integer");
			list.addItem("xsd:language", Constants.XSD_NAMESPACE + "language");
			list.addItem("xsd:long", Constants.XSD_NAMESPACE + "long");
			list.addItem("xsd:Name", Constants.XSD_NAMESPACE + "Name");
			list.addItem("xsd:normalizedString", Constants.XSD_NAMESPACE + "normalizedString");
			list.addItem("xsd:NCName", Constants.XSD_NAMESPACE + "NCName");
			list.addItem("xsd:NMTOKEN", Constants.XSD_NAMESPACE + "NMTOKEN");
			list.addItem("xsd:negativeInteger", Constants.XSD_NAMESPACE + "negativeInteger");
			list.addItem("xsd:nonNegativeInteger", Constants.XSD_NAMESPACE + "nonNegativeInteger");
			list.addItem("xsd:nonNegativeInteger", Constants.XSD_NAMESPACE + "nonPositiveInteger");
			list.addItem("xsd:positiveInteger", Constants.XSD_NAMESPACE + "positiveInteger");
			list.addItem("xsd:short", Constants.XSD_NAMESPACE + "short");
			list.addItem("xsd:string", Constants.XSD_NAMESPACE + "string");
			list.addItem("xsd:time", Constants.XSD_NAMESPACE + "time");
			list.addItem("xsd:token", Constants.XSD_NAMESPACE + "token");
			list.addItem("xsd:unsignedLong", Constants.XSD_NAMESPACE + "unsignedLong");
			list.addItem("xsd:unsignedInt", Constants.XSD_NAMESPACE + "unsignedInt");
			list.addItem("xsd:unsignedShort", Constants.XSD_NAMESPACE + "unsignedShort");
			list.addItem("xsd:unsignedByte", Constants.XSD_NAMESPACE + "unsignedByte");
			list.addItem("rdf:XMLLiteral", Constants.RDF_NAMESPACE + "XMLLiteral");

			list.setSelectedIndex(28);

			return list;
		}
	}
}
