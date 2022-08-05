package com.scandit.shelf.javaapp.ui.pricecheck;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.scandit.shelf.authentication.Authentication;
import com.scandit.shelf.catalog.ProductCatalog;
import com.scandit.shelf.common.CompletionHandler;
import com.scandit.shelf.core.ui.CaptureView;
import com.scandit.shelf.javaapp.catalog.CatalogStore;
import com.scandit.shelf.price.PriceCheck;
import com.scandit.shelf.price.PriceCheckListener;
import com.scandit.shelf.price.PriceCheckResult;
import com.scandit.shelf.price.ui.PriceCheckOverlay;

import kotlin.Unit;

public class PriceCheckViewModel extends ViewModel implements PriceCheckListener {

    private PriceCheck priceCheck = null;

    private final MutableLiveData<PriceCheckResult> _resultLiveData = new MutableLiveData<>();
    LiveData<PriceCheckResult> resultLiveData = _resultLiveData;

    private final MutableLiveData<Boolean> _logoutSucceededLiveData = new MutableLiveData<>();
    LiveData<Boolean> logoutSucceededLiveData = _logoutSucceededLiveData;

    public void initPriceCheck(CaptureView view, PriceCheckOverlay overlay) {
        ProductCatalog catalog = CatalogStore.getInstance().getProductCatalog();

        priceCheck = new PriceCheck(view, catalog);
        priceCheck.addListener(this);
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

    public void logout() {
        Authentication.logout(new CompletionHandler<Unit>() {
            @Override
            public void success(Unit result) {
                _logoutSucceededLiveData.postValue(true);
            }

            @Override
            public void failure(@NonNull Exception error) {
                _logoutSucceededLiveData.postValue(false);
            }
        });
    }

    @Override
    public void onCorrectPrice(@NonNull PriceCheckResult priceCheckResult) {
        _resultLiveData.postValue(priceCheckResult);
    }

    @Override
    public void onWrongPrice(@NonNull PriceCheckResult priceCheckResult) {
        _resultLiveData.postValue(priceCheckResult);
    }

    @Override
    public void onUnknownProduct(@NonNull PriceCheckResult priceCheckResult) {
        _resultLiveData.postValue(priceCheckResult);
    }
}
