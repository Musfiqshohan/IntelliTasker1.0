package info.androidhive.intellitasker.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;

import info.androidhive.intellitasker.R;

public class HomePage extends AppCompatActivity {


    private Button meetFriend, notificationButton, logOut;
    private ImageView setting_icon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page);
        // ititializing varibales

        meetFriend = (Button) findViewById(R.id.MeetAFriend);
        notificationButton = (Button) findViewById(R.id.checkNotifications);
        logOut = (Button) findViewById(R.id.CheckInbox);
        setting_icon = (ImageButton) findViewById(R.id.settings_icon);
        setting_icon.setImageResource(R.drawable.aa);

        // listeners for buttons

        notificationButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent i = new Intent(getApplicationContext(), ShowNotification.class);
                startActivity(i);

            }
        });


        meetFriend.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent i = new Intent(getApplicationContext(), MeetFriend.class);
                startActivity(i);


            }
        });


        logOut.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent i = new Intent(getApplicationContext(), SignIn.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);


            }
        });

        setting_icon.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


            }
        });


    }

}
