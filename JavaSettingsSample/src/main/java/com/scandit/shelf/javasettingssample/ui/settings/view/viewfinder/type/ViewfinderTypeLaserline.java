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

import androidx.annotation.Nullable;

import com.scandit.shelf.javasettingssample.R;
import com.scandit.shelf.javasettingssample.repository.ViewfinderRepository;
import com.scandit.shelf.javasettingssample.ui.settings.view.viewfinder.type.color.LaserlineDisabledColor;
import com.scandit.shelf.javasettingssample.ui.settings.view.viewfinder.type.color.LaserlineEnabledColor;
import com.scandit.shelf.sdk.core.common.geometry.FloatWithUnit;
import com.scandit.shelf.sdk.core.ui.viewfinder.LaserlineViewfinder;
import com.scandit.shelf.sdk.core.ui.viewfinder.LaserlineViewfinderStyle;
import com.scandit.shelf.sdk.core.ui.viewfinder.Viewfinder;

public class ViewfinderTypeLaserline extends ViewfinderType {

    public static ViewfinderTypeLaserline fromCurrentViewfinderAndSettings(
            Viewfinder currentViewfinder, ViewfinderRepository repository
    ) {
        return new ViewfinderTypeLaserline(
                currentViewfinder instanceof LaserlineViewfinder,
                repository.getLaserlineViewfinderWidth(),
                repository.getLaserlineViewfinderEnabledColor(),
                repository.getLaserlineViewfinderDisabledColor(),
                repository.getLaserlineViewfinderStyle()
        );
    }

    private FloatWithUnit width;

    private LaserlineEnabledColor enabledColor;
    private LaserlineDisabledColor disabledColor;

    private LaserlineViewfinderStyle style;

    private ViewfinderTypeLaserline(
            boolean enabled,
            FloatWithUnit width,
            LaserlineEnabledColor enabledColor,
            LaserlineDisabledColor disabledColor,
            LaserlineViewfinderStyle style
    ) {
        super(R.string.laserline, enabled);
        this.width = width;
        this.enabledColor = enabledColor;
        this.disabledColor = disabledColor;
        this.style = style;
    }

    @Nullable
    @Override
    public Viewfinder buildViewfinder() {
        LaserlineViewfinder viewfinder = new LaserlineViewfinder(style);
        viewfinder.setWidth(width);
        viewfinder.setEnabledColor(enabledColor.color);
        viewfinder.setDisabledColor(disabledColor.color);
        return viewfinder;
    }

    @Override
    public void resetDefaults() {
        LaserlineViewfinder viewfinder = new LaserlineViewfinder(style);
        width = viewfinder.getWidth();
        enabledColor = LaserlineEnabledColor.getDefaultForStyle(style);
        disabledColor = LaserlineDisabledColor.getDefaultForStyle(style);
    }

    public LaserlineEnabledColor getEnabledColor() {
        return enabledColor;
    }

    public void setEnabledColor(LaserlineEnabledColor enabledColor) {
        this.enabledColor = enabledColor;
    }

    public LaserlineDisabledColor getDisabledColor() {
        return disabledColor;
    }

    public void setDisabledColor(LaserlineDisabledColor disabledColor) {
        this.disabledColor = disabledColor;
    }

    public FloatWithUnit getWidth() {
        return width;
    }

    public void setWidth(FloatWithUnit width) {
        this.width = width;
    }

    public LaserlineViewfinderStyle getStyle() {
        return style;
    }

    public void setStyle(LaserlineViewfinderStyle style) {
        this.style = style;
    }
}
