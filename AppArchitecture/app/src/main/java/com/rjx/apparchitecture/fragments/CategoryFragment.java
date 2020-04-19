package com.rjx.apparchitecture.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.rjx.apparchitecture.R;
import com.rjx.apparchitecture.databinding.FragmentOthersBinding;

public class CategoryFragment extends Fragment {
    FragmentOthersBinding dataBinding;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_others,container,false);
        dataBinding.homeTxtTitle.setText(R.string.menu_categories);
        return dataBinding.getRoot();
    }
}
