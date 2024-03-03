package com.example.foodsi.ProjectFragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.foodsi.ProjectAdapter.RecyclerQuestionsAdapter;
import com.example.foodsi.ProjectClass.QuestionInfo;
import com.example.foodsi.ProjectClass.ReplacementFragment;
import com.example.foodsi.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.List;

public class QuestionList extends Fragment {

    View view;
    RecyclerView questionsRecyclerView;
    Button backToSubCategoryPage, prevBtn, nextBtn;
    TextView subCategoryNameHeading;
    ProgressBar progressBar;
    private ArrayList<QuestionInfo> questionList = new ArrayList<>();
    private FirebaseAuth fAuth;
    private FirebaseFirestore db;
    ProgressDialog progressDialog;
    SharedPreferences sharedPreferences;
    FragmentManager fragmentManager;
    private int id = 1;
    private int currentPage = 0;
    private int TOTAL_NO_ITEMS = 0;
    private static final int ITEMS_PER_PAGE = 8;
    private int ITEMS_REMAINING = 0;
    private int LAST_PAGE = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_question_list, container, false);

        //binding
        binding();

        //set prev and next btn and spinner
        prevBtn.setVisibility(View.GONE);
        nextBtn.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);

        //Fragment Manager
        fragmentManager = getActivity().getSupportFragmentManager();

        //sharedPreferences
        sharedPreferences = getContext().getSharedPreferences("subcategory", Context.MODE_PRIVATE);
        String getSubCategory = sharedPreferences.getString("selectedSubCategory","");

        //Set Sub-Category heading
        subCategoryNameHeading.setText(getSubCategory);

        //progressDialog Initialization
        progressDialog = new ProgressDialog(getActivity());

        //FireBase Initialization
        fAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        //Back to Sub-Category Page
        backToSubCategoryPage();

        //Fetch Questions and set Adapter
        fetchQuestionsFromDBAndSetAdapter(getSubCategory.trim());

        //next
        clickOnNextButton();

        //previous
        clickOnPreviousButton();

        return view;
    }

    private void binding(){
        questionsRecyclerView = view.findViewById(R.id.questions_recycler_view);
        backToSubCategoryPage = view.findViewById(R.id.back_to_sub_category_fragment);
        subCategoryNameHeading = view.findViewById(R.id.sub_category_name_heading);
        prevBtn = view.findViewById(R.id.question_list_prev_btn);
        nextBtn = view.findViewById(R.id.question_list_next_btn);
        progressBar = view.findViewById(R.id.question_list_loading);
    }

    private void setButtons(){
        if(currentPage==LAST_PAGE-1){
            prevBtn.setVisibility(View.VISIBLE);
            nextBtn.setVisibility(View.GONE);
        }
        else if(currentPage==0){
            prevBtn.setVisibility(View.GONE);
            nextBtn.setVisibility(View.VISIBLE);
        }
        else if(currentPage >= 1 && currentPage < LAST_PAGE-1){
            prevBtn.setVisibility(View.VISIBLE);
            nextBtn.setVisibility(View.VISIBLE);
        }
    }

    private void backToSubCategoryPage(){
        backToSubCategoryPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ReplacementFragment(fragmentManager, new SubCategoryList(), R.id.main_activity_frame_layout);
            }
        });
    }

    private void setQuestionList(ArrayList<QuestionInfo> pageData){
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        questionsRecyclerView.setLayoutManager(layoutManager);
        RecyclerQuestionsAdapter adapter = new RecyclerQuestionsAdapter(pageData);
        questionsRecyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void clickOnNextButton(){
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentPage++;
                setQuestionList(generatePageData(currentPage));
                setButtons();
            }
        });
    }

    private void clickOnPreviousButton(){
        prevBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentPage--;
                setQuestionList(generatePageData(currentPage));
                setButtons();
            }
        });
    }

    private ArrayList<QuestionInfo> generatePageData(int currentPage){
        int startItem = currentPage*ITEMS_PER_PAGE;
        int numOfData = ITEMS_PER_PAGE;
        ArrayList<QuestionInfo> pageData = new ArrayList<>();

        if(currentPage == LAST_PAGE - 1 && ITEMS_REMAINING > 0){
            for(int i = startItem; i< startItem+ITEMS_REMAINING; i++){
                pageData.add(questionList.get(i));
            }
        }
        else{
            for(int i = startItem; i< startItem+numOfData; i++){
                pageData.add(questionList.get(i));
            }
        }
        return pageData;
    }

    private void fetchQuestionsFromDBAndSetAdapter(String category){
//        progressDialog.setMessage("Loading ...");
//        progressDialog.setCancelable(false);
//        progressDialog.show();
        progressBar.setVisibility(View.VISIBLE);
        db.collection("Questions").whereEqualTo("category",category).orderBy("createdAt", Query.Direction.ASCENDING).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                progressBar.setVisibility(View.GONE);
                if (queryDocumentSnapshots==null) {
                    Log.d("QuestionDetails", "question details not found!");
                    return;
                }
                else{
                    List<DocumentSnapshot> q = queryDocumentSnapshots.getDocuments();
                    if(q.size() > 0){
                        QuestionInfo questionInfo;
                        TOTAL_NO_ITEMS = q.size();
                        ITEMS_REMAINING = TOTAL_NO_ITEMS % ITEMS_PER_PAGE;
                        LAST_PAGE = (TOTAL_NO_ITEMS / ITEMS_PER_PAGE) + ITEMS_REMAINING;
                        for(int i=0; i<q.size(); i++){
                            String question = q.get(i).get("question").toString();
                            List<String> options = (List<String>) q.get(i).get("options");
                            String ans = q.get(i).get("answer").toString();

                            questionInfo = new QuestionInfo( id +". "+ question, options.get(0), options.get(1),
                                    options.get(2), options.get(3), ans, false);

                            questionList.add(questionInfo);
                            id++;
                        }
                        if(TOTAL_NO_ITEMS > ITEMS_PER_PAGE) nextBtn.setVisibility(View.VISIBLE);
                        if(TOTAL_NO_ITEMS > 0) setQuestionList(generatePageData(currentPage));
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressBar.setVisibility(View.GONE);
                Log.d("ErrorQuestionDetails",e.getMessage());
                Context context = getContext();
                int duration = Toast.LENGTH_SHORT;
                Toast myToast = Toast.makeText(context, "Error getting data!", duration);
                myToast.show();
                e.printStackTrace();
            }
        });
    }
}