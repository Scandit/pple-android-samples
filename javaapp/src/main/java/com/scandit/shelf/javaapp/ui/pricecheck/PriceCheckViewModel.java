package com.scandit.shelf.javaapp.ui.pricecheck;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.scandit.shelf.sdk.catalog.ProductCatalog;
import com.scandit.shelf.sdk.common.CompletionHandler;
import com.scandit.shelf.sdk.price.PriceCheckResult;
import com.scandit.shelf.sdk.price.ui.PriceCheckOverlay;
import com.scandit.shelf.javaapp.catalog.CatalogStore;
import com.scandit.shelf.sdk.authentication.Authentication;
import com.scandit.shelf.sdk.core.ui.CaptureView;
import com.scandit.shelf.sdk.price.PriceCheck;
import com.scandit.shelf.sdk.price.PriceCheckListener;

import kotlin.Unit;

/**
 * A ViewModel that initializes and pauses price checking. It also allows to log the user out
 * from the organization.
 */
public class PriceCheckViewModel extends ViewModel implements PriceCheckListener {

    private PriceCheck priceCheck = null;

    private final MutableLiveData<PriceCheckResult> _resultLiveData = new MutableLiveData<>();
    // Posts the price check result.
    LiveData<PriceCheckResult> resultLiveData = _resultLiveData;

    private final MutableLiveData<Boolean> _logoutSucceededLiveData = new MutableLiveData<>();
    // Posts a Boolean when user is successfully logged out.
    LiveData<Boolean> logoutSucceededLiveData = _logoutSucceededLiveData;

    public void initPriceCheck(CaptureView view, PriceCheckOverlay overlay) {
        // Get the ProductCatalog object previously stored in CatalogStore
        ProductCatalog catalog = CatalogStore.getInstance().getProductCatalog();

        // Initialize the PriceCheck object
        priceCheck = new PriceCheck(view, catalog);
        priceCheck.addListener(this);
        // Add a PriceCheckOverlay created in PriceCheckFragment
        priceCheck.setOverlay(overlay);
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
                _logoutSucceededLiveData.postValue(true);
            }

            @Override
            public void failure(@NonNull Exception error) {
                // Logout failed. Communicate with PriceCheckFragment.
                _logoutSucceededLiveData.postValue(false);
            }
        });
    }

    @Override
    public void onCorrectPrice(@NonNull PriceCheckResult priceCheckResult) {
        // Handle result that a Product label was scanned with correct price
        _resultLiveData.postValue(priceCheckResult);
    }

    @Override
    public void onWrongPrice(@NonNull PriceCheckResult priceCheckResult) {
        // Handle result that a Product label was scanned with wrong price
        _resultLiveData.postValue(priceCheckResult);
    }

    @Override
    public void onUnknownProduct(@NonNull PriceCheckResult priceCheckResult) {
        // Handle result that a Product label was scanned for an unknown Product
        _resultLiveData.postValue(priceCheckResult);
    }
}
