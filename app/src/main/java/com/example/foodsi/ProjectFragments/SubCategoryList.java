package com.example.foodsi.ProjectFragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.foodsi.ProjectAdapter.RecyclerSubCategoryAdapter;
import com.example.foodsi.ProjectClass.ReplacementFragment;
import com.example.foodsi.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SubCategoryList extends Fragment {

    View view;
    TextView selectSubCategory, categoryNameHeading;
    RecyclerView subCategoryRecyclerView;
    RecyclerSubCategoryAdapter recyclerSubCategoryAdapter;
    Button backToCategoryPage;
    private ArrayList<String> subCategoryList;
    FragmentManager fragmentManager;
    private float v = 0;
    SharedPreferences sharedPreferences;
    private int selectedCategory;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_sub_category_list, container, false);

        //binding
        binding();

        setTextAnimation();
        setLayoutAnimation();

        //shared preferences
        sharedPreferences = getContext().getSharedPreferences("category", Context.MODE_PRIVATE);
        String getCategory = sharedPreferences.getString("selectedCategory","");
        sharedPreferences = getContext().getSharedPreferences("subcategory", Context.MODE_PRIVATE);

        //Fragment Manager
        fragmentManager = getActivity().getSupportFragmentManager();

        if(getCategory.equalsIgnoreCase("Arithmetic")) {
            selectedCategory = R.array.arithmetic;
        }
        else if(getCategory.equalsIgnoreCase("Social Studies")){
            selectedCategory = R.array.social_studies;
        }

        //Set Category Name heading
        categoryNameHeading.setText(getCategory);

        //Load sub-category items
        subCategoryList = new ArrayList<>(Arrays.asList(getResources().getStringArray(selectedCategory)));

        //Set Adapter
        setSubCategoryList();

        //Back to Category Page
        backToCategoryPage();

        return view;
    }

    private void setSubCategoryList(){
        recyclerSubCategoryAdapter = new RecyclerSubCategoryAdapter(subCategoryList, new RecyclerSubCategoryAdapter.ItemClickListener() {
            @Override
            public void onItemClick(String subCategoryName) {
                sharedPreferences.edit().putString("selectedSubCategory",subCategoryName.trim()).apply();
                new ReplacementFragment(fragmentManager, new QuestionList(), R.id.main_activity_frame_layout);
            }
        });
        subCategoryRecyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
        subCategoryRecyclerView.setAdapter(recyclerSubCategoryAdapter);
    }


    private void binding(){
        subCategoryRecyclerView = view.findViewById(R.id.sub_category_recycler_view);
        backToCategoryPage = view.findViewById(R.id.back_to_category_fragment);
        selectSubCategory = view.findViewById(R.id.select_sub_category_text);
        categoryNameHeading = view.findViewById(R.id.category_name_heading);
    }

    private void backToCategoryPage(){
        backToCategoryPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ReplacementFragment(fragmentManager, new CategoryList(), R.id.main_activity_frame_layout);
            }
        });
    }

    private void setTextAnimation(){
        selectSubCategory.setTranslationX(-500);
        selectSubCategory.setAlpha(v);
        selectSubCategory.animate().translationX(0).alpha(1).setDuration(600).setStartDelay(0).start();
    }

    private void setLayoutAnimation(){
        subCategoryRecyclerView.setTranslationY(900);
        subCategoryRecyclerView.setAlpha(v);
        subCategoryRecyclerView.animate().translationY(0).alpha(1).setDuration(600).setStartDelay(0).start();
    }

}