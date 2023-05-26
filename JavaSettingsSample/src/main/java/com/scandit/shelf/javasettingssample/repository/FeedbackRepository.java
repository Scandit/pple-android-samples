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

package com.scandit.shelf.javasettingssample.repository;

public class FeedbackRepository {

    private static final FeedbackRepository CURRENT = new FeedbackRepository();

    public static FeedbackRepository getCurrentSettings() {
        return CURRENT;
    }

    private FeedbackRepository() {}

    private boolean correctPriceSoundEnabled = true;
    private boolean correctPriceVibrationEnabled = true;
    private boolean wrongPriceSoundEnabled = true;
    private boolean wrongPriceVibrationEnabled = true;
    private boolean unknownProductSoundEnabled = true;
    private boolean unknownProductVibrationEnabled = true;

    public boolean isCorrectPriceSoundEnabled() {
        return correctPriceSoundEnabled;
    }

    public void setCorrectPriceSoundEnabled(boolean correctPriceSoundEnabled) {
        this.correctPriceSoundEnabled = correctPriceSoundEnabled;
    }

    public boolean isCorrectPriceVibrationEnabled() {
        return correctPriceVibrationEnabled;
    }

    public void setCorrectPriceVibrationEnabled(boolean correctPriceVibrationEnabled) {
        this.correctPriceVibrationEnabled = correctPriceVibrationEnabled;
    }

    public boolean isWrongPriceSoundEnabled() {
        return wrongPriceSoundEnabled;
    }

    public void setWrongPriceSoundEnabled(boolean wrongPriceSoundEnabled) {
        this.wrongPriceSoundEnabled = wrongPriceSoundEnabled;
    }

    public boolean isWrongPriceVibrationEnabled() {
        return wrongPriceVibrationEnabled;
    }

    public void setWrongPriceVibrationEnabled(boolean wrongPriceVibrationEnabled) {
        this.wrongPriceVibrationEnabled = wrongPriceVibrationEnabled;
    }

    public boolean isUnknownProductSoundEnabled() {
        return unknownProductSoundEnabled;
    }

    public void setUnknownProductSoundEnabled(boolean unknownProductSoundEnabled) {
        this.unknownProductSoundEnabled = unknownProductSoundEnabled;
    }

    public boolean isUnknownProductVibrationEnabled() {
        return unknownProductVibrationEnabled;
    }

    public void setUnknownProductVibrationEnabled(boolean unknownProductVibrationEnabled) {
        this.unknownProductVibrationEnabled = unknownProductVibrationEnabled;
    }
}
