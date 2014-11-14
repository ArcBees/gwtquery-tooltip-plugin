package com.arcbees.gquery.tooltip.client.event;

import com.google.gwt.event.shared.EventHandler;

public interface BeforeSetTooltipContentEventHandler extends EventHandler {
    void onBeforeSetContent(BeforeSetTooltipContentEvent event);
}
