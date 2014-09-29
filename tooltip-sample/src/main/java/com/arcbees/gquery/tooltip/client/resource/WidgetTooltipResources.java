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

package com.arcbees.gquery.tooltip.client.resource;

import com.arcbees.gquery.tooltip.client.TooltipResources;
import com.google.gwt.core.client.GWT;

public interface WidgetTooltipResources extends TooltipResources {
    WidgetTooltipResources INSTANCE = GWT.create(WidgetTooltipResources.class);

    public interface WidgetTooltipStyle extends TooltipStyle {
    }

    @Override
    @Source("com/arcbees/gquery/tooltip/client/resource/widgetTooltip.css")
    WidgetTooltipStyle css();
}