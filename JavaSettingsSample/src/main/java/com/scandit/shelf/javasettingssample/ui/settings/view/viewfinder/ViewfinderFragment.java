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

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;
import com.scandit.shelf.javasettingssample.R;
import com.scandit.shelf.javasettingssample.ui.base.NavigationFragment;
import com.scandit.shelf.javasettingssample.ui.settings.view.viewfinder.type.ViewfinderType;
import com.scandit.shelf.javasettingssample.ui.settings.view.viewfinder.type.ViewfinderTypeAdapter;
import com.scandit.shelf.javasettingssample.ui.settings.view.viewfinder.type.ViewfinderTypeAimer;
import com.scandit.shelf.javasettingssample.ui.settings.view.viewfinder.type.ViewfinderTypeLaserline;
import com.scandit.shelf.javasettingssample.ui.settings.view.viewfinder.type.ViewfinderTypeNone;
import com.scandit.shelf.javasettingssample.ui.settings.view.viewfinder.type.ViewfinderTypeRectangular;
import com.scandit.shelf.javasettingssample.ui.settings.view.viewfinder.type.color.LaserlineDisabledColor;
import com.scandit.shelf.javasettingssample.ui.settings.view.viewfinder.type.color.LaserlineEnabledColor;
import com.scandit.shelf.javasettingssample.ui.settings.view.viewfinder.type.color.RectangularDisabledColor;
import com.scandit.shelf.javasettingssample.ui.settings.view.viewfinder.type.color.RectangularEnabledColor;
import com.scandit.shelf.javasettingssample.ui.settings.view.viewfinder.type.laserlinewidth.ViewfinderLaserlineWidthMeasureFragment;
import com.scandit.shelf.javasettingssample.ui.settings.view.viewfinder.type.rectangleheight.ViewfinderRectangleHeightMeasureFragment;
import com.scandit.shelf.javasettingssample.ui.settings.view.viewfinder.type.rectanglewidth.ViewfinderRectangleWidthMeasureFragment;
import com.scandit.shelf.javasettingssample.utils.SizeSpecification;
import com.scandit.shelf.sdk.core.common.geometry.FloatWithUnit;
import com.scandit.shelf.sdk.core.ui.viewfinder.LaserlineViewfinderStyle;
import com.scandit.shelf.sdk.core.ui.viewfinder.RectangularViewfinderLineStyle;
import com.scandit.shelf.sdk.core.ui.viewfinder.RectangularViewfinderStyle;

