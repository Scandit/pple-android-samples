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

import com.scandit.shelf.javasettingssample.ui.settings.view.overlay.OverlayBrush;

public class OverlayRepository {

    private static final OverlayRepository CURRENT = new OverlayRepository();

    public static OverlayRepository getCurrentSettings() {
        return CURRENT;
    }

    private OverlayRepository() {
    }

    private boolean basicOverlayEnabled = true;
    private OverlayBrush correctPriceBrush = OverlayBrush.GREEN;
    private OverlayBrush wrongPriceBrush = OverlayBrush.RED;
    private OverlayBrush unknownProductBrush = OverlayBrush.GREY;
    private boolean advancedOverlayEnabled = false;
    private boolean customOverlayEnabled = false;

    public boolean isBasicOverlayEnabled() {
        return basicOverlayEnabled;
    }

    public void setBasicOverlayEnabled(boolean basicOverlayEnabled) {
        this.basicOverlayEnabled = basicOverlayEnabled;
    }

    public OverlayBrush getCorrectPriceBrush() {
        return correctPriceBrush;
    }

    public void setCorrectPriceBrush(OverlayBrush correctPriceBrush) {
        this.correctPriceBrush = correctPriceBrush;
    }

    public OverlayBrush getWrongPriceBrush() {
        return wrongPriceBrush;
    }

    public void setWrongPriceBrush(OverlayBrush wrongPriceBrush) {
        this.wrongPriceBrush = wrongPriceBrush;
    }

    public OverlayBrush getUnknownProductBrush() {
        return unknownProductBrush;
    }

    public void setUnknownProductBrush(OverlayBrush unknownProductBrush) {
        this.unknownProductBrush = unknownProductBrush;
    }

    public boolean isAdvancedOverlayEnabled() {
        return advancedOverlayEnabled;
    }

    public void setAdvancedOverlayEnabled(boolean advancedOverlayEnabled) {
        this.advancedOverlayEnabled = advancedOverlayEnabled;
    }

    public boolean isCustomOverlayEnabled() {
        return customOverlayEnabled;
    }

    public void setCustomOverlayEnabled(boolean customOverlayEnabled) {
        this.customOverlayEnabled = customOverlayEnabled;
    }
}
