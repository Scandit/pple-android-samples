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

package com.scandit.shelf.javasettingssample.ui.overlay;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.scandit.shelf.javasettingssample.R;
import com.scandit.shelf.javasettingssample.catalog.CatalogStore;
import com.scandit.shelf.sdk.catalog.Currency;
import com.scandit.shelf.sdk.core.common.geometry.Quadrilateral;
import com.scandit.shelf.sdk.core.ui.CaptureView;
import com.scandit.shelf.sdk.price.PriceLabel;
import com.scandit.shelf.sdk.price.PriceLabelSession;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * A view, demonstrating an exemplary implementation of a custom overlay view for PriceCheck - an alternative to
 * PriceCheckOverlays provided by the SDK: the BasicPriceCheckOverlay and the AdvancedPriceCheckOverlay.
 * <p>
 * This view is supposed to draw augmentations on top of the detected labels visible in the CameraView.
 * In this rudimentary implementation, the augmentation is simply a TextView displaying the price read
 * from the label, on a semi-transparent blue background.
 * CustomOverlayView needs to be informed about every new PriceLabelSession (check out the
 * updateOverlay(PriceLabelSession session, CaptureView captureView) method below) so that it can properly update
 * the visibility and position of every augmentation.
 */
public class CustomOverlayView extends FrameLayout {

    private Map<Integer, TextView> augmentationViews = new HashMap<>();

    public CustomOverlayView(@NonNull Context context) {
        super(context);
    }

    public CustomOverlayView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomOverlayView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public CustomOverlayView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onDetachedFromWindow() {
        augmentationViews.clear();
        super.onDetachedFromWindow();
    }

    public void updateOverlay(PriceLabelSession session, CaptureView captureView) {
        for (PriceLabel goneLabel : session.getRemovedLabels()) {
            TextView viewToRemove = augmentationViews.get(goneLabel.getTrackingId());
            removeView(viewToRemove);
            augmentationViews.remove(goneLabel.getTrackingId());
        }

        for (PriceLabel newLabel : session.getAddedLabels()) {
            TextView viewToAdd = createAugmentationView(newLabel);
            augmentationViews.put(newLabel.getTrackingId(), viewToAdd);
            addView(viewToAdd);
            updateAugmentationPosition(viewToAdd, newLabel.getPredictedBounds(), captureView);
        }

        for (PriceLabel updatedLabel : session.getUpdatedLabels()) {
            updateAugmentationPosition(augmentationViews.get(updatedLabel.getTrackingId()), updatedLabel.getPredictedBounds(), captureView);
        }
        invalidate();
    }

    private void updateAugmentationPosition(TextView augmentationView, Quadrilateral predictedBounds, CaptureView captureView) {
        if (augmentationView == null) return;

        Quadrilateral location = captureView.mapFrameQuadrilateralToView(predictedBounds);
        augmentationView.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
        float targetX = location.getTopRight().getX() - augmentationView.getMeasuredWidth();
        float targetY = location.getTopRight().getY();
        augmentationView.setTranslationX(targetX);
        augmentationView.setTranslationY(targetY);
    }

    private TextView createAugmentationView(PriceLabel label) {
        TextView view = new TextView(getContext());
        view.setLayoutParams(
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        view.setBackgroundResource(R.color.transparentBlue);
        view.setTextSize(24f);
        view.setPadding(4, 4, 4, 4);
        view.setText(priceFormat(label.getResult().getCapturedPrice()));
        return view;
    }

    private String priceFormat(float price) {
        Currency currency = CatalogStore.getInstance().getStore().getCurrency();
        return String.format(
                Locale.getDefault(),
                currency.getSymbol() + "%." + currency.getDecimalPlaces() + "f", price
        );
    }
}
