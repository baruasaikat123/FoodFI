package com.example.foodsi.ProjectClass;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class ReplacementFragment {

    public ReplacementFragment(FragmentManager fragmentManager, Fragment fragment, int layoutCode){
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(layoutCode, fragment);
        fragmentTransaction.commit();
    }
}
