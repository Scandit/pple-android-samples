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

package com.scandit.shelf.javasimplesample;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.scandit.shelf.sdk.authentication.Authentication;
import com.scandit.shelf.sdk.catalog.Catalog;
import com.scandit.shelf.sdk.catalog.Currency;
import com.scandit.shelf.sdk.catalog.ProductCatalog;
import com.scandit.shelf.sdk.catalog.Store;
import com.scandit.shelf.sdk.common.CompletionHandler;
import com.scandit.shelf.sdk.core.ui.CaptureView;
import com.scandit.shelf.sdk.core.ui.viewfinder.ViewfinderConfiguration;
import com.scandit.shelf.sdk.price.FrameData;
import com.scandit.shelf.sdk.price.PriceCheck;
import com.scandit.shelf.sdk.price.PriceCheckListener;
import com.scandit.shelf.sdk.price.PriceCheckResult;
import com.scandit.shelf.sdk.price.PriceLabelSession;
import com.scandit.shelf.sdk.price.ui.PriceCheckOverlay;

import java.util.List;
import java.util.Locale;

import kotlin.Unit;

/**
 * A ViewModel for the MainActivity.
 */
public class MainActivityViewModel extends ViewModel implements PriceCheckListener {

    // Enter your Scandit ShelfView credentials here.
    private static final String PPLE_USERNAME = "-- ENTER YOUR SCANDIT SHELFVIEW USERNAME HERE --";
    private static final String PPLE_PASSWORD = "-- ENTER YOUR SCANDIT SHELFVIEW PASSWORD HERE --";

    private ProductCatalog catalog = null;
    private PriceCheck priceCheck = null;

    private final MutableLiveData<SnackbarData> snackbarLiveData = new MutableLiveData<>();
    private final MutableLiveData<Status> statusLiveData = new MutableLiveData<>(Status.INIT);
    private final MutableLiveData<Store> currentStore = new MutableLiveData<>();

    // Posts the status of the price check setup.
    public LiveData<Status> getStatus() {
        return statusLiveData;
    }

    // Posts the snackbar data to the observing Activity to be displayed on the screen.
    public LiveData<SnackbarData> getSnackbarData() {
        return snackbarLiveData;
    }

    // Posts currently selected store.
    public LiveData<Store> getCurrentStore() {
        return currentStore;
    }

    // Perform the initial steps required for the price checking process, including:
    // - ShelfView authentication,
    // - fetching the list of stores belonging to your organization,
    // - preparing the ProductCatalog
    public void authenticateAndFetchData() {
        authenticate();
    }

    // Prepare the PriceCheck instance. Should only be called after the ProductCatalog is created,
    // otherwise the method will have no effect.
    public void initPriceCheck(
            CaptureView view,
            PriceCheckOverlay overlay,
            ViewfinderConfiguration viewfinderConfiguration
    ) {
        // Initialize the PriceCheck object
        priceCheck = new PriceCheck(view, catalog);
        priceCheck.addListener(this);
        // Add a PriceCheckOverlay and set the viewfinder configuration for a better user experience.
        priceCheck.addOverlay(overlay);
        priceCheck.setViewfinderConfiguration(viewfinderConfiguration);
    }

    // Enables the price check, which will start the camera and begin processing the camera frames.
    // Should only be called after the priceCheck instance has been initialized,
    // otherwise the method will have no effect.
    public void resumePriceCheck() {
        if (priceCheck != null) {
            priceCheck.enable(new CompletionHandler<Unit>() {
                @Override
                public void success(Unit result) {
                    // Handle price checking enable success
                }

                @Override
                public void failure(@NonNull Exception error) {
                    // Gracefully handle price checking enable failure
                }
            });
        }
    }

    // Disables the price check, which will stop the camera and the processing of the camera frames.
    // Should only be called after the priceCheck instance has been initialized,
    // otherwise the method will have no effect.
    public void pausePriceCheck() {
        if (priceCheck != null) {
            priceCheck.disable(new CompletionHandler<Unit>() {
                @Override
                public void success(Unit result) {
                    // Handle price checking disable success
                }

                @Override
                public void failure(@NonNull Exception error) {
                    // Gracefully handle price checking disable failure
                }
            });
        }
    }

    // Disposes price check to release resources consumed by it.
    public void onDestroyPriceCheck() {
        if (priceCheck != null) {
            priceCheck.dispose();
            priceCheck = null;
        }
        statusLiveData.setValue(Status.INIT);
    }

