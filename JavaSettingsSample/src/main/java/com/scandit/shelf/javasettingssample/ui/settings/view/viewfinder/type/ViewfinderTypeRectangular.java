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
import com.scandit.shelf.javasettingssample.ui.settings.view.viewfinder.type.color.RectangularDisabledColor;
import com.scandit.shelf.javasettingssample.ui.settings.view.viewfinder.type.color.RectangularEnabledColor;
import com.scandit.shelf.javasettingssample.utils.SizeSpecification;
import com.scandit.shelf.sdk.core.common.geometry.FloatWithUnit;
import com.scandit.shelf.sdk.core.common.geometry.SizeWithUnit;
import com.scandit.shelf.sdk.core.common.geometry.SizeWithUnitAndAspect;
import com.scandit.shelf.sdk.core.ui.viewfinder.RectangularViewfinder;
import com.scandit.shelf.sdk.core.ui.viewfinder.RectangularViewfinderAnimation;
import com.scandit.shelf.sdk.core.ui.viewfinder.RectangularViewfinderLineStyle;
import com.scandit.shelf.sdk.core.ui.viewfinder.RectangularViewfinderStyle;
import com.scandit.shelf.sdk.core.ui.viewfinder.Viewfinder;

public class ViewfinderTypeRectangular extends ViewfinderType {

    public static ViewfinderTypeRectangular fromCurrentViewfinderAndSettings(
            Viewfinder currentViewFinder, ViewfinderRepository repository
    ) {
        return new ViewfinderTypeRectangular(
                currentViewFinder instanceof RectangularViewfinder,
                repository.getRectangularViewfinderColor(),
                repository.getRectangularViewfinderDisabledColor(),
                repository.getRectangularViewfinderSizeSpecification(),
                repository.getRectangularViewfinderWidth(),
                repository.getRectangularViewfinderHeight(),
                repository.getRectangularViewfinderShorterDimension(),
                repository.getRectangularViewfinderWidthAspect(),
                repository.getRectangularViewfinderHeightAspect(),
                repository.getRectangularViewfinderLongerDimensionAspect(),
                repository.getRectangularViewfinderStyle(),
                repository.getRectangularViewfinderLineStyle(),
                repository.getRectangularViewfinderDimming(),
                repository.getRectangularViewfinderAnimationEnabled(),
                repository.getRectangularViewfinderLoopingEnabled()
        );
    }

    private RectangularEnabledColor color;
    private RectangularDisabledColor disabledColor;
    private SizeSpecification sizeSpecification;

    private FloatWithUnit width;
    private FloatWithUnit height;
    private FloatWithUnit shorterDimension;
    private float widthAspect;
    private float heightAspect;
    private float longerDimensionAspect;

    private float dimming;
    private boolean animation;
    private boolean looping;

    private RectangularViewfinderStyle style;
    private RectangularViewfinderLineStyle lineStyle;

    private ViewfinderTypeRectangular(
            boolean enabled,
            RectangularEnabledColor color,
            RectangularDisabledColor disabledColor,
            SizeSpecification sizeSpecification,
            FloatWithUnit width,
            FloatWithUnit height,
            FloatWithUnit shorterDimension,
            float widthAspect,
            float heightAspect,
            float longerDimensionAspect,
            RectangularViewfinderStyle style,
            RectangularViewfinderLineStyle lineStyle,
            float dimming,
            boolean animation,
            boolean looping
    ) {
        super(R.string.rectangular, enabled);
        this.color = color;
        this.disabledColor = disabledColor;
        this.sizeSpecification = sizeSpecification;
        this.width = width;
        this.height = height;
        this.shorterDimension = shorterDimension;
        this.widthAspect = widthAspect;
        this.heightAspect = heightAspect;
        this.longerDimensionAspect = longerDimensionAspect;
        this.style = style;
        this.lineStyle = lineStyle;
        this.dimming = dimming;
        this.animation = animation;
        this.looping = looping;
    }

    public SizeSpecification getSizeSpecification() {
        return sizeSpecification;
    }

    public void setSizeSpecification(SizeSpecification sizeSpecification) {
        this.sizeSpecification = sizeSpecification;
    }

    public RectangularEnabledColor getColor() {
        return color;
    }

    public void setColor(RectangularEnabledColor color) {
        this.color = color;
    }

    public RectangularDisabledColor getDisabledColor() {
        return disabledColor;
    }

    public void setDisabledColor(RectangularDisabledColor color) {
        this.disabledColor = color;
    }

