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

import com.scandit.shelf.javasettingssample.ui.settings.view.viewfinder.type.ViewfinderTypeAimer;
import com.scandit.shelf.javasettingssample.ui.settings.view.viewfinder.type.color.LaserlineDisabledColor;
import com.scandit.shelf.javasettingssample.ui.settings.view.viewfinder.type.color.LaserlineEnabledColor;
import com.scandit.shelf.javasettingssample.ui.settings.view.viewfinder.type.color.RectangularDisabledColor;
import com.scandit.shelf.javasettingssample.ui.settings.view.viewfinder.type.color.RectangularEnabledColor;
import com.scandit.shelf.javasettingssample.utils.SizeSpecification;
import com.scandit.shelf.sdk.core.common.geometry.FloatWithUnit;
import com.scandit.shelf.sdk.core.common.geometry.MeasureUnit;
import com.scandit.shelf.sdk.core.common.geometry.SizeWithUnit;
import com.scandit.shelf.sdk.core.ui.viewfinder.LaserlineViewfinderStyle;
import com.scandit.shelf.sdk.core.ui.viewfinder.RectangularViewfinder;
import com.scandit.shelf.sdk.core.ui.viewfinder.RectangularViewfinderLineStyle;
import com.scandit.shelf.sdk.core.ui.viewfinder.RectangularViewfinderStyle;
import com.scandit.shelf.sdk.core.ui.viewfinder.Viewfinder;

public class ViewfinderRepository {

    private static final ViewfinderRepository CURRENT = new ViewfinderRepository();

    public static ViewfinderRepository getCurrentSettings() {
        return CURRENT;
    }

    private Viewfinder viewfinder = null;

    private RectangularEnabledColor rectangularColor = RectangularEnabledColor.DEFAULT;
    private RectangularDisabledColor rectangularDisabledColor = RectangularDisabledColor.DEFAULT;
    private SizeSpecification rectangularViewfinderSizeSpecification = SizeSpecification.WIDTH_AND_HEIGHT;
    private FloatWithUnit rectangularViewfinderWidth = new FloatWithUnit(0.9f, MeasureUnit.FRACTION);
    private FloatWithUnit rectangularViewfinderHeight = new FloatWithUnit(0.3f, MeasureUnit.FRACTION);
    private FloatWithUnit rectangularViewfinderShorterDimension = new FloatWithUnit(1.0f, MeasureUnit.FRACTION);
    private float rectangularViewfinderWidthAspect = 0f;
    private float rectangularViewfinderHeightAspect = 0f;
    private float rectangularViewfinderLongerDimensionAspect = 0.5f;
    private RectangularViewfinderStyle rectangularViewfinderStyle = RectangularViewfinderStyle.ROUNDED;
    private RectangularViewfinderLineStyle rectangularViewfinderLineStyle = RectangularViewfinderLineStyle.LIGHT;
    private float rectangularViewfinderDimming = 0.6f;
    private boolean rectangularViewfinderAnimation = false;
    private boolean rectangularViewfinderLooping = false;

    private FloatWithUnit laserlineViewfinderWidth = new FloatWithUnit(0.75f, MeasureUnit.FRACTION);
    private LaserlineEnabledColor laserlineViewfinderEnabledColor = LaserlineEnabledColor.DEFAULT;
    private LaserlineDisabledColor laserlineViewfinderDisabledColor = LaserlineDisabledColor.DEFAULT;
    private LaserlineViewfinderStyle laserlineViewfinderStyle = LaserlineViewfinderStyle.LEGACY;

    private ViewfinderTypeAimer.FrameColor aimerViewfinderFrameColor = ViewfinderTypeAimer.FrameColor.DEFAULT;
    private ViewfinderTypeAimer.DotColor aimerViewfinderDotColor = ViewfinderTypeAimer.DotColor.DEFAULT;

    private ViewfinderRepository() {
        // Create default Viewfinder.
        RectangularViewfinder rectangularViewfinder = new RectangularViewfinder(
                rectangularViewfinderStyle,
                rectangularViewfinderLineStyle
        );
        rectangularViewfinder.setSize(new SizeWithUnit(rectangularViewfinderWidth, rectangularViewfinderHeight));
        rectangularViewfinder.setDimming(rectangularViewfinderDimming);

        viewfinder = rectangularViewfinder;
    }

    public Viewfinder getCurrentViewfinder() {
        return viewfinder;
    }

    public void setViewfinder(Viewfinder viewfinder) {
        this.viewfinder = viewfinder;
    }

    public RectangularEnabledColor getRectangularViewfinderColor() {
        return rectangularColor;
    }

    public void setRectangularViewfinderColor(RectangularEnabledColor rectangularEnabledColor) {
        this.rectangularColor = rectangularEnabledColor;
    }

    public RectangularDisabledColor getRectangularViewfinderDisabledColor() {
        return rectangularDisabledColor;
    }

    public void setRectangularViewfinderDisabledColor(
            RectangularDisabledColor rectangularDisabledColor) {
        this.rectangularDisabledColor = rectangularDisabledColor;
    }

    public SizeSpecification getRectangularViewfinderSizeSpecification() {
        return rectangularViewfinderSizeSpecification;
    }

    public void setRectangularViewfinderSizeSpecification(
            SizeSpecification rectangularViewfinderSizeSpecification
    ) {
        this.rectangularViewfinderSizeSpecification = rectangularViewfinderSizeSpecification;
    }

