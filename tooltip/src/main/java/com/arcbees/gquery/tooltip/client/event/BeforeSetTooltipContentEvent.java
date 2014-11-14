package com.arcbees.gquery.tooltip.client.event;

import com.google.gwt.event.shared.HasHandlers;
import com.google.gwt.query.client.GQuery;

public class BeforeSetTooltipContentEvent extends AbstractTooltipEvent<BeforeSetTooltipContentEventHandler> {
    public static Type<BeforeSetTooltipContentEventHandler> TYPE = new Type<BeforeSetTooltipContentEventHandler>();

    public static void fire(GQuery tooltip, GQuery triggerElement, HasHandlers source) {
        source.fireEvent(new BeforeSetTooltipContentEvent(tooltip, triggerElement));
    }

    private BeforeSetTooltipContentEvent(GQuery tooltip, GQuery triggerElement) {
        super(tooltip, triggerElement);
    }


    public Type<BeforeSetTooltipContentEventHandler> getAssociatedType() {
        return TYPE;
    }

    protected void dispatch(BeforeSetTooltipContentEventHandler handler) {
        handler.onBeforeSetContent(this);
    }
}
