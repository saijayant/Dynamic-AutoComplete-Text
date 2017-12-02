package network;

import com.google.gson.JsonElement;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by macbookpro on 01/11/17.
 */
public interface RetrofitInterface {

    @GET("company_name.php?str=tat")
    Call<JsonElement> getName();


}



