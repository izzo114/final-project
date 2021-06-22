package com.example.login2.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.login2.ADDplayground;
import com.example.login2.R;
import com.example.login2.groung;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import static android.content.Context.MODE_PRIVATE;

public class ReserveFragment extends Fragment {
    private View leagueView;
    private adapter_reserve adapter;
    private RecyclerView myleaguelist;
    private DatabaseReference leagueref;
    FirebaseRecyclerOptions<groung> options;
    SharedPreferences sharedPreferences;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        leagueView = inflater.inflate(R.layout.reserve_fregment, container, false);
        myleaguelist = (RecyclerView) leagueView.findViewById(R.id.reserve_list);
        myleaguelist.setLayoutManager(new LinearLayoutManager(getActivity()));
        leagueref = FirebaseDatabase.getInstance().getReference().child("playground");

        sharedPreferences = getActivity().getSharedPreferences("myPref", MODE_PRIVATE);


        Query queries = leagueref;
        options = new FirebaseRecyclerOptions.Builder<groung>()
                .setQuery(queries, groung.class)
                .build();
        adapter = new adapter_reserve(options);
        myleaguelist.setAdapter(adapter);
        queries.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    adapter.startListening();
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "there is problem", Toast.LENGTH_SHORT).show();
            }
        });

        return leagueView;

    }
    //enable options menu in this fragment
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    //inflate the menue
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);
        menu.findItem(R.id.action_settings);
        //to hide items from menu items
        String userType = sharedPreferences.getString("userType",null);
        MenuItem addLeagueItem = menu.findItem(R.id.addLeague);
        if(userType.equals("Client")){
            addLeagueItem.setVisible(false);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    // handle item clicks on menu
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.addLeague:
                Intent intent = new Intent(getActivity(), ADDplayground.class);
                startActivity(intent);

                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }
}