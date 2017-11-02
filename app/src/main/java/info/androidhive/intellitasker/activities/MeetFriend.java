package info.androidhive.intellitasker.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import info.androidhive.intellitasker.R;
import info.androidhive.intellitasker.adapters_designer.DividerItemDecoration;
import info.androidhive.intellitasker.adapters_designer.PeoplesAdapter;
import info.androidhive.intellitasker.adapters_designer.RecyclerTouchListener;
import info.androidhive.intellitasker.Entities.People;

/**
 * Created by samiul on 9/27/17.
 */

public class MeetFriend extends AppCompatActivity {
    private List<People> peopleList = new ArrayList<>();
    private List<People> searchList = new ArrayList<>();
    private RecyclerView recyclerView;
    private PeoplesAdapter recyclerViewAdapter;
    private TextView searchText;
    private Button searchButton;
    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.meet_a_friend);
        // inititalizing varibales

        recyclerView = (RecyclerView) findViewById(R.id.meet_friend_recycler_view);
        searchButton = (Button) findViewById(R.id.meet_friend_search_button);
        searchText = (TextView) findViewById(R.id.meet_friend_search_text);
        spinner = (Spinner) findViewById(R.id.meet_friend_search_spinner);
        recyclerViewAdapter = new PeoplesAdapter(searchList);

        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(recyclerViewAdapter);

        // listener for search items
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                People people = searchList.get(position);
                Toast.makeText(getApplicationContext(), people.getName() + " is selected!", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(getApplicationContext(), ShowProfile.class);
                // putting information to show in profile
                i.putExtra("Name", people.getName());
                i.putExtra("Occupation", people.getOccupation());
                i.putExtra("Interests", people.getInterests());
                i.putExtra("Institution", people.getInstitution());
                i.putExtra("Study", people.getStudy());
                i.putExtra("id", people.getId());
                startActivity(i);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        searchButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                searchList.clear();
                recyclerViewAdapter.notifyDataSetChanged();
                if (searchText.getText().toString().length() > 0) {
                    showSearchResult();
                }
            }
        });

// retreiving all user information from firebase
        preparePeopleData();


    }

    private void prepare(Map<String, Object> users) {


        //iterate through each user, ignoring their UID
        for (Map.Entry<String, Object> entry : users.entrySet()) {

            //Get user map
            Map singleUser = (Map) entry.getValue();
// constructing People object from map
            People pe = new People();
            pe.getParsedValues(singleUser);
            pe.setId(entry.getKey());
            peopleList.add(pe);


        }


    }

    // for loading all people data
    private void preparePeopleData() {


        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("users");
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

    // show search result by the chosen category
    private void showSearchResult() {
        String searchingFor = searchText.getText().toString();
        String searchBy = spinner.getSelectedItem().toString();

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        if (searchBy.equals("Name")) {
            for (int i = 0; i < peopleList.size(); i++) {
                People peo = peopleList.get(i);
                if (peo.getName().toLowerCase().contains(searchingFor.toLowerCase())) {
                    if (!peo.getId().equals(uid)) {
                        searchList.add(peo);
                    }
                }
            }
            recyclerViewAdapter.notifyDataSetChanged();


        } else if (searchBy.equals("Job")) {
            for (int i = 0; i < peopleList.size(); i++) {
                People peo = peopleList.get(i);
                if (peo.getOccupation().toLowerCase().contains(searchingFor.toLowerCase())) {
                    if (!peo.getId().equals(uid)) {
                        searchList.add(peo);
                    }
                }
            }
            recyclerViewAdapter.notifyDataSetChanged();
        } else if (searchBy.equals("Interests")) {
            for (int i = 0; i < peopleList.size(); i++) {
                People peo = peopleList.get(i);
                if (peo.getInterests().toLowerCase().contains(searchingFor.toLowerCase())) {
                    if (!peo.getId().equals(uid)) {
                        searchList.add(peo);
                    }
                }
            }
            recyclerViewAdapter.notifyDataSetChanged();
        } else if (searchBy.equals("Institute")) {
            for (int i = 0; i < peopleList.size(); i++) {
                People peo = peopleList.get(i);
                if (peo.getInstitution().toLowerCase().contains(searchingFor.toLowerCase())) {
                    if (!peo.getId().equals(uid)) {
                        searchList.add(peo);
                    }
                }
            }
            recyclerViewAdapter.notifyDataSetChanged();
        }


    }


}
