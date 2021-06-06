package com.example.login2.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.login2.R;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class AcademyFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.academy_fragment,container,false);
    }
}


