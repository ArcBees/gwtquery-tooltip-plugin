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

package com.arcbees.gquery.tooltip.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.query.client.GQuery;

public abstract class AbstractTooltipEvent<T extends EventHandler> extends GwtEvent<T> {
    private final GQuery tooltip;
    private final GQuery tooltipTrigger;

    protected AbstractTooltipEvent(GQuery tooltip, GQuery tooltipTrigger) {
        this.tooltip = tooltip;
        this.tooltipTrigger = tooltipTrigger;
    }

    /**
     * Return the {@see GQuery} object wrapping the tooltip.
     */
    public GQuery getTooltip() {
        return tooltip;
    }

    /**
     * Return the {@see GQuery} object of the element triggering the tooltip.
     */
    public GQuery getTooltipTrigger() {
        return tooltipTrigger;
    }
}
