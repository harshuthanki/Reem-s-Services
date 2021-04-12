package com.reemsservices.fragment.navigationtab.profile;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.reemsservices.R;
import com.reemsservices.fragment.navigationtab.profile.addbusiness.AddBusinessFragment;
import com.reemsservices.fragment.navigationtab.profile.addbusiness.EditBusinessFragment;
import com.reemsservices.helper.AppConstant;
import com.reemsservices.helper.CustomTextviewMontserratRegular;
import com.reemsservices.helper.CustomTextviewMontserratSemiBold;
import com.reemsservices.helper.SecurePreferences;
import com.reemsservices.model.AddServiceModel;
import com.reemsservices.model.CityModel;
import com.reemsservices.model.ServicesModel;
import com.reemsservices.model.ViewBusinessModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;
import es.dmoral.toasty.Toasty;

public class ManageBusinessFragment extends Fragment {

    @BindView(R.id.recycle_manage_business)
    RecyclerView recycle_manage_business;

    List<ViewBusinessModel> list_viewBusiness;
    List<CityModel> list_city;

    private KProgressHUD kProgressHUD;
    ViewBusinessModel viewBusinessModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.managebusinessfragment, container, false);
        ButterKnife.bind(this, view);
        list_viewBusiness = new ArrayList();
        list_city = new ArrayList<>();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        recycle_manage_business.setLayoutManager(layoutManager);
        ViewBusiness();
        return view;
    }

    class BusinessListAdapter extends RecyclerView.Adapter<BusinessListAdapter.BusinessListHolder> {
        @NonNull
        @Override
        public BusinessListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.row_manage_business, parent, false);
            BusinessListHolder businessListHolder = new BusinessListHolder(view);
            return businessListHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull BusinessListHolder holder, int position) {
            ViewBusinessModel viewBusinessModel = list_viewBusiness.get(position);
            Glide.with(getContext()).load(AppConstant.ImageURL + viewBusinessModel.getCatImg()).into(holder.img_business_cat);
            holder.txt_name_of_business.setText(viewBusinessModel.getBusName());
            holder.txt_rating.setText("Rating");
            holder.txt_rating_number.setText(viewBusinessModel.getTotalRating());
            holder.linear_edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AppConstant.addFragment(getFragmentManager(), new EditBusinessFragment(viewBusinessModel,list_viewBusiness,list_city), "EditBusinessFragment");
                }
            });
            holder.btn_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Dialog dialog = new Dialog(getActivity());
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    if (dialog.getWindow() != null)
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.setCancelable(false);
                    dialog.setContentView(R.layout.custom_dialog_delete_business);

                    Button btnCancel = dialog.findViewById(R.id.btnCancel);
                    Button btnYes = dialog.findViewById(R.id.btn_yes);

                    btnYes.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            deleteBusinessApi(viewBusinessModel.getBusId());
                            dialog.dismiss();
                        }
                    });

                    btnCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                }
            });
        }


        @Override
        public int getItemCount() {
            return list_viewBusiness.size();
        }

        class BusinessListHolder extends RecyclerView.ViewHolder {

            @BindView(R.id.card_business_list)
            CardView card_business_list;

            @BindView(R.id.img_business_cat)
            ImageView img_business_cat;

            @BindView(R.id.txt_name_of_business)
            CustomTextviewMontserratSemiBold txt_name_of_business;

            @BindView(R.id.txt_rating)
            CustomTextviewMontserratRegular txt_rating;

            @BindView(R.id.txt_rating_number)
            CustomTextviewMontserratRegular txt_rating_number;

            @BindView(R.id.linear_edit)
            LinearLayout linear_edit;

            @BindView(R.id.btn_delete)
            TextView btn_delete;


            public BusinessListHolder(@NonNull View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }
    }

    @OnClick(R.id.img_back)
    public void img_back() {
        getFragmentManager().popBackStack();
    }

    @OnClick(R.id.btn_add_business)
    public void AddBusiness() {
        AppConstant.addFragment(getFragmentManager(), new AddBusinessFragment(), "AddBusinessFragment");
    }

    public void ViewBusiness() {

        kProgressHUD = KProgressHUD.create(getActivity()).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE).setAnimationSpeed(5).setDimAmount(0.5f);
        kProgressHUD.show();

        String user_id = SecurePreferences.getStringPreference(getActivity(), AppConstant.USERID);
        String user_unique_id = SecurePreferences.getStringPreference(getActivity(), AppConstant.USERUNIQUEID);
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("user_id", user_id);
        params.put("user_unique_id", user_unique_id);

        asyncHttpClient.post(AppConstant.BaseURL + "view_business.php", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if (kProgressHUD.isShowing()) kProgressHUD.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(new String(responseBody));
                    boolean status = jsonObject.getBoolean("status");
                    if (status) {
                        JSONArray jsonArray = jsonObject.getJSONArray("business_view");
                        JSONArray jsonArray1 = jsonObject.getJSONArray("city");

                        Gson gson = new Gson();
                        Type type = new TypeToken<List<ViewBusinessModel>>() {
                        }.getType();
                        list_viewBusiness = gson.fromJson(jsonArray.toString(), type);

                        type = new TypeToken<List<CityModel>>() {
                        }.getType();
                        list_city = gson.fromJson(jsonArray1.toString(), type);


                        if (list_viewBusiness.size() > 0) {
                            BusinessListAdapter businessListAdapter = new BusinessListAdapter();
                            recycle_manage_business.setAdapter(businessListAdapter);
                        } else {
                            Toasty.error(getActivity(), "List empty", 5000).show();
                        }

                    }
                } catch (JSONException e) {
                    if (kProgressHUD.isShowing()) kProgressHUD.dismiss();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toasty.error(getActivity(), "fail", 5000).show();
            }
        });
    }

    public void deleteBusinessApi(String busId) {

        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("user_id", SecurePreferences.getStringPreference(getActivity(), AppConstant.USERID));
        params.put("user_unique_id", SecurePreferences.getStringPreference(getActivity(), AppConstant.USERUNIQUEID));
        params.put("bus_id",busId);

        asyncHttpClient.post(AppConstant.BaseURL + "delete_business.php", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    JSONObject jsonObject = new JSONObject(new String(responseBody));
                    boolean status = jsonObject.getBoolean("status");
                    if(status){
                        Toasty.success(getActivity(),jsonObject.optString("message"),5000).show();
                        ViewBusiness();
                    }else {
                        Toasty.error(getActivity(),jsonObject.optString("message"),5000).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });

    }
}
