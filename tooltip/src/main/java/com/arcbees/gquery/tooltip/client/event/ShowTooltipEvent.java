package com.arcbees.gquery.tooltip.client.event;

import com.google.gwt.event.shared.HasHandlers;
import com.google.gwt.query.client.GQuery;

public class ShowTooltipEvent extends AbstractTooltipEvent<ShowTooltipEventHandler> {
    public static Type<ShowTooltipEventHandler> TYPE = new Type<ShowTooltipEventHandler>();

    public static void fire(GQuery tooltip, GQuery triggerElement, HasHandlers source) {
        source.fireEvent(new ShowTooltipEvent(tooltip, triggerElement));
    }

    private ShowTooltipEvent(GQuery tooltip, GQuery triggerElement) {
        super(tooltip, triggerElement);
    }

    public Type<ShowTooltipEventHandler> getAssociatedType() {
        return TYPE;
    }

    protected void dispatch(ShowTooltipEventHandler handler) {
        handler.onShow(this);
    }
}
