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

package com.scandit.shelf.javasettingssample.ui.settings.view.viewfinder.type.laserlinewidth;

import com.scandit.shelf.javasettingssample.repository.ViewfinderRepository;
import com.scandit.shelf.javasettingssample.ui.settings.view.viewfinder.type.ViewfinderTypeViewModel;
import com.scandit.shelf.sdk.core.common.geometry.FloatWithUnit;
import com.scandit.shelf.sdk.core.common.geometry.MeasureUnit;

public class ViewfinderLaserlineWidthViewModel extends ViewfinderTypeViewModel {

    private final ViewfinderRepository repository = ViewfinderRepository.getCurrentSettings();

    void valueChanged(float newValue) {
        MeasureUnit currentMeasureUnit = repository.getLaserlineViewfinderWidth().getUnit();
        repository.setLaserlineViewfinderWidth(new FloatWithUnit(newValue, currentMeasureUnit));
        updateViewfinder();
    }

    void measureChanged(MeasureUnit measureUnit) {
        float currentValue = repository.getLaserlineViewfinderWidth().getValue();
        repository.setLaserlineViewfinderWidth(new FloatWithUnit(currentValue, measureUnit));
        updateViewfinder();
    }

    FloatWithUnit getCurrentWidth() {
        return repository.getLaserlineViewfinderWidth();
    }
}
