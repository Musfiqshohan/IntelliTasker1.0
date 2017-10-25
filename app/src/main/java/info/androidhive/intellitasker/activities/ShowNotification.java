package info.androidhive.intellitasker.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import info.androidhive.intellitasker.R;

public class ShowNotification extends AppCompatActivity {

    private List<String> notificationList = new ArrayList<>(50);
    private ListView listView;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notifications);
        listView = (ListView) findViewById(R.id.list);
        notificationList.clear();


        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, notificationList);

        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                int itemPosition = position;

                String itemValue = (String) listView.getItemAtPosition(position);


                Toast.makeText(getApplicationContext(),
                        "Position :" + itemPosition + "  ListItem : " + itemValue, Toast.LENGTH_LONG)
                        .show();

            }

        });

        prepareNotificationData();

        adapter.notifyDataSetChanged();

    }


    private void prepare(Map<String, Object> users) {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String UserID = user.getUid();

        for (Map.Entry<String, Object> entry : users.entrySet()) {

            if (UserID.equals(entry.getKey())) {
                Map<String, Object> singleUser = (Map<String, Object>) entry.getValue();

                for (Map.Entry<String, Object> entryy : singleUser.entrySet()) {


                    String key = entryy.getKey();
                    String task = (String) entryy.getValue();

                    notificationList.add(task);
                    adapter.notifyDataSetChanged();

                }
            }


        }


    }

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

}
