package com.arcbees.gquery.tooltip.client.event;

import com.google.gwt.event.shared.EventHandler;

public interface BeforeHideTooltipEventHandler extends EventHandler {
    void onBeforeHide(BeforeHideTooltipEvent event);
}
