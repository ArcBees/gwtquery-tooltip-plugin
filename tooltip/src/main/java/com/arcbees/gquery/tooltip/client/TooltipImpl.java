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

import com.arcbees.gquery.tooltip.client.TooltipOptions.TooltipPlacement;
import com.arcbees.gquery.tooltip.client.TooltipOptions.TooltipTrigger;
import com.arcbees.gquery.tooltip.client.TooltipResources.TooltipStyle;
import com.arcbees.gquery.tooltip.client.event.BeforeHideTooltipEvent;
import com.arcbees.gquery.tooltip.client.event.BeforeSetTooltipContentEvent;
import com.arcbees.gquery.tooltip.client.event.BeforeShowTooltipEvent;
import com.arcbees.gquery.tooltip.client.event.HideTooltipEvent;
import com.arcbees.gquery.tooltip.client.event.ShowTooltipEvent;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.GwtEvent.Type;
import com.google.gwt.event.shared.HasHandlers;
import com.google.gwt.query.client.Function;
import com.google.gwt.query.client.GQuery;
import com.google.gwt.query.client.GQuery.Offset;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.HandlerRegistration;
import com.google.web.bindery.event.shared.HandlerRegistrations;
import com.google.web.bindery.event.shared.SimpleEventBus;

import static com.arcbees.gquery.tooltip.client.Tooltip.Tooltip;
import static com.arcbees.gquery.tooltip.client.Tooltip.getImpl;
import static com.google.gwt.query.client.GQuery.$;
import static com.google.gwt.query.client.GQuery.document;

public class TooltipImpl implements HasHandlers {
    public static interface DefaultTemplate extends SafeHtmlTemplates {
        public DefaultTemplate template = GWT.create(DefaultTemplate.class);

        @Template("<div class=\"{0}\"><div class=\"{1}\"></div><div class=\"{2}\"></div></div>")
        SafeHtml html(String tooltipClass, String tooltipArrowClass, String tooltipInnerClass);
    }

    private static class OffsetInfo {
        private static OffsetInfo from(GQuery element) {
            OffsetInfo oi = new OffsetInfo();
            Offset offset = element.offset();
            oi.left = offset.left;
            oi.top = offset.top;
            oi.width = element.get(0).getOffsetWidth();
            oi.height = element.get(0).getOffsetHeight();

            return oi;
        }

        private long height;
        private long left;
        private long top;
        private long width;
    }

    private static interface Converter<T> {
        T convert(String s);
    }

    private static class StringConverter implements Converter<String> {
        @Override
        public String convert(String s) {
            return s;
        }
    }

    private static class BooleanConverter implements Converter<Boolean> {
        @Override
        public Boolean convert(String s) {
            return Boolean.parseBoolean(s);
        }
    }

    private static class IntegerConverter implements Converter<Integer> {
        @Override
        public Integer convert(String s) {
            try {
                return Integer.parseInt(s);
            } catch (NumberFormatException e) {
                return null;
            }
        }
    }

    private static class EnumConverter<T extends Enum<T>> implements Converter<T> {
        private Class<T> enumClass;

        private EnumConverter(Class<T> enumClass) {
            this.enumClass = enumClass;
        }

        @Override
        public T convert(String s) {
            return Enum.valueOf(enumClass, s.toUpperCase());
        }
    }

    private static final String TITLE_ATTRIBUTE = "title";
    private static final String DATA_TITLE_ATTRIBUTE = "data-original-title";
    private static final int ANIMATION_DURATION = 150;
    private static TooltipResources DEFAULT_RESOURCES;

    private static void enter(Event e, TooltipImpl originalImpl) {
        if (!originalImpl.enabled) {
            return;
        }

        Element target = e.getCurrentEventTarget().cast();
        final TooltipImpl impl = getTooltipImpl(target, originalImpl.delegationOptions);

        impl.cancelTimer();

        if (impl.options.getDelayShow() == 0) {
            impl.show();
            return;
        }

        impl.setHover(true);

        Timer timer = new Timer() {
            @Override
            public void run() {
                if (impl.isHover()) {
                    impl.show();
                }
            }
        };

        impl.setTimer(timer);
        timer.schedule(impl.options.getDelayShow());
    }

