package com.example.foodsi.ProjectFragments;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.foodsi.ProjectClass.ReplacementFragment;
import com.example.foodsi.R;


public class WelcomeScreen extends Fragment {

    Button getStarted;
    View view;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_welcome_screen, container, false);

        //binding
        binding();

        //Click on getStarted button
        clickOnGetStarted();

        return view;
    }

    private void binding(){
        getStarted = view.findViewById(R.id.get_started_btn);
    }

    private void clickOnGetStarted(){
        getStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getContext().getSharedPreferences("firstTime", Context.MODE_PRIVATE).edit().putBoolean("isFirstRun", false).apply();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                new ReplacementFragment(fragmentManager, new CategoryList(), R.id.main_activity_frame_layout);
            }
        });
    }
}