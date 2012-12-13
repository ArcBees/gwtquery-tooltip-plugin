package com.arcbees.gquery.tooltip.client;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;

public interface TooltipResources extends ClientBundle{

    public interface TooltipStyle extends CssResource{

        String tooltip();
        String in();
        String top();
        String left();
        String right();
        String bottom();
        @ClassName("tooltip-inner")
        String tooltipInner();
        @ClassName("tooltip-arrow")
        String tooltipArrow();



    }

    @Source("com/zafinlabs/zui/zuicore/client/ui/Tooltip.css")
    TooltipStyle css();
}
