/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.scandit.shelf.javasimplesample.ui.storeselection;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.scandit.shelf.sdk.catalog.Store;

/**
 * A ViewModel that exposes the currently selected Store to Activity/Fragment
 */
public class StoreItemViewModel {
    // Communicates with Fragment when Store selection in stores list changes.
    private final MutableLiveData<Store> selectedStoreLiveData;

    // Refers to the current Store item bound through StoresAdapter
    private Store item = null;

    private final MutableLiveData<String> storeName = new MutableLiveData<>();

    // Observes Store item binding in StoresAdapter
    public LiveData<String> getStoreName() {
        return storeName;
    }

    public StoreItemViewModel(MutableLiveData<Store> selectedStoreLiveData) {
        this.selectedStoreLiveData = selectedStoreLiveData;
    }

    public void bind(Store store) {
        item = store;
        storeName.postValue(store.getName());
    }

    public void onClick() {
        if (item != null) {
            selectedStoreLiveData.postValue(item);
        }
    }
}