    public FloatWithUnit getWidth() {
        return width;
    }

    public void setWidth(FloatWithUnit width) {
        this.width = width;
    }

    public FloatWithUnit getHeight() {
        return height;
    }

    public void setHeight(FloatWithUnit height) {
        this.height = height;
    }

    public FloatWithUnit getShorterDimension() {
        return shorterDimension;
    }

    public void setShorterDimension(FloatWithUnit shorterDimension) {
        this.shorterDimension = shorterDimension;
    }

    public float getHeightAspect() {
        return heightAspect;
    }

    public void setHeightAspect(float heightAspect) {
        this.heightAspect = heightAspect;
    }

    public float getWidthAspect() {
        return widthAspect;
    }

    public void setWidthAspect(float widthAspect) {
        this.widthAspect = widthAspect;
    }

    public float getLongerDimensionAspect() {
        return longerDimensionAspect;
    }

    public void setLongerDimensionAspect(float longerDimensionAspect) {
        this.longerDimensionAspect = longerDimensionAspect;
    }

    public RectangularViewfinderStyle getStyle() {
        return style;
    }

    public void setStyle(RectangularViewfinderStyle style) {
        this.style = style;
    }

    public RectangularViewfinderLineStyle getLineStyle() {
        return lineStyle;
    }

    public void setLineStyle(RectangularViewfinderLineStyle style) {
        this.lineStyle = style;
    }

    public float getDimming() {
        return dimming;
    }

    public void setDimming(float dimming) {
        this.dimming = dimming;
    }

    public boolean getAnimation() {
        return animation;
    }

    public void setAnimation(boolean animation) {
        this.animation = animation;
    }

    public boolean getLooping() {
        return looping;
    }

    public void setLooping(boolean looping) {
        this.looping = looping;
    }

    @Nullable
    @Override
    public Viewfinder buildViewfinder() {
        RectangularViewfinder viewfinder = new RectangularViewfinder(style, lineStyle);
        viewfinder.setColor(color.color);
        viewfinder.setDisabledColor(disabledColor.color);
        viewfinder.setDimming(dimming);

        RectangularViewfinderAnimation finalAnimation = null;
        if (animation) {
            finalAnimation = new RectangularViewfinderAnimation(looping);
        }
        viewfinder.setAnimation(finalAnimation);

        switch (sizeSpecification) {
            case WIDTH_AND_HEIGHT -> viewfinder.setSize(new SizeWithUnit(width, height));
            case WIDTH_AND_HEIGHT_ASPECT -> viewfinder.setWidthAndAspectRatio(width, heightAspect);
            case HEIGHT_AND_WIDTH_ASPECT -> viewfinder.setHeightAndAspectRatio(height, widthAspect);
            case SHORTER_DIMENSION_AND_ASPECT ->
                    viewfinder.setShorterDimensionAndAspectRatio(shorterDimension.getValue(), longerDimensionAspect);
        }
        return viewfinder;
    }

    @Override
    public void resetDefaults() {
        RectangularViewfinder viewfinder = new RectangularViewfinder(style);

        color = RectangularEnabledColor.getDefaultForStyle(style);
        disabledColor = RectangularDisabledColor.getDefaultForStyle(style);
        lineStyle = viewfinder.getLineStyle();
        dimming = viewfinder.getDimming();

        RectangularViewfinderAnimation currentAnimation = viewfinder.getAnimation();
        animation = currentAnimation != null;
        looping = currentAnimation == null || currentAnimation.isLooping();

        SizeWithUnitAndAspect size = viewfinder.getSizeWithUnitAndAspect();
        sizeSpecification = SizeSpecification.forSizingMode(size.getSizingMode());
        switch (sizeSpecification) {
            case WIDTH_AND_HEIGHT -> {
                width = size.getWidthAndHeight().getWidth();
                height = size.getWidthAndHeight().getHeight();
            }
            case WIDTH_AND_HEIGHT_ASPECT -> {
                width = size.getWidthAndAspectRatio().getSize();
                heightAspect = size.getWidthAndAspectRatio().getAspect();
            }
            case HEIGHT_AND_WIDTH_ASPECT -> {
                height = size.getHeightAndAspectRatio().getSize();
                widthAspect = size.getHeightAndAspectRatio().getAspect();
            }
            case SHORTER_DIMENSION_AND_ASPECT -> {
                shorterDimension = size.getShorterDimensionAndAspectRatio().getSize();
                longerDimensionAspect = size.getShorterDimensionAndAspectRatio().getAspect();
            }
        }
    }
}
