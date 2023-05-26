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
import com.scandit.shelf.sdk.core.ui.viewfinder.RectangularViewfinder;
import com.scandit.shelf.sdk.core.ui.viewfinder.RectangularViewfinderStyle;

import java.util.HashMap;
import java.util.Map;

public class RectangularEnabledColor {

    public final Color color;
    @StringRes public final int displayName;

    private static final Map<RectangularViewfinderStyle, RectangularEnabledColor[]> map =
            new HashMap<>();

    public static final RectangularEnabledColor DEFAULT = new RectangularEnabledColor(
            new RectangularViewfinder(RectangularViewfinderStyle.LEGACY).getColor(),
            R.string._default
    );

    static {
        RectangularViewfinderStyle[] styles = RectangularViewfinderStyle.values();

        RectangularEnabledColor blue = new RectangularEnabledColor(new Color(0, 0, 255, 0), R.string.blue);
        RectangularEnabledColor black = new RectangularEnabledColor(new Color(0, 0, 0, 0), R.string.black);

        for (RectangularViewfinderStyle style : styles) {
            RectangularViewfinder viewfinder = new RectangularViewfinder(style);
            RectangularEnabledColor[] colors = {
                    new RectangularEnabledColor(viewfinder.getColor(), R.string._default),
                    blue,
                    black
            };
            map.put(style, colors);
        }
    }

    public static RectangularEnabledColor[] getAllForStyle(RectangularViewfinderStyle style) {
        return map.get(style);
    }

    public static RectangularEnabledColor getDefaultForStyle(RectangularViewfinderStyle style) {
        return getAllForStyle(style)[0];
    }

    private RectangularEnabledColor(Color color, @StringRes int displayName) {
        this.color = color;
        this.displayName = displayName;
    }
}
