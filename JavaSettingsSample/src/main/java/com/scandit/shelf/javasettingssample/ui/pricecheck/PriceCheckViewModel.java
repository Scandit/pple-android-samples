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

package com.scandit.shelf.javasettingssample.ui.pricecheck;

import android.content.Context;
import android.graphics.Color;

import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.scandit.shelf.javasettingssample.R;
import com.scandit.shelf.javasettingssample.catalog.CatalogStore;
import com.scandit.shelf.javasettingssample.repository.FeedbackRepository;
import com.scandit.shelf.javasettingssample.repository.OverlayRepository;
import com.scandit.shelf.javasettingssample.repository.ScanAreaRepository;
import com.scandit.shelf.javasettingssample.repository.ViewfinderRepository;
import com.scandit.shelf.sdk.catalog.ProductCatalog;
import com.scandit.shelf.sdk.common.CompletionHandler;
import com.scandit.shelf.sdk.core.area.RectangularLocationSelection;
import com.scandit.shelf.sdk.core.common.geometry.SizeWithUnit;
import com.scandit.shelf.sdk.core.feedback.Feedback;
import com.scandit.shelf.sdk.core.feedback.PriceCheckFeedback;
import com.scandit.shelf.sdk.core.feedback.Sound;
import com.scandit.shelf.sdk.core.feedback.Vibration;
import com.scandit.shelf.sdk.core.ui.CaptureView;
import com.scandit.shelf.sdk.core.ui.style.Brush;
import com.scandit.shelf.sdk.core.ui.viewfinder.ViewfinderConfiguration;
import com.scandit.shelf.sdk.price.PriceCheck;
import com.scandit.shelf.sdk.price.PriceCheckListener;
import com.scandit.shelf.sdk.price.PriceCheckResult;
import com.scandit.shelf.sdk.price.ui.AdvancedPriceCheckOverlay;
import com.scandit.shelf.sdk.price.ui.BasicPriceCheckOverlay;
import com.scandit.shelf.sdk.price.ui.DefaultPriceCheckAdvancedOverlayListener;

import kotlin.Unit;

/**
 * A ViewModel that initializes and pauses price checking. It also allows to log the user out
 * from the organization.
 */
public class PriceCheckViewModel extends ViewModel implements PriceCheckListener {

    private PriceCheck priceCheck = null;

    private final MutableLiveData<PriceCheckResult> resultLiveData = new MutableLiveData<>();

    // Posts the price check result.
    public LiveData<PriceCheckResult> getResult() {
        return resultLiveData;
    }

    public void initPriceCheck(
            CaptureView view,
            Context context
    ) {
        // Reset the price check result live data to null
        resultLiveData.setValue(null);

        // Get the ProductCatalog object previously stored in CatalogStore
        ProductCatalog catalog = CatalogStore.getInstance().getProductCatalog();

        // Initialize the PriceCheck object
        priceCheck = new PriceCheck(view, catalog);
        // Set sound and vibration feedback for scanned labels
        priceCheck.setFeedback(createPriceCheckFeedback());
        priceCheck.addListener(this);
        setPriceCheckOverlays(context);
        priceCheck.setViewfinderConfiguration(createViewfinderConfiguration());
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

    private void setPriceCheckOverlays(Context context) {
        OverlayRepository overlaySettings = OverlayRepository.getCurrentSettings();
        if (overlaySettings.isBasicOverlayEnabled()) {
            // Add BasicPriceCheckOverlay that draw highlights on top of captured labels
            BasicPriceCheckOverlay basicPriceCheckOverlay = createBasicPriceCheckOverlay(context, overlaySettings);
            priceCheck.addOverlay(basicPriceCheckOverlay);
        }
        if (overlaySettings.isAdvancedOverlayEnabled()) {
            // Add AdvancedPriceCheckOverlay that draw views on top of captured labels
            AdvancedPriceCheckOverlay advancedPriceCheckOverlay = createAdvancedPriceCheckOverlay();
            priceCheck.addOverlay(advancedPriceCheckOverlay);
        }
    }

    @NonNull
    private BasicPriceCheckOverlay createBasicPriceCheckOverlay(Context context, OverlayRepository overlaySettings) {
        return new BasicPriceCheckOverlay(
                createBrush(context, overlaySettings.getCorrectPriceBrush().colorResource),
                createBrush(context, overlaySettings.getWrongPriceBrush().colorResource),
                createBrush(context, overlaySettings.getUnknownProductBrush().colorResource)
        );
    }

    @NonNull
    private Brush createBrush(Context context, @ColorRes int colorResource) {
        return new Brush(ContextCompat.getColor(context, colorResource), Color.TRANSPARENT, 0f);
    }

    @NonNull
    private AdvancedPriceCheckOverlay createAdvancedPriceCheckOverlay() {
        return new AdvancedPriceCheckOverlay(new DefaultPriceCheckAdvancedOverlayListener());
    }

    private PriceCheckFeedback createPriceCheckFeedback() {
        FeedbackRepository feedbackSettings = FeedbackRepository.getCurrentSettings();
        Feedback correctPriceFeedback = createFeedback(
                feedbackSettings.isCorrectPriceSoundEnabled(),
                feedbackSettings.isCorrectPriceVibrationEnabled()
        );
        Feedback wrongPriceFeedback = createFeedback(
                feedbackSettings.isWrongPriceSoundEnabled(),
                feedbackSettings.isWrongPriceVibrationEnabled()
        );
        Feedback unknownProductFeedback = createFeedback(
                feedbackSettings.isUnknownProductSoundEnabled(),
                feedbackSettings.isUnknownProductVibrationEnabled()
        );
        return new PriceCheckFeedback(correctPriceFeedback, wrongPriceFeedback, unknownProductFeedback);
    }

    private Feedback createFeedback(boolean soundEnabled, boolean vibrationEnabled) {
        Sound soundFeedback = createSoundFeedback(soundEnabled);
        Vibration vibrationFeedback = createVibrationFeedback(vibrationEnabled);
        return new Feedback(soundFeedback, vibrationFeedback);
    }

    private Sound createSoundFeedback(boolean soundEnabled) {
        if (soundEnabled) {
            return new Sound.ResourceSound(R.raw.scan);
        } else {
            return null;
        }
    }

    private Vibration createVibrationFeedback(boolean vibrationEnabled) {
        if (vibrationEnabled) {
            return new Vibration();
        } else {
            return null;
        }
    }

    private ViewfinderConfiguration createViewfinderConfiguration() {
        ScanAreaRepository scanAreaSettings = ScanAreaRepository.getCurrentSettings();
        ViewfinderRepository viewfinderRepository = ViewfinderRepository.getCurrentSettings();
        ViewfinderConfiguration config = new ViewfinderConfiguration(
                viewfinderRepository.getCurrentViewfinder(),
                RectangularLocationSelection.withSize(
                        new SizeWithUnit(
                                scanAreaSettings.getWidth(),
                                scanAreaSettings.getHeight()
                        )
                )
        );
        config.setShouldShowScanAreaGuides(scanAreaSettings.shouldShowScanAreaGuides());
        return config;
    }
}
