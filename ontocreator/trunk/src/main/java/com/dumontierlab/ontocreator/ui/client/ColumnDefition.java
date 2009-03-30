package com.dumontierlab.ontocreator.ui.client;

import java.util.List;

import com.dumontierlab.ontocreator.ui.client.model.OWLClassBean;
import com.dumontierlab.ontocreator.ui.client.rpc.OntologyService;
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

	private static final String STYLE_NAME = "columnDefinition";
	private static final String HEADER_STYLE_NAME = "columnDefinition-header";
	private static final String URI_TEXTBOX_STYLE_NAME = "uriTextbox";

	private final int columnIndex;
	private final String columnName;
	private final int mappingIndex;
	private final SimplePanel content;

	private Widget literalSelectors;
	private Widget individualSelectors;
	private Widget classSelectors;

	public ColumnDefition(int columnIndex, String columnName, int mappingIndex) {
		this.columnIndex = columnIndex;
		this.columnName = columnName;
		this.mappingIndex = mappingIndex;
		content = new SimplePanel();
		initWidget(createUi());
	}

	private Widget createUi() {
		FlexTable table = new FlexTable();
		table.setStyleName(STYLE_NAME);
		table.setWidget(0, 0, new Label(columnName + (mappingIndex > 0 ? "." + mappingIndex : "")));

		table.setWidget(0, 1, createMappingTypeSelector());
		table.getCellFormatter().setHorizontalAlignment(0, 1, HasHorizontalAlignment.ALIGN_RIGHT);

		table.getRowFormatter().addStyleName(0, HEADER_STYLE_NAME);

		table.setWidget(1, 0, content);
		table.getFlexCellFormatter().setColSpan(1, 0, 2);

		useClassSelectors();

		return table;
	}

	private ListBox createMappingTypeSelector() {
		final ListBox list = new ListBox();
		list.addItem("OWL Class");
		list.addItem("OWL Individual");
		list.addItem("Literal");

		list.addChangeListener(new ChangeListener() {
			public void onChange(Widget sender) {
				switch (list.getSelectedIndex()) {
				case 0:
					useClassSelectors();
					break;
				case 1:
					useIndividualSelectors();
					break;
				case 2:
					useLiteralSelectors();
					break;
				}

			}
		});
		return list;
	}

	private void useLiteralSelectors() {
		if (literalSelectors == null) {
			HorizontalPanel panel = new HorizontalPanel();
			panel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
			panel.setSpacing(5);
			panel.add(new Label("Datatype:"));
			panel.add(createDatatypesList());
			literalSelectors = panel;
		}
		content.setWidget(literalSelectors);

	}

	private Widget createDatatypesList() {
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

	private void useIndividualSelectors() {
		if (individualSelectors == null) {
			FlowPanel panel = new FlowPanel();
			HorizontalPanel uriPanel = new HorizontalPanel();
			uriPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
			uriPanel.setSpacing(5);
			uriPanel.add(new Label("URI:"));
			uriPanel.add(createUriTextBox());
			panel.add(uriPanel);

			HorizontalPanel typePanel = new HorizontalPanel();
			typePanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
			typePanel.setSpacing(5);
			typePanel.add(new Label("Instance of:"));
			ListBox classes = createClassesList();
			initializeClassLists(classes);
			typePanel.add(classes);

			panel.add(typePanel);
			individualSelectors = panel;

		}
		content.setWidget(individualSelectors);
	}

	private ListBox createClassesList() {
		return createClassesList(false);
	}

	private ListBox createClassesList(boolean allowNoSelection) {
		final ListBox list = new ListBox();
		if (allowNoSelection) {
			list.addItem("Not specified", null);
		}
		list.addItem("owl:Thing", Constants.OWL_NAMESPACE + "Thing");
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
				for (OWLClassBean concept : result) {
					for (ListBox list : lists) {
						list.addItem(concept.getLabel(), concept.getUri());
					}
				}
			}

		}.call();
	}

	private String createDefaultUri() {
		return "http://ontocreator.dumontierlab.com/${columnValue}";
	}

	private void useClassSelectors() {
		if (classSelectors == null) {
			FlowPanel panel = new FlowPanel();
			HorizontalPanel uriPanel = new HorizontalPanel();
			uriPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
			uriPanel.setSpacing(5);
			uriPanel.add(new Label("URI:"));
			uriPanel.add(createUriTextBox());
			panel.add(uriPanel);

			HorizontalPanel subclassOfPanel = new HorizontalPanel();
			subclassOfPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
			subclassOfPanel.setSpacing(5);
			subclassOfPanel.add(new Label("Subclass of:"));
			ListBox subclassOfList = createClassesList();
			subclassOfPanel.add(subclassOfList);

			HorizontalPanel equivalentToPanel = new HorizontalPanel();
			equivalentToPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
			equivalentToPanel.setSpacing(5);
			equivalentToPanel.add(new Label("Equivalent to:"));
			ListBox equivalentToList = createClassesList(true);
			equivalentToPanel.add(equivalentToList);

			HorizontalPanel superclassOfPanel = new HorizontalPanel();
			superclassOfPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
			superclassOfPanel.setSpacing(5);
			superclassOfPanel.add(new Label("Superclass of:"));
			ListBox superclassOfList = createClassesList(true);
			superclassOfPanel.add(superclassOfList);

			panel.add(subclassOfPanel);
			panel.add(equivalentToPanel);
			panel.add(superclassOfPanel);

			classSelectors = panel;
		}
		content.setWidget(classSelectors);
	}

	private TextBox createUriTextBox() {
		TextBox uriTextbox = new TextBox();
		uriTextbox.addStyleName(URI_TEXTBOX_STYLE_NAME);
		uriTextbox.setText(createDefaultUri());

		return uriTextbox;
	}
}
