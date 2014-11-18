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

import java.util.ArrayList;
import java.util.List;

import com.arcbees.gquery.tooltip.client.event.BeforeHideTooltipEventHandler;
import com.arcbees.gquery.tooltip.client.event.BeforeSetTooltipContentEventHandler;
import com.arcbees.gquery.tooltip.client.event.BeforeShowTooltipEventHandler;
import com.arcbees.gquery.tooltip.client.event.HideTooltipEventHandler;
import com.arcbees.gquery.tooltip.client.event.ShowTooltipEventHandler;
import com.google.gwt.dom.client.Element;
import com.google.gwt.query.client.GQuery;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;

public class TooltipOptions {
    public enum TooltipPlacement {
        TOP, BOTTOM, LEFT, RIGHT;
    }

    public enum TooltipTrigger {
        CLICK, HOVER, FOCUS, MANUAL;
    }

    public interface TooltipContentProvider {
        String getContent(Element element);
    }

    public interface TooltipWidgetContentProvider {
        IsWidget getContent(Element element);
    }

    public interface TooltipPlacementProvider {
        TooltipPlacement getPlacement(Element element);
    }

    public interface TooltipOffsetProvider {
        GQuery.Offset getOffset(Element element);
    }

    private static boolean globalAnimation;
    private static boolean globalAutoClose;
    private static String globalContainer;
    private static String globalContent;
    private static TooltipContentProvider globalContentProvider;
    private static TooltipPlacementProvider globalPlacementProvider;
    private static int globalDelayShow;
    private static int globalDelayHide;
    private static boolean globalHtml;
    private static TooltipPlacement globalPlacement;
    private static TooltipResources globalResources;
    private static String globalSelector;
    private static SafeHtml globalTemplate;
    private static TooltipTrigger globalTrigger;
    private static GQuery.Offset globalOffset;
    private static TooltipOffsetProvider globalOffsetProvider;
    private static Widget globalWidget;
    private static boolean globalAlwaysVisible;
    private static String globalClosingPartner;
    private static List<BeforeHideTooltipEventHandler> globalBeforeHideTooltipEventHandlers;
    private static List<HideTooltipEventHandler> globalHideTooltipEventHandlers;
    private static List<BeforeShowTooltipEventHandler> globalBeforeShowTooltipEventHandlers;
    private static List<ShowTooltipEventHandler> globalShowTooltipEventHandlers;
    private static List<BeforeSetTooltipContentEventHandler> globalBeforeSetTooltipContentEventHandlers;

    public static void setGlobalAnimation(boolean globalAnimation) {
        TooltipOptions.globalAnimation = globalAnimation;
    }

    public static void setGlobalAutoClose(boolean globalAutoClose) {
        TooltipOptions.globalAutoClose = globalAutoClose;
    }

    public static void setGlobalAlwaysVisible(boolean globalAlwaysVisible) {
        TooltipOptions.globalAlwaysVisible = globalAlwaysVisible;
    }

    public static void setGlobalContainer(String globalContainer) {
        TooltipOptions.globalContainer = globalContainer;
    }

    public static void setGlobalContent(String globalContent) {
        TooltipOptions.globalContent = globalContent;
    }

    public static void setGlobalContentProvider(TooltipContentProvider globalContentProvider) {
        TooltipOptions.globalContentProvider = globalContentProvider;
    }

    public static void setGlobalDelayShow(int globalDelayShow) {
        TooltipOptions.globalDelayShow = globalDelayShow;
    }

    public static void setGlobalDelayHide(int globalDelayHide) {
        TooltipOptions.globalDelayHide = globalDelayHide;
    }

    public static void setGlobalHtml(boolean globalHtml) {
        TooltipOptions.globalHtml = globalHtml;
    }

    public static void setGlobalOffset(GQuery.Offset globalOffset) {
        TooltipOptions.globalOffset = globalOffset;
    }

    public static void setGlobalOffsetProvider(TooltipOffsetProvider globalOffsetProvider) {
        TooltipOptions.globalOffsetProvider = globalOffsetProvider;
    }

