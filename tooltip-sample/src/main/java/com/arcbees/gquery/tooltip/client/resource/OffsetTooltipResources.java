package com.arcbees.gquery.tooltip.client.resource;

import com.arcbees.gquery.tooltip.client.TooltipResources;
import com.google.gwt.core.client.GWT;

public interface OffsetTooltipResources extends TooltipResources {
    public OffsetTooltipResources INSTANCE = GWT.create(OffsetTooltipResources.class);

    public interface OffsetTooltipStyle extends TooltipStyle {
    }

    @Override
    @Source("com/arcbees/gquery/tooltip/client/resource/offsetTooltip.css")
    OffsetTooltipStyle css();
}
