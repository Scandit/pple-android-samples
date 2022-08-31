package com.scandit.shelf.javaapp.ui.pricecheck;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.snackbar.Snackbar;
import com.scandit.shelf.sdk.core.ui.style.Brush;
import com.scandit.shelf.sdk.core.ui.viewfinder.RectangularViewfinder;
import com.scandit.shelf.sdk.price.PriceCheckResult;
import com.scandit.shelf.sdk.price.ui.PriceCheckOverlay;
import com.scandit.shelf.javaapp.R;
import com.scandit.shelf.javaapp.ui.base.CameraPermissionFragment;
import com.scandit.shelf.javaapp.ui.login.LoginFragment;
import com.scandit.shelf.sdk.core.ui.CaptureView;

import java.util.Locale;

/**
 * Price checking of Product labels happen in this Fragment. It takes a Store object that was
 * selected in the StoreSelectionFragment. Additionally, there's an option to click a
 * button in order to discontinue price checking and log user out of the organization.
 */
public class PriceCheckFragment extends CameraPermissionFragment {

    private static final String ARG_STORE_NAME = "store_name";

    public static PriceCheckFragment newInstance(String storeName) {
        // Create a PriceCheckFragment instance by passing as argument name of the user-selected Store.
        Bundle args = new Bundle();
        args.putString(ARG_STORE_NAME, storeName);
        PriceCheckFragment fragment = new PriceCheckFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private PriceCheckViewModel viewModel;
    private CaptureView captureView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = (new ViewModelProvider(this)).get(PriceCheckViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        return inflater.inflate(R.layout.price_check_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setUpToolbar(rootView.findViewById(R.id.toolbar), "", true);

        rootView.findViewById(R.id.btn_logout).setOnClickListener(view1 -> viewModel.pauseAndLogout());

        Bundle args = getArguments();
        // Get the Store name that was passed to this Fragment as argument.
        String storeName = args == null ? "" : args.getString(ARG_STORE_NAME);
        ((TextView) rootView.findViewById(R.id.store_name)).setText(storeName);

        captureView = rootView.findViewById(R.id.capture_view);

        observeLiveData();
    }

    @Override
    public void onResume() {
        super.onResume();
        // Good time to request user for camera permission for price label scanning.
        requestCameraPermission();
    }

    @Override
    public void onCameraPermissionGranted() {
        viewModel.initPriceCheck(
                captureView,
                // Create an augmented overlay visual that will be shown over price labels.
                new PriceCheckOverlay(
                        new RectangularViewfinder(),
                        solidBrush(requireContext(), R.color.transparentGreen),
                        solidBrush(requireContext(), R.color.transparentRed),
                        solidBrush(requireContext(), R.color.transparentGrey)
                )
        );
    }

    @Override
    public void onPause() {
        // Pause price checking when Fragment pauses.
        viewModel.pausePriceCheck();
        super.onPause();
    }

    private void observeLiveData() {
        // Observe the LivaData that posts price checking results.
        viewModel.resultLiveData.observe(
                getViewLifecycleOwner(),
                priceCheckResult -> showTopSnackbar(toMessage(priceCheckResult))
        );
        // Observe the LiveData that posts logout success event.
        viewModel.logoutSucceededLiveData.observe(
                getViewLifecycleOwner(),
                loggedOut -> {
                    if (loggedOut) {
                        clearBackStack();
                        moveToFragment(LoginFragment.newInstance(), false, null);
                    } else {
                        showSnackbar("Logout failed");
                    }
                }
        );
    }

    private String toMessage(PriceCheckResult priceCheckResult) {
        if (priceCheckResult.getCorrectPrice() == null) {
            return "Unrecognized product - captured price: " + priceFormat(priceCheckResult.getCapturedPrice());
        } else if (priceCheckResult.getCapturedPrice() == priceCheckResult.getCorrectPrice()) {
            return priceCheckResult.getName() + "\nCorrect Price: " + priceFormat(priceCheckResult.getCapturedPrice());
        } else {
            return priceCheckResult.getName() + "\nWrong Price: " + priceFormat(priceCheckResult.getCapturedPrice())
                    + ", should be " + priceFormat(priceCheckResult.getCorrectPrice());
        }
    }

    private void showTopSnackbar(String message) {
        View snackbar = rootView.findViewById(R.id.top_snackbar);
        Snackbar.make(snackbar, message, Snackbar.LENGTH_LONG).show();
    }

    private String priceFormat(float price) {
        return String.format(Locale.getDefault(), "%.2f", price);
    }

    private Brush solidBrush(Context context, @ColorRes int color) {
        return new Brush(ContextCompat.getColor(context, color), Color.TRANSPARENT, 0f);
    }
}
