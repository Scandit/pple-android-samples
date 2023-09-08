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
import com.scandit.shelf.javasettingssample.repository.FlowRepository;
import com.scandit.shelf.javasettingssample.repository.OverlayRepository;
import com.scandit.shelf.javasettingssample.repository.ScanAreaRepository;
import com.scandit.shelf.javasettingssample.repository.ViewfinderRepository;
import com.scandit.shelf.sdk.catalog.Currency;
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
import com.scandit.shelf.sdk.price.FrameData;
import com.scandit.shelf.sdk.price.PriceCheck;
import com.scandit.shelf.sdk.price.PriceCheckListener;
import com.scandit.shelf.sdk.price.PriceCheckResult;
import com.scandit.shelf.sdk.price.PriceLabelSession;
import com.scandit.shelf.sdk.price.ui.AdvancedPriceCheckOverlay;
import com.scandit.shelf.sdk.price.ui.BasicPriceCheckOverlay;
import com.scandit.shelf.sdk.price.ui.DefaultPriceCheckAdvancedOverlayListener;

import java.util.Locale;

import kotlin.Unit;

/**
 * A ViewModel that initializes and pauses price checking. It also allows to log the user out
 * from the organization.
 */
public class PriceCheckViewModel extends ViewModel implements PriceCheckListener {

    private PriceCheck priceCheck = null;

    private final FlowRepository flowSettings = FlowRepository.getCurrentSettings();
    private final OverlayRepository overlaySettings = OverlayRepository.getCurrentSettings();

    private final MutableLiveData<SnackbarData> snackbarLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isNonContinuousFlowPausedLiveData = new MutableLiveData<>();
    private final MutableLiveData<PriceLabelSession> sessionLiveData = new MutableLiveData<>();

    // Posts the message to be displayed on a snackbar to the observing Fragment.
    public LiveData<SnackbarData> getSnackbarData() {
        return snackbarLiveData;
    }

    // Posts whether non-continuous price check flow is paused via LiveData to the observing Fragment.
    public LiveData<Boolean> getIsNonContinuousFlowPaused() {
        return isNonContinuousFlowPausedLiveData;
    }

    // Posts the PriceLabelSessions via LiveData to the observing Fragment.
    public LiveData<PriceLabelSession> getSession() {
        return sessionLiveData;
    }

    public void initPriceCheck(
            CaptureView view,
            Context context
    ) {
        // Reset the snackbar live data to null.
        snackbarLiveData.setValue(null);

        // Get the ProductCatalog object previously stored in CatalogStore.
        ProductCatalog catalog = CatalogStore.getInstance().getProductCatalog();

        if (priceCheck != null) {
            // Dispose previously used PriceCheck instance, to release some resources held by it.
            priceCheck.dispose();
        }
        // Initialize the PriceCheck object.
        priceCheck = new PriceCheck(view, catalog);
        // Set sound and vibration feedback for scanned labels.
        priceCheck.setFeedback(createPriceCheckFeedback());
        priceCheck.addListener(this);
        setPriceCheckOverlays(context);
        priceCheck.setViewfinderConfiguration(createViewfinderConfiguration());
    }

    public void resumePriceCheck() {
        if (!flowSettings.isContinuousFlowEnabled()) {
            isNonContinuousFlowPausedLiveData.postValue(false);
        }
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
        if (!flowSettings.isContinuousFlowEnabled()) {
            isNonContinuousFlowPausedLiveData.postValue(true);
        }
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

    public boolean isCustomOverlayEnabled() {
        return overlaySettings.isCustomOverlayEnabled();
    }

    @Override
    public void onCorrectPrice(@NonNull PriceCheckResult priceCheckResult) {
        // Handle result that a Product label was scanned with correct price - in our case,
        // we will pass details of a snackbar to the Fragment to be displayed on a screen.
        snackbarLiveData.postValue(new SnackbarData(toMessage(priceCheckResult), R.color.transparentGreen));
    }

    @Override
    public void onWrongPrice(@NonNull PriceCheckResult priceCheckResult) {
        // Handle result that a Product label was scanned with wrong price - in our case,
        // we will pass details of a snackbar to the Fragment to be displayed on a screen.
        snackbarLiveData.postValue(new SnackbarData(toMessage(priceCheckResult), R.color.transparentRed));
    }

    @Override
    public void onUnknownProduct(@NonNull PriceCheckResult priceCheckResult) {
        // Handle result that a Product label was scanned for an unknown Product - in our case,
        // we will pass details of a snackbar to the Fragment to be displayed on a screen.
        snackbarLiveData.postValue(new SnackbarData(toMessage(priceCheckResult), R.color.transparentGrey));
    }

    @Override
    public void onSessionUpdate(@NonNull PriceLabelSession session, @NonNull FrameData frameData) {
        // Post the sessions to LiveData for CustomOverlayView used by the Fragment, if the overlay is enabled.
        if (overlaySettings.isCustomOverlayEnabled()) {
            sessionLiveData.postValue(session);
        }

        // Condition for pausing the price check process when continuous flow is disabled and a label has been captured.
        if (!flowSettings.isContinuousFlowEnabled() && !session.getAddedLabels().isEmpty()) {
            pausePriceCheck();
        }
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
        Currency currency = CatalogStore.getInstance().getStore().getCurrency();
        return String.format(
                Locale.getDefault(),
                currency.getSymbol() + "%." + currency.getDecimalPlaces() + "f", price
        );
    }

    private void setPriceCheckOverlays(Context context) {
        if (overlaySettings.isBasicOverlayEnabled()) {
            // Add BasicPriceCheckOverlay that draw highlights on top of captured labels
            BasicPriceCheckOverlay basicPriceCheckOverlay = createBasicPriceCheckOverlay(context);
            priceCheck.addOverlay(basicPriceCheckOverlay);
        }
        if (overlaySettings.isAdvancedOverlayEnabled()) {
            // Add AdvancedPriceCheckOverlay that draw views on top of captured labels
            AdvancedPriceCheckOverlay advancedPriceCheckOverlay = createAdvancedPriceCheckOverlay();
            priceCheck.addOverlay(advancedPriceCheckOverlay);
        }
    }

    @NonNull
    private BasicPriceCheckOverlay createBasicPriceCheckOverlay(Context context) {
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
