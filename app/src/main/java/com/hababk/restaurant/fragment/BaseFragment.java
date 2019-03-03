package com.hababk.restaurant.fragment;

import android.support.v4.app.Fragment;
import android.widget.Toast;

/**
 * Created by Tayeb-Ali on 01-03-2019.
 */

public class BaseFragment extends Fragment {
    public void toast(String message, boolean isShort) {
        if (getActivity() != null && getActivity().getApplicationContext() != null)
            Toast.makeText(getActivity().getApplicationContext(), message, isShort ? Toast.LENGTH_SHORT : Toast.LENGTH_LONG).show();
    }
}
