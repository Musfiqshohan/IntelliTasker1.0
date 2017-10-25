package info.androidhive.intellitasker.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.*;

import info.androidhive.intellitasker.R;
import info.androidhive.intellitasker.classes.Task;

public class SuggestTime extends AppCompatActivity {
    String receiver = "", sender = "", sendername = "";

    List<Task> senderList = new ArrayList<>(50);
    List<Task> receiverList = new ArrayList<>(50);


    private List<String> availableList = new ArrayList<>(50);
    private ListView listView;
    private ArrayAdapter<String> adapter;
    private Button seeFreeSlots;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.suggested_meet_time);
        listView = (ListView) findViewById(R.id.taskList);
        seeFreeSlots = (Button) findViewById(R.id.checkFreeSlots);
        senderList.clear();
        final Bundle i = getIntent().getExtras();


        sender = i.getString("senderUID");
        receiver = i.getString("receiverUID");

        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, availableList);


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
                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
                mDatabase.child("notification").child(receiver).child(sender).setValue(sendername + " wants to meet you" + " within : " + itemValue);

            }

        });

        {
            try {
                prepareNotificationData();
            } catch (Exception e) {
            }
        }

        try {

            sendername = getUserName();
        } catch (Exception e) {

        }

        seeFreeSlots.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                availableList.clear();
                adapter.notifyDataSetChanged();
                processTime();


            }
        });


    }

    private String getUserName() {

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(sender).child("name");
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                sendername = (String) dataSnapshot.getValue();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        databaseReference.addValueEventListener(postListener);
        return sendername;
    }


    private void prepare(Map<String, Object> users) {


        for (Map.Entry<String, Object> entry : users.entrySet()) {


            Map<String, Object> singleUser = (Map<String, Object>) entry.getValue();


            for (Map.Entry<String, Object> entryy : singleUser.entrySet()) {


                String key = entryy.getKey();
                String task = (String) entryy.getValue();

                if (sender.equals(entry.getKey())) {

                    String fromTo[] = task.split("#");

                    Date starts = new Date(Long.parseLong(fromTo[0]));
                    Date ends = new Date(Long.parseLong(fromTo[1]));

                    senderList.add(new Task(starts, ends));


                }

                if (receiver.equals(entry.getKey())) {
                    String fromTo[] = task.split("#");

                    Date starts = new Date(Long.parseLong(fromTo[0]));
                    Date ends = new Date(Long.parseLong(fromTo[1]));

                    receiverList.add(new Task(starts, ends));
                }

            }


        }


    }

    private void prepareNotificationData() {


        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("tasks");
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


    private void processTime() {
        long currentTime = System.currentTimeMillis();
        Date currentDate = new Date(currentTime);


        for (int i = 0; i < receiverList.size(); i++) {
            Task tempTask = receiverList.get(i);

            if (tempTask.endTime.before(currentDate)) {
                receiverList.remove(i);
            }

        }

        Calendar calendar = Calendar.getInstance();

        Collections.sort(receiverList, Task.TaskSorter);
        try {

            String available = "";
            Task firstTask = receiverList.get(0);
            Date start = new Date(firstTask.endTime.getTime());


            for (int i = 1; i < receiverList.size(); i++) {

                calendar.setTime(start);

                int hours = calendar.get(Calendar.HOUR_OF_DAY);
                int minutes = calendar.get(Calendar.MINUTE);
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.DAY_OF_MONTH);
                int year = calendar.get(Calendar.YEAR);
                available += Integer.toString(hours) + ":" + Integer.toString(minutes) + "----" + Integer.toString(day) + "/" + Integer.toString(month) + "/" + Integer.toString(year);
                available += " to ";


                Task tempTask = receiverList.get(i);
                Date end = new Date(tempTask.startTime.getTime());
                calendar.setTime(end);


                hours = calendar.get(Calendar.HOUR_OF_DAY);
                minutes = calendar.get(Calendar.MINUTE);
                day = calendar.get(Calendar.DAY_OF_MONTH);
                month = calendar.get(Calendar.DAY_OF_MONTH);
                year = calendar.get(Calendar.YEAR);
                available += Integer.toString(hours) + ":" + Integer.toString(minutes) + "----" + Integer.toString(day) + "/" + Integer.toString(month) + "/" + Integer.toString(year);
                if (end.getTime() - start.getTime() >= 1200000) {
                    availableList.add(available);
                    adapter.notifyDataSetChanged();
                }
                start = tempTask.endTime;
                available = "";


            }


        } catch (Exception e) {

        } finally {
            availableList.add("unspecified time");

            adapter.notifyDataSetChanged();
        }


    }


}
