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

package com.scandit.shelf.javasettingssample.ui.settings.view;

import androidx.annotation.StringRes;

import com.scandit.shelf.javasettingssample.R;

public enum ViewSettingsOverviewEntry {
    OVERLAY(R.string.overlay_settings),
    VIEWFINDER(R.string.viewfinder_settings),
    SCAN_AREA(R.string.scan_area_settings);

    @StringRes
    public final int displayNameResource;

    ViewSettingsOverviewEntry(@StringRes int displayNameResource) {
        this.displayNameResource = displayNameResource;
    }
}
