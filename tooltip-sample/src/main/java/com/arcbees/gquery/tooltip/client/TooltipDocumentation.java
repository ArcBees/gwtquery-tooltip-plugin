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

import com.arcbees.gquery.tooltip.client.TooltipOptions.TooltipContentProvider;
import com.arcbees.gquery.tooltip.client.TooltipOptions.TooltipPlacement;
import com.arcbees.gquery.tooltip.client.TooltipOptions.TooltipPlacementProvider;
import com.arcbees.gquery.tooltip.client.TooltipOptions.TooltipTrigger;
import com.arcbees.gquery.tooltip.client.contactcell.ContactCellList;
import com.arcbees.gquery.tooltip.client.contactcell.ContactCellTable;
import com.arcbees.gquery.tooltip.client.contactcell.ContactDatabase;
import com.arcbees.gquery.tooltip.client.contactcell.ContactDatabase.ContactInfo;
import com.arcbees.gquery.tooltip.client.contactcell.ContactTemplates;
import com.arcbees.gquery.tooltip.client.contactcell.ContactTooltipResources;
import com.arcbees.gquery.tooltip.client.contactcell.ShowMorePagerPanel;
import com.arcbees.gquery.tooltip.client.resource.OffsetTooltipResources;
import com.arcbees.gquery.tooltip.client.resource.ValidationTooltipResources;
import com.arcbees.gquery.tooltip.client.resource.WidgetTooltipResources;
import com.arcbees.gquery.tooltip.client.widget.RichTextToolbar;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.query.client.Function;
import com.google.gwt.query.client.GQuery;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.RichTextArea;
import com.google.gwt.user.client.ui.RootPanel;

import static com.arcbees.gquery.tooltip.client.Tooltip.Tooltip;
import static com.google.gwt.query.client.GQuery.$;

public class TooltipDocumentation implements EntryPoint {
    private static final SafeHtml contactImage = AbstractImagePrototype.create(ContactTooltipResources.INSTANCE
            .contact()).getSafeHtml();

    public void onModuleLoad() {
        //use the tooltip plugin on all elements having a title attribute
        $("[title]").as(Tooltip).tooltip();

        setupMenuTooltip();

        setupCellListTooltip();

        setupFormTooltip();

        setupCellTableWidgetProviderTooltip();
        setupWidgetTooltip();

        //dynamic tooltip
        $("#dynamicTooltip").as(Tooltip).tooltip(new TooltipOptions().withContent(new TooltipContentProvider() {
            private int counter = 0;

            @Override
            public String getContent(Element element) {
                counter++;
                return "This tooltip has been displayed " + counter + (counter > 1 ? " times" : " time");
            }
        }));

        $("#dynamicPlacement").as(Tooltip).tooltip(new TooltipOptions()
                .withPlacement(new TooltipPlacementProvider() {
                    private int counter = 0;

                    @Override
                    public TooltipPlacement getPlacement(Element element) {
                        return TooltipPlacement.values()[counter++ % TooltipPlacement.values()
                                .length];
                    }
                }).withContent("Placement is dynamic"));

        $("#offsetTooltip").as(Tooltip).tooltip(new TooltipOptions().withOffset(new GQuery.Offset(-110, 0))
                .withResources(OffsetTooltipResources.INSTANCE)
                .withContent("We override the resources file in order to move the arrow to the right"));
    }

    private void setupWidgetTooltip() {
        HTMLPanel htmlPanel = new HTMLPanel("<b>Hover here to show a widget</b>");
        RootPanel.get("widget").add(htmlPanel);

        ContactDatabase contactDatabase = ContactDatabase.get();
        ContactCellList ccl = new ContactCellList(contactDatabase);

        TooltipOptions options = new TooltipOptions();
        options.withContent(ccl);
        options.withPlacement(TooltipPlacement.LEFT);
        options.withResources(WidgetTooltipResources.INSTANCE);
        options.withContainer("element");

        $(htmlPanel).as(Tooltip).tooltip(options);
    }

