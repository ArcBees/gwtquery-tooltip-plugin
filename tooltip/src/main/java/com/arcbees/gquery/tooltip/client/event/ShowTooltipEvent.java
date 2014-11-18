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

import com.google.gwt.event.shared.HasHandlers;
import com.google.gwt.query.client.GQuery;

public class ShowTooltipEvent extends AbstractTooltipEvent<ShowTooltipEventHandler> {
    public static Type<ShowTooltipEventHandler> TYPE = new Type<ShowTooltipEventHandler>();

    public static void fire(GQuery tooltip, GQuery triggerElement, HasHandlers source) {
        source.fireEvent(new ShowTooltipEvent(tooltip, triggerElement));
    }

    private ShowTooltipEvent(GQuery tooltip, GQuery triggerElement) {
        super(tooltip, triggerElement);
    }

    public Type<ShowTooltipEventHandler> getAssociatedType() {
        return TYPE;
    }

    protected void dispatch(ShowTooltipEventHandler handler) {
        handler.onShow(this);
    }
}
