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

package com.arcbees.gquery.tooltip.client.contactcell;

import com.arcbees.gquery.tooltip.client.contactcell.ContactDatabase.ContactInfo;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.TextColumn;

public class ContactCellTable extends CellTable<ContactInfo> {
    public ContactCellTable() {
        super(30);

        initColumns();
        setKeyboardPagingPolicy(KeyboardPagingPolicy.INCREASE_RANGE);
    }

    private void initColumns() {
        TextColumn<ContactInfo> firstNameColumn = new TextColumn<ContactInfo>() {
            @Override
            public String getValue(ContactInfo contactInfo) {
                return contactInfo.getFirstName();
            }
        };

        TextColumn<ContactInfo> lastNameColumn = new TextColumn<ContactInfo>() {
            @Override
            public String getValue(ContactInfo contactInfo) {
                return contactInfo.getLastName();
            }
        };

        TextColumn<ContactInfo> addressColumn = new TextColumn<ContactInfo>() {
            @Override
            public String getValue(ContactInfo contactInfo) {
                return contactInfo.getAddress();
            }
        };

        addColumn(firstNameColumn, "First Name");
        addColumn(lastNameColumn, "Last Name");
        addColumn(addressColumn, "Address");
    }
}
