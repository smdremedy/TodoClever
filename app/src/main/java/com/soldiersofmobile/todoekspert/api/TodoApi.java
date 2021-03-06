package com.soldiersofmobile.todoekspert.api;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.Query;


public interface TodoApi {

    @Headers({
            "X-Parse-REST-API-Key: LCTpX53aBmbtIXOtFmDb9dklESKUd0q58hFbnRYc",
            "X-Parse-Revocable-Session: 1"
    })
    @GET("/login")
    Call<User> getLogin(@Query("username") String username,
                        @Query("password") String password);

    @Headers({
            "X-Parse-REST-API-Key: LCTpX53aBmbtIXOtFmDb9dklESKUd0q58hFbnRYc"
    })
    @GET("/classes/Todo")
    Call<TodosResponse> getTodos(@Header("X-Parse-Session-Token") String token);

}
