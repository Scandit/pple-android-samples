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

package com.scandit.shelf.javasettingssample.ui.settings.view.overlay;

import androidx.annotation.ColorRes;

import com.scandit.shelf.javasettingssample.R;

public enum OverlayBrush {
    GREEN(R.color.transparentGreen),
    RED(R.color.transparentRed),
    YELLOW(R.color.transparentYellow),
    GREY(R.color.transparentGrey),
    BLUE(R.color.transparentBlue);

    @ColorRes
    public final int colorResource;

    OverlayBrush(@ColorRes int colorResource) {
        this.colorResource = colorResource;
    }
}
