package info.androidhive.intellitasker.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import info.androidhive.intellitasker.R;

public class PageZero extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_zero);

        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        // check if user is logged in
        // if logged in then go directly to homepage
        if (user != null) {
            Intent i = new Intent(getApplicationContext(), HomePage.class);
            startActivity(i);
        }
        // if not logged in then log in
        else {
            Intent i = new Intent(getApplicationContext(), SignIn.class);
            startActivity(i);
        }


    }
}