    public FloatWithUnit getRectangularViewfinderWidth() {
        return rectangularViewfinderWidth;
    }

    public void setRectangularViewfinderWidth(FloatWithUnit rectangularViewfinderWidth) {
        this.rectangularViewfinderWidth = rectangularViewfinderWidth;
    }

    public FloatWithUnit getRectangularViewfinderHeight() {
        return rectangularViewfinderHeight;
    }

    public void setRectangularViewfinderHeight(FloatWithUnit rectangularViewfinderHeight) {
        this.rectangularViewfinderHeight = rectangularViewfinderHeight;
    }

    public FloatWithUnit getRectangularViewfinderShorterDimension() {
        return rectangularViewfinderShorterDimension;
    }

    public void setRectangularViewfinderShorterDimension(
            FloatWithUnit rectangularViewfinderShorterDimension
    ) {
        this.rectangularViewfinderShorterDimension = rectangularViewfinderShorterDimension;
    }

    public float getRectangularViewfinderWidthAspect() {
        return rectangularViewfinderWidthAspect;
    }

    public void setRectangularViewfinderWidthAspect(float rectangularViewfinderWidthAspect) {
        this.rectangularViewfinderWidthAspect = rectangularViewfinderWidthAspect;
    }

    public float getRectangularViewfinderHeightAspect() {
        return rectangularViewfinderHeightAspect;
    }

    public void setRectangularViewfinderHeightAspect(float rectangularViewfinderHeightAspect) {
        this.rectangularViewfinderHeightAspect = rectangularViewfinderHeightAspect;
    }

    public float getRectangularViewfinderLongerDimensionAspect() {
        return rectangularViewfinderLongerDimensionAspect;
    }

    public void setRectangularViewfinderLongerDimensionAspect(
            float rectangularViewfinderLongerDimensionAspect
    ) {
        this.rectangularViewfinderLongerDimensionAspect = rectangularViewfinderLongerDimensionAspect;
    }

    public RectangularViewfinderStyle getRectangularViewfinderStyle() {
        return rectangularViewfinderStyle;
    }

    public void setRectangularViewfinderStyle(RectangularViewfinderStyle style) {
        this.rectangularViewfinderStyle = style;
    }

    public RectangularViewfinderLineStyle getRectangularViewfinderLineStyle() {
        return rectangularViewfinderLineStyle;
    }

    public void setRectangularViewfinderLineStyle(RectangularViewfinderLineStyle style) {
        this.rectangularViewfinderLineStyle = style;
    }

    public float getRectangularViewfinderDimming() {
        return rectangularViewfinderDimming;
    }

    public void setRectangularViewfinderDimming(float dimming) {
        this.rectangularViewfinderDimming = dimming;
    }

    public boolean getRectangularViewfinderAnimationEnabled() {
        return rectangularViewfinderAnimation;
    }

    public void setRectangularViewfinderAnimationEnabled(boolean enabled) {
        this.rectangularViewfinderAnimation = enabled;
    }

    public boolean getRectangularViewfinderLoopingEnabled() {
        return rectangularViewfinderLooping;
    }

    public void setRectangularViewfinderLoopingEnabled(boolean enabled) {
        this.rectangularViewfinderLooping = enabled;
    }

    public LaserlineEnabledColor getLaserlineViewfinderEnabledColor() {
        return laserlineViewfinderEnabledColor;
    }

    public void setLaserlineViewfinderEnabledColor(
            LaserlineEnabledColor laserlineViewfinderEnabledColor
    ) {
        this.laserlineViewfinderEnabledColor = laserlineViewfinderEnabledColor;
    }

    public LaserlineDisabledColor getLaserlineViewfinderDisabledColor() {
        return laserlineViewfinderDisabledColor;
    }

    public void setLaserlineViewfinderDisabledColor(
            LaserlineDisabledColor laserlineViewfinderDisabledColor
    ) {
        this.laserlineViewfinderDisabledColor = laserlineViewfinderDisabledColor;
    }

    public FloatWithUnit getLaserlineViewfinderWidth() {
        return laserlineViewfinderWidth;
    }

    public void setLaserlineViewfinderWidth(FloatWithUnit laserlineViewfinderWidth) {
        this.laserlineViewfinderWidth = laserlineViewfinderWidth;
    }

    public LaserlineViewfinderStyle getLaserlineViewfinderStyle() {
        return laserlineViewfinderStyle;
    }

    public void setLaserlineViewfinderStyle(LaserlineViewfinderStyle style) {
        this.laserlineViewfinderStyle = style;
    }

    public ViewfinderTypeAimer.FrameColor getAimerViewfinderFrameColor() {
        return aimerViewfinderFrameColor;
    }

    public void setAimerViewfinderFrameColor(
            ViewfinderTypeAimer.FrameColor aimerViewfinderFrameColor
    ) {
        this.aimerViewfinderFrameColor = aimerViewfinderFrameColor;
    }

    public ViewfinderTypeAimer.DotColor getAimerViewfinderDotColor() {
        return aimerViewfinderDotColor;
    }

    public void setAimerViewfinderDotColor(
            ViewfinderTypeAimer.DotColor aimerViewfinderDotColor
    ) {
        this.aimerViewfinderDotColor = aimerViewfinderDotColor;
    }
}
