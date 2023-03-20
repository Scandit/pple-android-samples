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

package com.scandit.shelf.javaadvancedoverlaysample.ui.pricecheck;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.scandit.shelf.javaadvancedoverlaysample.catalog.CatalogStore;
import com.scandit.shelf.sdk.authentication.Authentication;
import com.scandit.shelf.sdk.catalog.ProductCatalog;
import com.scandit.shelf.sdk.common.CompletionHandler;
import com.scandit.shelf.sdk.core.ui.CaptureView;
import com.scandit.shelf.sdk.core.ui.viewfinder.ViewfinderConfiguration;
import com.scandit.shelf.sdk.price.PriceCheck;
import com.scandit.shelf.sdk.price.PriceCheckListener;
import com.scandit.shelf.sdk.price.PriceCheckResult;
import com.scandit.shelf.sdk.price.ui.PriceCheckOverlay;

import kotlin.Unit;

/**
 * A ViewModel that initializes and pauses price checking. It also allows to log the user out
 * from the organization.
 */
public class PriceCheckViewModel extends ViewModel implements PriceCheckListener {

    private PriceCheck priceCheck = null;

    private final MutableLiveData<PriceCheckResult> resultLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> logoutSucceededLiveData = new MutableLiveData<>();

    // Posts the price check result.
    public LiveData<PriceCheckResult> getResult() {
        return resultLiveData;
    }

    // Posts a Boolean when user is successfully logged out.
    public LiveData<Boolean> hasLogoutSucceeded() {
        return logoutSucceededLiveData;
    }

    public void initPriceCheck(CaptureView view, PriceCheckOverlay overlay, ViewfinderConfiguration viewfinderConfiguration) {
        // Get the ProductCatalog object previously stored in CatalogStore
        ProductCatalog catalog = CatalogStore.getInstance().getProductCatalog();

        // Initialize the PriceCheck object
        priceCheck = new PriceCheck(view, catalog);
        priceCheck.addListener(this);
        // Add a PriceCheckOverlay created in PriceCheckFragment.
        // By default, price labels are sought on the whole capture view. If you want to limit the scan area,
        // pass a non-null LocationSelection to PriceCheckOverlay's constructor.
        priceCheck.addOverlay(overlay);
        priceCheck.setViewfinderConfiguration(viewfinderConfiguration);
    }

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

    public void pauseAndLogout() {
        if (priceCheck != null) {
            priceCheck.disable(new CompletionHandler<Unit>() {
                @Override
                public void success(Unit result) {
                    logout();
                }

                @Override
                public void failure(@NonNull Exception error) {
                    logout();
                }
            });
        }
    }

    private void logout() {
        // Use the Authentication singleton to log user out of organization
        Authentication.logout(new CompletionHandler<Unit>() {
            @Override
            public void success(Unit result) {
                // User was logged out successfully. Communicate with PriceCheckFragment.
                logoutSucceededLiveData.postValue(true);
            }

            @Override
            public void failure(@NonNull Exception error) {
                // Logout failed. Communicate with PriceCheckFragment.
                logoutSucceededLiveData.postValue(false);
            }
        });
    }

    @Override
    public void onCorrectPrice(@NonNull PriceCheckResult priceCheckResult) {
        // Handle result that a Product label was scanned with correct price
        resultLiveData.postValue(priceCheckResult);
    }

    @Override
    public void onWrongPrice(@NonNull PriceCheckResult priceCheckResult) {
        // Handle result that a Product label was scanned with wrong price
        resultLiveData.postValue(priceCheckResult);
    }

    @Override
    public void onUnknownProduct(@NonNull PriceCheckResult priceCheckResult) {
        // Handle result that a Product label was scanned for an unknown Product
        resultLiveData.postValue(priceCheckResult);
    }
}
