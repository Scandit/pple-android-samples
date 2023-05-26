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

package com.scandit.shelf.javasettingssample.ui.settings.view.overlay;

import androidx.lifecycle.ViewModel;

import com.scandit.shelf.javasettingssample.repository.OverlayRepository;

public class OverlayViewModel extends ViewModel {

    private final OverlayRepository currentSettings = OverlayRepository.getCurrentSettings();

    public boolean isBasicOverlayEnabled() {
        return currentSettings.isBasicOverlayEnabled();
    }

    public void setBasicOverlayEnabled(boolean basicOverlayEnabled) {
        currentSettings.setBasicOverlayEnabled(basicOverlayEnabled);
    }

    public boolean isAdvancedOverlayEnabled() {
        return currentSettings.isAdvancedOverlayEnabled();
    }

    public void setAdvancedOverlayEnabled(boolean advancedOverlayEnabled) {
        currentSettings.setAdvancedOverlayEnabled(advancedOverlayEnabled);
    }

    public OverlayBrush getCorrectPriceBrush() {
        return currentSettings.getCorrectPriceBrush();
    }

    public void setCorrectPriceBrush(OverlayBrush correctPriceBrush) {
        currentSettings.setCorrectPriceBrush(correctPriceBrush);
    }

    public OverlayBrush getWrongPriceBrush() {
        return currentSettings.getWrongPriceBrush();
    }

    public void setWrongPriceBrush(OverlayBrush wrongPriceBrush) {
        currentSettings.setWrongPriceBrush(wrongPriceBrush);
    }

    public OverlayBrush getUnknownProductBrush() {
        return currentSettings.getUnknownProductBrush();
    }

    public void setUnknownProductBrush(OverlayBrush unknownProductBrush) {
        currentSettings.setUnknownProductBrush(unknownProductBrush);
    }
}
