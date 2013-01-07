package com.arcbees.gquery.tooltip.client;

import com.arcbees.gquery.tooltip.client.TooltipOptions.TooltipPlacement;
import com.arcbees.gquery.tooltip.client.TooltipOptions.TooltipTrigger;
import com.arcbees.gquery.tooltip.client.TooltipResources.TooltipStyle;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.query.client.Function;
import com.google.gwt.query.client.GQuery;
import com.google.gwt.query.client.GQuery.Offset;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Timer;

import static com.arcbees.gquery.tooltip.client.Tooltip.TOOLTIP_DATA_KEY;
import static com.arcbees.gquery.tooltip.client.Tooltip.Tooltip;
import static com.google.gwt.query.client.GQuery.$;

public class TooltipImpl {
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

    private static void enter(Event e, TooltipOptions delegateOptions) {
        Element target = e.getCurrentEventTarget().cast();
        final TooltipImpl impl = getImpl(target, delegateOptions);

        if (impl.options.getDelayShow() == 0) {
            impl.show();
            return;
        }

        impl.cancelTimer();
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

    private static TooltipImpl getImpl(Element e, TooltipOptions initOption) {
        //ensure that a tooltip was initialized for the element (in case of delegation) and get the implementation
        return $(e).as(Tooltip).tooltip(initOption).data(TOOLTIP_DATA_KEY,
                TooltipImpl.class);
    }

    private static void leave(Event e, TooltipOptions delegateOptions) {
        Element target = e.getCurrentEventTarget().cast();
        final TooltipImpl impl = getImpl(target, delegateOptions);

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
        TooltipImpl impl = getImpl(target, options);

        impl.toggle();
    }

    private GQuery $element;
    private GQuery $tip;
    private TooltipOptions delegationOptions;
    private boolean enabled;
    private boolean hover;
    private TooltipOptions options;
    private Timer timer;
    private TooltipStyle style;

    public TooltipImpl(Element element, TooltipOptions options) {
        this(element, options, getDefaultResources());
    }

    public TooltipImpl(Element element, TooltipOptions options, TooltipResources resources) {
        this.$element = $(element);
        this.options = getOptions(options);
        this.style = resources.css();
        init();
    }

    public void destroy() {
        hide();
        unbind();
    }

    public void disable() {
        enabled = false;
    }

    public void enable() {
        enabled = true;
    }

    public void hide() {
        final GQuery tooltip = getTip();

        tooltip.removeClass(style.in());

        if (options.isAnimation()) {
            tooltip.fadeOut(ANIMATION_DURATION, new Function() {
                @Override
                public void f() {
                    tooltip.detach();
                }
            });
        } else {
            tooltip.detach();
        }
    }

    public void show() {
        GQuery tooltip = getTip();

        String title = getTitle();

        if (!enabled || title == null || title.length() == 0) {
            return;
        }

        setContent(title);

        tooltip.detach()
                .removeClass("in", "top", "bottom", "left", "right")
                .css("top", "0")
                .css("left", "0")
                .css("display", "block");

        if (options.getContainer() != null && options.getContainer().length() > 0) {
            tooltip.appendTo($(options.getContainer()));
        } else {
            tooltip.insertAfter($element);
        }

        OffsetInfo oi = OffsetInfo.from($element);
        long actualWidth = tooltip.get(0).getOffsetWidth();
        long actualHeight = tooltip.get(0).getOffsetHeight();
        long finalTop = -1;
        long finalLeft = -1;
        String placementClass = null;

        switch (options.getPlacement()) {
            case BOTTOM:
                finalTop = oi.top + oi.height;
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

        //TODO use GQuery.offset() method when it will be implemented
        tooltip.offset((int) finalTop, (int) finalLeft);
        tooltip.addClass(placementClass)
                .addClass(style.in());
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
            $element.get(0).removeAttribute(TITLE_ATTRIBUTE);
        }
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
        options.withHtml(readDataAttributes("animation", options.isAnimation(), new BooleanConverter()));
        options.withDelayHide(readDataAttributes("delayHide", options.getDelayHide(), new IntegerConverter()));
        options.withDelayShow(readDataAttributes("delayShow", options.getDelayShow(), new IntegerConverter()));
        options.withHtml(readDataAttributes("html", options.isHtml(), new BooleanConverter()));
        options.withContent(readDataAttributes("content", options.getContent(), new StringConverter()));
        options.withPlacement(readDataAttributes("placement", options.getPlacement(),
                new EnumConverter<TooltipPlacement>(TooltipPlacement.class)));
        options.withTrigger(readDataAttributes("trigger", options.getTrigger(), new EnumConverter<TooltipTrigger>
                (TooltipTrigger.class)));
        options.withSelector(readDataAttributes("selector", options.getSelector(), new StringConverter()));

        return options;
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
                    enter(e, delegationOptions);
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
    }

    private boolean isHover() {
        return this.hover;
    }

    private <T> T readDataAttributes(String name, T defaultData, Converter<T> converter) {
        String value = $element.data("tooltip-" + name, String.class);

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

    private void setContent(String title) {
        GQuery inner = getTip().find("." + style.tooltipInner());
        if (options.isHtml()) {
            inner.html(title);
        } else {
            inner.text(title);
        }
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
            $element.die(".tooltip");
        } else {
            $element.unbind(".tooltip");
        }
    }
}

