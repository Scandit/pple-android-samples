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
import androidx.annotation.StringRes;

import com.scandit.shelf.javasettingssample.R;
import com.scandit.shelf.javasettingssample.repository.ViewfinderRepository;
import com.scandit.shelf.sdk.core.common.Color;
import com.scandit.shelf.sdk.core.ui.viewfinder.AimerViewfinder;
import com.scandit.shelf.sdk.core.ui.viewfinder.Viewfinder;

public class ViewfinderTypeAimer extends ViewfinderType {

    public static ViewfinderTypeAimer fromCurrentViewfinderAndSettings(
            Viewfinder currentViewfinder, ViewfinderRepository repository
    ) {
        return new ViewfinderTypeAimer(
                currentViewfinder instanceof AimerViewfinder,
                repository.getAimerViewfinderFrameColor(),
                repository.getAimerViewfinderDotColor()
        );
    }

    private FrameColor frameColor;
    private DotColor dotColor;

    private ViewfinderTypeAimer(
            boolean enabled,
            FrameColor frameColor,
            DotColor dotColor
    ) {
        super(R.string.aimer, enabled);
        this.frameColor = frameColor;
        this.dotColor = dotColor;
    }

    @Nullable
    @Override
    public Viewfinder buildViewfinder() {
        AimerViewfinder viewfinder = new AimerViewfinder();
        viewfinder.setFrameColor(frameColor.color);
        viewfinder.setDotColor(dotColor.color);
        return viewfinder;
    }

    @Override
    public void resetDefaults() {}

    public FrameColor getFrameColor() {
        return frameColor;
    }

    public void setFrameColor(FrameColor frameColor) {
        this.frameColor = frameColor;
    }

    public DotColor getDotColor() {
        return dotColor;
    }

    public void setDotColor(DotColor dotColor) {
        this.dotColor = dotColor;
    }

    public enum FrameColor {
        DEFAULT(new AimerViewfinder().getFrameColor(), R.string._default),
        BLUE(new Color(0, 0, 255, 255), R.string.blue),
        RED(new Color(255, 0, 0, 255), R.string.red);

        public final Color color;
        @StringRes public final int displayName;

        FrameColor(Color color, @StringRes int displayName) {
            this.color = color;
            this.displayName = displayName;
        }
    }

    public enum DotColor {
        DEFAULT(new AimerViewfinder().getDotColor(), R.string._default),
        BLUE(new Color(0, 0, 255, 204), R.string.blue),
        RED(new Color(255, 0, 0, 204), R.string.red);

        public final Color color;
        @StringRes public final int displayName;

        DotColor(Color color, @StringRes int displayName) {
            this.color = color;
            this.displayName = displayName;
        }
    }
}
