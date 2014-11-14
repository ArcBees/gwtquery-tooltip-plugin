package com.arcbees.gquery.tooltip.client.event;

import com.google.gwt.event.shared.HasHandlers;
import com.google.gwt.query.client.GQuery;

public class BeforeHideTooltipEvent extends AbstractTooltipEvent<BeforeHideTooltipEventHandler> {
    public static Type<BeforeHideTooltipEventHandler> TYPE = new Type<BeforeHideTooltipEventHandler>();

    public static void fire(GQuery tooltip, GQuery triggerElement, HasHandlers source) {
        source.fireEvent(new BeforeHideTooltipEvent(tooltip, triggerElement));
    }

    private BeforeHideTooltipEvent(GQuery tooltip, GQuery triggerElement) {
        super(tooltip, triggerElement);
    }

    public Type<BeforeHideTooltipEventHandler> getAssociatedType() {
        return TYPE;
    }

    protected void dispatch(BeforeHideTooltipEventHandler handler) {
        handler.onBeforeHide(this);
    }
}
