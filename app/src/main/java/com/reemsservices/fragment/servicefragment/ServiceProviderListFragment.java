package com.reemsservices.fragment.servicefragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.reemsservices.R;
import com.reemsservices.fragment.navigationtab.home.SelectCityFragment;
import com.reemsservices.helper.AppConstant;
import com.reemsservices.helper.SecurePreferences;
import com.reemsservices.model.CityModel;
import com.reemsservices.model.GetBusinessModel;
import com.reemsservices.model.ServicesModel;
import com.reemsservices.model.SliderModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.lang.reflect.Type;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;
import es.dmoral.toasty.Toasty;

public class ServiceProviderListFragment extends Fragment {

    ServicesModel servicesModel;
    List<GetBusinessModel> list_business;
    List<GetBusinessModel> list_subCat;

    @BindView(R.id.recycle_servicelist)
    RecyclerView recycle_servicelist;

    @BindView(R.id.txt_servicetitle)
    TextView txt_servicetitle;

    @BindView(R.id.tablayout)
    TabLayout tabLayout;

    @BindView(R.id.relative_noBusinessFound)
    RelativeLayout relative_noBusinessFound;

    @BindView(R.id.btn_selectCity)
    Button btn_selectCity;

    List<CityModel> cityList;
    private KProgressHUD kProgressHUD;
    String catId;

    public ServiceProviderListFragment(ServicesModel servicesModel, List<CityModel> cityList, String catId) {
        this.servicesModel = servicesModel;
        this.cityList = cityList;
        this.catId = catId;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.serviceproviderlistfragment,container,false);
        ButterKnife.bind(this,view);

        txt_servicetitle.setText(servicesModel.getCatName());
        callServiceInfoApi();

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recycle_servicelist.setLayoutManager(layoutManager);

        return view;
    }
    public class ServiceProviderAdapter extends RecyclerView.Adapter<ServiceProviderAdapter.ServiceProviderHolder> {
        @NonNull
        @Override
        public ServiceProviderHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.row_serviceproviderlist_recycle,parent,false);
            ServiceProviderHolder serviceProviderHolder = new ServiceProviderHolder(view);
            return serviceProviderHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull ServiceProviderHolder holder, int position) {
            GetBusinessModel getBusinessModel = list_business.get(position);
            Glide.with(holder.itemView).load(AppConstant.ImageURL + getBusinessModel.getUserProfile()).into(holder.img_serviceperson);
            holder.txt_servicenameofperson.setText(getBusinessModel.getBusName());
            holder.txt_servicerating.setText(getBusinessModel.getTotalRating());

            holder.card_serviceperson.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AppConstant.addFragment(getFragmentManager(),new ServiceFragment(getBusinessModel),"ServiceFragment");
                }
            });

        }

        @Override
        public int getItemCount() {
            return list_business.size();
        }

        public class ServiceProviderHolder extends RecyclerView.ViewHolder {

            @BindView(R.id.img_serviceperson)
            ImageView img_serviceperson;

            @BindView(R.id.txt_servicenameofperson)
            TextView txt_servicenameofperson;

            @BindView(R.id.txt_servicerating)
            TextView txt_servicerating;

            @BindView(R.id.card_serviceperson)
            CardView card_serviceperson;

            public ServiceProviderHolder(@NonNull View itemView) {
                super(itemView);
                ButterKnife.bind(this,itemView);
            }
        }
    }
    public void callServiceInfoApi(){

        kProgressHUD = KProgressHUD.create(getActivity()).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE).setAnimationSpeed(5).setDimAmount(0.5f);
        kProgressHUD.show();

        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        if(SecurePreferences.getStringPreference(getActivity(), AppConstant.USERID).isEmpty()){
            params.put("user_id","0");
        }
        else {
            params.put("user_id", SecurePreferences.getStringPreference(getActivity(), AppConstant.USERID));
        }
        params.put("user_unique_id", SecurePreferences.getStringPreference(getActivity(), AppConstant.USERUNIQUEID));
        params.put("cat_id", servicesModel.getCatId());
        params.put("bus_loc", SecurePreferences.getStringPreference(getActivity(),AppConstant.USERSELECTEDCITY));

        asyncHttpClient.post(AppConstant.BaseURL + "getBusiness.php", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if (kProgressHUD.isShowing()) kProgressHUD.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(new String(responseBody));
                    boolean status = jsonObject.getBoolean("status");
                    boolean is_sub = jsonObject.getBoolean("is_sub");

                    if(status){
                        if(is_sub){
                            tabLayout.setVisibility(View.VISIBLE);
                            JSONArray jsonArray = jsonObject.getJSONArray("sub_categories");
                            Gson gson = new Gson();
                            Type type = new TypeToken<List<GetBusinessModel>>() {}.getType();
                            list_subCat = gson.fromJson(jsonArray.toString(), type);

                            setList(0);

                            for(int i = 0 ; i<list_subCat.size(); i++){
                                tabLayout.addTab(tabLayout.newTab().setText(list_subCat.get(i).getCat_name()));
                            }

                            tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                                @Override
                                public void onTabSelected(TabLayout.Tab tab) {
                                    setList(tabLayout.getSelectedTabPosition());
                                }

                                @Override
                                public void onTabUnselected(TabLayout.Tab tab) {

                                }

                                @Override
                                public void onTabReselected(TabLayout.Tab tab) {

                                }
                            });
                        }
                        else {
                            tabLayout.setVisibility(View.GONE);
                            JSONArray jsonArray = jsonObject.getJSONArray("business");
                            Gson gson = new Gson();
                            Type type = new TypeToken<List<GetBusinessModel>>() {}.getType();
                            list_business = gson.fromJson(jsonArray.toString(), type);

                            if(list_business.size()>0){
                                relative_noBusinessFound.setVisibility(View.GONE);
                                btn_selectCity.setVisibility(View.GONE);
                                ServiceProviderAdapter serviceProviderAdapter = new ServiceProviderAdapter();
                                recycle_servicelist.setAdapter(serviceProviderAdapter);
                            }
                            else {
                                relative_noBusinessFound.setVisibility(View.VISIBLE);
                                btn_selectCity.setVisibility(View.VISIBLE);
                                btn_selectCity.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        SelectCityFragment selectCityFragment = new SelectCityFragment(cityList);
                                        selectCityFragment.show(getFragmentManager(),"SelectCityFragment");

                                    }
                                });
                            }


                        }

                    }
                    else {
                        Toasty.error(getActivity(),jsonObject.optString("message"),5000).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                if (kProgressHUD.isShowing()) kProgressHUD.dismiss();
            }
        });
    }

    private void setList(int position)
    {
        list_business = list_subCat.get(position).getBusiness_list();
        ServiceProviderAdapter serviceProviderAdapter = new ServiceProviderAdapter();
        recycle_servicelist.setAdapter(serviceProviderAdapter);
    }

    @OnClick(R.id.img_back)
    public void img_back(){
        getFragmentManager().popBackStack();
    }
}
