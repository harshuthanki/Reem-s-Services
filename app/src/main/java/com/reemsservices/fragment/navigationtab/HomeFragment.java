package com.reemsservices.fragment.navigationtab;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.reemsservices.R;
import com.reemsservices.fragment.servicefragment.ServiceProviderListFragment;
import com.reemsservices.helper.AppConstant;
import com.reemsservices.helper.SecurePreferences;
import com.reemsservices.model.ServicesModel;
import com.reemsservices.model.SliderModel;
import com.yarolegovich.discretescrollview.DiscreteScrollView;
import com.yarolegovich.discretescrollview.InfiniteScrollAdapter;
import com.yarolegovich.discretescrollview.transform.ScaleTransformer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

public class HomeFragment extends Fragment {

    @BindView(R.id.recycle_popular_deals)
    RecyclerView recycle_popular_deals;

    @BindView(R.id.recycle_services)
    RecyclerView recycle_services;

    @BindView(R.id.txt_location)
    TextView txt_locationaddress;

    @BindView(R.id.picker)
    DiscreteScrollView picker;

    List<ServicesModel> servicelist;
    List<SliderModel> sliderlist;

    private KProgressHUD kProgressHUD;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.homefragment,container,false);
        ButterKnife.bind(this,view);
        servicelist = new ArrayList<>();
        sliderlist = new ArrayList<>();
//        txt_locationaddress.setSelected(true);

        RecyclerView.LayoutManager layoutManager1 = new GridLayoutManager(getContext(),4);
        recycle_services.setLayoutManager(layoutManager1);
        HomeApi();
        return view;
    }
    class PopularDealsAdapter extends RecyclerView.Adapter<PopularDealsAdapter.PopularDealsHolder> {
        @NonNull
        @Override
        public PopularDealsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.row_populardeals_recycle,parent,false);
            PopularDealsHolder popularDealsHolder = new PopularDealsHolder(view);
            return popularDealsHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull PopularDealsHolder holder, int position)
        {
            SliderModel sliderModel = sliderlist.get(position);
            Glide.with(getContext()).load(AppConstant.ImageURL + sliderModel.getSliderPhoto()).into(holder.img_populardeals);
        }

        @Override
        public int getItemCount() {
            return sliderlist.size();
        }

        class PopularDealsHolder extends RecyclerView.ViewHolder {

            @BindView(R.id.img_populardeals)
            ImageView img_populardeals;

            public PopularDealsHolder(@NonNull View itemView) {
                super(itemView);
                ButterKnife.bind(this,itemView);
            }
        }
    }
    class ServicesAdapter extends RecyclerView.Adapter<ServicesAdapter.ServicesHolder> {
        @NonNull
        @Override
        public ServicesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.row_services_recycle,parent,false);
            ServicesHolder servicesHolder = new ServicesHolder(view);
            return servicesHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull ServicesHolder holder, int position) {
            ServicesModel servicesModel = servicelist.get(position);
            Glide.with(getContext()).load(AppConstant.ImageURL + servicesModel.getCatImg()).into(holder.img_service);
            holder.txt_servicename.setText(servicesModel.getCatName());

            holder.linear_service.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AppConstant.addFragment(getFragmentManager(), new ServiceProviderListFragment(servicesModel),"ServiceProviderListFragment");

                }
            });

        }

        @Override
        public int getItemCount() {
            return servicelist.size();
        }

        class ServicesHolder extends RecyclerView.ViewHolder {

            @BindView(R.id.img_service)
            ImageView img_service;

            @BindView(R.id.txt_servicename)
            TextView txt_servicename;

            @BindView(R.id.linear_service)
            LinearLayout linear_service;

            public ServicesHolder(@NonNull View itemView) {
                super(itemView);
                ButterKnife.bind(this,itemView);
            }
        }
    }

    public void HomeApi(){

        kProgressHUD = KProgressHUD.create(getActivity()).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE).setAnimationSpeed(5).setDimAmount(0.5f);
        kProgressHUD.show();

        String user_id = SecurePreferences.getStringPreference(getActivity(),AppConstant.USERID);
        String user_unique_id = SecurePreferences.getStringPreference(getActivity(),AppConstant.USERUNIQUEID);
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        if(SecurePreferences.getStringPreference(getActivity(),AppConstant.USERID).isEmpty()){
            params.put("user_id","0");
        }else{
            params.put("user_id",user_id);
            params.put("user_unique_id",user_unique_id);
        }

        asyncHttpClient.post(AppConstant.BaseURL + "home.php", params ,new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody)
            {
                if (kProgressHUD.isShowing()) kProgressHUD.dismiss();
                String response = new String(responseBody);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("popular_deals");
                    Gson gson = new Gson();

                    Type type = new TypeToken<List<SliderModel>>() {
                    }.getType();

                    sliderlist = gson.fromJson(jsonArray.toString(), type);

                    if (sliderlist.size() > 0)
                    {
                        PopularDealsAdapter popularDealsAdapter = new PopularDealsAdapter();

                        InfiniteScrollAdapter wrapper = InfiniteScrollAdapter.wrap(popularDealsAdapter);
                        picker.setAdapter(wrapper);
                        picker.setItemTransformer(new ScaleTransformer.Builder()
                                .setMinScale(0.8f)
                                .build());

                    }

                    jsonArray = jsonObject.getJSONArray("services");

                    type = new TypeToken<List<ServicesModel>>() {
                    }.getType();

                    servicelist = gson.fromJson(jsonArray.toString(), type);

                    if (servicelist.size() > 0)
                    {
                        ServicesAdapter servicesAdapter = new ServicesAdapter();
                        recycle_services.setAdapter(servicesAdapter);
                    }

                } catch (JSONException e) {
                    if (kProgressHUD.isShowing()) kProgressHUD.dismiss();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }
}
