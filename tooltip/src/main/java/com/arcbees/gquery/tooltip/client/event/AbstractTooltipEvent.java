package com.arcbees.gquery.tooltip.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.query.client.GQuery;

public abstract class AbstractTooltipEvent<T extends EventHandler> extends GwtEvent<T> {
    private final GQuery tooltip;
    private final GQuery tooltipTrigger;

    protected AbstractTooltipEvent(GQuery tooltip, GQuery tooltipTrigger) {
        this.tooltip = tooltip;
        this.tooltipTrigger = tooltipTrigger;
    }

    /**
     * Return the {@see GQuery} object wrapping the tooltip.
     */
    public GQuery getTooltip() {
        return tooltip;
    }

    /**
     * Return the {@see GQuery} object of the element that triggers the tooltip.
     */
    public GQuery getTooltipTrigger() {
        return tooltipTrigger;
    }
}
