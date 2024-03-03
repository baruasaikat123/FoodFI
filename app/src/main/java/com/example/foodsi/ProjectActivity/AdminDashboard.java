package com.example.foodsi.ProjectActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.foodsi.ProjectClass.StatusBar;
import com.example.foodsi.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class AdminDashboard extends AppCompatActivity {

    private FirebaseAuth fAuth;
    private FirebaseFirestore db;
    ProgressDialog progressDialog;
    Button logoutBtn, submitBtn;
    AutoCompleteTextView dropdownItemCategory, dropdownItemSubCategory, dropdownItemCorrectAns;
    TextInputLayout dropdownItemSubCategoryLayout, questionTextFieldLayout, option1TextFieldLayout, option2TextFieldLayout, option3TextFieldLayout,
            option4TextFieldLayout, ansTextFieldLayout, dropdownItemAnsLayout;
    TextInputEditText questionTextField, option1TextField, option2TextField, option3TextField, option4TextField, ansTextField;

    private ArrayList<String> categoryList, subCategoryList;
    private ArrayList<String> ansList = new ArrayList<>();
    TextView subCategoryText;
    private String selectedCategory = "", selectedSubCategory = "";
    private int categoryId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        //setStatusBar();
        new StatusBar().setStatusBarColor(this.getWindow(),getApplicationContext(),R.color.app_status_bar);

        //Initialize Firebase Auth
        fAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        //binding
        binding();

        //hide sub-category
        subCategoryText.setVisibility(View.GONE);
        dropdownItemSubCategoryLayout.setVisibility(View.GONE);

        //Load Category
        loadCategoryList();

        //select category
        selectCategory();

        //check for category change
        checkForCategoryChanges();

        //ans dropdown
        //setAnswerDropdown();

        //setDropdownAdapter(dropdownItemCorrectAns, ansList);

        //save in DB
        submitANDSaveToDB();

        //logout --> Move to MainActivity
        logout();

    }

    private void binding(){
        logoutBtn = findViewById(R.id.logout);
        dropdownItemCategory = findViewById(R.id.dropdown_item_category);
        dropdownItemSubCategoryLayout = findViewById(R.id.dropdown_item_sub_category_layout);
        dropdownItemSubCategory = findViewById(R.id.dropdown_item_sub_category);
        subCategoryText = findViewById(R.id.sub_category_text_view);
        questionTextFieldLayout = findViewById(R.id.question_text_field_layout);
        option1TextFieldLayout = findViewById(R.id.option1_text_field_layout);
        option2TextFieldLayout = findViewById(R.id.option2_text_field_layout);
        option3TextFieldLayout = findViewById(R.id.option3_text_field_layout);
        option4TextFieldLayout = findViewById(R.id.option4_text_field_layout);
        ansTextFieldLayout = findViewById(R.id.ans_text_field_layout);
        questionTextField = findViewById(R.id.question_text_field);
        option1TextField = findViewById(R.id.option1_text_field);
        option2TextField = findViewById(R.id.option2_text_field);
        option3TextField = findViewById(R.id.option3_text_field);
        option4TextField = findViewById(R.id.option4_text_field);
        ansTextField = findViewById(R.id.ans_text_field);
        submitBtn = findViewById(R.id.submit_btn);
        //dropdownItemAnsLayout = findViewById(R.id.dropdown_item_ans_layout);
       // dropdownItemCorrectAns = findViewById(R.id.dropdown_item_ans);
    }

    private void loadCategoryList(){
        categoryList = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.category)));
        setDropdownAdapter(dropdownItemCategory, categoryList);
    }

    private void selectCategory(){
        dropdownItemCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                subCategoryText.setVisibility(View.VISIBLE);
                dropdownItemSubCategoryLayout.setVisibility(View.VISIBLE);
                selectedCategory = adapterView.getItemAtPosition(i).toString();
                loadSubCategory();
                selectSubCategory();
            }
        });
    }

    private void loadSubCategory() {
        if(selectedCategory.equalsIgnoreCase("Arithmetic")){
            categoryId = R.array.arithmetic;
        }
        else if(selectedCategory.equalsIgnoreCase("Social Studies")){
            categoryId = R.array.social_studies;
        }
        subCategoryList = new ArrayList<>(Arrays.asList(getResources().getStringArray(categoryId)));
        setDropdownAdapter(dropdownItemSubCategory, subCategoryList);
    }
    private void  selectSubCategory(){
        dropdownItemSubCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                selectedSubCategory = adapterView.getItemAtPosition(i).toString();
            }
        });
    }

    private void setDropdownAdapter(AutoCompleteTextView autoCompleteTextView, ArrayList<String> item){
        ArrayAdapter<String> items = new ArrayAdapter<>(this, R.layout.dropdown_item, item);
        autoCompleteTextView.setAdapter(items);
        autoCompleteTextView.setDropDownBackgroundResource(R.color.dropdown_black);
    }

    private void checkForCategoryChanges(){
        dropdownItemCategory.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                dropdownItemSubCategory.setText("");
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

//    private void setAnswerDropdown(){
//        option1TextField.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                ansList.add(option1TextField.getText().toString());
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//            }
//        });
//        option2TextField.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                ansList.add(option2TextField.getText().toString());
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//            }
//        });
//        option3TextField.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                CharSequence text = "after text changed";
//                int duration = Toast.LENGTH_SHORT;
//                Toast myToast = Toast.makeText(AdminDashboard.this, charSequence.toString(), duration);
//                myToast.show();
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//
//            }
//        });
//        option4TextField.addTextChangedListener(new TextWatcher() {
//            String getOption4 = "";
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                getOption4 = option1TextField.getText().toString();
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//                ansList.add(getOption4);
//            }
//        });
//    }

    private void submitANDSaveToDB(){
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String question = questionTextField.getText().toString().trim();
                String category = dropdownItemCategory.getText().toString().trim();
                String subCategory = dropdownItemSubCategory.getText().toString();
                String option1 = option1TextField.getText().toString().trim();
                String option2 = option2TextField.getText().toString().trim();
                String option3 = option3TextField.getText().toString().trim();
                String option4 = option4TextField.getText().toString().trim();
                String ans = ansTextField.getText().toString().trim();

                questionTextFieldLayout.setError("");
                option1TextFieldLayout.setError("");
                option2TextFieldLayout.setError("");
                option3TextFieldLayout.setError("");
                option4TextFieldLayout.setError("");
                ansTextFieldLayout.setError("");

                if(category.isEmpty()){
                    CharSequence text = "Please select a category!";
                    int duration = Toast.LENGTH_SHORT;
                    Toast myToast = Toast.makeText(AdminDashboard.this, text, duration);
                    myToast.show();
                    return;
                }

                if(subCategory.isEmpty()){
                    CharSequence text = "Please select a sub-category!";
                    int duration = Toast.LENGTH_SHORT;
                    Toast myToast = Toast.makeText(AdminDashboard.this, text, duration);
                    myToast.show();
                    return;
                }

                if(question.isEmpty()){
                    questionTextFieldLayout.setError("Question is required");
                    return;
                }
                if(option1.isEmpty()){
                    option1TextFieldLayout.setError("Option1 is required");
                    return;
                }
                if(option2.isEmpty()){
                    option2TextFieldLayout.setError("Option2 is required");
                    return;
                }
                if(option3.isEmpty()){
                    option3TextFieldLayout.setError("Option3 is required");
                    return;
                }
                if(option4.isEmpty()){
                    option4TextFieldLayout.setError("Option4 is required");
                    return;
                }
                if(ans.isEmpty()){
                    ansTextFieldLayout.setError("Answer is required");
                }

                progressDialog = new ProgressDialog(AdminDashboard.this);
                progressDialog.setMessage("Loading ...");
                progressDialog.setCancelable(false);
                progressDialog.show();

                saveQuestionDetails(subCategory, question, new ArrayList<>(Arrays.asList(option1,option2,option3,option4)), ans);

            }
        });
    }

    private void saveQuestionDetails(String category, String question, ArrayList<String> options, String ans){

        HashMap<String, Object> map = new HashMap<>();
        map.put("question",question);
        map.put("category",category);
        map.put("answer",ans);
        map.put("options",options);
        map.put("createdAt", FieldValue.serverTimestamp());

        db.collection("Questions").document().set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                progressDialog.dismiss();
                CharSequence text = "Saved successfully!";
                int duration = Toast.LENGTH_SHORT;
                Toast myToast = Toast.makeText(AdminDashboard.this, text, duration);
                myToast.show();
                dropdownItemCategory.setText("");
                dropdownItemSubCategory.setText("");
                dropdownItemSubCategoryLayout.setVisibility(View.GONE);
                questionTextField.getText().clear();
                option1TextField.getText().clear();
                option2TextField.getText().clear();
                option3TextField.getText().clear();
                option4TextField.getText().clear();
                ansTextField.getText().clear();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                CharSequence text = "Failed to save data. Please try again!";
                int duration = Toast.LENGTH_SHORT;
                Toast myToast = Toast.makeText(AdminDashboard.this, text, duration);
                myToast.show();
                dropdownItemCategory.setText("");
                dropdownItemSubCategory.setText("");
                dropdownItemSubCategoryLayout.setVisibility(View.GONE);
                questionTextField.getText().clear();
                option1TextField.getText().clear();
                option2TextField.getText().clear();
                option3TextField.getText().clear();
                option4TextField.getText().clear();
                ansTextField.getText().clear();
            }
        });

    }



    private void logout(){
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fAuth.signOut();
                startActivity(new Intent(AdminDashboard.this, MainActivity.class));
            }
        });
    }
}