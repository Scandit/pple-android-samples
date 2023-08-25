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

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.switchmaterial.SwitchMaterial;
import com.scandit.shelf.javasettingssample.R;
import com.scandit.shelf.javasettingssample.ui.base.NavigationFragment;

public class OverlayFragment extends NavigationFragment {

    public static OverlayFragment newInstance() {
        return new OverlayFragment();
    }

    private OverlayViewModel viewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(OverlayViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        return inflater.inflate(R.layout.overlay_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpToolbar(rootView.findViewById(R.id.toolbar), getString(R.string.overlay_settings), true);
        setSwitchStates(view);
        setSpinners(view);
    }

    private void setSwitchStates(View view) {
        SwitchMaterial basicOverlay = view.findViewById(R.id.enable_basic_overlay);
        basicOverlay.setChecked(viewModel.isBasicOverlayEnabled());
        basicOverlay.setOnCheckedChangeListener((buttonView, isChecked) -> viewModel.setBasicOverlayEnabled(isChecked));

        SwitchMaterial advancedOverlay = view.findViewById(R.id.enable_advanced_overlay);
        advancedOverlay.setChecked(viewModel.isAdvancedOverlayEnabled());
        advancedOverlay.setOnCheckedChangeListener((buttonView, isChecked) -> viewModel.setAdvancedOverlayEnabled(isChecked));

        SwitchMaterial customOverlay = view.findViewById(R.id.enable_custom_overlay);
        customOverlay.setChecked(viewModel.isCustomOverlayEnabled());
        customOverlay.setOnCheckedChangeListener((buttonView, isChecked) -> viewModel.setCustomOverlayEnabled(isChecked));
    }

    private void setSpinners(View view) {
        Spinner correctBrushSpinner = view.findViewById(R.id.correct_price_spinner);
        correctBrushSpinner.setAdapter(new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, OverlayBrush.values()));
        correctBrushSpinner.setSelection(viewModel.getCorrectPriceBrush().ordinal());
        correctBrushSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                viewModel.setCorrectPriceBrush(OverlayBrush.values()[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                viewModel.setCorrectPriceBrush(OverlayBrush.GREEN);
            }
        });

        Spinner wrongBrushSpinner = view.findViewById(R.id.wrong_price_spinner);
        wrongBrushSpinner.setAdapter(new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, OverlayBrush.values()));
        wrongBrushSpinner.setSelection(viewModel.getWrongPriceBrush().ordinal());
        wrongBrushSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                viewModel.setWrongPriceBrush(OverlayBrush.values()[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                viewModel.setWrongPriceBrush(OverlayBrush.RED);
            }
        });

        Spinner unknownBrushSpinner = view.findViewById(R.id.unknown_product_spinner);
        unknownBrushSpinner.setAdapter(new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, OverlayBrush.values()));
        unknownBrushSpinner.setSelection(viewModel.getUnknownProductBrush().ordinal());
        unknownBrushSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                viewModel.setUnknownProductBrush(OverlayBrush.values()[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                viewModel.setUnknownProductBrush(OverlayBrush.GREY);
            }
        });
    }
}
