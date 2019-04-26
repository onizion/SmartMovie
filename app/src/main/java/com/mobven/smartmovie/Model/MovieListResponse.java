package com.mobven.smartmovie.Model;

import java.util.ArrayList;

public class MovieListResponse {

    private  ArrayList<Movie> results = new ArrayList<>();
    private int page;
    private int total_results;

    public ArrayList<Movie> getResults() {
        return results;
    }

    public void setResults(ArrayList<Movie> results) {
        this.results = results;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getTotal_results() {
        return total_results;
    }

    public void setTotal_results(int total_results) {
        this.total_results = total_results;
    }
}
