package com.example.login2.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.login2.Acadmy_details;
import com.example.login2.R;
import com.example.login2.ui.home.acadm;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class Adapteracadmy extends FirebaseRecyclerAdapter<acadm,Adapteracadmy.academyviewholder> {

    private DatabaseReference leagueref, referencee;

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public Adapteracadmy(@NonNull FirebaseRecyclerOptions<acadm> options) {
        super(options);
    }


    @NonNull
    @Override
    public academyviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Adapteracadmy.academyviewholder(LayoutInflater.from(parent.getContext()).inflate(R.layout.user_display_layout, parent, false));

    }



    @SuppressLint("SetTextI18n")
    @Override
    protected void onBindViewHolder(@NonNull academyviewholder holder, int position, @NonNull acadm model) {
        leagueref = FirebaseDatabase.getInstance().getReference().child("Acadamy");
        referencee = FirebaseDatabase.getInstance().getReference().child("Acadamy");

       holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//انت معايا
//ايوةةةة
                Intent profilintent = new Intent(v.getContext(), Acadmy_details.class);
                profilintent.putExtra("visite_league_details", getRef(position).getKey());
                v.getContext().startActivity(profilintent);
                //startActivity(new Intent(getActivity(),profileActivity.class));
            }
        });


        String league_id = getRef(position).getKey();
        referencee.child(league_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String Lname = snapshot.child("Acadamy name").getValue().toString();
                    String Locname = snapshot.child("location").getValue().toString();
                    String image = snapshot.child("image").getValue().toString();


                    holder.leaguesname.setText(Lname);
                    holder.locations.setText(Locname);
                    if (image != null) {
                        Picasso.get().load(image).into(holder.profimage);
                    } else {
                        holder.profimage.setImageResource(R.drawable.ic_profile);
                    }

                }


            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


            public static class academyviewholder extends RecyclerView.ViewHolder {
                TextView leaguesname, locations;
                ImageView profimage;

                public academyviewholder(@NonNull View itemView) {
                    super(itemView);

                    leaguesname = itemView.findViewById(R.id.league_name);
                    locations = itemView.findViewById(R.id.location_name);
                    profimage = itemView.findViewById(R.id.my_image);

                }
            }
        }