package com.dumontierlab.ontocreator.ui.client;

import java.util.List;
import java.util.Set;

import com.dumontierlab.ontocreator.ui.client.model.ColumnMappingBean;
import com.dumontierlab.ontocreator.ui.client.model.ColumnMappingRelationshipBean;
import com.dumontierlab.ontocreator.ui.client.model.OWLPropertyBean;
import com.dumontierlab.ontocreator.ui.client.model.ColumnMappingBean.ColumnMappingType;
import com.dumontierlab.ontocreator.ui.client.model.ColumnMappingRelationshipBean.ColumnMappingRelationshipType;
import com.dumontierlab.ontocreator.ui.client.rpc.DataService;
import com.dumontierlab.ontocreator.ui.client.rpc.OntologyService;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

public class ColumnRelationships extends Composite implements WizardTab {

	private final SimplePanel tableContainer;
	private FlexTable table;
	private final Set<ColumnMappingBean> mappings;

	public ColumnRelationships(Set<ColumnMappingBean> mappings) {
		this.mappings = mappings;
		tableContainer = new SimplePanel();
		initWidget(createUi());
	}

	private Widget createUi() {
		return tableContainer;
	}

	public String getTabName() {
		return "Step 3: Inter-column relationships";
	}

	public String getTabCaption() {
		// TODO Auto-generated method stub
		return null;
	}

	public void initialize() {
		table = new FlexTable();
		createMappingTable();
		tableContainer.setWidget(table);
	}

	@Override
	public Widget getWidget() {
		return this;
	}

	public void complete(final AsyncCallback<Boolean> callback) {
		DataService.Util.getInstance().setColumnMappings(mappings, new AsyncCallback<Void>() {
			public void onFailure(Throwable caught) {
				callback.onFailure(caught);

			}

			public void onSuccess(Void result) {
				Window.open("http://pellet.owldl.com/owlsight/?ontology=" + GWT.getModuleBaseURL() + "download",
						"Output Ontology", null);
				callback.onSuccess(true);
			}

		});

	}

	private void createMappingTable() {
		int i = 1;
		for (ColumnMappingBean mapping : mappings) {
			String html = "<span class='bold'>" + mapping.getName() + "</span><br />(" + mapping.getMappingType() + ")";
			table.setWidget(0, i, new HTML(html));
			table.setWidget(i, 0, new HTML(html));
			i++;
		}

		int x = 1;
		for (ColumnMappingBean mappingSource : mappings) {
			int y = 1;
			for (ColumnMappingBean mappingDestination : mappings) {
				if (x != y) {
					final TypedListBox<ColumnMappingRelationshipBean> relationships = createRelationshipsList(
							mappingSource.getMappingType(), mappingDestination.getMappingType());
					if (relationships != null) {
						final ColumnMappingBean finalMappingDestination = mappingDestination;
						final ColumnMappingBean finalMappingSource = mappingSource;
						relationships.addChangeListener(new ChangeListener() {
							public void onChange(Widget sender) {
								finalMappingSource.addRelationship(relationships.getSelectedValue(),
										finalMappingDestination);
							}

						});
						table.setWidget(x, y, relationships);
					} else {
						table.getCellFormatter().setStyleName(x, y, "greyCell");
					}

				} else {
					table.getCellFormatter().setStyleName(x, y, "greyCell");
				}
				y++;
			}
			x++;
		}
	}

	private TypedListBox<ColumnMappingRelationshipBean> createRelationshipsList(ColumnMappingType sourceMappingType,
			ColumnMappingType targetMappingType) {
		TypedListBox<ColumnMappingRelationshipBean> list = new TypedListBox<ColumnMappingRelationshipBean>();
		list.addItem("None", (ColumnMappingRelationshipBean) null);
		if (sourceMappingType == ColumnMappingType.OWL_CLASS && targetMappingType == ColumnMappingType.OWL_CLASS) {
			list.addItem(ColumnMappingRelationshipType.SUBCLASS_OF.toString(), new ColumnMappingRelationshipBean(
					ColumnMappingRelationshipType.SUBCLASS_OF));
			list.addItem(ColumnMappingRelationshipType.EQUIVALENT_TO.toString(), new ColumnMappingRelationshipBean(
					ColumnMappingRelationshipType.EQUIVALENT_TO));
			list.addItem(ColumnMappingRelationshipType.COMPLEMENT_OF.toString(), new ColumnMappingRelationshipBean(
					ColumnMappingRelationshipType.COMPLEMENT_OF));
			list.addItem(ColumnMappingRelationshipType.DISJOINT_WITH.toString(), new ColumnMappingRelationshipBean(
					ColumnMappingRelationshipType.DISJOINT_WITH));

		} else if (sourceMappingType == ColumnMappingType.OWL_INDIVIDUAL
				&& targetMappingType == ColumnMappingType.OWL_INDIVIDUAL) {
			list.addItem(ColumnMappingRelationshipType.SAME_AS.toString(), new ColumnMappingRelationshipBean(
					ColumnMappingRelationshipType.SAME_AS));
			loadObjectProperties(list, ColumnMappingRelationshipType.OBJECT_PROPERTY);
		} else if (sourceMappingType == ColumnMappingType.OWL_INDIVIDUAL
				&& targetMappingType == ColumnMappingType.LITERAL) {
			loadDataProperties(list, ColumnMappingRelationshipType.DATA_PROPERTY);
		} else if (sourceMappingType == ColumnMappingType.OWL_INDIVIDUAL
				&& targetMappingType == ColumnMappingType.OWL_CLASS) {
			list.addItem(ColumnMappingRelationshipType.INSTANCE_OF.toString(), new ColumnMappingRelationshipBean(
					ColumnMappingRelationshipType.INSTANCE_OF));
		} else {
			return null;
		}

		return list;

	}

	private void loadObjectProperties(final TypedListBox<ColumnMappingRelationshipBean> list,
			final ColumnMappingRelationshipType relationshipType) {
		OntologyService.Util.getInstace().getInputObjectProperties(new AsyncCallback<List<OWLPropertyBean>>() {
			public void onFailure(Throwable caught) {
				UserMessage.serverError(caught.getMessage(), caught);
			};

			public void onSuccess(List<OWLPropertyBean> result) {
				for (OWLPropertyBean prop : result) {
					ColumnMappingRelationshipBean relationship = new ColumnMappingRelationshipBean(relationshipType);
					relationship.setUri(prop.getUri());
					list.addItem(prop.getLabel(), relationship);
				}
			};
		});
	}

	private void loadDataProperties(final TypedListBox<ColumnMappingRelationshipBean> list,
			final ColumnMappingRelationshipType relationshipType) {
		OntologyService.Util.getInstace().getInputDataProperties(new AsyncCallback<List<OWLPropertyBean>>() {
			public void onFailure(Throwable caught) {
				UserMessage.serverError(caught.getMessage(), caught);
			};

			public void onSuccess(List<OWLPropertyBean> result) {
				for (OWLPropertyBean prop : result) {
					ColumnMappingRelationshipBean relationship = new ColumnMappingRelationshipBean(relationshipType);
					relationship.setUri(prop.getUri());
					list.addItem(prop.getLabel(), relationship);
				}
			};
		});
	}
}
