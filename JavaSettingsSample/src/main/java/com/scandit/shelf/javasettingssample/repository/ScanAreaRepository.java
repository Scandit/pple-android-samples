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

import com.scandit.shelf.sdk.core.common.geometry.FloatWithUnit;
import com.scandit.shelf.sdk.core.common.geometry.MeasureUnit;

public class ScanAreaRepository {

    private static final ScanAreaRepository CURRENT = new ScanAreaRepository();

    public static ScanAreaRepository getCurrentSettings() {
        return CURRENT;
    }

    private ScanAreaRepository() {
    }

    private float widthValue = 0.9f;
    private MeasureUnit widthUnit = MeasureUnit.FRACTION;
    private float heightValue = 0.3f;
    private MeasureUnit heightUnit = MeasureUnit.FRACTION;
    private boolean showScanAreaGuides = false;

    public FloatWithUnit getWidth() {
        return new FloatWithUnit(widthValue, widthUnit);
    }

    public void setWidthValue(float value) {
        widthValue = value;
    }

    public void setWidthUnit(MeasureUnit measureUnit) {
        widthUnit = measureUnit;
    }

    public FloatWithUnit getHeight() {
        return new FloatWithUnit(heightValue, heightUnit);
    }

    public void setHeightValue(float value) {
        heightValue = value;
    }

    public void setHeightUnit(MeasureUnit measureUnit) {
        heightUnit = measureUnit;
    }

    public boolean shouldShowScanAreaGuides() {
        return showScanAreaGuides;
    }

    public void setShowScanAreaGuides(boolean showScanAreaGuides) {
        this.showScanAreaGuides = showScanAreaGuides;
    }
}
