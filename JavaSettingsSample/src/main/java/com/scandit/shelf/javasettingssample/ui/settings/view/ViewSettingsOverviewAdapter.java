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

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.scandit.shelf.javasettingssample.R;
import com.scandit.shelf.javasettingssample.utils.SingleTextViewHolder;

public class ViewSettingsOverviewAdapter
        extends RecyclerView.Adapter<ViewSettingsOverviewAdapter.ViewHolder> {

    private final Callback callback;
    private final ViewSettingsOverviewEntry[] entries;

    ViewSettingsOverviewAdapter(ViewSettingsOverviewEntry[] entries, Callback callback) {
        this.entries = entries;
        this.callback = callback;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new ViewHolder(inflater.inflate(R.layout.single_text_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final ViewSettingsOverviewEntry currentEntry = entries[position];
        holder.setFirstTextView(currentEntry.displayNameResource);
        holder.itemView.setOnClickListener(v -> callback.onEntryClick(currentEntry));
    }

    @Override
    public int getItemCount() {
        return entries.length;
    }

    protected static class ViewHolder extends SingleTextViewHolder {

        ViewHolder(View itemView) {
            super(itemView, R.id.text_field);
        }
    }

    interface Callback {
        void onEntryClick(ViewSettingsOverviewEntry entry);
    }
}