    public static void setGlobalPlacement(TooltipPlacement globalPlacement) {
        TooltipOptions.globalPlacement = globalPlacement;
    }

    public static void setGlobalPlacementProvider(TooltipPlacementProvider globalPlacementProvider) {
        TooltipOptions.globalPlacementProvider = globalPlacementProvider;
    }

    public static void setGlobalResources(TooltipResources globalResources) {
        TooltipOptions.globalResources = globalResources;
    }

    public static void setGlobalSelector(String globalSelector) {
        TooltipOptions.globalSelector = globalSelector;
    }

    public static void setGlobalTemplate(SafeHtml globalTemplate) {
        TooltipOptions.globalTemplate = globalTemplate;
    }

    public static void setGlobalTrigger(TooltipTrigger globalTrigger) {
        TooltipOptions.globalTrigger = globalTrigger;
    }

    public static void setGlobalWidget(IsWidget globalWidget) {
        TooltipOptions.globalWidget = globalWidget.asWidget();
    }

    public static void setGlobalClosingPartner(String selector) {
        TooltipOptions.globalClosingPartner = selector;
    }

    public static void addGlobalBeforeHideTooltipEventHandler(BeforeHideTooltipEventHandler handler) {
        globalBeforeHideTooltipEventHandlers.add(handler);
    }

    public static void addGlobalHideTooltipEventHandler(HideTooltipEventHandler handler) {
        globalHideTooltipEventHandlers.add(handler);
    }

    public static void addGlobalBeforeShowTooltipEventHandler(BeforeShowTooltipEventHandler handler) {
        globalBeforeShowTooltipEventHandlers.add(handler);
    }

    public static void addGlobalShowTooltipEventHandler(ShowTooltipEventHandler handler) {
        globalShowTooltipEventHandlers.add(handler);
    }

    public static void addGlobalBeforeSetTooltipContentEventHandler(BeforeSetTooltipContentEventHandler handler) {
        globalBeforeSetTooltipContentEventHandlers.add(handler);
    }

    static {
        //set default options globally
        globalAnimation = true;
        globalPlacement = TooltipPlacement.TOP;
        globalTrigger = TooltipTrigger.HOVER;
        globalDelayShow = globalDelayHide = 0;
        globalContainer = "body";
        globalAlwaysVisible = true;
        globalBeforeHideTooltipEventHandlers = new ArrayList<>();
        globalShowTooltipEventHandlers = new ArrayList<>();
        globalBeforeShowTooltipEventHandlers = new ArrayList<>();
        globalHideTooltipEventHandlers = new ArrayList<>();
        globalBeforeSetTooltipContentEventHandlers = new ArrayList<>();
    }

    private Boolean animation;
    private Boolean autoClose;
    private String container;
    private String content;
    private TooltipContentProvider contentProvider;
    private Integer delayShow;
    private Integer delayHide;
    private Boolean html;
    private TooltipPlacement placement;
    private TooltipPlacementProvider placementProvider;
    private TooltipResources resources;
    private String selector;
    private SafeHtml template;
    private TooltipTrigger trigger;
    private GQuery.Offset offset;
    private TooltipOffsetProvider offsetProvider;
    private Widget contentWidget;
    private TooltipWidgetContentProvider widgetContentProvider;
    private Boolean alwaysVisible;
    private List<BeforeHideTooltipEventHandler> beforeHideTooltipEventHandlers;
    private List<HideTooltipEventHandler> hideTooltipEventHandlers;
    private List<BeforeShowTooltipEventHandler> beforeShowTooltipEventHandlers;
    private List<ShowTooltipEventHandler> showTooltipEventHandlers;
    private List<BeforeSetTooltipContentEventHandler> beforeSetTooltipContentEventHandlers;
    private String closingPartner;

    public TooltipOptions() {
        beforeHideTooltipEventHandlers = new ArrayList<>();
        showTooltipEventHandlers = new ArrayList<>();
        beforeShowTooltipEventHandlers = new ArrayList<>();
        hideTooltipEventHandlers = new ArrayList<>();
        beforeSetTooltipContentEventHandlers = new ArrayList<>();
    }

