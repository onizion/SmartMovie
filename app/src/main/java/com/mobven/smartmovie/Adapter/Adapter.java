package com.mobven.smartmovie.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.mobven.smartmovie.Dao.AppDatabase;
import com.mobven.smartmovie.Model.Movie;
import com.mobven.smartmovie.R;
import com.mobven.smartmovie.RestClient.Util;

import java.util.ArrayList;
import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.MyViewHolder> implements Filterable {

    private List<Movie> arrayList;
    private List<Movie> filteredUserList;
    private Context context;
    private SparseArray<Movie> sparseFavList;
    private boolean isFavScren =false;
    @Override
    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                String searchString = charSequence.toString();

                if (searchString.isEmpty()) {
                    filteredUserList = arrayList;
                } else {
                    ArrayList<Movie> tempFilteredList = new ArrayList<>();
                    for (Movie movie : arrayList) {
                        // search for movie Title or movie overview
                        if (movie.getTitle().toLowerCase().contains(searchString) ||
                                movie.getOverview().toLowerCase().contains(searchString)) {
                            tempFilteredList.add(movie);
                        }
                    }

                    filteredUserList = tempFilteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredUserList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                filteredUserList = (ArrayList<Movie>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }




    public Adapter(List<Movie> arrayList, Context context,boolean isFavScren,SparseArray<Movie> sparseFavList) {
        this.filteredUserList = arrayList;
        this.arrayList = arrayList;
        this.context = context;
        this.isFavScren = isFavScren;
        this.sparseFavList = sparseFavList;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        LayoutInflater mInflater = LayoutInflater.from( context );
        view = mInflater.inflate( R.layout.movie_item, parent, false );
        return new MyViewHolder( view );


    }


    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder viewHolder, final int position) {

        final Movie movie = filteredUserList.get(position);

        RequestOptions options = new RequestOptions();
        options.centerCrop();
        viewHolder.txtOriginalTitle.setText( movie.getTitle() );
        viewHolder.txtOverview.setText( movie.getOverview() );

        String average_rate = String.valueOf(movie.getVote_average());
        viewHolder.txtRating.setText(average_rate);
        viewHolder.ratingBar.setRating(Float.parseFloat(String.valueOf(movie.getVote_average()/2)));
        Glide.with( viewHolder.itemView ).load(Util.imageUrl+movie.getBackdrop_path()).apply(options).into( viewHolder.ivMovieCover );

            if(sparseFavList.get(movie.getId())==null){
                viewHolder.ivFavorite.setBackgroundResource(R.drawable.ic_baseline_favorite_border_24px);
            }else {
                viewHolder.ivFavorite.setBackgroundResource(R.drawable.ic_baseline_favorite_24px);
            }

            viewHolder.ivFavorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(sparseFavList.get(movie.getId())==null){
                        //addtoFav
                        AppDatabase.getAppDatabase(context).configurationWrapperDao().insert(movie);
                        sparseFavList.put(movie.getId(),movie);
                    }else {
                        //removeFromFav
                        AppDatabase.getAppDatabase(context).configurationWrapperDao().delete(movie);
                        sparseFavList.remove(movie.getId());
                        if(isFavScren){
                            //remove from fav screen instantly
                            filteredUserList.remove(position);
                        }
                    }
                    notifyDataSetChanged();


                }
            });




    }
    @Override
    public int getItemCount() {
        return filteredUserList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {


        public TextView txtOriginalTitle;
        public TextView txtRating;
        public TextView txtOverview;
        public ImageView ivMovieCover,ivFavorite;
        public RatingBar ratingBar;



        public MyViewHolder(@NonNull View itemView) {
            super( itemView );

            txtOriginalTitle = itemView.findViewById( R.id.txtOriginalTitle );
            txtOverview = itemView.findViewById( R.id.txtOverview );
            ivMovieCover = itemView.findViewById( R.id.ivMovieCover );
            txtRating = itemView.findViewById( R.id.txtRating );
            ratingBar = itemView.findViewById( R.id.ratingBar );
            ivFavorite = itemView.findViewById( R.id.ivFavorite );


        }
    }
}
