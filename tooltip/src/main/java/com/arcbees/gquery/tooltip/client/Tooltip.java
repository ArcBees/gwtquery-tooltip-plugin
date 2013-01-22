/**
 * Copyright 2013 ArcBees Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.arcbees.gquery.tooltip.client;

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
                if (options.getResources() == null) {
                    impl = new TooltipImpl(e, options);
                } else {
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


