package fpoly.thuyltph35992.lab5;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    TextInputEditText search;
    ListView listViewMain;

    FloatingActionButton btnThem;
    ApiService apiService;
    List<CompanyModel> companyModelList;

    CompanyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        anhxa();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiService.DOMAIN)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(ApiService.class);

        getCompaniesList();

        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_them();
            }
        });

    }

    public void dialog_them() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        View view = getLayoutInflater().inflate(R.layout.dialog_add, null);
        builder.setView(view);
        Dialog dialog = builder.create();
        dialog.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextInputEditText edtTenCongTy;
        TextInputLayout layoutTenCongTy;

        edtTenCongTy = view.findViewById(R.id.edtTenCongTy);
        layoutTenCongTy = view.findViewById(R.id.layoutTenCongTy);


        Button btnAdd = view.findViewById(R.id.btnThem);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ten = edtTenCongTy.getText().toString().trim();

                layoutTenCongTy.setError(null);

                if (ten.isEmpty()) {
                    layoutTenCongTy.setError("Tên đang để trống");
                    return;
                }

                CompanyModel newComp = new CompanyModel(ten);

                ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
                progressDialog.setMessage("Đang thêm...");
                progressDialog.show();

                Call<Void> call = apiService.addCompany(newComp);

                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            companyModelList.add(newComp);
                            adapter.notifyDataSetChanged();
                            getCompaniesList();
                            Toast.makeText(MainActivity.this, "Thêm cty thành công", Toast.LENGTH_SHORT).show();
                        } else {
                            Log.e(TAG, "Failed to add company: " + response.message());
                            Toast.makeText(MainActivity.this, "Thêm cty không thành công", Toast.LENGTH_SHORT).show();
                        }
                        progressDialog.dismiss();

                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        progressDialog.dismiss();
                        Toast.makeText(MainActivity.this, "Network error", Toast.LENGTH_SHORT).show();
                    }
                });

                dialog.dismiss();

            }
        });
    }

    private void getCompaniesList() {
        Call<List<CompanyModel>> call = apiService.getCompanies();
        call.enqueue(new Callback<List<CompanyModel>>() {
            @Override
            public void onResponse(Call<List<CompanyModel>> call, Response<List<CompanyModel>> response) {
                if (response.isSuccessful()){
                    companyModelList = response.body();
                    adapter = new CompanyAdapter(MainActivity.this, companyModelList);
                    adapter.notifyDataSetChanged();
                    listViewMain.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<List<CompanyModel>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Không lấy được danh sách", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void anhxa() {
        search = findViewById(R.id.search);
        listViewMain = findViewById(R.id.listViewMain);
        btnThem = findViewById(R.id.btnThem);
    }
}