public class ViewfinderFragment extends NavigationFragment
        implements ViewfinderTypeAdapter.Callback {

    public static ViewfinderFragment newInstance() {
        return new ViewfinderFragment();
    }

    private ViewfinderViewModel viewModel;

    private RecyclerView recyclerViewfinderTypes;

    private View cardRectangular;
    private View cardRectangularSize;
    private View cardLaserline;
    private View cardAimer;

    private View containerHeight;
    private View containerWidth;
    private View containerSizeSpec;
    private View containerHeightAspect;
    private View containerWidthAspect;
    private View containerShorterDimension;
    private View containerLongerDimensionAspect;
    private View containerRectangularColor;
    private View containerRectangularDisabledColor;
    private View containerEnabledColor;
    private View containerDisabledColor;
    private View containerLaserlineWidth;
    private View containerAimerFrameColor;
    private View containerAimerDotColor;
    private View containerRectangularStyle;
    private View containerRectangularLineStyle;
    private View containerLaserlineStyle;

    private MaterialTextView textType;
    private MaterialTextView textRectangularColor;
    private MaterialTextView textRectangularDisabledColor;
    private MaterialTextView textSizeSpecification;
    private MaterialTextView textWidth;
    private MaterialTextView textHeight;
    private MaterialTextView textEnabledColor;
    private MaterialTextView textDisabledColor;
    private MaterialTextView textLaserlineWidth;
    private MaterialTextView textAimerFrameColor;
    private MaterialTextView textAimerDotColor;
    private MaterialTextView textRectangularStyle;
    private MaterialTextView textRectangularLineStyle;
    private MaterialTextView textLaserlineStyle;

    private TextInputEditText editRectangularDimming;
    private TextInputEditText editHeightAspect;
    private TextInputEditText editWidthAspect;
    private TextInputEditText editShorterDimension;
    private TextInputEditText editLongerDimensionAspect;

    private SwitchMaterial switchRectangularAnimation;
    private SwitchMaterial switchRectangularLooping;

    private ViewfinderTypeAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(ViewfinderViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        return inflater.inflate(R.layout.viewfinder_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setUpToolbar(root.findViewById(R.id.toolbar), getString(R.string.viewfinder_settings), true);

        recyclerViewfinderTypes = view.findViewById(R.id.recycler_viewfinder_types);
        setupRecyclerTypes();

        cardRectangular = view.findViewById(R.id.card_rectangular);
        cardRectangularSize = view.findViewById(R.id.card_rectangular_size);
        cardLaserline = view.findViewById(R.id.card_laserline);
        cardAimer = view.findViewById(R.id.card_aimer);

        textType = view.findViewById(R.id.text_viewfinder_type);
        textRectangularColor = view.findViewById(R.id.text_rectangular_color);
        textRectangularDisabledColor = view.findViewById(R.id.text_rectangular_disabled_color);
        textSizeSpecification = view.findViewById(R.id.text_rectangular_size_specification);
        textWidth = view.findViewById(R.id.text_rectangular_width);
        textHeight = view.findViewById(R.id.text_rectangular_height);
        editShorterDimension = view.findViewById(R.id.edit_rectangular_shorter_dimension);
        editHeightAspect = view.findViewById(R.id.edit_height);
        editWidthAspect = view.findViewById(R.id.edit_rectangular_width);
        editLongerDimensionAspect =
                view.findViewById(R.id.edit_rectangular_longer_dimension_aspect);
        textRectangularStyle = view.findViewById(R.id.text_rectangular_style);
        textRectangularLineStyle = view.findViewById(R.id.text_rectangular_line_style);
        editRectangularDimming = view.findViewById(R.id.edit_rectangular_dimming);
        textLaserlineStyle = view.findViewById(R.id.text_laserline_style);
        switchRectangularAnimation = view.findViewById(R.id.switch_rectangular_animation);
        switchRectangularLooping = view.findViewById(R.id.switch_rectangular_looping);

        containerRectangularColor = view.findViewById(R.id.container_rectangular_color);
        containerRectangularDisabledColor =
                view.findViewById(R.id.container_rectangular_disabled_color);
        containerHeight = view.findViewById(R.id.container_rectangular_height);
        containerWidth = view.findViewById(R.id.container_rectangular_width);
        containerShorterDimension = view.findViewById(R.id.container_rectangular_shorter_dimension);
        containerHeightAspect = view.findViewById(R.id.container_rectangular_height_aspect);
        containerWidthAspect = view.findViewById(R.id.container_rectangular_width_aspect);
        containerLongerDimensionAspect =
                view.findViewById(R.id.container_rectangular_longer_dimension_aspect);
        containerSizeSpec = view.findViewById(R.id.container_rectangular_size_specification);
        containerRectangularStyle = view.findViewById(R.id.container_rectangular_style);
        containerRectangularLineStyle = view.findViewById(R.id.container_rectangular_line_style);

        containerLaserlineWidth = view.findViewById(R.id.container_laserline_width);
        textLaserlineWidth = view.findViewById(R.id.text_laserline_width);
        textEnabledColor = view.findViewById(R.id.text_enabled_color);
        textDisabledColor = view.findViewById(R.id.text_disabled_color);
        containerEnabledColor = view.findViewById(R.id.container_enabled_color);
        containerDisabledColor = view.findViewById(R.id.container_disabled_color);
        containerLaserlineStyle = view.findViewById(R.id.container_laserline_style);

        containerAimerFrameColor = view.findViewById(R.id.container_aimer_frame_color);
        containerAimerDotColor = view.findViewById(R.id.container_aimer_dot_color);
        textAimerFrameColor = view.findViewById(R.id.text_aimer_frame_color);
        textAimerDotColor = view.findViewById(R.id.text_aimer_dot_color);

        setupListeners();
        showHideSubSettings();
    }

    private void setupRecyclerTypes() {
        adapter = new ViewfinderTypeAdapter(viewModel.getViewfinderTypes(), this);
        recyclerViewfinderTypes.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerViewfinderTypes.setAdapter(adapter);
    }

    private void setupListeners() {
        containerRectangularColor.setOnClickListener(v -> buildAndShowRectangularColorMenu());

        containerRectangularDisabledColor.setOnClickListener(v -> buildAndShowRectangularDisabledColorMenu());

        containerHeight.setOnClickListener(v ->
                moveToFragment(ViewfinderRectangleHeightMeasureFragment.newInstance(), true, null));

        containerWidth.setOnClickListener(v ->
                moveToFragment(ViewfinderRectangleWidthMeasureFragment.newInstance(), true, null));

        containerSizeSpec.setOnClickListener(v -> buildAndShowSizeSpecificationMenu());

        editShorterDimension.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                parseShorterDimensionAndApplyChange(v.getText().toString());
                dismissKeyboard(editWidthAspect);
                editShorterDimension.clearFocus();
                return true;
            }
            return false;
        });

        editHeightAspect.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                parseHeightAspectAndApplyChange(v.getText().toString());
                dismissKeyboard(editHeightAspect);
                editHeightAspect.clearFocus();
                return true;
            }
            return false;
        });

        editWidthAspect.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                parseWidthAspectAndApplyChange(v.getText().toString());
                dismissKeyboard(editWidthAspect);
                editWidthAspect.clearFocus();
                return true;
            }
            return false;
        });

        editLongerDimensionAspect.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                parseLongerDimensionAspectAndApplyChange(v.getText().toString());
                dismissKeyboard(editWidthAspect);
                editLongerDimensionAspect.clearFocus();
                return true;
            }
            return false;
        });

        containerRectangularStyle.setOnClickListener(v -> buildAndShowRectangularStyleMenu());

        containerRectangularLineStyle.setOnClickListener(v -> buildAndShowRectangularLineStyleMenu());

        editRectangularDimming.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                parseDimmingTextAndApplyChange(v.getText().toString());
                dismissKeyboard(editRectangularDimming);
                editRectangularDimming.clearFocus();
                return true;
            }
            return false;
        });

        switchRectangularAnimation.setOnCheckedChangeListener((buttonView, isChecked) -> {
            viewModel.setRectangularAnimationEnabled(isChecked);
            refreshRectangularAnimation(isChecked);
        });

        switchRectangularLooping.setOnCheckedChangeListener((buttonView, isChecked) ->
                viewModel.setRectangularLoopingEnabled(isChecked));

        containerLaserlineWidth.setOnClickListener(v ->
                moveToFragment(ViewfinderLaserlineWidthMeasureFragment.newInstance(), true, null));

        containerEnabledColor.setOnClickListener(v -> buildAndShowEnabledColorMenu());

        containerDisabledColor.setOnClickListener(v -> buildAndShowDisabledColorMenu());

        containerLaserlineStyle.setOnClickListener(v -> buildAndShowLaserlineStyleMenu());

        containerAimerFrameColor.setOnClickListener(v -> buildAndShowAimerFrameColorMenu());

        containerAimerDotColor.setOnClickListener(v -> buildAndShowAimerDotColorMenu());
    }

    private void parseShorterDimensionAndApplyChange(String text) {
        try {
            float parsedNumber = getTextAsPositiveFloat(text, false);
            viewModel.setRectangularViewfinderShorterDimension(parsedNumber);
            showHideSubSettings();
        } catch (NumberFormatException e) {
            e.printStackTrace();
            showInvalidNumberToast();
        }
    }

    private void parseHeightAspectAndApplyChange(String text) {
        try {
            float parsedNumber = getTextAsPositiveFloat(text, false);
            viewModel.setRectangularViewfinderHeightAspect(parsedNumber);
            showHideSubSettings();
        } catch (NumberFormatException e) {
            e.printStackTrace();
            showInvalidNumberToast();
        }
    }

    private void parseWidthAspectAndApplyChange(String text) {
        try {
            float parsedNumber = getTextAsPositiveFloat(text, false);
            viewModel.setRectangularViewfinderWidthAspect(parsedNumber);
            showHideSubSettings();
        } catch (NumberFormatException e) {
            e.printStackTrace();
            showInvalidNumberToast();
        }
    }

    private void parseLongerDimensionAspectAndApplyChange(String text) {
        try {
            float parsedNumber = getTextAsPositiveFloat(text, false);
            viewModel.setRectangularViewfinderLongerDimensionAspect(parsedNumber);
            showHideSubSettings();
        } catch (NumberFormatException e) {
            e.printStackTrace();
            showInvalidNumberToast();
        }
    }

    private float getTextAsPositiveFloat(
            String text, boolean allowZero) throws NumberFormatException {
        float parsedNumber = Float.parseFloat(text);
        if ((!allowZero && parsedNumber <= 0) || (allowZero && parsedNumber < 0)
                || Float.isInfinite(parsedNumber) || Float.isNaN(parsedNumber)) {
            throw new NumberFormatException("This is not a valid number");
        } else {
            return parsedNumber;
        }
    }

    private void showInvalidNumberToast() {
        Toast.makeText(requireContext(), R.string.number_not_valid, Toast.LENGTH_LONG).show();
    }

    private void refreshRecyclerTypesData() {
        adapter.updateData(viewModel.getViewfinderTypes());
    }

    private void refreshRectangularViewfinderData(ViewfinderTypeRectangular viewfinder) {
        textType.setText(viewfinder.displayName);
        textRectangularColor.setText(viewfinder.getColor().displayName);
        textRectangularDisabledColor.setText(viewfinder.getDisabledColor().displayName);
        SizeSpecification spec = viewfinder.getSizeSpecification();
        textSizeSpecification.setText(spec.displayName);
        textRectangularStyle.setText(viewfinder.getStyle().name());
        textRectangularLineStyle.setText(viewfinder.getLineStyle().name());

        FloatWithUnit height = viewfinder.getHeight();
        FloatWithUnit width = viewfinder.getWidth();
        refreshHeight(height);
        refreshWidth(width);
        refreshShorterDimension(viewfinder.getShorterDimension());
        refreshHeightAspect(viewfinder.getHeightAspect());
        refreshWidthAspect(viewfinder.getWidthAspect());
        refreshLongerDimensionAspect(viewfinder.getLongerDimensionAspect());
        refreshDimming(viewfinder.getDimming());
        refreshRectangularAnimation(viewfinder.getAnimation());
        refreshRectangularLooping(viewfinder.getLooping());
    }

    private void refreshLaserlineViewfinderData(ViewfinderTypeLaserline viewfinder) {
        textType.setText(viewfinder.displayName);
        FloatWithUnit width = viewfinder.getWidth();
        textLaserlineWidth.setText(
                getString(R.string.size_with_unit, width.getValue(), width.getUnit().name())
        );
        textEnabledColor.setText(viewfinder.getEnabledColor().displayName);
        textDisabledColor.setText(viewfinder.getDisabledColor().displayName);
        textLaserlineStyle.setText(viewfinder.getStyle().name());
    }

    private void refreshAimerViewfinderData(ViewfinderTypeAimer viewfinder) {
        textType.setText(viewfinder.displayName);
        textAimerFrameColor.setText(viewfinder.getFrameColor().displayName);
        textAimerDotColor.setText(viewfinder.getDotColor().displayName);
    }

    private void refreshHeight(FloatWithUnit height) {
        textHeight.setText(
                getString(R.string.size_with_unit, height.getValue(), height.getUnit().name())
        );
    }

    private void refreshWidth(FloatWithUnit width) {
        textWidth.setText(
                getString(R.string.size_with_unit, width.getValue(), width.getUnit().name())
        );
    }

    private void refreshShorterDimension(FloatWithUnit shorterDimension) {
        float value = 0;
        if (shorterDimension != null) {
            value = shorterDimension.getValue();
        }
        editShorterDimension.setText(getString(R.string.size_no_unit, value));
    }

    private void refreshHeightAspect(float heightAspect) {
        editHeightAspect.setText(getString(R.string.size_no_unit, heightAspect));
    }

    private void refreshWidthAspect(float widthAspect) {
        editWidthAspect.setText(getString(R.string.size_no_unit, widthAspect));
    }

    private void refreshLongerDimensionAspect(float aspect) {
        editLongerDimensionAspect.setText(getString(R.string.size_no_unit, aspect));
    }

    private void refreshDimming(float dimming) {
        editRectangularDimming.setText(getString(R.string.size_no_unit, dimming));
    }

    private void refreshRectangularAnimation(boolean animation) {
        switchRectangularAnimation.setChecked(animation);
        switchRectangularLooping.setVisibility(animation ? View.VISIBLE : View.GONE);
    }

    private void refreshRectangularLooping(boolean looping) {
        switchRectangularLooping.setChecked(looping);
    }

    private void showHideSubSettings() {
        ViewfinderType viewfinderType = viewModel.getCurrentViewfinderType();
        if (viewfinderType instanceof ViewfinderTypeNone) {
            setupForNoViewfinder();
        } else if (viewfinderType instanceof ViewfinderTypeRectangular) {
            setupForRectangularViewfinder((ViewfinderTypeRectangular) viewfinderType);
        } else if (viewfinderType instanceof ViewfinderTypeLaserline) {
            setupForLaserlineViewfinder((ViewfinderTypeLaserline) viewfinderType);
        } else if (viewfinderType instanceof ViewfinderTypeAimer) {
            setupForAimerViewfinder((ViewfinderTypeAimer) viewfinderType);
        }
    }

    private void setupForNoViewfinder() {
        textType.setVisibility(View.GONE);
        cardRectangular.setVisibility(View.GONE);
        cardRectangularSize.setVisibility(View.GONE);
        cardLaserline.setVisibility(View.GONE);
        cardAimer.setVisibility(View.GONE);
    }

    private void setupForRectangularViewfinder(ViewfinderTypeRectangular viewfinder) {
        textType.setVisibility(View.VISIBLE);
        cardRectangular.setVisibility(View.VISIBLE);
        cardRectangularSize.setVisibility(View.VISIBLE);
        cardLaserline.setVisibility(View.GONE);
        cardAimer.setVisibility(View.GONE);

        SizeSpecification spec = viewfinder.getSizeSpecification();

        switch (spec) {
            case WIDTH_AND_HEIGHT -> {
                containerHeight.setVisibility(View.VISIBLE);
                containerWidth.setVisibility(View.VISIBLE);
                containerShorterDimension.setVisibility(View.GONE);
                containerHeightAspect.setVisibility(View.GONE);
                containerWidthAspect.setVisibility(View.GONE);
                containerLongerDimensionAspect.setVisibility(View.GONE);
            }
            case HEIGHT_AND_WIDTH_ASPECT -> {
                containerHeight.setVisibility(View.VISIBLE);
                containerWidth.setVisibility(View.GONE);
                containerShorterDimension.setVisibility(View.GONE);
                containerHeightAspect.setVisibility(View.GONE);
                containerWidthAspect.setVisibility(View.VISIBLE);
                containerLongerDimensionAspect.setVisibility(View.GONE);
            }
            case WIDTH_AND_HEIGHT_ASPECT -> {
                containerHeight.setVisibility(View.GONE);
                containerWidth.setVisibility(View.VISIBLE);
                containerShorterDimension.setVisibility(View.GONE);
                containerHeightAspect.setVisibility(View.VISIBLE);
                containerWidthAspect.setVisibility(View.GONE);
                containerLongerDimensionAspect.setVisibility(View.GONE);
            }
            case SHORTER_DIMENSION_AND_ASPECT -> {
                containerHeight.setVisibility(View.GONE);
                containerWidth.setVisibility(View.GONE);
                containerShorterDimension.setVisibility(View.VISIBLE);
                containerHeightAspect.setVisibility(View.GONE);
                containerWidthAspect.setVisibility(View.GONE);
                containerLongerDimensionAspect.setVisibility(View.VISIBLE);
            }
        }

        refreshRectangularViewfinderData(viewfinder);
    }

    private void setupForLaserlineViewfinder(ViewfinderTypeLaserline viewfinder) {
        textType.setVisibility(View.VISIBLE);
        cardRectangular.setVisibility(View.GONE);
        cardRectangularSize.setVisibility(View.GONE);
        cardLaserline.setVisibility(View.VISIBLE);
        cardAimer.setVisibility(View.GONE);

        refreshLaserlineViewfinderData(viewfinder);
    }

    private void setupForAimerViewfinder(ViewfinderTypeAimer viewfinder) {
        textType.setVisibility(View.VISIBLE);
        cardRectangular.setVisibility(View.GONE);
        cardRectangularSize.setVisibility(View.GONE);
        cardLaserline.setVisibility(View.GONE);
        cardAimer.setVisibility(View.VISIBLE);

        refreshAimerViewfinderData(viewfinder);
    }

    private void buildAndShowSizeSpecificationMenu() {
        PopupMenu menu = new PopupMenu(requireContext(), containerSizeSpec, Gravity.END);

        SizeSpecification[] values =
                SizeSpecification.values();
        for (int i = 0; i < values.length; i++) {
            SizeSpecification spec = values[i];
            menu.getMenu().add(0, i, i, spec.displayName);
        }

        menu.setOnMenuItemClickListener(item -> {
            int selectedSizeSpec = item.getItemId();
            viewModel.setRectangularViewfinderSizeSpec(
                    SizeSpecification.values()[selectedSizeSpec]
            );
            showHideSubSettings();
            return true;
        });
        menu.show();
    }

    private void buildAndShowRectangularStyleMenu() {
        PopupMenu menu = new PopupMenu(requireContext(), containerRectangularStyle, Gravity.END);

        RectangularViewfinderStyle[] values = RectangularViewfinderStyle.values();
        for (int i = 0; i < values.length; i++) {
            RectangularViewfinderStyle style = values[i];
            menu.getMenu().add(0, i, i, style.name());
        }

        menu.setOnMenuItemClickListener(item -> {
            int selectedStyle = item.getItemId();
            viewModel.setRectangularViewfinderStyle(
                    RectangularViewfinderStyle.values()[selectedStyle]
            );
            showHideSubSettings();
            return true;
        });
        menu.show();
    }

    private void buildAndShowRectangularLineStyleMenu() {
        PopupMenu menu = new PopupMenu(requireContext(), containerRectangularLineStyle, Gravity.END);

        RectangularViewfinderLineStyle[] values = RectangularViewfinderLineStyle.values();
        for (int i = 0; i < values.length; i++) {
            RectangularViewfinderLineStyle style = values[i];
            menu.getMenu().add(0, i, i, style.name());
        }

        menu.setOnMenuItemClickListener(item -> {
            int selectedStyle = item.getItemId();
            viewModel.setRectangularViewfinderLineStyle(
                    RectangularViewfinderLineStyle.values()[selectedStyle]
            );
            showHideSubSettings();
            return true;
        });
        menu.show();
    }

    private void parseDimmingTextAndApplyChange(String text) {
        try {
            float parsedNumber = getTextAsPositiveFloat(text, true);
            viewModel.setRectangularDimming(parsedNumber);
            showHideSubSettings();
        } catch (NumberFormatException e) {
            e.printStackTrace();
            showInvalidNumberToast();
        }
    }

    private void buildAndShowRectangularColorMenu() {
        PopupMenu menu = new PopupMenu(requireContext(), containerRectangularColor, Gravity.END);

        RectangularEnabledColor[] colors =
                RectangularEnabledColor.getAllForStyle(viewModel.getRectangularViewfinderStyle());
        for (int i = 0; i < colors.length; i++) {
            RectangularEnabledColor color = colors[i];
            menu.getMenu().add(0, i, i, color.displayName);
        }

        menu.setOnMenuItemClickListener(item -> {
            int selectedColor = item.getItemId();
            viewModel.setRectangularViewfinderColor(
                    RectangularEnabledColor.getAllForStyle(
                            viewModel.getRectangularViewfinderStyle()
                    )[selectedColor]
            );
            showHideSubSettings();
            return true;
        });
        menu.show();
    }

    private void buildAndShowRectangularDisabledColorMenu() {
        PopupMenu menu = new PopupMenu(requireContext(), containerRectangularDisabledColor, Gravity.END);

        RectangularDisabledColor[] colors =
                RectangularDisabledColor.getAllForStyle(viewModel.getRectangularViewfinderStyle());
        for (int i = 0; i < colors.length; i++) {
            RectangularDisabledColor color = colors[i];
            menu.getMenu().add(0, i, i, color.displayName);
        }

        menu.setOnMenuItemClickListener(item -> {
            int selectedColor = item.getItemId();
            viewModel.setRectangularViewfinderDisabledColor(
                    RectangularDisabledColor.getAllForStyle(
                            viewModel.getRectangularViewfinderStyle()
                    )[selectedColor]
            );
            showHideSubSettings();
            return true;
        });
        menu.show();
    }

    private void buildAndShowEnabledColorMenu() {
        PopupMenu menu = new PopupMenu(requireContext(), containerEnabledColor, Gravity.END);

        LaserlineEnabledColor[] colors =
                LaserlineEnabledColor.getAllForStyle(viewModel.getLaserlineViewfinderStyle());
        for (int i = 0; i < colors.length; i++) {
            LaserlineEnabledColor color = colors[i];
            menu.getMenu().add(0, i, i, color.displayName);
        }

        menu.setOnMenuItemClickListener(item -> {
            int selectedColor = item.getItemId();
            viewModel.setLaserlineViewfinderEnabledColor(
                    LaserlineEnabledColor.getAllForStyle(
                            viewModel.getLaserlineViewfinderStyle()
                    )[selectedColor]
            );
            showHideSubSettings();
            return true;
        });
        menu.show();
    }

    private void buildAndShowDisabledColorMenu() {
        PopupMenu menu = new PopupMenu(requireContext(), containerDisabledColor, Gravity.END);

        LaserlineDisabledColor[] colors =
                LaserlineDisabledColor.getAllForStyle(viewModel.getLaserlineViewfinderStyle());
        for (int i = 0; i < colors.length; i++) {
            LaserlineDisabledColor color = colors[i];
            menu.getMenu().add(0, i, i, color.displayName);
        }

        menu.setOnMenuItemClickListener(item -> {
            int selectedColor = item.getItemId();
            viewModel.setLaserlineViewfinderDisabledColor(
                    LaserlineDisabledColor.getAllForStyle(
                            viewModel.getLaserlineViewfinderStyle()
                    )[selectedColor]
            );
            showHideSubSettings();
            return true;
        });
        menu.show();
    }

    private void buildAndShowLaserlineStyleMenu() {
        PopupMenu menu = new PopupMenu(requireContext(), containerLaserlineStyle, Gravity.END);

        LaserlineViewfinderStyle[] values = LaserlineViewfinderStyle.values();
        for (int i = 0; i < values.length; i++) {
            LaserlineViewfinderStyle style = values[i];
            menu.getMenu().add(0, i, i, style.name());
        }

        menu.setOnMenuItemClickListener(item -> {
            int selectedStyle = item.getItemId();
            viewModel.setLaserlineViewfinderStyle(
                    LaserlineViewfinderStyle.values()[selectedStyle]
            );
            showHideSubSettings();
            return true;
        });
        menu.show();
    }

    private void buildAndShowAimerFrameColorMenu() {
        PopupMenu menu = new PopupMenu(requireContext(), containerAimerFrameColor, Gravity.END);

        ViewfinderTypeAimer.FrameColor[] values =
                ViewfinderTypeAimer.FrameColor.values();
        for (int i = 0; i < values.length; i++) {
            ViewfinderTypeAimer.FrameColor color = values[i];
            menu.getMenu().add(0, i, i, color.displayName);
        }

        menu.setOnMenuItemClickListener(item -> {
            int selectedColor = item.getItemId();
            viewModel.setAimerViewfinderFrameColor(
                    ViewfinderTypeAimer.FrameColor.values()[selectedColor]
            );
            showHideSubSettings();
            return true;
        });
        menu.show();
    }

    private void buildAndShowAimerDotColorMenu() {
        PopupMenu menu = new PopupMenu(requireContext(), containerAimerDotColor, Gravity.END);

        ViewfinderTypeAimer.DotColor[] values =
                ViewfinderTypeAimer.DotColor.values();
        for (int i = 0; i < values.length; i++) {
            ViewfinderTypeAimer.DotColor color = values[i];
            menu.getMenu().add(0, i, i, color.displayName);
        }

        menu.setOnMenuItemClickListener(item -> {
            int selectedColor = item.getItemId();
            viewModel.setAimerViewfinderDotColor(
                    ViewfinderTypeAimer.DotColor.values()[selectedColor]
            );
            showHideSubSettings();
            return true;
        });
        menu.show();
    }

    @Override
    public void onViewfinderTypeClick(ViewfinderType entry) {
        viewModel.setViewfinderType(entry);
        refreshRecyclerTypesData();
        showHideSubSettings();
    }
}