    TooltipOptions(TooltipOptions options) {
        if (options != null) {
            animation = options.isAnimation();
            autoClose = options.isAutoClose();
            html = options.isHtml();
            placement = options.getPlacement();
            placementProvider = options.getPlacementProvider();
            selector = options.getSelector();
            content = options.getContent();
            container = options.getContainer();
            contentProvider = options.getContentProvider();
            trigger = options.getTrigger();
            delayShow = options.getDelayShow();
            delayHide = options.getDelayHide();
            resources = options.getResources();
            offset = options.getOffset();
            offsetProvider = options.getOffsetProvider();
            contentWidget = options.getWidget();
            widgetContentProvider = options.getWidgetContentProvider();
            beforeHideTooltipEventHandlers = new ArrayList<>(options.getBeforeHideTooltipEventHandlers());
            showTooltipEventHandlers = new ArrayList<>(options.getShowTooltipEventHandlers());
            beforeShowTooltipEventHandlers = new ArrayList<>(options.getBeforeShowTooltipEventHandlers());
            hideTooltipEventHandlers = new ArrayList<>(options.getHideTooltipEventHandlers());
            beforeSetTooltipContentEventHandlers = new ArrayList<>(options.getBeforeSetTooltipContentEventHandlers());
            closingPartner = options.getClosingPartner();
        }
    }

    public String getContainer() {
        return getFirstOr(container, globalContainer);
    }

    public String getContent() {
        return getFirstOr(content, globalContent);
    }

    public TooltipContentProvider getContentProvider() {
        return getFirstOr(contentProvider, globalContentProvider);
    }

    public String getClosingPartner() {
        return getFirstOr(closingPartner, globalClosingPartner);
    }

    public int getDelayHide() {
        return getFirstOr(delayHide, globalDelayHide);
    }

    public int getDelayShow() {
        return getFirstOr(delayShow, globalDelayShow);
    }

    public GQuery.Offset getOffset() {
        return getFirstOr(offset, globalOffset);
    }

    public TooltipOffsetProvider getOffsetProvider() {
        return getFirstOr(offsetProvider, globalOffsetProvider);
    }

    public TooltipPlacement getPlacement() {
        return getFirstOr(placement, globalPlacement);
    }

    public TooltipPlacementProvider getPlacementProvider() {
        return getFirstOr(placementProvider, globalPlacementProvider);
    }

    public TooltipResources getResources() {
        return getFirstOr(resources, globalResources);
    }

    public String getSelector() {
        return getFirstOr(selector, globalSelector);
    }

    public SafeHtml getTemplate() {
        return getFirstOr(template, globalTemplate);
    }

    public TooltipTrigger getTrigger() {
        return getFirstOr(trigger, globalTrigger);
    }

    public boolean isAnimation() {
        return getFirstOr(animation, globalAnimation);
    }

    public boolean isAutoClose() {
        return getFirstOr(autoClose, globalAutoClose);
    }

    public boolean isHtml() {
        return getFirstOr(html, globalHtml);
    }

    public boolean isAlwaysVisible() {
        return getFirstOr(alwaysVisible, globalAlwaysVisible);
    }

    public Widget getWidget() {
        return getFirstOr(contentWidget, globalWidget);
    }

    public TooltipWidgetContentProvider getWidgetContentProvider() {
        return widgetContentProvider;
    }

    public List<BeforeHideTooltipEventHandler> getBeforeHideTooltipEventHandlers() {
        List<BeforeHideTooltipEventHandler> handlers = new ArrayList<>(globalBeforeHideTooltipEventHandlers);
        handlers.addAll(beforeHideTooltipEventHandlers);
        return handlers;
    }

    public List<HideTooltipEventHandler> getHideTooltipEventHandlers() {
        List<HideTooltipEventHandler> handlers = new ArrayList<>(globalHideTooltipEventHandlers);
        handlers.addAll(hideTooltipEventHandlers);
        return handlers;
    }

