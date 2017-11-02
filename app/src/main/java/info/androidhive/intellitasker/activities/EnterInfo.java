package info.androidhive.intellitasker.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import info.androidhive.intellitasker.R;
import info.androidhive.intellitasker.Entities.People;

public class EnterInfo extends AppCompatActivity {

    private TextView name, study, occupation, institution, interests;
    private Button enterInfoButton, skipButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.enter_information);

        //initializing variables
        enterInfoButton = (Button) findViewById(R.id.ei_button);
        skipButton = (Button) findViewById(R.id.ei_skip);

        name = (TextView) findViewById(R.id.ei_name);
        study = (TextView) findViewById(R.id.ei_study);
        occupation = (TextView) findViewById(R.id.ei_occupation);
        institution = (TextView) findViewById(R.id.ei_institution);
        interests = (TextView) findViewById(R.id.ei_interests);

        //getting user id to store in firebase

        String uid = "";

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            uid = user.getUid();
        }

        final String finalUid = uid;

        // listener for button
        enterInfoButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                // retreiving data from textbox

                String n = name.getText().toString();
                String oc = occupation.getText().toString();
                String ins = institution.getText().toString();
                String in = interests.getText().toString();
                String s = study.getText().toString();


                final People person = new People(n, oc, s, ins, in);

                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
                mDatabase.child("users").child(finalUid).setValue(person);
                // starting homepage
                Intent i = new Intent(getApplicationContext(), HomePage.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                startActivity(i);


            }
        });


        skipButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // starting homepage without storing personal data for anonymity
                Intent i = new Intent(getApplicationContext(), HomePage.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
            }
        });


    }
}