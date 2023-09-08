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

package com.scandit.shelf.javasettingssample.ui.settings.view.scanarea;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textview.MaterialTextView;
import com.scandit.shelf.javasettingssample.R;
import com.scandit.shelf.javasettingssample.ui.base.NavigationFragment;
import com.scandit.shelf.javasettingssample.ui.settings.view.scanarea.height.ScanAreaHeightFragment;
import com.scandit.shelf.javasettingssample.ui.settings.view.scanarea.width.ScanAreaWidthFragment;
import com.scandit.shelf.sdk.core.common.geometry.FloatWithUnit;

public class ScanAreaFragment extends NavigationFragment {

    public static ScanAreaFragment newInstance() {
        return new ScanAreaFragment();
    }

    private ScanAreaViewModel viewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(ScanAreaViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        return inflater.inflate(R.layout.scan_area_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpToolbar(root.findViewById(R.id.toolbar), getString(R.string.scan_area_settings), true);
        setContainers(view);
        setSwitchState(view);
    }

    private void setContainers(View view) {
        FloatWithUnit width = viewModel.getWidth();
        View containerWidth = view.findViewById(R.id.container_width);
        containerWidth.setOnClickListener(v -> moveToFragment(ScanAreaWidthFragment.newInstance(), true, null));
        MaterialTextView textWidth = view.findViewById(R.id.text_width);
        textWidth.setText(
                getString(R.string.size_with_unit, width.getValue(), width.getUnit().name())
        );

        FloatWithUnit height = viewModel.getHeight();
        View containerHeight = view.findViewById(R.id.container_height);
        containerHeight.setOnClickListener(v -> moveToFragment(ScanAreaHeightFragment.newInstance(), true, null));
        MaterialTextView textHeight = view.findViewById(R.id.text_height);
        textHeight.setText(
                getString(R.string.size_with_unit, height.getValue(), height.getUnit().name())
        );
    }

    private void setSwitchState(View view) {
        SwitchMaterial advancedOverlay = view.findViewById(R.id.should_show_scan_area_guides);
        advancedOverlay.setChecked(viewModel.shouldShowScanAreaGuides());
        advancedOverlay.setOnCheckedChangeListener((buttonView, isChecked) -> viewModel.setShowScanAreaGuides(isChecked));
    }
}
