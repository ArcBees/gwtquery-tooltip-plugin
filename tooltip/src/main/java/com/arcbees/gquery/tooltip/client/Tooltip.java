/**
 * Copyright 2014 ArcBees Inc.
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

import java.util.HashMap;
import java.util.Map;

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
    private static final String NULL_SELECTOR_KEY = "__NULL_SELECTOR_KEY";

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

            Map<String, TooltipImpl> impls = $e.data(TOOLTIP_DATA_KEY);
            if (impls == null) {
                impls = new HashMap<String, TooltipImpl>();
            }

            if (!impls.containsKey(notNullSelector(options.getSelector()))) {
                TooltipImpl impl;
                //use 2 different constructors for GWT optimization purpose
                if (options.getResources() == null) {
                    impl = new TooltipImpl(e, options);
                } else {
                    impl = new TooltipImpl(e, options, options.getResources());
                }

                if (impls.size() == 0) {
                    $e.data(TOOLTIP_DATA_KEY, impls);
                }

                impls.put(notNullSelector(options.getSelector()), impl);
            }
        }
        return this;
    }

    /**
     * Enable all tooltips, including tooltip using delegation, related to the selected elements.
     */
    public Tooltip enable() {
        for (Element e : elements()) {
            Map<String, TooltipImpl> impls = $(e).data(TOOLTIP_DATA_KEY);
            if (impls != null) {
                for (TooltipImpl impl : impls.values()) {
                    impl.enable();
                }
            }
        }
        return this;
    }

    /**
     * Enable the tooltip related to the selected elements and using the corresponding delegation selector.
     */
    public Tooltip enable(String delegationSelector) {
        for (Element e : elements()) {
            TooltipImpl impl = getImpl(e, delegationSelector);
            if (impl != null) {
                impl.enable();
            }
        }
        return this;
    }

    /**
     * Disable all tooltips (tooltip using delegation included) related to the selected elements.
     */
    public Tooltip disable() {
        for (Element e : elements()) {
            Map<String, TooltipImpl> impls = $(e).data(TOOLTIP_DATA_KEY);
            if (impls != null) {
                for (TooltipImpl impl : impls.values()) {
                    impl.disable();
                }
            }
        }
        return this;
    }

    /**
     * Use this method if you use delegation for your tooltips and you want to disable a specific tooltip
     */
    public Tooltip disable(String delegationSelector) {
        for (Element e : elements()) {
            TooltipImpl impl = getImpl(e, delegationSelector);
            if (impl != null) {
                impl.disable();
            }
        }
        return this;
    }

    /**
     * Toggle the enable state of all tooltips (tooltip using delegation included) related to the selected elements.
     */
    public Tooltip toggleEnabled() {
        for (Element e : elements()) {
            Map<String, TooltipImpl> impls = $(e).data(TOOLTIP_DATA_KEY);
            if (impls != null) {
                for (TooltipImpl impl : impls.values()) {
                    impl.toggleEnabled();
                }
            }
        }
        return this;
    }

    /**
     * Toggle the enable state of the tooltip related to the selected elements and using the corresponding delegation
     * selector.
     *
     * @see TooltipOptions#getSelector()
     */
    public Tooltip toggleEnabled(String delegationSelector) {
        for (Element e : elements()) {
            TooltipImpl impl = getImpl(e, delegationSelector);
            if (impl != null) {
                impl.toggleEnabled();
            }
        }
        return this;
    }

    /**
     * Destroy all the tooltip (tooltip using delegation included) related to the selected elements.
     * @return
     */
    public Tooltip destroy() {
        for (Element e : elements()) {
            Map<String, TooltipImpl> impls = $(e).data(TOOLTIP_DATA_KEY);
            if (impls != null) {
                for (TooltipImpl impl : impls.values()) {
                    impl.destroy();

                    String delegationSelector = impl.getOptions().getSelector();
                    if (delegationSelector != null) {
                        $(delegationSelector, e).as(Tooltip).destroy();
                    }
                }
            }

            $(e).removeData(TOOLTIP_DATA_KEY);
        }
        return this;
    }

    /**
     * Destroy the tooltip related to the selected elements and using the corresponding delegation selector.
     * @see TooltipOptions#getSelector()
     */
    public Tooltip destroy(String delegationSelector) {
        for (Element e : elements()) {
            TooltipImpl impl = getImpl(e, delegationSelector);
            if (impl != null) {
                impl.destroy();

                if (delegationSelector != null) {
                    $(delegationSelector, e).as(Tooltip).destroy();
                }

                Map<String, TooltipImpl> impls = $(e).data(TOOLTIP_DATA_KEY);
                impls.remove(notNullSelector(delegationSelector));

                if (impls.size() == 0) {
                    $(e).removeData(TOOLTIP_DATA_KEY);
                }
            }
        }
        return this;
    }

    /**
     * Toggle the open state of the tooltip related to the selected elements. This method doesn't affect
     * tooltip using delegation because they are lazy loaded.
     */
    public Tooltip toggle() {
        for (Element e : elements()) {
            Map<String, TooltipImpl> impls = $(e).data(TOOLTIP_DATA_KEY);
            if (impls != null) {
                for (TooltipImpl impl : impls.values()) {
                    impl.toggle();
                }
            }
        }
        return this;
    }

    /**
     * Show the tooltip related to the selected elements. This method doesn't affect
     * tooltip using delegation.
     */
    public Tooltip show() {
        for (Element e : elements()) {
            Map<String, TooltipImpl> impls = $(e).data(TOOLTIP_DATA_KEY);
            if (impls != null) {
                for (TooltipImpl impl : impls.values()) {
                    impl.show();
                }
            }
        }
        return this;
    }


    /**
     * Hide the tooltip related to the selected elements. This method doesn't affect
     * tooltip using delegation
     */
    public Tooltip hide() {
        for (Element e : elements()) {
            Map<String, TooltipImpl> impls = $(e).data(TOOLTIP_DATA_KEY);
            if (impls != null) {
                for (TooltipImpl impl : impls.values()) {
                    impl.hide();
                }
            }
        }
        return this;
    }

    static TooltipImpl getImpl(Element e) {
       return getImpl(e, null);
    }

    static TooltipImpl getImpl(Element e, String delegationSelector) {
        Map<String, TooltipImpl> impls = $(e).data(TOOLTIP_DATA_KEY);

        return impls.get(notNullSelector(delegationSelector));
    }


    private static String notNullSelector(String delegationSelector) {
        return delegationSelector == null ? NULL_SELECTOR_KEY : delegationSelector.trim();
    }
}


