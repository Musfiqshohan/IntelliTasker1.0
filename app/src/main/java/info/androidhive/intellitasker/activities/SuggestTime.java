package info.androidhive.intellitasker.activities;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import info.androidhive.intellitasker.R;
import info.androidhive.intellitasker.Entities.Task;

public class SuggestTime extends AppCompatActivity {
    String receiver = "", sender = "", sendername = "";
    Calendar myCalendar = Calendar.getInstance();
    EditText meetDate;
    List<Task> senderList = new ArrayList<>(50);
    List<Task> receiverList = new ArrayList<>(50);


    List<String> availableList = new ArrayList<>(50);
    ListView listView;
    ArrayAdapter<String> adapter;
    Button seeFreeSlots;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.suggested_meet_time);

        // initialization

        listView = (ListView) findViewById(R.id.taskList);
        seeFreeSlots = (Button) findViewById(R.id.checkFreeSlots);
        senderList.clear();
        adapter = new ArrayAdapter<String>(this,
                R.layout.list_item, android.R.id.text1, availableList);

        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        // getting data from previous intents

        final Bundle i = getIntent().getExtras();

        sender = i.getString("senderUID");
        receiver = i.getString("receiverUID");

        // adding listener for listview

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {


                int itemPosition = position;

                String itemValue = (String) listView.getItemAtPosition(position);


                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
                mDatabase.child("notification").child(receiver).child(sender).setValue(sendername + " wants to meet you. Time -- " + itemValue + " on " + meetDate.getText().toString());

            }

        });

        myCalendar.setTime(new Date());

        meetDate = (EditText) findViewById(R.id.setMeetDate);
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                myCalendar.set(Calendar.HOUR_OF_DAY, 0);
                myCalendar.set(Calendar.MINUTE, 0);
                meetDate.setText(Integer.toString(myCalendar.get(Calendar.DAY_OF_MONTH)) + "/" + Integer.toString(myCalendar.get(Calendar.MONTH) + 1) + "/" + Integer.toString(myCalendar.get(Calendar.YEAR)));

            }

        };

        meetDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO Auto-generated method stub
                new DatePickerDialog(SuggestTime.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        // getting sender's name to show in receiver's notification menu
        try {

            sendername = getUserName();
        } catch (Exception e) {

        }

        seeFreeSlots.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // preparing tasklist for users
                {
                    try {
                        prepareNotificationData();
                    } catch (Exception e) {
                    }
                }
                availableList.clear();
                adapter.notifyDataSetChanged();
                processTime();


            }
        });


    }

    // get userame
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

    // get tasklists from firebase
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

    // listener for firebase
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

    // process the received data from firebase
    private void processTime() {


        Log.d(String.valueOf(receiverList.size()) + "sizenow", String.valueOf(senderList.size()));


        Date from = new Date(myCalendar.getTime().getTime() + 28800000 - 3600000);
        Date to = new Date(myCalendar.getTime().getTime() + 72000000 + 3600000);

        boolean[] receiverFree = new boolean[805];
        boolean[] senderFree = new boolean[805];

        for (int i = 0; i < 720; i++) {
            receiverFree[i] = true;
            senderFree[i] = true;
        }


        for (int i = 0; i < receiverList.size(); i++) {
            Task tempTask = receiverList.get(i);

            if (!(tempTask.endTime.before(to) && tempTask.startTime.after(from))) {


                Log.d(tempTask.startTime.toString() + "invalid", tempTask.endTime.toString());
                receiverList.remove(i);
                i = 0;
            }

        }

        for (int i = 0; i < senderList.size(); i++) {
            Task tempTask = senderList.get(i);

            if (!(tempTask.endTime.before(to) && tempTask.startTime.after(from))) {
                Log.d(tempTask.startTime.toString() + "invalid", tempTask.endTime.toString());
                senderList.remove(i);
                i = 0;
            }

        }

        for (int i = 0; i < receiverList.size(); i++) {
            Task tempTask = receiverList.get(i);

            if (!(tempTask.endTime.before(to) && tempTask.startTime.after(from))) {


                Log.d(tempTask.startTime.toString() + "invalid", tempTask.endTime.toString());
                receiverList.remove(i);
                i = 0;
            }

        }

        for (int i = 0; i < senderList.size(); i++) {
            Task tempTask = senderList.get(i);

            if (!(tempTask.endTime.before(to) && tempTask.startTime.after(from))) {

                Log.d(tempTask.startTime.toString() + "invalid", tempTask.endTime.toString());
                senderList.remove(i);
                i = 0;
            }

        }

        Log.d(String.valueOf(receiverList.size()) + "size", String.valueOf(senderList.size()));

        for (int i = 0; i < receiverList.size(); i++) {
            Task tempTask = receiverList.get(i);

            int start = (int) ((tempTask.startTime.getTime() - from.getTime()) / 60000);

            int end = (int) ((tempTask.endTime.getTime() - from.getTime()) / 60000);


            for (int time = start; time <= end; time++) {
                receiverFree[time] = false;
            }
        }


        for (int i = 0; i < senderList.size(); i++) {

            Task tempTask = senderList.get(i);

            int start = (int) ((tempTask.startTime.getTime() - from.getTime() - 3600000) / 60000);

            int end = (int) ((tempTask.endTime.getTime() - from.getTime() - 3600000) / 60000);

            for (int time = start; time <= end; time++) {
                senderFree[time] = false;
            }


        }


        for (int i = 0; i < 720; i++) {

            int cnt = 0;
            if (receiverFree[i] == true && senderFree[i] == true && i < 720)

            {
                int st = i;
                while (receiverFree[i] == true && senderFree[i] == true && i < 720) {

                    cnt++;
                    i++;

                }
                if (cnt > 20) {

                    int en = i;

                    long freeFrom = myCalendar.getTime().getTime() + (480 + st) * 60 * 1000;
                    long freeTill = myCalendar.getTime().getTime() + (480 + en) * 60 * 1000;

                    Log.d(String.valueOf(st) + " fromto ", String.valueOf(en));


                    Calendar tempCal = Calendar.getInstance();

                    Date fstart = new Date(freeFrom);
                    Date fend = new Date(freeTill);


                    tempCal.setTime(fstart);

                    int min = tempCal.get(Calendar.MINUTE);
                    int hour = tempCal.get(Calendar.HOUR_OF_DAY);

                    String formattedHour = String.format("%02d", hour);
                    String formattedMin = String.format("%02d", min);

                    String slot = formattedHour + ":" + formattedMin;

                    tempCal.setTime(fend);

                    min = tempCal.get(Calendar.MINUTE);
                    hour = tempCal.get(Calendar.HOUR_OF_DAY);


                    formattedHour = String.format("%02d", hour);
                    formattedMin = String.format("%02d", min);

                    slot += " to " + formattedHour + ":" + formattedMin;


                    availableList.add(slot);
                    adapter.notifyDataSetChanged();


                }
            }
        }


    }


}