    @Override
    public void onCorrectPrice(@NonNull PriceCheckResult priceCheckResult) {
        // Handle result that a Product label was scanned with correct price - in our case,
        // we will pass details of a snackbar to the Activity to be displayed on a screen.
        snackbarLiveData.postValue(new SnackbarData(toMessage(priceCheckResult), R.color.transparentGreen));
    }

    @Override
    public void onWrongPrice(@NonNull PriceCheckResult priceCheckResult) {
        // Handle result that a Product label was scanned with wrong price - in our case,
        // we will pass details of a snackbar to the Activity to be displayed on a screen.
        snackbarLiveData.postValue(new SnackbarData(toMessage(priceCheckResult), R.color.transparentRed));
    }

    @Override
    public void onUnknownProduct(@NonNull PriceCheckResult priceCheckResult) {
        // Handle result that a Product label was scanned for an unknown Product - in our case,
        // we will pass details of a snackbar to the Activity to be displayed on a screen.
        snackbarLiveData.postValue(new SnackbarData(toMessage(priceCheckResult), R.color.transparentGrey));
    }

    @Override
    public void onSessionUpdate(@NonNull PriceLabelSession session, @NonNull FrameData frameData) {
        // Callback containing PriceLabelSession and FrameData.
    }

    public void authenticate() {
        // Use the PPLE Authentication singleton to log in the user to an organization.
        Authentication.login(
                PPLE_USERNAME,
                PPLE_PASSWORD,
                new CompletionHandler<Unit>() {
                    @Override
                    public void success(Unit result) {
                        getStores();
                    }

                    @Override
                    public void failure(@NonNull Exception error) {
                        statusLiveData.postValue(Status.AUTH_FAILED);
                    }
                }
        );
    }

    private void getStores() {
        // Get/update the list of stores by using the Catalog singleton object of the PPLE SDK.
        // Pass a CompletionHandler to the getStores method for handling API result.
        Catalog.getStores(new CompletionHandler<List<Store>>() {
            @Override
            public void success(List<Store> result) {
                if (result.isEmpty()) {
                    statusLiveData.postValue(Status.STORES_EMPTY);
                } else {
                    // Get products for a selected store from your organization.
                    // For simplicity reasons, below we select the first store on the list.
                    Store store = result.get(0);
                    currentStore.postValue(store);
                    getProducts(store);
                }
            }

            @Override
            public void failure(@NonNull Exception error) {
                statusLiveData.postValue(Status.STORE_DOWNLOAD_FAILED);
            }
        });
    }

    private void getProducts(Store store) {
        // Get/update the Product items for a given Store.

        // First create the ProductCatalog object.

        // If you are using the ShelfView backend as your product catalog provider, you only need to specify the Store,
        // for which you will perform the Price Check - just like in the code below.

        // If on the other hand, you would like to use a different source of data for the ProductCatalog,
        // you should should pass your custom implementation of the ProductProvider interface, as the second argument
        // for the Catalog.getProductCatalog method - check the docs for more details.
        catalog = Catalog.getProductCatalog(store);
        // Now we can update the list of Products by calling update() on ProductCatalog.
        catalog.update(
                new CompletionHandler<Unit>() {
                    @Override
                    public void success(Unit result) {
                        statusLiveData.postValue(Status.READY);
                    }

                    @Override
                    public void failure(@NonNull Exception error) {
                        statusLiveData.postValue(Status.CATALOG_DOWNLOAD_FAILED);
                    }
                });
    }

    private String toMessage(PriceCheckResult result) {
        if (result.getCorrectPrice() == null) {
            return "Unrecognized product - captured price: " + priceFormat(result.getCapturedPrice());
        } else if (result.getCapturedPrice() == result.getCorrectPrice()) {
            return result.getName() + "\nCorrect Price: " + priceFormat(result.getCapturedPrice());
        } else {
            return result.getName() + "\nWrong Price: " + priceFormat(result.getCapturedPrice())
                    + ", should be " + priceFormat(result.getCorrectPrice());
        }
    }

    private String priceFormat(float price) {
        Currency currency = currentStore.getValue().getCurrency();
        return String.format(
                Locale.getDefault(),
                currency.getSymbol() + "%." + currency.getDecimalPlaces() + "f", price
        );
    }
}
