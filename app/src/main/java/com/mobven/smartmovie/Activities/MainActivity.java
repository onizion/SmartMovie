package com.mobven.smartmovie.Activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.mobven.smartmovie.Fragments.MovieFragment;
import com.mobven.smartmovie.R;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );


        BottomNavigationView bottomNavigationView = findViewById( R.id.navigation );
        bottomNavigationView.setOnNavigationItemSelectedListener( navListener );

        changeFragment( MovieFragment.newInstance("now_playing"));
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                    switch (menuItem.getItemId()) {
                        case R.id.navigation_nowplaying:
                            changeFragment( MovieFragment.newInstance( "now_playing" ) );
                            break;
                        case R.id.navigation_upcomingmovies:
                            changeFragment( MovieFragment.newInstance( "upcoming" ) );
                            break;
                        case R.id.navigation_topratedmovies:
                            changeFragment( MovieFragment.newInstance( "top_rated" ) );
                            break;
                        case R.id.navigation_favorites:
                             changeFragment( MovieFragment.newInstance( "favorite" ) );
                            break;
                    }


                    return true;
                }
            };

    public void changeFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace( R.id.framelayout, fragment ).commit();

    }


}