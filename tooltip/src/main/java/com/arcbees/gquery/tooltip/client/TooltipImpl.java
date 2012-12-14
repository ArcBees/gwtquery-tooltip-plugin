package com.arcbees.gquery.tooltip.client;

import com.arcbees.gquery.tooltip.client.TooltipOptions.TooltipTrigger;
import com.arcbees.gquery.tooltip.client.TooltipResources.TooltipStyle;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.query.client.Function;
import com.google.gwt.query.client.GQuery;
import com.google.gwt.query.client.GQuery.Offset;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Timer;

import static com.google.gwt.query.client.GQuery.$;
import static com.arcbees.gquery.tooltip.client.Tooltip.TOOLTIP_DATA_KEY;

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
    private static final String TITLE_ATTRIBUTE = "title";
    private static final String DATA_TITLE_ATTRIBUTE = "data-original-title";
    private static final int ANIMATION_DURATION = 150;

    private static void enter(Event e, TooltipOptions options) {
        Element target = e.getEventTarget().cast();
        final TooltipImpl impl = getImpl(target, options);

        if (impl.options.getDelay() == 0) {
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
        timer.schedule(options.getDelay());

    }

    private static TooltipImpl getImpl(Element e, TooltipOptions initOption) {
        //ensure that a tooltip was initialized for the element (in case of delegation) and get the implementation
        return $(e).as(Tooltip.Tooltip).tooltip(initOption).data(TOOLTIP_DATA_KEY,
                TooltipImpl.class);
    }

    private static void leave(Event e, TooltipOptions options) {
        Element target = e.getEventTarget().cast();
        final TooltipImpl impl = getImpl(target, options);

        if (impl.options.getDelay() == 0) {
            impl.hide();
            return;
        }

        impl.cancelTimer();
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
        timer.schedule(options.getDelay());
    }

    private static void toggle(Event e, TooltipOptions options) {
        Element target = e.getEventTarget().cast();
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

    public TooltipImpl(Element element, TooltipOptions options, TooltipResources resources) {
        this.$element = $(element);
        this.options = options;
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
                .removeClass("fade", "in", "top", "bottom", "left", "right")
                .css("top", "0")
                .css("left", "0")
                .css("display", "block")
                .insertAfter($element);

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
        setOffset(tooltip, finalTop, finalLeft);
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
        if (options.getSelector() != null) {
            //TODO we should add a namespace, but delegate doesn't support it yet
            $element.delegate(options.getSelector(), eventType, callback);
        } else {
            $element.bind(eventType + ".tooltip", callback);
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
            $element.attr(DATA_TITLE_ATTRIBUTE, title).removeAttr(TITLE_ATTRIBUTE);
        }
    }

    private SafeHtml getTemplate(){
        if (options.getTemplate() != null){
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
            title = options.getContent();
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

    private void setContent(String title) {
        GQuery inner = getTip().find("."+style.tooltipInner());
        if (options.isHtml()) {
            inner.html(title);
        } else {
            inner.text(title);
        }
    }

    private void setHover(boolean b) {
        this.hover = b;
    }

    //TODO move this code in GwtQuery
    private void setOffset(GQuery $element, long top, long left){
        String position = $element.css("position", true);
        if ("static".equals(position)){
            $element.get(0).getStyle().setPosition(Position.RELATIVE);
        }

        Offset curOffset = $element.offset();
        String curCSSTop = $element.css("top", true);
        String curCSSLeft = $element.css("left", true);
        long curTop = 0;
        long curLeft = 0;

        if (("absolute".equals(position) || "fixed".equals(position)) && ("auto".equals(curCSSTop) || "auto".equals
                (curCSSLeft))){
            Offset curPosition = $element.position();
            curTop = curPosition.top;
            curLeft = curPosition.left;
        }else{
            try {
                curTop = Long.parseLong(curCSSTop);
            }catch(NumberFormatException e){
                curTop = 0;
            }

            try {
                curLeft = Long.parseLong(curCSSLeft);
            }catch(NumberFormatException e){
                curLeft = 0;
            }
        }

        long newTop = top - curOffset.top + curTop;
        long newLeft = left - curOffset.left + curLeft;

        $element.css("top",""+newTop).css("left", ""+newLeft);

    }

    private void setTimer(Timer timer) {
        assert this.timer == null : "timer should be first cancelled";
        this.timer = timer;
    }

    //TODO use GQuery.off() method when it will be implemented in GQuery :)
    private void unbind() {
        if (options.getSelector() != null) {
            //TODO we should add a namespace, but die doesn't support it yet
            $element.die();
        } else {
            $element.unbind(".tooltip");
        }
    }
}

