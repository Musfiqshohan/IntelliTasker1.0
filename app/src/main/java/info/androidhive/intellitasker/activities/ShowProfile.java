package info.androidhive.intellitasker.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import info.androidhive.intellitasker.R;

/**
 * Created by samiul on 9/27/17.
 */

public class ShowProfile extends AppCompatActivity {


    private TextView name, study, occupation, institution, interests;
    public String nam = "";
    private Button meet;
    public String receiver = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);
        // initialization
        name = (TextView) findViewById(R.id.person_name);
        study = (TextView) findViewById(R.id.person_study);
        occupation = (TextView) findViewById(R.id.person_occupation);
        institution = (TextView) findViewById(R.id.person_institution);
        interests = (TextView) findViewById(R.id.person_interests);
        meet = (Button) findViewById(R.id.meet_request);

        // getting information from previous intent
        Bundle i = getIntent().getExtras();


        nam = i.getString("Name");
        String Occup = i.getString("Occupation");
        String Inter = i.getString("Interests");
        String Inst = i.getString("Institution");
        String Stud = i.getString("Study");
        receiver = i.getString("id");

        name.setText("Name : " + nam);
        study.setText("Studies : " + Stud);
        occupation.setText("Occupation : " + Occup);
        institution.setText("Instituion : " + Inst);
        interests.setText("Interests : " + Inter);
        // listener for meet button
        meet.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showFreeSlotsActivity();
            }
        });


    }


    private void showFreeSlotsActivity() {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


        String UserID = user.getUid();

        Intent i = new Intent(getApplicationContext(), SuggestTime.class);
        // sending sender and receiver user id for use in next activity
        i.putExtra("senderUID", UserID);
        i.putExtra("receiverUID", receiver);

        startActivity(i);


    }
}