    private void setupCellTableWidgetProviderTooltip() {
        final ContactCellTable contactCellTable = new ContactCellTable();

        final ContactDatabase contactDatabase = ContactDatabase.get();
        contactDatabase.addDataDisplay(contactCellTable);

        ShowMorePagerPanel pagerPanel = new ShowMorePagerPanel();
        pagerPanel.setDisplay(contactCellTable);
        pagerPanel.addStyleName("pagerLarge");

        RootPanel.get("contactWidgetProvider").add(pagerPanel);

        TooltipOptions options = new TooltipOptions();

        //provide dynamic content
        options.withContent(new TooltipOptions.TooltipWidgetContentProvider() {
            @Override
            public IsWidget getContent(Element element) {
                int absoluteRowIndex = Integer.valueOf($(element).attr("__gwt_row"));
                int pageStartIndex = contactCellTable.getVisibleRange().getStart();
                int relativeIndex = absoluteRowIndex - pageStartIndex;

                final ContactInfo contactInfo = contactCellTable.getVisibleItem(relativeIndex);
                Button deleteButton = new Button("Delete");
                deleteButton.addClickHandler(new ClickHandler() {
                    @Override
                    public void onClick(ClickEvent event) {
                        contactDatabase.remove(contactInfo);
                    }
                });

                return deleteButton;
            }
        });

        options.withResources(ContactTooltipResources.INSTANCE);
        options.withPlacement(TooltipPlacement.LEFT);
        options.withSelector("tbody tr");
        options.withContainer("element");

        //apply plugin to the cell table
        $(contactCellTable).as(Tooltip).tooltip(options);
    }

    private void setupCellListTooltip() {
        final ContactDatabase contactDatabase = ContactDatabase.get();

        //CellList with pager
        ContactCellList ccl = new ContactCellList(contactDatabase);
        RootPanel.get("contactCellList").add(ccl);

        TooltipOptions options = new TooltipOptions();

        //provide dynamic content
        options.withContent(new TooltipContentProvider() {
            @Override
            public String getContent(Element element) {
                Integer id = Integer.parseInt(element.getAttribute("data-contact-id"));
                ContactInfo contact = contactDatabase.queryContactById(id);

                return ContactTemplates.INSTANCE.contactCellTooltip(contactImage, contact.getFullName(),
                        contact.getAddress()).asString();
            }
        });

        options.withResources(ContactTooltipResources.INSTANCE);
        options.withPlacement(TooltipPlacement.RIGHT);
        // event delegation : the plugin will run on all elements inside the CellList having 'tooltipable' as css
        // class, present in the dom or added in the future.
        options.withSelector(".tooltipable");
        options.withHtml(true);

        //apply plugin to the cell list
        $(ccl).as(Tooltip).tooltip(options);
    }

    private void setupMenuTooltip() {
        // Create the text area and toolbar
        RichTextArea area = new RichTextArea();
        area.setSize("100%", "30px");
        RichTextToolbar toolbar = new RichTextToolbar(area);
        toolbar.ensureDebugId("cwRichText-toolbar");
        toolbar.setWidth("100%");

        RootPanel.get("widgetSample").add(toolbar);
        RootPanel.get("widgetSample").add(area);

        //apply tooltip plugin on element of the toolbar defining a title
        $("[title]", toolbar).as(Tooltip).tooltip(new TooltipOptions().withDelayShow(200).withDelayHide(100)
                .withContainer("element"));
    }

    private void setupFormTooltip() {
        $("#loginButton").click(new Function() {
            @Override
            public void f() {

                String userName = $("#userNameField").val();
                if (userName == null || userName.length() == 0) {
                    $("#userNameField").as(Tooltip).show().addClass("invalid");
                } else {
                    $("#userNameField").as(Tooltip).hide().removeClass("invalid");
                }

                String password = $("#passwordField").val();
                if (password == null || password.length() == 0) {
                    $("#passwordField").as(Tooltip).show().addClass("invalid");
                } else {
                    $("#passwordField").as(Tooltip).hide().removeClass("invalid");
                }
            }
        });

        $("#passwordField").as(Tooltip).tooltip(new TooltipOptions()
                .withContent("Password cannot be empty")
                .withTrigger(TooltipTrigger.MANUAL)
                .withPlacement(TooltipPlacement.RIGHT)
                .withResources(ValidationTooltipResources.INSTANCE));

        $("#userNameField").as(Tooltip).tooltip(new TooltipOptions()
                .withContent("Username cannot be empty")
                .withTrigger(TooltipTrigger.MANUAL)
                .withPlacement(TooltipPlacement.RIGHT)
                .withResources(ValidationTooltipResources.INSTANCE));
    }
}
