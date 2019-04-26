package com.mobven.smartmovie.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.mobven.smartmovie.Adapter.Adapter;
import com.mobven.smartmovie.Dao.AppDatabase;
import com.mobven.smartmovie.Model.Movie;
import com.mobven.smartmovie.Model.MovieListResponse;
import com.mobven.smartmovie.R;
import com.mobven.smartmovie.RestClient.Api;
import com.mobven.smartmovie.RestClient.RestInterface;
import com.mobven.smartmovie.RestClient.Util;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieFragment extends Fragment {


    private static final String ARG_PARAM1 = "param1";

    RecyclerView recyclerView;
    private String mParam1;
    ProgressBar progressBar;
    Adapter adapter;
    SparseArray<Movie> sparseFavList;
    public MovieFragment() {
    }


    public static MovieFragment newInstance(String category) {
        MovieFragment fragment = new MovieFragment();
        Bundle args = new Bundle();
        args.putString( ARG_PARAM1, category );
        fragment.setArguments( args );
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setHasOptionsMenu(true);

        if (getArguments() != null) {
            mParam1 = getArguments().getString( ARG_PARAM1 );
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.search_menu, menu);
        MenuItem item = menu.findItem(R.id.menu_search);
        SearchView searchView = null;
        if (item != null) {
            searchView = (SearchView) item.getActionView();
        }
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText.length()>0)
                adapter.getFilter().filter(newText);
                return false;
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate( R.layout.fragment_movie,container,false );
        progressBar = view.findViewById( R.id.progressBar );
        recyclerView = view.findViewById( R.id.recyclerView );
        recyclerView.setLayoutManager( new GridLayoutManager( getContext(),2 ) );
        List<Movie> myList = AppDatabase.getAppDatabase(getContext()).configurationWrapperDao().getAllFav();
        sparseFavList = new SparseArray<>();
        for (int i = 0; i <myList.size() ; i++) {
            Movie movie = myList.get(i);
            sparseFavList.append(movie.getId(),movie);
        }
        if(mParam1.equals("favorite")){
            prpAdapter(myList,true);
        }else{
            getMovieList();
        }

        return  view;
    }


    public void getMovieList(){

        progressBar.setVisibility( View.VISIBLE );
        RestInterface apiService = Api.getInstance().getRestInterface();
        Call<MovieListResponse> call = apiService.getMovieList(mParam1, Util.apiKey,"tr_TR");
        call.enqueue( new Callback<MovieListResponse>() {
            @Override
            public void onResponse(Call<MovieListResponse> call, Response<MovieListResponse> response) {

                //status code is 200
                if(response.isSuccessful()){
                    List<Movie> myList = response.body().getResults();
                    Log.d( getTag(),String.valueOf( response.body().getTotal_results() ));
                    prpAdapter(myList,false);

                }

            }

            @Override
            public void onFailure(Call<MovieListResponse> call, Throwable t) {
                Log.d( getTag(),t.getLocalizedMessage() );
                progressBar.setVisibility( View.INVISIBLE );
                Toast.makeText( getContext(),getString( R.string.fetching_data_error ),Toast.LENGTH_LONG ).show();


            }
        } );

    }

    private void prpAdapter(List<Movie> myList,boolean isFavScreen) {
        adapter = new Adapter(myList,getContext(),isFavScreen,sparseFavList);
        recyclerView.setAdapter( adapter );
        adapter.notifyDataSetChanged();
        progressBar.setVisibility( View.INVISIBLE );
    }

}