    private static TooltipResources getDefaultResources() {
        if (DEFAULT_RESOURCES == null) {
            DEFAULT_RESOURCES = GWT.create(TooltipResources.class);
        }
        return DEFAULT_RESOURCES;
    }

    private static TooltipImpl getTooltipImpl(Element e, TooltipOptions initOption) {
        //ensure that a tooltip was initialized for the element (in case of delegation) and get the implementation
        $(e).as(Tooltip).tooltip(initOption);
        return getImpl(e, initOption.getSelector());
    }

    private static void leave(Event e, TooltipOptions delegateOptions) {
        Element target = e.getCurrentEventTarget().cast();
        final TooltipImpl impl = getTooltipImpl(target, delegateOptions);

        impl.cancelTimer();

        if (impl.options.getDelayHide() == 0) {
            impl.hide();
            return;
        }

        impl.setHover(false);

        Timer timer = new Timer() {
            @Override
            public void run() {
                if (!impl.isHover()) {
                    impl.hide();
                }
            }
        };

        impl.setTimer(timer);
        timer.schedule(impl.options.getDelayHide());
    }

    private static void toggle(Event e, TooltipOptions options) {
        Element target = e.getCurrentEventTarget().cast();
        TooltipImpl impl = getTooltipImpl(target, options);

        impl.toggle();
    }

    private final EventBus eventBus;

    private GQuery $element;
    private GQuery $tip;
    private TooltipOptions delegationOptions;
    private boolean enabled;
    private boolean hover;
    private TooltipOptions options;
    private Timer timer;
    private TooltipStyle style;
    private IsWidget widget;
    private Function autoCloseFunction;
    private HandlerRegistration allHandlerRegistration;


    public TooltipImpl(Element element, TooltipOptions options) {
        this(element, options, getDefaultResources());
    }

    public TooltipImpl(Element element, TooltipOptions options, TooltipResources resources) {
        this.$element = $(element);
        this.options = getOptions(options);
        this.style = resources.css();
        this.autoCloseFunction = new Function() {
            @Override
            public boolean f(Event e) {
                return onDocumentClick(e);
            }
        };
        eventBus = new SimpleEventBus();

        init();
    }

    public void destroy() {
        hide();
        unbind();

        if (options.getSelector() != null) {
            $(options.getSelector(), $element.get(0)).as(Tooltip).destroy();
        }

        if (allHandlerRegistration != null) {
            allHandlerRegistration.removeHandler();
        }
    }

    public void disable() {
        enabled = false;
    }

    public void enable() {
        enabled = true;
    }

    public void hide() {
        GQuery tooltip = getTip();

        BeforeHideTooltipEvent.fire(tooltip, $element, this);

        tooltip.removeClass(style.in());

        if (options.isAnimation()) {
            tooltip.fadeOut(ANIMATION_DURATION, new Function() {
                @Override
                public void f() {
                    detach();
                }
            });
        } else {
            detach();
        }

        $(document).unbind("click", autoCloseFunction);

        HideTooltipEvent.fire(tooltip, $element, this);
    }

    public void show() {
        GQuery tooltip = getTip();

        if (getTip().hasClass(style.in())) {
            return;
        }

        assignWidget();
        if (!enabled || noContentInTooltip()) {
            return;
        }

        tooltip.detach()
                .removeClass(style.in(), style.top(), style.bottom(), style.left(), style.right())
                .css("top", "0")
                .css("left", "0")
                .css("display", "block")
                .css("visibility", "hidden");

        String container = options.getContainer();

        if (container == null || "parent".equals(container)) {
            tooltip.insertAfter($element);
        } else if ("element".equals(container)) {
            tooltip.appendTo($element);
        } else {
            tooltip.appendTo($(container));
        }

        BeforeSetTooltipContentEvent.fire(tooltip, $element, this);

        setContent();

        BeforeShowTooltipEvent.fire(tooltip, $element, this);

        showTooltip();

        ShowTooltipEvent.fire(tooltip, $element, this);
    }

    public void toggle() {
        if (getTip().hasClass(style.in())) {
            hide();
        } else {
            show();
        }
    }

