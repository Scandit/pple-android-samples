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

package com.scandit.shelf.javasettingssample.ui.settings.view.viewfinder;

import com.scandit.shelf.javasettingssample.repository.ViewfinderRepository;
import com.scandit.shelf.javasettingssample.ui.settings.view.viewfinder.type.ViewfinderType;
import com.scandit.shelf.javasettingssample.ui.settings.view.viewfinder.type.ViewfinderTypeAimer;
import com.scandit.shelf.javasettingssample.ui.settings.view.viewfinder.type.ViewfinderTypeNone;
import com.scandit.shelf.javasettingssample.ui.settings.view.viewfinder.type.ViewfinderTypeRectangular;
import com.scandit.shelf.javasettingssample.ui.settings.view.viewfinder.type.ViewfinderTypeViewModel;
import com.scandit.shelf.javasettingssample.ui.settings.view.viewfinder.type.color.RectangularDisabledColor;
import com.scandit.shelf.javasettingssample.ui.settings.view.viewfinder.type.color.RectangularEnabledColor;
import com.scandit.shelf.javasettingssample.utils.SizeSpecification;
import com.scandit.shelf.sdk.core.common.geometry.FloatWithUnit;
import com.scandit.shelf.sdk.core.common.geometry.MeasureUnit;
import com.scandit.shelf.sdk.core.ui.viewfinder.RectangularViewfinderLineStyle;
import com.scandit.shelf.sdk.core.ui.viewfinder.RectangularViewfinderStyle;

public class ViewfinderViewModel extends ViewfinderTypeViewModel {

    private final ViewfinderRepository repository = ViewfinderRepository.getCurrentSettings();

    ViewfinderType[] getViewfinderTypes() {
        return new ViewfinderType[]{
                ViewfinderTypeNone.fromCurrentViewfinder(repository.getCurrentViewfinder()),
                ViewfinderTypeRectangular.fromCurrentViewfinderAndSettings(repository.getCurrentViewfinder(), repository),
                ViewfinderTypeAimer.fromCurrentViewfinderAndSettings(repository.getCurrentViewfinder(), repository)
        };
    }

    void setViewfinderType(ViewfinderType viewfinderType) {
        if (viewfinderType instanceof ViewfinderTypeRectangular) {
            ViewfinderTypeRectangular casted = (ViewfinderTypeRectangular) viewfinderType;
            repository.setRectangularViewfinderColor(casted.getColor());
            repository.setRectangularViewfinderDisabledColor(casted.getDisabledColor());
            repository.setRectangularViewfinderDimming(casted.getDimming());
            repository.setRectangularViewfinderAnimationEnabled(casted.getAnimation());
            repository.setRectangularViewfinderLoopingEnabled(casted.getLooping());
            repository.setRectangularViewfinderSizeSpecification(
                    casted.getSizeSpecification()
            );
            repository.setRectangularViewfinderWidth(casted.getWidth());
            repository.setRectangularViewfinderWidthAspect(casted.getWidthAspect());
            repository.setRectangularViewfinderHeight(casted.getHeight());
            repository.setRectangularViewfinderHeightAspect(casted.getHeightAspect());
            repository.setRectangularViewfinderShorterDimension(casted.getShorterDimension());
            repository.setRectangularViewfinderLongerDimensionAspect(casted.getLongerDimensionAspect());
            repository.setRectangularViewfinderStyle(casted.getStyle());
            repository.setRectangularViewfinderLineStyle(casted.getLineStyle());
        } else if (viewfinderType instanceof ViewfinderTypeAimer) {
            ViewfinderTypeAimer casted = (ViewfinderTypeAimer) viewfinderType;
            repository.setAimerViewfinderFrameColor(casted.getFrameColor());
            repository.setAimerViewfinderDotColor(casted.getDotColor());
        }
        updateViewfinder(viewfinderType);
    }

    void setRectangularViewfinderColor(RectangularEnabledColor color) {
        ViewfinderType currentViewfinder = getCurrentViewfinderType();
        if (currentViewfinder instanceof ViewfinderTypeRectangular) {
            ((ViewfinderTypeRectangular) currentViewfinder).setColor(color);
            setViewfinderType(currentViewfinder);
        }
    }

    void setRectangularViewfinderDisabledColor(RectangularDisabledColor color) {
        ViewfinderType currentViewfinder = getCurrentViewfinderType();
        if (currentViewfinder instanceof ViewfinderTypeRectangular) {
            ((ViewfinderTypeRectangular) currentViewfinder).setDisabledColor(color);
            setViewfinderType(currentViewfinder);
        }
    }

