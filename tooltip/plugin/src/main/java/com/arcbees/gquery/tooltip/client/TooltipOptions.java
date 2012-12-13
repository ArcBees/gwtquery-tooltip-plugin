package com.arcbees.gquery.tooltip.client;

import com.google.gwt.safehtml.shared.SafeHtml;

public class TooltipOptions {

    public enum TooltipPlacement {
        TOP, BOTTOM, LEFT, RIGHT;
    }

    public enum TooltipTrigger {
        CLICK, HOVER, FOCUS, MANUAL;
    }
    private boolean animation;
    private String content;
    private int delay;
    private boolean html;
    private TooltipPlacement placement;
    private TooltipResources resources;
    private String selector;
    private SafeHtml template;
    private TooltipTrigger trigger;

    public TooltipOptions() {
        initDefault();
    }

    public TooltipOptions(TooltipOptions options) {
        if (options != null) {
            animation = options.isAnimation();
            html = options.isHtml();
            placement = options.getPlacement();
            selector = options.getSelector();
            content = options.getContent();
            trigger = options.getTrigger();
            delay = options.getDelay();
            resources = options.getResources();
        }
    }

    public String getContent() {
        return content;
    }

    public int getDelay() {
        return delay;
    }

    public TooltipPlacement getPlacement() {
        return placement;
    }

    public TooltipResources getResources() {
        return resources;
    }

    public String getSelector() {
        return selector;
    }

    public SafeHtml getTemplate() {
        return template;
    }

    public TooltipTrigger getTrigger() {
        return trigger;
    }

    public boolean isAnimation() {
        return animation;
    }

    public boolean isHtml() {
        return html;
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
     * Content of the tooltip, if <code>title</code> attribute doesn't exist on the element
     *
     * @param content
     */
    public TooltipOptions withContent(String content) {
        this.content = content;
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
        this.delay = delay;
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
     * Resources used by the tooltip
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
     * @param content
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

    private void initDefault() {
        animation = true;
        html = false;
        placement = TooltipPlacement.TOP;
        trigger = TooltipTrigger.HOVER;
        delay = 0;
    }
}
