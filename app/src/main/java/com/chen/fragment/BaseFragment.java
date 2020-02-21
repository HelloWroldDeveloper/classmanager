package com.chen.fragment;

import androidx.fragment.app.Fragment;

import com.chen.handle.ScreenUtil;

public class BaseFragment extends Fragment {
    @Override
    public void onDestroy() {
        super.onDestroy();
        ScreenUtil.resetScreen(getActivity());
    }
}
