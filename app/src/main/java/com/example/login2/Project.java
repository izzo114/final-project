package com.example.login2;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.login2.ui.AcademyFragment;
import com.example.login2.ui.LeagueFragment;
import com.example.login2.ui.ReserveFragment;
import com.example.login2.ui.home.HomeFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class Project extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private NavigationView navigationView;
    private DrawerLayout drawer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project);



        BottomNavigationView bottomnav =findViewById(R.id.bottom_navigation);
        bottomnav.setOnNavigationItemSelectedListener(navListner);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new HomeFragment()).commit();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



         drawer = findViewById(R.id.drawer_layout);
         navigationView = findViewById(R.id.nav_view);
       navigationView.setNavigationItemSelectedListener(listener);



       // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
               R.id.nav_home, R.id.nav_profile,R.id.nav_call)
                .setDrawerLayout(drawer)
               .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }


   private NavigationView.OnNavigationItemSelectedListener listener = new NavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            int id = item.getItemId();
            switch (id){
                case R.id.nav_home:
                    Toast.makeText(Project.this, "home", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.nav_profile:
                    Toast.makeText(Project.this, "profile", Toast.LENGTH_SHORT).show();
                    break;


                case R.id.nav_call:
                    Toast.makeText(Project.this, "Contact Us", Toast.LENGTH_SHORT).show();
                    break;
            }
            return true;
        }
    };





    private BottomNavigationView.OnNavigationItemSelectedListener navListner=
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;
                    switch (item.getItemId()) {
                        case R.id.ic_baseline_home_24:
                            selectedFragment = new HomeFragment();
                            getSupportActionBar().setTitle("Home");
                            break;
                        case R.id.ic_baseline_receipt_long_24:
                            selectedFragment = new ReserveFragment();
                            getSupportActionBar().setTitle("Reserve");
                            break;
                        case R.id.ic_baseline_sports_24:
                            selectedFragment = new AcademyFragment();
                            getSupportActionBar().setTitle("Academy");
                            break;
                        case R.id.ic_baseline_sports_soccer_24:
                            selectedFragment = new LeagueFragment();
                            getSupportActionBar().setTitle("League");
                            break;
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,selectedFragment).commit();
                    return true;
                }
            };

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}