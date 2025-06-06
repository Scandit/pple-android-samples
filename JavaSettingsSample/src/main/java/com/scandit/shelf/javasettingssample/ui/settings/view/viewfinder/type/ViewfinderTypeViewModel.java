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

package com.scandit.shelf.javasettingssample.ui.settings.view.viewfinder.type;

import androidx.lifecycle.ViewModel;

import com.scandit.shelf.javasettingssample.repository.ViewfinderRepository;
import com.scandit.shelf.sdk.core.ui.viewfinder.AimerViewfinder;
import com.scandit.shelf.sdk.core.ui.viewfinder.RectangularViewfinder;
import com.scandit.shelf.sdk.core.ui.viewfinder.RectangularViewfinderStyle;
import com.scandit.shelf.sdk.core.ui.viewfinder.Viewfinder;

public class ViewfinderTypeViewModel extends ViewModel {

    private final ViewfinderRepository repository = ViewfinderRepository.getCurrentSettings();

    public ViewfinderType getCurrentViewfinderType() {
        Viewfinder viewfinder = getCurrentViewfinder();
        if (viewfinder instanceof RectangularViewfinder) {
            return ViewfinderTypeRectangular.fromCurrentViewfinderAndSettings(viewfinder, repository);
        } else if (viewfinder instanceof AimerViewfinder) {
            return ViewfinderTypeAimer.fromCurrentViewfinderAndSettings(viewfinder, repository);
        } else {
            return ViewfinderTypeNone.fromCurrentViewfinder(viewfinder);
        }
    }

    public Viewfinder getCurrentViewfinder() {
        return repository.getCurrentViewfinder();
    }

    public void updateViewfinder() {
        updateViewfinder(getCurrentViewfinderType());
    }

    public void updateViewfinder(ViewfinderType viewfinderType) {
        repository.setViewfinder(viewfinderType.buildViewfinder());
    }

    public RectangularViewfinderStyle getRectangularViewfinderStyle() {
        return repository.getRectangularViewfinderStyle();
    }
}