    void setRectangularViewfinderSizeSpec(SizeSpecification spec) {
        ViewfinderType currentViewfinder = getCurrentViewfinderType();
        if (currentViewfinder instanceof ViewfinderTypeRectangular) {
            ((ViewfinderTypeRectangular) currentViewfinder).setSizeSpecification(spec);
            setViewfinderType(currentViewfinder);
        }
    }

    void setRectangularViewfinderShorterDimension(float fraction) {
        ViewfinderType currentViewfinder = getCurrentViewfinderType();
        if (currentViewfinder instanceof ViewfinderTypeRectangular) {
            ((ViewfinderTypeRectangular) currentViewfinder).setShorterDimension(
                    new FloatWithUnit(fraction, MeasureUnit.FRACTION));
            setViewfinderType(currentViewfinder);
        }
    }

    void setRectangularViewfinderHeightAspect(float aspect) {
        ViewfinderType currentViewfinder = getCurrentViewfinderType();
        if (currentViewfinder instanceof ViewfinderTypeRectangular) {
            ((ViewfinderTypeRectangular) currentViewfinder).setHeightAspect(aspect);
            setViewfinderType(currentViewfinder);
        }
    }

    void setRectangularViewfinderWidthAspect(float aspect) {
        ViewfinderType currentViewfinder = getCurrentViewfinderType();
        if (currentViewfinder instanceof ViewfinderTypeRectangular) {
            ((ViewfinderTypeRectangular) currentViewfinder).setWidthAspect(aspect);
            setViewfinderType(currentViewfinder);
        }
    }

    void setRectangularViewfinderLongerDimensionAspect(float aspect) {
        ViewfinderType currentViewfinder = getCurrentViewfinderType();
        if (currentViewfinder instanceof ViewfinderTypeRectangular) {
            ((ViewfinderTypeRectangular) currentViewfinder).setLongerDimensionAspect(aspect);
            setViewfinderType(currentViewfinder);
        }
    }

    void setRectangularViewfinderStyle(RectangularViewfinderStyle style) {
        ViewfinderType currentViewfinder = getCurrentViewfinderType();
        if (currentViewfinder instanceof ViewfinderTypeRectangular) {
            ((ViewfinderTypeRectangular) currentViewfinder).setStyle(style);
            currentViewfinder.resetDefaults();
            setViewfinderType(currentViewfinder);
        }
    }

    void setRectangularViewfinderLineStyle(RectangularViewfinderLineStyle style) {
        ViewfinderType currentViewfinder = getCurrentViewfinderType();
        if (currentViewfinder instanceof ViewfinderTypeRectangular) {
            ((ViewfinderTypeRectangular) currentViewfinder).setLineStyle(style);
            setViewfinderType(currentViewfinder);
        }
    }

    void setRectangularDimming(float dimming) {
        ViewfinderType currentViewfinder = getCurrentViewfinderType();
        if (currentViewfinder instanceof ViewfinderTypeRectangular) {
            ((ViewfinderTypeRectangular) currentViewfinder).setDimming(dimming);
            setViewfinderType(currentViewfinder);
        }
        repository.setRectangularViewfinderDimming(dimming);
    }

    void setRectangularAnimationEnabled(boolean enabled) {
        ViewfinderType currentViewfinder = getCurrentViewfinderType();
        if (currentViewfinder instanceof ViewfinderTypeRectangular) {
            ((ViewfinderTypeRectangular) currentViewfinder).setAnimation(enabled);
            setViewfinderType(currentViewfinder);
        }
        repository.setRectangularViewfinderAnimationEnabled(enabled);
    }

    void setRectangularLoopingEnabled(boolean enabled) {
        ViewfinderType currentViewfinder = getCurrentViewfinderType();
        if (currentViewfinder instanceof ViewfinderTypeRectangular) {
            ((ViewfinderTypeRectangular) currentViewfinder).setLooping(enabled);
            setViewfinderType(currentViewfinder);
        }
        repository.setRectangularViewfinderLoopingEnabled(enabled);
    }

    void setAimerViewfinderFrameColor(ViewfinderTypeAimer.FrameColor color) {
        ViewfinderType currentViewfinder = getCurrentViewfinderType();
        if (currentViewfinder instanceof ViewfinderTypeAimer) {
            ((ViewfinderTypeAimer) currentViewfinder).setFrameColor(color);
            setViewfinderType(currentViewfinder);
        }
        repository.setAimerViewfinderFrameColor(color);
    }

    void setAimerViewfinderDotColor(ViewfinderTypeAimer.DotColor color) {
        ViewfinderType currentViewfinder = getCurrentViewfinderType();
        if (currentViewfinder instanceof ViewfinderTypeAimer) {
            ((ViewfinderTypeAimer) currentViewfinder).setDotColor(color);
            setViewfinderType(currentViewfinder);
        }
        repository.setAimerViewfinderDotColor(color);
    }
}
