package com.example.foodsi.ProjectActivity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.example.foodsi.ProjectClass.ReplacementFragment;
import com.example.foodsi.ProjectClass.StatusBar;
import com.example.foodsi.ProjectFragments.CategoryList;
import com.example.foodsi.ProjectFragments.WelcomeScreen;
import com.example.foodsi.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    FragmentManager fragmentManager;
    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //setStatusBar();
        new StatusBar().setStatusBarColor(this.getWindow(),getApplicationContext(),R.color.app_status_bar);

        //Fragment Manager
        fragmentManager = getSupportFragmentManager();

        //Initialize firebase Auth
        fAuth = FirebaseAuth.getInstance();

        //Check for First Time Open
        checkFirstOpen();

    }

    private void checkFirstOpen(){
        SharedPreferences sharedPreferences = getSharedPreferences("firstTime",MODE_PRIVATE);
        boolean isFirstTime = sharedPreferences.getBoolean("isFirstRun",true);


        if(!isFirstTime){
            new ReplacementFragment(fragmentManager, new CategoryList(), R.id.main_activity_frame_layout);
        }
        else{
            new ReplacementFragment(fragmentManager, new WelcomeScreen(), R.id.main_activity_frame_layout);
        }
    }

    @Override
    public void onBackPressed() {
        TextView textView = new TextView(this);
        textView.setText("Alert!");
        textView.setTextColor(getResources().getColor(R.color.white));
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to exit ?");
        builder.setTitle("Alert!");
        builder.setCancelable(false);

        builder.setNegativeButton("No", (DialogInterface.OnClickListener) (dialog, which) -> {
            // If user click no then dialog box is canceled.
            dialog.cancel();
        });

        builder.setPositiveButton("Yes", (DialogInterface.OnClickListener) (dialog, which) -> {
            // When the user click yes button then app will close
            finishAffinity();
        });

        AlertDialog alertDialog = builder.create();
        // Show the Alert Dialog box

        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.app_main));
                alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.app_main));
            }
        });
        alertDialog.show();
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = fAuth.getCurrentUser();
        if(currentUser!=null){
            Intent intent = new Intent(MainActivity.this, AdminDashboard.class);
            startActivity(intent);
        }
    }
}