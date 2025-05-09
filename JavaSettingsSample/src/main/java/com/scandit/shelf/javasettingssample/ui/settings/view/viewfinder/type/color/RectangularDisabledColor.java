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

public class RectangularDisabledColor {

    public final Color color;
    @StringRes public final int displayName;

    private static final Map<RectangularViewfinderStyle, RectangularDisabledColor[]> map =
            new HashMap<>();

    public static final RectangularDisabledColor DEFAULT = new RectangularDisabledColor(
            new RectangularViewfinder(RectangularViewfinderStyle.SQUARE).getColor(),
            R.string._default
    );

    static {
        RectangularViewfinderStyle[] styles = RectangularViewfinderStyle.values();

        RectangularDisabledColor black = new RectangularDisabledColor(new Color(0, 0, 0, 255), R.string.black);
        RectangularDisabledColor white = new RectangularDisabledColor(Color.WHITE, R.string.white);

        for (RectangularViewfinderStyle style : styles) {
            RectangularViewfinder viewfinder = new RectangularViewfinder(style);
            RectangularDisabledColor[] colors = {
                    new RectangularDisabledColor(viewfinder.getColor(), R.string._default),
                    black,
                    white
            };
            map.put(style, colors);
        }
    }

    public static RectangularDisabledColor[] getAllForStyle(RectangularViewfinderStyle style) {
        return map.get(style);
    }

    public static RectangularDisabledColor getDefaultForStyle(RectangularViewfinderStyle style) {
        return getAllForStyle(style)[0];
    }

    private RectangularDisabledColor(Color color, @StringRes int displayName) {
        this.color = color;
        this.displayName = displayName;
    }
}
