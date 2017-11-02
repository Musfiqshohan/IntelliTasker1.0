package info.androidhive.intellitasker.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import info.androidhive.intellitasker.R;

public class SignIn extends AppCompatActivity {

    private TextView eMail, passWord;
    private Button signin, signup;
    private FirebaseAuth auth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in);

        // initialization

        passWord = (TextView) findViewById(R.id.si_password);
        eMail = (TextView) findViewById(R.id.si_email);
        signin = (Button) findViewById(R.id.si_signin);
        signup = (Button) findViewById(R.id.si_signup);

        signin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                String email = eMail.getText().toString();
                String password = passWord.getText().toString();
                // trying to log in
                if (email.length() > 0 && password.length() > 0) {
                    auth.signInWithEmailAndPassword(eMail.getText().toString(), passWord.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                Intent i = new Intent(getApplicationContext(), HomePage.class);
                                startActivity(i);

                            } else {
                                Toast.makeText(getApplicationContext(), "Login Failed. Check Email and Password", Toast.LENGTH_LONG);
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            Toast.makeText(getApplicationContext(), "Login Failed. Check Email and Password", Toast.LENGTH_LONG);

                        }
                    });
                }
            }
        });


        signup.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                // open new account if not logged in already

                Intent i = new Intent(getApplicationContext(), SignUp.class);
                startActivity(i);

            }

        });


    }
}