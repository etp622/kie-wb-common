/*
 * Copyright 2013 JBoss Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.kie.workbench.common.screens.projecteditor.client.forms;

import com.google.gwt.user.client.ui.IsWidget;

public interface CRUDListBoxView
        extends HasRemoveItemHandlers,
        HasAddItemHandlers,
        IsWidget {


    public interface Presenter {

        void onAdd();

        void onDelete();
    }

    void setPresenter(Presenter presenter);

    void addItem(String name);

    String getSelectedItem();

    void removeItem(String itemName);

    void addItemAndFireEvent(String name);

    void makeReadOnly();

    void makeEditable();
}
