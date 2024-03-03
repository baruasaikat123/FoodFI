package com.example.foodsi.ProjectFragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.foodsi.ProjectClass.ReplacementFragment;
import com.example.foodsi.R;

public class CategoryList extends Fragment {

    CardView arithmeticTab, socialStudiesTab;
    TextView selectCategory, adminLogin;
    View view;
    FragmentManager fragmentManager;
    private float v = 0;
    SharedPreferences sharedPreferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_category_list, container, false);

        //binding
        binding();

        setTextAnimation();
        setLayoutAnimation(arithmeticTab);
        setLayoutAnimation(socialStudiesTab);

        sharedPreferences = getContext().getSharedPreferences("category",Context.MODE_PRIVATE);

        //Fragment Manager
        fragmentManager = getActivity().getSupportFragmentManager();

        //Admin Section
        navigateToAdminLoginPage();

        //Arithmetic Tab
        clickOnArithmeticTab();

        //Social Studies Tab
        clickOnSocialStudiesTab();

        return view;
    }

    private void binding(){
        arithmeticTab = view.findViewById(R.id.category_arithmetic);
        socialStudiesTab = view.findViewById(R.id.category_social_studies);
        selectCategory = view.findViewById(R.id.select_category_text);
        adminLogin = view.findViewById(R.id.admin_login);
    }

    private void navigateToAdminLoginPage(){
        adminLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ReplacementFragment(fragmentManager, new AdminLogin(), R.id.main_activity_frame_layout);
            }
        });
    }

    private void clickOnArithmeticTab(){
        arithmeticTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sharedPreferences.edit().putString("selectedCategory","Arithmetic").apply();
                new ReplacementFragment(fragmentManager, new SubCategoryList(), R.id.main_activity_frame_layout);
            }
        });
    }

    private void clickOnSocialStudiesTab(){
        socialStudiesTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sharedPreferences.edit().putString("selectedCategory","Social Studies").apply();
                new ReplacementFragment(fragmentManager, new SubCategoryList(), R.id.main_activity_frame_layout);
            }
        });
    }

    private void setTextAnimation(){
        selectCategory.setTranslationX(-500);
        selectCategory.setAlpha(v);
        selectCategory.animate().translationX(0).alpha(1).setDuration(600).setStartDelay(0).start();
    }

    private void setLayoutAnimation(CardView cardView){
        cardView.setTranslationY(900);
        cardView.setAlpha(v);
        cardView.animate().translationY(0).alpha(1).setDuration(600).setStartDelay(0).start();
    }
}