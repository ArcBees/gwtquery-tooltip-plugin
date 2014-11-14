package com.arcbees.gquery.tooltip.client.event;

import com.google.gwt.event.shared.HasHandlers;
import com.google.gwt.query.client.GQuery;

public class HideTooltipEvent extends AbstractTooltipEvent<HideTooltipEventHandler> {
    public static Type<HideTooltipEventHandler> TYPE = new Type<HideTooltipEventHandler>();

    public static void fire(GQuery tooltip, GQuery triggerElement, HasHandlers source) {
        source.fireEvent(new HideTooltipEvent(tooltip, triggerElement));
    }

    private HideTooltipEvent(GQuery tooltip, GQuery triggerElement) {
        super(tooltip, triggerElement);
    }

    public Type<HideTooltipEventHandler> getAssociatedType() {
        return TYPE;
    }

    protected void dispatch(HideTooltipEventHandler handler) {
        handler.onHide(this);
    }
}
