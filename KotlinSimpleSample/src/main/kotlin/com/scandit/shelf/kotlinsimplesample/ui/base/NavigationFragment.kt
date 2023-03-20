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

package com.scandit.shelf.kotlinsimplesample.ui.base

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.snackbar.Snackbar
import com.scandit.shelf.kotlinsimplesample.R

/**
 * Base Fragment that handles all basic setup operations such as setting up the toolbar, setting
 * up navigation from/to the Fragment, inflating options menu and showing a Snackbar message.
 */
open class NavigationFragment : Fragment() {

    protected lateinit var rootView: View

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rootView = view
    }

    protected fun setUpToolbar(toolbar: Toolbar, toolbarTitle: String, showBackButton: Boolean) {
        (activity as? AppCompatActivity)?.apply {
            setSupportActionBar(toolbar)
            supportActionBar?.apply {
                show()
                setHasOptionsMenu(true)
                setDisplayHomeAsUpEnabled(showBackButton)
                setHomeAsUpIndicator(R.drawable.ic_toolbar_back)
                title = toolbarTitle
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            requireActivity().onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    protected fun clearBackStack() {
        with(requireActivity().supportFragmentManager) {
            var entryCount = backStackEntryCount
            while (entryCount-- > 0) {
                popBackStack()
            }
        }
    }

    protected open fun moveToFragment(fragment: Fragment, addToBackStack: Boolean, tag: String?) {
        val transaction = requireActivity().supportFragmentManager
            .beginTransaction()
            .setReorderingAllowed(true)
            .replace(R.id.fragment_container, fragment)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)

        if (addToBackStack) {
            transaction.addToBackStack(tag)
        }
        transaction.commit()
    }

    fun showSnackbar(message: String) {
        Snackbar.make(rootView, message, Snackbar.LENGTH_LONG).show()
    }
}
