package info.androidhive.intellitasker.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import info.androidhive.intellitasker.R;

public class ShowNotification extends AppCompatActivity {

    private String senderID = "";
    private List<String> notificationList = new ArrayList<>(50);
    private List<String> receiverIDList = new ArrayList<>(50);
    private ListView listView;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notifications);
        // initialization
        listView = (ListView) findViewById(R.id.list);
        notificationList.clear();
        senderID = getUserID();


        adapter = new ArrayAdapter<String>(this,
                R.layout.list_item, android.R.id.text1, notificationList);

        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        // listener for listview

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                int itemPosition = position;
                //16:31 to 20:00

                String itemValue = (String) listView.getItemAtPosition(position);


                String[] div = itemValue.split(" -- ");
                itemValue = div[1];

                String[] dateandtime = itemValue.split(" on ");
                String meetingDate = "-" + dateandtime[1];
                itemValue = dateandtime[0];


                String[] fromto = itemValue.split(" to ");
                String from = fromto[0];
                String to = fromto[1];

                SimpleDateFormat parser = new SimpleDateFormat("HH:mm-dd/MM/yyyy");


                Date TimeFrom = null;

                try {
                    TimeFrom = parser.parse(from + meetingDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                Date TimeTo = null;

                try {
                    TimeTo = parser.parse(to + meetingDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                String addTaskTo = receiverIDList.get(position);

                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
                mDatabase.child("tasks").child(addTaskTo).child(senderID).setValue(String.valueOf(TimeFrom.getTime()) + "#" + String.valueOf(TimeTo.getTime()));
                mDatabase.child("tasks").child(senderID).child(addTaskTo).setValue(String.valueOf(TimeFrom.getTime()) + "#" + String.valueOf(TimeTo.getTime()));

            }

        });

        prepareNotificationData();

        adapter.notifyDataSetChanged();

    }

    // add notifications for current user
    private void prepare(Map<String, Object> users) {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String UserID = user.getUid();

        for (Map.Entry<String, Object> entry : users.entrySet()) {

            if (UserID.equals(entry.getKey())) {
                Map<String, Object> singleUser = (Map<String, Object>) entry.getValue();

                for (Map.Entry<String, Object> entryy : singleUser.entrySet()) {


                    String key = entryy.getKey();
                    String task = (String) entryy.getValue();
                    receiverIDList.add(key);
                    notificationList.add(task);
                    adapter.notifyDataSetChanged();

                }
            }


        }


    }

    // retreive notifications from firebase
    private void prepareNotificationData() {

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("notification");
        ref.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        prepare((Map<String, Object>) dataSnapshot.getValue());
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


    }


    private String getUserID() {

        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }
}
