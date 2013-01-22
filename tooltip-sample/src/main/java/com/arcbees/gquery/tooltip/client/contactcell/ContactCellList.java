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

package com.arcbees.gquery.tooltip.client.contactcell;

import com.arcbees.gquery.tooltip.client.contactcell.ContactDatabase.ContactInfo;
import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.cellview.client.HasKeyboardPagingPolicy.KeyboardPagingPolicy;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.view.client.RangeChangeEvent.Handler;

public class ContactCellList extends Composite {
    /**
     * The Cell used to render a {@link ContactInfo}. Code coming from the GWT
     * showcase
     */
    private static class ContactCell extends AbstractCell<ContactInfo> {
        public ContactCell() {
        }

        @Override
        public void render(Context context, ContactInfo value, SafeHtmlBuilder sb) {
            // Value can be null, so do a null check..
            if (value == null) {
                return;
            }

            sb.append(ContactTemplates.INSTANCE.contactCell(value.getLastName(), "" + value.getId()));
        }
    }

    public ContactCellList() {
        ShowMorePagerPanel pagerPanel = new ShowMorePagerPanel();
        pagerPanel.setDisplay(initCellList());

        initWidget(pagerPanel);
    }

    private CellList<ContactInfo> initCellList() {
        ContactCell contactCell = new ContactCell();

        CellList<ContactInfo> cellList = new CellList<ContactInfo>(contactCell,
                ContactDatabase.ContactInfo.KEY_PROVIDER) {

            @Override
            public HandlerRegistration addRangeChangeHandler(Handler handler) {
                return super.addRangeChangeHandler(handler);    //To change body of overridden methods use File |
                // Settings | File Templates.
            }
        };

        cellList.setPageSize(30);
        cellList.setKeyboardPagingPolicy(KeyboardPagingPolicy.INCREASE_RANGE);

        ContactDatabase.get().addDataDisplay(cellList);

        return cellList;
    }
}
