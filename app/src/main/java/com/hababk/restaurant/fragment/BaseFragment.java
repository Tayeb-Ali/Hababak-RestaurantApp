package com.hababk.appstore.fragment;

import android.support.v4.app.Fragment;
import android.widget.Toast;

/**
 * Created by a_man on 12-03-2018.
 */

public class BaseFragment extends Fragment {
    public void toast(String message, boolean isShort) {
        if (getActivity() != null && getActivity().getApplicationContext() != null)
            Toast.makeText(getActivity().getApplicationContext(), message, isShort ? Toast.LENGTH_SHORT : Toast.LENGTH_LONG).show();
    }
}
