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

package com.scandit.shelf.javasettingssample.ui.settings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textview.MaterialTextView;
import com.scandit.shelf.ShelfSdkVersion;
import com.scandit.shelf.javasettingssample.R;
import com.scandit.shelf.javasettingssample.ui.base.NavigationFragment;
import com.scandit.shelf.javasettingssample.ui.settings.feedback.FeedbackFragment;
import com.scandit.shelf.javasettingssample.ui.settings.flow.FlowFragment;
import com.scandit.shelf.javasettingssample.ui.settings.view.ViewSettingsOverviewFragment;

public class SettingsOverviewFragment extends NavigationFragment
        implements SettingsOverviewAdapter.Callback {

    public static SettingsOverviewFragment newInstance() {
        return new SettingsOverviewFragment();
    }

    private SettingsOverviewViewModel viewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(SettingsOverviewViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        return inflater.inflate(R.layout.settings_overview_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setUpToolbar(root.findViewById(R.id.toolbar), getString(R.string.overview_settings), true);

        MaterialTextView versionTextView = root.findViewById(R.id.sdk_version);
        versionTextView.setText(ShelfSdkVersion.VERSION_STRING);

        RecyclerView recyclerOverviewOptions = view.findViewById(R.id.recycler_overview);
        recyclerOverviewOptions.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerOverviewOptions.setAdapter(
                new SettingsOverviewAdapter(viewModel.getOverviewEntries(), this)
        );
    }

    @Override
    public void onEntryClick(SettingsOverviewEntry entry) {
        moveToDeeperSettings(entry);
    }

    private void moveToDeeperSettings(SettingsOverviewEntry entry) {
        switch (entry) {
            case FEEDBACK -> moveToFragment(FeedbackFragment.newInstance(), true, null);
            case VIEW -> moveToFragment(ViewSettingsOverviewFragment.newInstance(), true, null);
            case FLOW -> moveToFragment(FlowFragment.newInstance(), true, null);
        }
    }
}
