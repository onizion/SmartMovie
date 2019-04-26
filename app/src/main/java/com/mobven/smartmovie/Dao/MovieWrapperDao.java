package com.mobven.smartmovie.Dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.mobven.smartmovie.Model.Movie;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface MovieWrapperDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Movie... movies);


    @Query("SELECT * FROM Movie ORDER BY id DESC")
    public List<Movie> getAllFav();


    @Delete
    void delete(Movie movie);

}
