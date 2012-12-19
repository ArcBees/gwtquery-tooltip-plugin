package com.arcbees.gquery.tooltip.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.query.client.GQuery;
import com.google.gwt.query.client.plugins.Plugin;

public class Tooltip extends GQuery {
    public static final Class<Tooltip> Tooltip = GQuery.registerPlugin(Tooltip.class,
            new Plugin<Tooltip>() {
                public Tooltip init(GQuery gq) {
                    return new Tooltip(gq);
                }
            });

    static String TOOLTIP_DATA_KEY = "__GQUERY_TOOLTIP";

    public Tooltip(GQuery gq) {
        super(gq);
    }

    public Tooltip tooltip() {
        return tooltip(new TooltipOptions());
    }

    public Tooltip tooltip(TooltipOptions options) {
        for (Element e : elements()) {
            GQuery $e = $(e);
            if ($e.data(TOOLTIP_DATA_KEY) == null) {
                TooltipImpl impl;
                //use 2 different constructors for GWT optimization purpose
                if (options.getResources() == null){
                    impl = new TooltipImpl(e, options);
                }else{
                    impl = new TooltipImpl(e, options, options.getResources());
                }
                $e.data(TOOLTIP_DATA_KEY, impl);
            }
        }
        return this;
    }

    public Tooltip enable() {
        for (Element e : elements()) {
            TooltipImpl impl = getImpl(e);
            if (impl != null) {
                impl.enable();
            }
        }
        return this;
    }

    public Tooltip disable() {
        for (Element e : elements()) {
            TooltipImpl impl = getImpl(e);
            if (impl != null) {
                impl.disable();
            }
        }
        return this;
    }

    public Tooltip toggleEnabled() {
        for (Element e : elements()) {
            TooltipImpl impl = getImpl(e);
            if (impl != null) {
                impl.toggleEnabled();
            }
        }
        return this;
    }

    public Tooltip destroy() {
        for (Element e : elements()) {
            TooltipImpl impl = getImpl(e);
            if (impl != null) {
                impl.destroy();
                $(e).removeData(TOOLTIP_DATA_KEY);
            }
        }
        return this;
    }

    public Tooltip toggle() {
        for (Element e : elements()) {
            TooltipImpl impl = getImpl(e);
            if (impl != null) {
                impl.toggle();
            }
        }
        return this;
    }

    public Tooltip show() {
        for (Element e : elements()) {
            TooltipImpl impl = getImpl(e);
            if (impl != null) {
                impl.show();
            }
        }
        return this;
    }

    public Tooltip hide() {
        for (Element e : elements()) {
            TooltipImpl impl = getImpl(e);
            if (impl != null) {
                impl.hide();
            }
        }
        return this;
    }

    private TooltipImpl getImpl(Element e) {
        return $(e).data(TOOLTIP_DATA_KEY, TooltipImpl.class);
    }
}


