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

package com.scandit.shelf.javasettingssample.ui.settings.view.viewfinder.type.rectanglewidth;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.scandit.shelf.javasettingssample.R;
import com.scandit.shelf.javasettingssample.ui.base.measureunit.MeasureUnitFragment;
import com.scandit.shelf.sdk.core.common.geometry.FloatWithUnit;
import com.scandit.shelf.sdk.core.common.geometry.MeasureUnit;

public class ViewfinderRectangleWidthMeasureFragment extends MeasureUnitFragment {

    public static ViewfinderRectangleWidthMeasureFragment newInstance() {
        return new ViewfinderRectangleWidthMeasureFragment();
    }

    private ViewfinderRectangleWidthViewModel viewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(ViewfinderRectangleWidthViewModel.class);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpToolbar(root.findViewById(R.id.toolbar), getString(R.string.width), true);
    }

    @Override
    public FloatWithUnit provideCurrentFloatWithUnit() {
        return viewModel.getCurrentWidth();
    }

    @Override
    public void onValueChanged(float newValue) {
        viewModel.valueChanged(newValue);
        refreshMeasureUnitAdapterData();
    }

    @Override
    public void onMeasureUnitChanged(MeasureUnit measureUnit) {
        viewModel.measureChanged(measureUnit);
        refreshMeasureUnitAdapterData();
    }
}
