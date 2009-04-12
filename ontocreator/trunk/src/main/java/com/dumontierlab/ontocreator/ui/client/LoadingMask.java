package com.dumontierlab.ontocreator.ui.client;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;

public class LoadingMask extends Composite {

	private static final String STYLE_NAME = "loadingMask";
	private static final String STYLE_NAME_LOADING = "loadingMask-loading";

	private final FlowPanel m_panel;
	private final Widget m_widget;
	private final FlowPanel m_mask;
	private final LoadingIndicator m_loadingIndicator;

	private boolean m_loading;

	public LoadingMask(Widget widget) {
		this(widget, null);
	}

	public LoadingMask(Widget widget, String loadingMessage) {
		m_panel = new FlowPanel();
		m_mask = new FlowPanel();
		m_widget = widget;
		m_loadingIndicator = loadingMessage == null ? new LoadingIndicator() : new LoadingIndicator(loadingMessage);
		initWidget(createUi());
	}

	public void setLoading(boolean loading) {
		if (loading && !m_loading) {
			m_panel.add(m_mask);
			m_loadingIndicator.show();

		} else if (!loading && m_loading) {
			m_loadingIndicator.hide();
			m_panel.remove(m_mask);
		}
		m_loading = loading;
	}

	@Override
	protected void onLoad() {
		repositionLoadingIndicator();
	}

	private Widget createUi() {
		m_panel.addStyleName(STYLE_NAME);
		m_panel.add(m_widget);

		DOM.setStyleAttribute(m_panel.getElement(), "position", "relative");

		m_mask.addStyleName(STYLE_NAME_LOADING);
		m_panel.add(m_loadingIndicator);

		DOM.setStyleAttribute(m_loadingIndicator.getElement(), "position", "absolute");
		DOM.setStyleAttribute(m_loadingIndicator.getElement(), "top", "50%");
		DOM.setStyleAttribute(m_loadingIndicator.getElement(), "left", "50%");

		return m_panel;
	}

	private void repositionLoadingIndicator() {
		DOM.setStyleAttribute(m_loadingIndicator.getElement(), "marginTop", "0px");
		DOM.setStyleAttribute(m_loadingIndicator.getElement(), "marginLeft", "0px");
		int width = m_loadingIndicator.getOffsetWidth();
		int height = m_loadingIndicator.getOffsetHeight();
		DOM.setStyleAttribute(m_loadingIndicator.getElement(), "marginTop", "-" + height / 2 + "px");
		DOM.setStyleAttribute(m_loadingIndicator.getElement(), "marginLeft", "-" + width / 2 + "px");
	}
}
