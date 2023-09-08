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

package com.scandit.shelf.javasettingssample.ui.settings.feedback;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.switchmaterial.SwitchMaterial;
import com.scandit.shelf.javasettingssample.R;
import com.scandit.shelf.javasettingssample.ui.base.NavigationFragment;

public class FeedbackFragment extends NavigationFragment {

    public static FeedbackFragment newInstance() {
        return new FeedbackFragment();
    }

    private FeedbackViewModel viewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(FeedbackViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        return inflater.inflate(R.layout.feedback_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpToolbar(root.findViewById(R.id.toolbar), getString(R.string.feedback_settings), true);
        setSwitchStates(view);
    }

    private void setSwitchStates(View view) {
        setCorrectPriceSwitchStates(view);
        setWrongPriceSwitchStates(view);
        setUnknownProductSwitchStates(view);
    }

    private void setUnknownProductSwitchStates(View view) {
        SwitchMaterial unknownSoundFeedback = view.findViewById(R.id.unknown_sound_feedback);
        unknownSoundFeedback.setChecked(viewModel.isUnknownProductSoundEnabled());
        unknownSoundFeedback.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                viewModel.setUnknownProductSoundEnabled(isChecked);
            }
        });

        SwitchMaterial unknownVibrationFeedback = view.findViewById(R.id.unknown_vibration_feedback);
        unknownVibrationFeedback.setChecked(viewModel.isUnknownProductVibrationEnabled());
        unknownVibrationFeedback.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                viewModel.setUnknownProductVibrationEnabled(isChecked);
            }
        });
    }

    private void setWrongPriceSwitchStates(View view) {
        SwitchMaterial wrongSoundFeedback = view.findViewById(R.id.wrong_sound_feedback);
        wrongSoundFeedback.setChecked(viewModel.isWrongPriceSoundEnabled());
        wrongSoundFeedback.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                viewModel.setWrongPriceSoundEnabled(isChecked);
            }
        });

        SwitchMaterial wrongVibrationFeedback = view.findViewById(R.id.wrong_vibration_feedback);
        wrongVibrationFeedback.setChecked(viewModel.isWrongPriceVibrationEnabled());
        wrongVibrationFeedback.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                viewModel.setWrongPriceVibrationEnabled(isChecked);
            }
        });
    }

    private void setCorrectPriceSwitchStates(View view) {
        SwitchMaterial correctSoundFeedback = view.findViewById(R.id.correct_sound_feedback);
        correctSoundFeedback.setChecked(viewModel.isCorrectPriceSoundEnabled());
        correctSoundFeedback.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                viewModel.setCorrectPriceSoundEnabled(isChecked);
            }
        });

        SwitchMaterial correctVibrationFeedback = view.findViewById(R.id.correct_vibration_feedback);
        correctVibrationFeedback.setChecked(viewModel.isCorrectPriceVibrationEnabled());
        correctVibrationFeedback.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                viewModel.setCorrectPriceVibrationEnabled(isChecked);
            }
        });
    }
}
