package fpoly.thuyltph35992.lab5;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {
    String DOMAIN = "http://192.168.91.93/";

    @GET("/api/list")
    Call<List<CompanyModel>> getCompanies();

    @DELETE("/delete/{id}")
    Call<Void> deleteCompany(@Path("id") String id);

    @POST("/add")
    Call<Void> addCompany(@Body CompanyModel companyModel);
}
