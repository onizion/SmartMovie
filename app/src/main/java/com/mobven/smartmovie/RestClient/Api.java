package com.mobven.smartmovie.RestClient;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Api {

    private static Api instance = null;
    private RestInterface restInterface;
    private Retrofit retrofit;

    private Api() {

        retrofit = new Retrofit.Builder()
                .baseUrl( Util.baseUrl )
                .addConverterFactory( GsonConverterFactory.create() )
                .build();

        restInterface = retrofit.create( RestInterface.class);


    }


    public static Api getInstance() {

        if (instance == null) {
            instance = new Api();
        }

        return instance;
    }


    public RestInterface getRestInterface() {
        return restInterface;


    }
}