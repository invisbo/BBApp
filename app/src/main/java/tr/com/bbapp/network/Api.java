package tr.com.bbapp.network;

import retrofit2.Call;
import retrofit2.http.GET;
import tr.com.bbapp.network.response.ListResponse;

public interface Api {

    @GET("emredirican/mock-api/db")
    Call<ListResponse> getList();
}
