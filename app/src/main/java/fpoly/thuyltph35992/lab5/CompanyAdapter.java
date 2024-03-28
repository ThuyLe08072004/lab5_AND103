package fpoly.thuyltph35992.lab5;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CompanyAdapter extends BaseAdapter {
    private List<CompanyModel> companyModelList;
    private ApiService apiService;
    private Context context;

    public CompanyAdapter(Context context, List<CompanyModel> companyModelList) {
        this.context = context;
        this.companyModelList = companyModelList;
    }

    @Override
    public int getCount() {
        return companyModelList.size();
    }

    @Override
    public Object getItem(int position) {
        return companyModelList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.list_item, parent, false);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiService.DOMAIN)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(ApiService.class);

        TextView txtName = rowView.findViewById(R.id.txtName);
        Button btnXoa = rowView.findViewById(R.id.btnXoa);

        CompanyModel companyModel = companyModelList.get(position);

        txtName.setText(companyModel.getTen());

        btnXoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Xác nhận xóa");
                builder.setMessage("Bạn có chắc chắn muốn xóa " + companyModel.getTen() + " không?");
                builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteCompany(companyModel);
                    }
                });
                builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
                    }
                });
                builder.show();
            }
        });

        return rowView;



    }

    private void deleteCompany(CompanyModel companyModel) {
//        Log.d("ID", "deleteCompany: "+companyModel.getId());
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Đang xóa...");
        progressDialog.show();

        Call<Void> call = apiService.deleteCompany(companyModel.getId());
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(context, "Xóa thành công", Toast.LENGTH_SHORT).show();
                    companyModelList.remove(companyModel);
                    notifyDataSetChanged();
                }else {
                    Toast.makeText(context, "Xóa thất bại", Toast.LENGTH_SHORT).show();
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("Xoa", t.getMessage() );
                Toast.makeText(context, "Xóa lỗi", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }
}
