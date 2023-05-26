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

package com.scandit.shelf.javasettingssample.ui.settings.feedback;

import androidx.lifecycle.ViewModel;

import com.scandit.shelf.javasettingssample.repository.FeedbackRepository;

public class FeedbackViewModel extends ViewModel {

    private final FeedbackRepository currentSettings = FeedbackRepository.getCurrentSettings();

    public boolean isCorrectPriceSoundEnabled() {
        return currentSettings.isCorrectPriceSoundEnabled();
    }

    public void setCorrectPriceSoundEnabled(boolean correctPriceSoundEnabled) {
        currentSettings.setCorrectPriceSoundEnabled(correctPriceSoundEnabled);
    }

    public boolean isCorrectPriceVibrationEnabled() {
        return currentSettings.isCorrectPriceVibrationEnabled();
    }

    public void setCorrectPriceVibrationEnabled(boolean correctPriceVibrationEnabled) {
        currentSettings.setCorrectPriceVibrationEnabled(correctPriceVibrationEnabled);
    }

    public boolean isWrongPriceSoundEnabled() {
        return currentSettings.isWrongPriceSoundEnabled();
    }

    public void setWrongPriceSoundEnabled(boolean wrongPriceSoundEnabled) {
        currentSettings.setWrongPriceSoundEnabled(wrongPriceSoundEnabled);
    }

    public boolean isWrongPriceVibrationEnabled() {
        return currentSettings.isWrongPriceVibrationEnabled();
    }

    public void setWrongPriceVibrationEnabled(boolean wrongPriceVibrationEnabled) {
        currentSettings.setWrongPriceVibrationEnabled(wrongPriceVibrationEnabled);
    }

    public boolean isUnknownProductSoundEnabled() {
        return currentSettings.isUnknownProductSoundEnabled();
    }

    public void setUnknownProductSoundEnabled(boolean unknownProductSoundEnabled) {
        currentSettings.setUnknownProductSoundEnabled(unknownProductSoundEnabled);
    }

    public boolean isUnknownProductVibrationEnabled() {
        return currentSettings.isUnknownProductVibrationEnabled();
    }

    public void setUnknownProductVibrationEnabled(boolean unknownProductVibrationEnabled) {
        currentSettings.setUnknownProductVibrationEnabled(unknownProductVibrationEnabled);
    }
}
