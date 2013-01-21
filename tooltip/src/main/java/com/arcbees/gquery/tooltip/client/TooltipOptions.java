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
import com.google.gwt.safehtml.shared.SafeHtml;

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

    public interface TooltipPlacementProvider {
        TooltipPlacement getPlacement(Element element);
    }

    private static boolean globalAnimation;
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

    public static void setGlobalAnimation(boolean globalAnimation) {
        TooltipOptions.globalAnimation = globalAnimation;
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

    static {
        //set default options globally
        globalAnimation = true;
        globalHtml = false;
        globalPlacement = TooltipPlacement.TOP;
        globalTrigger = TooltipTrigger.HOVER;
        globalDelayShow = globalDelayHide = 0;
        globalContainer = "body";
    }

    private Boolean animation;
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

    public TooltipOptions() {
    }

    TooltipOptions(TooltipOptions options) {
        if (options != null) {
            animation = options.isAnimation();
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
        }
    }

    public String getContainer() {
        return container != null ? container : globalContainer;
    }

    public String getContent() {
        return content != null ? content : globalContent;
    }

    public TooltipContentProvider getContentProvider() {
        return contentProvider != null ? contentProvider : globalContentProvider;
    }

    public int getDelayHide() {
        return delayHide != null ? delayHide : globalDelayHide;
    }

    public int getDelayShow() {
        return delayShow != null ? delayShow : globalDelayShow;
    }

    public TooltipPlacement getPlacement() {
        return placement != null ? placement : globalPlacement;
    }

    public TooltipPlacementProvider getPlacementProvider() {
        return placementProvider != null ? placementProvider : globalPlacementProvider;
    }

    public TooltipResources getResources() {
        return resources != null ? resources : globalResources;
    }

    public String getSelector() {
        return selector != null ? selector : globalSelector;
    }

    public SafeHtml getTemplate() {
        return template != null ? template : globalTemplate;
    }

    public TooltipTrigger getTrigger() {
        return trigger != null ? trigger : globalTrigger;
    }

    public boolean isAnimation() {
        return animation != null ? animation : globalAnimation;
    }

    public boolean isHtml() {
        return html != null ? html : globalHtml;
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
}
