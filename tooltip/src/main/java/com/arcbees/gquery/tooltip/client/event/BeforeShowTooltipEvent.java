package com.arcbees.gquery.tooltip.client.event;

import com.google.gwt.event.shared.HasHandlers;
import com.google.gwt.query.client.GQuery;

public class BeforeShowTooltipEvent extends AbstractTooltipEvent<BeforeShowTooltipEventHandler> {
    public static Type<BeforeShowTooltipEventHandler> TYPE = new Type<BeforeShowTooltipEventHandler>();

    public static void fire(GQuery tooltip, GQuery triggerElement, HasHandlers source) {
        source.fireEvent(new BeforeShowTooltipEvent(tooltip, triggerElement));
    }

    private BeforeShowTooltipEvent(GQuery tooltip, GQuery triggerElement) {
        super(tooltip, triggerElement);
    }

    public Type<BeforeShowTooltipEventHandler> getAssociatedType() {
        return TYPE;
    }

    protected void dispatch(BeforeShowTooltipEventHandler handler) {
        handler.onBeforeShow(this);
    }
}