    public List<BeforeShowTooltipEventHandler> getBeforeShowTooltipEventHandlers() {
        List<BeforeShowTooltipEventHandler> handlers = new ArrayList<>(globalBeforeShowTooltipEventHandlers);
        handlers.addAll(beforeShowTooltipEventHandlers);
        return handlers;
    }

    public List<ShowTooltipEventHandler> getShowTooltipEventHandlers() {
        List<ShowTooltipEventHandler> handlers = new ArrayList<>(globalShowTooltipEventHandlers);
        handlers.addAll(showTooltipEventHandlers);
        return handlers;
    }

    public List<BeforeSetTooltipContentEventHandler> getBeforeSetTooltipContentEventHandlers() {
        List<BeforeSetTooltipContentEventHandler> handlers = new ArrayList<>(globalBeforeSetTooltipContentEventHandlers);
        handlers.addAll(beforeSetTooltipContentEventHandlers);
        return handlers;
    }

    /**
     * Do we apply a css fade transition to the tooltip ?
     *
     * @param animation
     */
    public TooltipOptions withAnimation(boolean animation) {
        this.animation = animation;
        return this;
    }

    /**
     * Determine if the tooltip should be hidden when the user clicks outside the tooltip and the tooltip is triggered
     * by click (TooltipTrigger.CLICK)
     *
     * @param autoClose
     */
    public TooltipOptions withAutoClose(boolean autoClose) {
        this.autoClose = autoClose;
        return this;
    }

    /**
     * If this property is set to true (default), the tooltip will change its placement in order to ensure to be always
     * entirely visible.
     * <p/>
     * Ex: if the TooltipPlacement is set to BOTTOM and that the element is on the bottom of the page and there is not
     * enough place to display the tooltip. The tooltip will be placed above the element.
     *
     */
    public TooltipOptions withAlwaysVisible(boolean alwaysVisible) {
        this.alwaysVisible = alwaysVisible;
        return this;
    }

    /**
     * Css selector defining the container where the tooltip will be appended. If the
     * container is null (or empty string, the tooltip will be added after the element targeted by the tooltip.
     * <p/>
     * If the <code>container</code> is equals to 'parent', the tooltip will be appended to the parent element of the
     * element.
     * <p/>
     * If the <code>container</code> is equals to 'element', the tooltip will be appended directly to  the
     * element. With this options, the tooltip is not hidden when the user hovers it.
     *
     * @param container
     */
    public TooltipOptions withContainer(String container) {
        this.container = container;
        return this;
    }

    /**
     * Content of the tooltip, if <code>title</code> attribute doesn't exist on the element
     *
     * @param content
     */
    public TooltipOptions withContent(String content) {
        this.content = content;
        return this;
    }

    /**
     * Set the Object used for retrieving the content of the tooltip
     *
     * @param contentProvider
     */
    public TooltipOptions withContent(TooltipContentProvider contentProvider) {
        this.contentProvider = contentProvider;
        return this;
    }

    /**
     * Set the Object used for retrieving the content of the tooltip
     *
     * @param widgetContentProvider
     */
    public TooltipOptions withContent(TooltipWidgetContentProvider widgetContentProvider) {
        this.widgetContentProvider = widgetContentProvider;
        return this;
    }

    /**
     * Set a widget as the content of the tooltip
     *
     * @param widget
     */
    public TooltipOptions withContent(IsWidget widget) {
        if (widget != null) {
            this.contentWidget = widget.asWidget();
        }
        return this;
    }

    /**
     * Html content of the tooltip, if <code>title</code> attribute doesn't exist on the element
     *
     * @param content
     */
    public TooltipOptions withContent(SafeHtml content) {
        if (content != null) {
            this.content = content.asString();
            withHtml(true);
        } else {
            this.content = null;
        }
        return this;
    }

    /**
     * delay showing and hiding the tooltip (ms)
     *
     * @param delay
     */
    public TooltipOptions withDelay(int delay) {
        this.delayShow = this.delayHide = delay;
        return this;
    }

    /**
     * delay hiding the tooltip (ms)
     *
     * @param delay
     */
    public TooltipOptions withDelayHide(int delay) {
        this.delayHide = delay;
        return this;
    }

