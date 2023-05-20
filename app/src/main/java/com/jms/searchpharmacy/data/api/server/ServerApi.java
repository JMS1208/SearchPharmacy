package com.jms.searchpharmacy.data.api.server;


import retrofit2.Response;
import com.jms.searchpharmacy.data.model.server.Convenience;
import com.jms.searchpharmacy.data.model.server.Hospital;
import com.jms.searchpharmacy.data.model.server.Line;

import com.jms.searchpharmacy.data.model.server.Pharmacy;
import com.jms.searchpharmacy.data.model.server.PharmacyLocation;
import com.jms.searchpharmacy.data.model.server.Station;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ServerApi {

    @GET("getLines/")
    Call<List<Line>> getLines();

    @GET("getStationsByLine/{line_name}/")
    Call<List<Station>> getStationByLine(@Path("line_name") String line_name);

    @GET("getDongBySearch/{station_name}/")
    Call<List<Station>> getDongBySearch(@Path("station_name") String station_name);

    @GET("getDongBySelect/{station_name}/")
    Call<List<Station>> getDongByStation(@Path("station_name") String station_name);

    @GET("search/{dong}")
    Call<List<PharmacyLocation>> getPLList(@Path("dong") String dong);

    @GET("detail/{index}")
    Call<PharmacyLocation> getPLDetail(@Path("index") int index);

    @GET("detail/{index}/hospital")
    Call<List<Hospital>> getHospitalList(@Path("index") int index);

    @GET("detail/{index}/pharmacy")
    Call<List<Pharmacy>> getPharmacyList(@Path("index") int index);

    @GET("detail/{index}/convenience")
    Call<List<Convenience>> getConvenienceList(@Path("index") int index);

    @GET("getTop5")
    Call<List<PharmacyLocation>> getTop5();
}
