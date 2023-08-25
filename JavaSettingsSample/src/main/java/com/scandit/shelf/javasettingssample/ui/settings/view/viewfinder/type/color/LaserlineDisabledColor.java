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

package com.scandit.shelf.javasettingssample.ui.settings.view.viewfinder.type.color;

import androidx.annotation.StringRes;

import com.scandit.shelf.javasettingssample.R;
import com.scandit.shelf.sdk.core.common.Color;
import com.scandit.shelf.sdk.core.ui.viewfinder.LaserlineViewfinder;
import com.scandit.shelf.sdk.core.ui.viewfinder.LaserlineViewfinderStyle;

import java.util.HashMap;
import java.util.Map;

public class LaserlineDisabledColor {

    public final Color color;
    @StringRes public final int displayName;

    private static final Map<LaserlineViewfinderStyle, LaserlineDisabledColor[]> map =
            new HashMap<>();

    public static final LaserlineDisabledColor DEFAULT = new LaserlineDisabledColor(
            new LaserlineViewfinder(LaserlineViewfinderStyle.LEGACY).getDisabledColor(),
            R.string._default
    );

    static {
        LaserlineViewfinderStyle[] styles = LaserlineViewfinderStyle.values();

        LaserlineDisabledColor blue = new LaserlineDisabledColor(new Color(0, 0, 255, 255), R.string.blue);
        LaserlineDisabledColor red = new LaserlineDisabledColor(new Color(255, 0, 0, 255), R.string.red);
        LaserlineDisabledColor transparent = new LaserlineDisabledColor(Color.TRANSPARENT, R.string.transparent);

        for (LaserlineViewfinderStyle style : styles) {
            LaserlineViewfinder viewfinder = new LaserlineViewfinder(style);
            LaserlineDisabledColor[] colors = {
                    new LaserlineDisabledColor(viewfinder.getEnabledColor(), R.string._default),
                    blue,
                    red,
                    transparent
            };
            map.put(style, colors);
        }
    }

    public static LaserlineDisabledColor[] getAllForStyle(LaserlineViewfinderStyle style) {
        return map.get(style);
    }

    public static LaserlineDisabledColor getDefaultForStyle(LaserlineViewfinderStyle style) {
        return getAllForStyle(style)[0];
    }

    private LaserlineDisabledColor(Color color, @StringRes int displayName) {
        this.color = color;
        this.displayName = displayName;
    }
}
