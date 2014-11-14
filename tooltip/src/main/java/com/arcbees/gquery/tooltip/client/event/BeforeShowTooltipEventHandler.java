package com.arcbees.gquery.tooltip.client.event;

import com.google.gwt.event.shared.EventHandler;

public interface BeforeShowTooltipEventHandler extends EventHandler {
    void onBeforeShow(BeforeShowTooltipEvent event);
}