    /**
     * delay showing the tooltip (ms)
     *
     * @param delay
     */
    public TooltipOptions withDelayShow(int delay) {
        this.delayShow = delay;
        return this;
    }

    /**
     * Do we accept html for tooltip content
     *
     * @param html
     */
    public TooltipOptions withHtml(boolean html) {
        this.html = html;
        return this;
    }

    /**
     * Offset to apply to the tooltip's position.
     *
     * @param offset
     */
    public TooltipOptions withOffset(GQuery.Offset offset) {
        this.offset = offset;
        return this;
    }

    /**
     * Offset to apply to the tooltip's position.
     *
     * @param offsetProvider
     */
    public TooltipOptions withOffsetProvider(TooltipOffsetProvider offsetProvider) {
        this.offsetProvider = offsetProvider;
        return this;
    }

    /**
     * How to position the tooltip
     *
     * @param placement
     */
    public TooltipOptions withPlacement(TooltipPlacement placement) {
        this.placement = placement;
        return this;
    }

    /**
     * How to position the tooltip
     *
     * @param placementProvider
     */
    public TooltipOptions withPlacement(TooltipPlacementProvider placementProvider) {
        this.placementProvider = placementProvider;
        return this;
    }

    /**
     * Resources used by the tooltip
     *
     * @param resources
     */
    public TooltipOptions withResources(TooltipResources resources) {
        this.resources = resources;
        return this;
    }

    /**
     * If a selector is provided, tooltip objects will be delegated to the specified targets.
     *
     * @param selector
     */
    public TooltipOptions withSelector(String selector) {
        this.selector = selector;
        return this;
    }

    /**
     * Content of the tooltip, if <code>title</code> attribute doesn't exist on the element
     *
     * @param template
     */
    public TooltipOptions withTemplate(SafeHtml template) {
        this.template = template;
        return this;
    }

    /**
     * How tooltip is triggered
     *
     * @param trigger
     */
    public TooltipOptions withTrigger(TooltipTrigger trigger) {
        this.trigger = trigger;
        return this;
    }

    /**
     * Add a {@link BeforeHideTooltipEventHandler}
     * <p>
     * This event handler will be called before to hide the tooltip.
     */
    public TooltipOptions addBeforeHideTooltipEventHandler(BeforeHideTooltipEventHandler handler) {
        beforeHideTooltipEventHandlers.add(handler);
        return this;
    }

    /**
     * Add a {@link HideTooltipEventHandler}
     * <p>
     * This event handler will be called once the tooltip is hidden.
     */
    public TooltipOptions addHideTooltipEventHandler(HideTooltipEventHandler handler) {
        hideTooltipEventHandlers.add(handler);
        return this;
    }

    /**
     * Add a {@link BeforeShowTooltipEventHandler}
     * <p>
     * This event handler will be called before to show the tooltip on the screen.
     */
    public TooltipOptions addBeforeShowTooltipEventHandler(BeforeShowTooltipEventHandler handler) {
        beforeShowTooltipEventHandlers.add(handler);
        return this;
    }

    /**
     * Add a {@link ShowTooltipEventHandler}
     * <p>
     * This event handler will be called when the tooltip is visible on the screen.
     */
    public TooltipOptions addShowTooltipEventHandler(ShowTooltipEventHandler handler) {
        showTooltipEventHandlers.add(handler);
        return this;
    }

    /**
     * Add a {@link BeforeSetTooltipContentEventHandler}.
     * <p>
     * This event handler will be called before the tooltip content is set when the tooltip is showing.
     */
    public TooltipOptions addBeforeSetTooltipContentEventHandler(BeforeSetTooltipContentEventHandler handler) {
        beforeSetTooltipContentEventHandlers.add(handler);
        return this;
    }

    /**
     * Specify, by a css selector, the elements inside the tooltip that close the tooltip when the user clicks on.
     */
    public TooltipOptions withClosingPartner(String selector) {
        closingPartner = selector;

        return this;
    }

    private <T> T getFirstOr(T first, T global) {
        return first != null ? first : global;
    }
}