    public void toggleEnabled() {
        enabled = !enabled;
    }

    public TooltipOptions getOptions() {
        return options;
    }

    @Override
    public void fireEvent(GwtEvent<?> gwtEvent) {
        eventBus.fireEventFromSource(gwtEvent, this);
    }

    private void showTooltip() {
        if (widget != null) {
            Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
                @Override
                public void execute() {
                    doShowTooltip();
                }
            });
        } else {
            doShowTooltip();
        }
    }

    private void doShowTooltip() {
        GQuery tooltip = getTip();

        OffsetInfo oi = OffsetInfo.from($element);
        long actualWidth = tooltip.get(0).getOffsetWidth();
        long actualHeight = tooltip.get(0).getOffsetHeight();
        Offset additionalOffset = getAdditionalOffset();
        if (additionalOffset != null) {
            oi.top += additionalOffset.top;
            oi.left += additionalOffset.left;
        }

        long finalTop = 0;
        long finalLeft = 0;
        String placementClass = null;

        switch (getPlacement(oi, actualHeight, actualWidth)) {
            case BOTTOM:
                finalTop += oi.top + oi.height;
                finalLeft = oi.left + oi.width / 2 - actualWidth / 2;
                placementClass = style.bottom();
                break;
            case TOP:
                finalTop = oi.top - actualHeight;
                finalLeft = oi.left + oi.width / 2 - actualWidth / 2;
                placementClass = style.top();
                break;
            case LEFT:
                finalTop = oi.top + oi.height / 2 - actualHeight / 2;
                finalLeft = oi.left - actualWidth;
                placementClass = style.left();
                break;
            case RIGHT:
                finalTop = oi.top + oi.height / 2 - actualHeight / 2;
                finalLeft = oi.left + oi.width;
                placementClass = style.right();
                break;
        }

        tooltip.offset((int) finalTop, (int) finalLeft);
        tooltip.addClass(placementClass)
                .addClass(style.in());
        tooltip.css("visibility", "visible");

        if (options.getTrigger() != TooltipTrigger.HOVER && options.isAutoClose()) {
            $(document).delay(1, new Function() {
                @Override
                public void f() {
                    $(document).click(autoCloseFunction);
                }
            });
        }
    }

    //TODO use GQuery.on() method when it will be implemented in GQuery :)
    private void bind(String eventType, Function callback) {
        //add namespace
        eventType += ".tooltip";
        if (options.getSelector() != null) {
            $element.delegate(options.getSelector(), eventType, callback);
        } else {
            $element.bind(eventType, callback);
        }
    }

    private void assignWidget() {
        if (options.getWidget() != null) {
            widget = options.getWidget();
            detachWidget();
        } else if (options.getWidgetContentProvider() != null) {
            detachWidget();
            widget = options.getWidgetContentProvider().getContent($element.get(0));
        }
    }

    private void detach() {
        getTip().detach();
        detachWidget();
    }

    private void detachWidget() {
        if (widget != null && RootPanel.isInDetachList(widget.asWidget())) {
            RootPanel.detachNow(widget.asWidget());
            $(widget.asWidget()).get(0).removeFromParent();
        }
    }

    private boolean noContentInTooltip() {
        String title = getTitle();
        return (title == null && widget == null) || (title != null && title.length() == 0);
    }

    private boolean onDocumentClick(Event e) {
        Element target = e.getEventTarget().cast();
        GQuery $e = $(target);

        // Ensure that the user didn't click on the tooltip
        if ($e.parents("." + style.tooltip()).length() == 0) {
            hide();
        }
        return false;
    }

    private void cancelTimer() {
        if (this.timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    private void fixTitle() {
        String title = $element.attr(TITLE_ATTRIBUTE);

        if (title != null && title.length() > 0) {
            $element.attr(DATA_TITLE_ATTRIBUTE, title);
        }
    }

    private Offset getAdditionalOffset() {
        if (options.getOffsetProvider() != null) {
            return options.getOffsetProvider().getOffset($element.get(0));
        }

        return options.getOffset();
    }

    private TooltipOptions getOptions(TooltipOptions initialOptions) {
        TooltipOptions options;
        if (initialOptions == null) {
            options = new TooltipOptions();
        } else {
            //make a fresh copy to not impact other tooltips if the element overrides some options with its attributes
            options = new TooltipOptions(initialOptions);
        }

        //read data-* attributes on element
        options.withAnimation(readDataAttributes("animation", options.isAnimation(), new BooleanConverter()));
        options.withAutoClose(readDataAttributes("autoClose", options.isAutoClose(), new BooleanConverter()));

        //delay override delayHide and delayShow, set it only  if a data-delay attribute is specified
        int delay = readDataAttributes("delay", -1, new IntegerConverter());
        if (delay != -1) {
            options.withDelay(delay);
        }

        options.withDelayHide(readDataAttributes("delayHide", options.getDelayHide(), new IntegerConverter()));
        options.withDelayShow(readDataAttributes("delayShow", options.getDelayShow(), new IntegerConverter()));
        options.withHtml(readDataAttributes("html", options.isHtml(), new BooleanConverter()));
        options.withContent(readDataAttributes("content", options.getContent(), new StringConverter()));
        options.withContainer(readDataAttributes("container", options.getContainer(), new StringConverter()));
        options.withPlacement(readDataAttributes("placement", options.getPlacement(),
                new EnumConverter<TooltipPlacement>(TooltipPlacement.class)));
        options.withTrigger(readDataAttributes("trigger", options.getTrigger(), new EnumConverter<TooltipTrigger>
                (TooltipTrigger.class)));
        options.withSelector(readDataAttributes("selector", options.getSelector(), new StringConverter()));

        return options;
    }

    private TooltipPlacement getPlacement(OffsetInfo oi, long actualHeight, long actualWidth) {
        TooltipPlacement tooltipPlacement;
        if (options.getPlacementProvider() != null) {
            tooltipPlacement = options.getPlacementProvider().getPlacement($element.get(0));
        } else {
            tooltipPlacement = options.getPlacement();
        }

        if (!options.isAlwaysVisible()) {
            return tooltipPlacement;
        }

        return ensureTooltipIsVisible(tooltipPlacement, oi, actualHeight, actualWidth);
    }

    private TooltipPlacement ensureTooltipIsVisible(TooltipPlacement originalTooltipPlacement, OffsetInfo oi,
            long actualHeight, long actualWidth) {
        long bound;

        switch (originalTooltipPlacement) {
            case TOP:
                bound = oi.top - actualHeight;

                if (bound < 0) {
                    return TooltipPlacement.BOTTOM;
                }

                break;
            case BOTTOM:
                bound = oi.top + oi.height + actualHeight;

                if (bound > Window.getClientHeight()) {
                    return TooltipPlacement.TOP;
                }

                break;
            case LEFT:
                bound = oi.left - actualWidth;

                if (bound < 0) {
                    return TooltipPlacement.RIGHT;
                }

                break;
            case RIGHT:
                bound = oi.left + oi.width;

                if (bound > Window.getClientWidth()) {
                    return TooltipPlacement.LEFT;
                }

                break;
            default:
                throw new RuntimeException("unknown or null TooltipPlacement");
        }

        return originalTooltipPlacement;
    }

    private SafeHtml getTemplate() {
        if (options.getTemplate() != null) {
            return options.getTemplate();
        }

        return DefaultTemplate.template.html(style.tooltip(), style.tooltipArrow(), style.tooltipInner());
    }

    private GQuery getTip() {
        if ($tip == null) {
            $tip = $(getTemplate().asString());
        }
        return $tip;
    }

    private String getTitle() {
        String title = $element.attr(DATA_TITLE_ATTRIBUTE);

        if (title == null || title.length() == 0) {
            if (options.getContentProvider() != null) {
                title = options.getContentProvider().getContent($element.get(0));
            } else {
                title = options.getContent();
            }
        }
        return title;
    }

    private void init() {
        style.ensureInjected();
        enabled = true;
        hover = false;

        if (options.getSelector() != null) {
            //options use in case of delegation
            delegationOptions = new TooltipOptions(options).withTrigger(TooltipTrigger.MANUAL).withSelector(null);
        } else {
            delegationOptions = options;
            fixTitle();
        }

        if (options.getTrigger() == TooltipTrigger.CLICK) {
            bind("click", new Function() {
                @Override
                public boolean f(Event e) {
                    toggle(e, delegationOptions);
                    return true;
                }
            });
        } else if (options.getTrigger() != TooltipTrigger.MANUAL) {
            String eventIn = options.getTrigger() == TooltipTrigger.HOVER ? "mouseenter" : "focus";
            String eventOut = options.getTrigger() == TooltipTrigger.HOVER ? "mouseleave" : "blur";
            bind(eventIn, new Function() {
                @Override
                public boolean f(Event e) {
                    enter(e, TooltipImpl.this);
                    return true;
                }
            });
            bind(eventOut, new Function() {
                @Override
                public boolean f(Event e) {
                    leave(e, delegationOptions);
                    return true;
                }
            });
        }

        addHandlers();
    }

    private void addHandlers() {
        List<HandlerRegistration> handlerRegistrations = new ArrayList<>();

        addHandlersList(options.getBeforeHideTooltipEventHandlers(), BeforeHideTooltipEvent.TYPE, handlerRegistrations);
        addHandlersList(options.getHideTooltipEventHandlers(), HideTooltipEvent.TYPE, handlerRegistrations);
        addHandlersList(options.getBeforeShowTooltipEventHandlers(), BeforeShowTooltipEvent.TYPE, handlerRegistrations);
        addHandlersList(options.getShowTooltipEventHandlers(), ShowTooltipEvent.TYPE, handlerRegistrations);
        addHandlersList(options.getBeforeSetTooltipContentEventHandlers(), BeforeSetTooltipContentEvent.TYPE,
                handlerRegistrations);

        allHandlerRegistration = HandlerRegistrations.compose(handlerRegistrations.toArray(
                new HandlerRegistration[handlerRegistrations.size()]));
    }

    private <T extends EventHandler> void addHandlersList(List<T> handlers, Type<T> type,
            List<HandlerRegistration> handlerRegistrations) {
        for (T handler : handlers) {
            handlerRegistrations.add(eventBus.addHandler(type, handler));
        }
    }

    private boolean isHover() {
        return this.hover;
    }

    private <T> T readDataAttributes(String name, T defaultData, Converter<T> converter) {
        String value = $element.data("tooltip-" + name, String.class);

        // waiting fix https://github.com/gwtquery/gwtquery/pull/298
        if ("null".equals(value)) {
            value = null;
        }

        if (value == null || value.length() == 0) {
            //TODO $.data() should be able to read html5 data-* attributes
            value = $element.attr("data-tooltip-" + name);
        }

        if (value == null || value.length() == 0) {
            return defaultData;
        }

        T result = converter.convert(value);

        return result != null ? result : defaultData;
    }

    private void setContent() {
        GQuery inner = getInner();
        if (widget != null) {
            setWidgetContent(inner);
        } else {
            setContent(inner);
        }
    }

    private GQuery getInner() {
        return getTip().find("." + style.tooltipInner());
    }

    private void setContent(GQuery inner) {
        String title = getTitle();
        if (options.isHtml()) {
            inner.html(title);
        } else {
            inner.text(title);
        }
    }

    private void setWidgetContent(GQuery inner) {
        String oldDisplay = $(widget).css("display");
        $(widget).css("display", "none");
        attachWidget();
        $(widget).appendTo(inner).css("display", oldDisplay);
    }

    private void attachWidget() {
        RootPanel.get().add(widget);
        if (options.getWidgetContentProvider() != null) {
            RootPanel.detachOnWindowClose(widget.asWidget());
        }
        RootPanel.get().getElement().removeChild(widget.asWidget().getElement());
    }

    private void setHover(boolean b) {
        this.hover = b;
    }

    private void setTimer(Timer timer) {
        assert this.timer == null : "timer should be first cancelled";
        this.timer = timer;
    }

    //TODO use GQuery.off() method when it will be implemented in GQuery :)
    private void unbind() {
        if (options.getSelector() != null) {
            //TODO we should add a namespace, but die doesn't support it yet
            $element.undelegate(options.getSelector(), ".tooltip");
        } else {
            $element.unbind(".tooltip");
        }
    }
}

