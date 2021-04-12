package com.reemsservices.fragment.navigationtab.profile.addbusiness;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.reemsservices.R;
import com.reemsservices.fragment.navigationtab.profile.ManageBusinessFragment;
import com.reemsservices.helper.AppConstant;
import com.reemsservices.helper.SecurePreferences;
import com.reemsservices.model.CityModel;
import com.reemsservices.model.ServicesModel;
import com.reemsservices.model.SubServicesModel;

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

public class AddBusinessFragment extends Fragment {

    @BindView(R.id.edt_business_name)
    EditText edt_business_name;

    @BindView(R.id.edt_business_address)
    EditText edt_business_address;

    @BindView(R.id.edt_mobile_number)
    EditText edt_mobile_number;

    @BindView(R.id.linear_form_one)
    LinearLayout linear_form_one;

    @BindView(R.id.linear_form_two)
    LinearLayout linear_form_two;

    @BindView(R.id.spinner_category)
    Spinner spinner_category;

    @BindView(R.id.spinner_city)
    Spinner spinner_city;

    @BindView(R.id.spinner_sub_category)
    Spinner spinner_sub_category;

    @BindView(R.id.layout_sub)
    LinearLayout layout_sub;

    @BindView(R.id.txt_one)
    TextView txt_one;

    @BindView(R.id.txt_two)
    TextView txt_two;

    @BindView(R.id.view_one)
    View view_one;

    @BindView(R.id.card_step_one)
    CardView card_step_one;

    @BindView(R.id.card_step_two)
    CardView card_step_two;

    @BindView(R.id.img_one_check)
    ImageView img_one_check;

    @BindView(R.id.linear_previous_step_one)
    LinearLayout linear_previous_step_one;

    List<ServicesModel> list_services;
    List<SubServicesModel> list_subServices = new ArrayList<>();
    List<CityModel> list_city;
    private String selectedCategory_id;
    private String selectedSubCategory_id="0";
    private KProgressHUD kProgressHUD;

    String user_id , user_unique_id;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.addbusinessfragment,container,false);
        ButterKnife.bind(this,view);

        user_id = SecurePreferences.getStringPreference(getActivity(), AppConstant.USERID);
        user_unique_id = SecurePreferences.getStringPreference(getActivity(), AppConstant.USERUNIQUEID);
        list_city = new ArrayList<>();
        servicesAPI();
        return view;
    }
    @OnClick(R.id.img_back)
    public void img_back(){
        getFragmentManager().popBackStack();
    }

    public class CustomAdapterCity extends BaseAdapter {
        LayoutInflater inflate;

        public CustomAdapterCity() {
            inflate = (LayoutInflater.from(getActivity()));
        }

        @Override
        public int getCount() {
            return list_city.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = inflate.inflate(R.layout.simple_item_spinner, null);
            TextView txt_name = (TextView) view.findViewById(R.id.txt_name);
            txt_name.setText(list_city.get(i).city_name);
            return view;
        }
    }

    public class CustomAdapterServices extends BaseAdapter {
        LayoutInflater inflate;

        public CustomAdapterServices() {
            inflate = (LayoutInflater.from(getActivity()));
        }

        @Override
        public int getCount() {
            return list_services.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = inflate.inflate(R.layout.simple_item_spinner, null);
            TextView txt_name = (TextView) view.findViewById(R.id.txt_name);
            txt_name.setText(list_services.get(i).getCatName());
            return view;
        }
    }
    public class CustomAdapterSub_Services extends BaseAdapter {
        LayoutInflater inflate;

        public CustomAdapterSub_Services() {
            inflate = (LayoutInflater.from(getActivity()));
        }

        @Override
        public int getCount() {
            return list_subServices.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = inflate.inflate(R.layout.simple_item_spinner, null);
            TextView txt_name = (TextView) view.findViewById(R.id.txt_name);
            txt_name.setText(list_subServices.get(i).getCatName());
            return view;
        }
    }

    public void servicesAPI(){

        kProgressHUD = KProgressHUD.create(getActivity()).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE).setAnimationSpeed(5).setDimAmount(0.5f);
        kProgressHUD.show();

        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("user_id",user_id);
        params.put("user_unique_id",user_unique_id);
        asyncHttpClient.post(AppConstant.BaseURL + "cat_master.php", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if (kProgressHUD.isShowing()) kProgressHUD.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(new String(responseBody));
                    boolean status = jsonObject.getBoolean("status");
                    if(status){
                        JSONArray jsonArray = jsonObject.getJSONArray("services");
                        Gson gson = new Gson();
                        Type type = new TypeToken<List<ServicesModel>>(){}.getType();
                        list_services = gson.fromJson(jsonArray.toString(),type);
                        spinner_category.setAdapter(new CustomAdapterServices());

                        jsonArray = jsonObject.getJSONArray("city");
                        type = new TypeToken<List<CityModel>>(){}.getType();
                        list_city = gson.fromJson(jsonArray.toString(),type);
                        spinner_city.setAdapter(new CustomAdapterCity());
                        spinner_city.getSelectedItem();
                        list_city.get(spinner_city.getSelectedItemPosition());


                        if (list_services.size()>0){
                            if (list_services.get(0).getCatSub().size()>0){
                                layout_sub.setVisibility(View.VISIBLE);
                            }else{
                                layout_sub.setVisibility(View.GONE);
                            }
                        }
                        spinner_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                list_subServices = list_services.get(position).getCatSub();
                                selectedCategory_id = list_services.get(position).getCatId();

                                if (list_services.get(position).getCatSub().size()>0){
                                    spinner_sub_category.setAdapter(new CustomAdapterSub_Services());
                                    selectedSubCategory_id = list_subServices.get(0).getCatId();
                                    layout_sub.setVisibility(View.VISIBLE);
                                }else{
                                    layout_sub.setVisibility(View.GONE);
                                }

                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {
                            }
                        });
                        spinner_sub_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                selectedSubCategory_id = list_subServices.get(i).getCatId();
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });
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
    @OnClick(R.id.linear_next_step_one)
    public void StepOne(){
        if(edt_business_name.getText().toString().isEmpty()){
            Toasty.error(getActivity(),"Business Name can't be empty",5000).show();
        }
        else if(edt_business_address.getText().toString().isEmpty()){
            Toasty.error(getActivity(),"Business Address can't be empty",5000).show();
        }
        else if(edt_mobile_number.getText().toString().isEmpty()){
            Toasty.error(getActivity(),"Business Mobile number can't be empty",5000).show();
        }else if(edt_mobile_number.getText().toString().length()!=10){
            Toasty.error(getActivity(),"Business Mobile number can't be less than 10 digits",5000).show();
        }
        else{
            linear_form_one.setVisibility(View.GONE);
            linear_form_two.setVisibility(View.VISIBLE);
            txt_one.setVisibility(View.GONE);
            img_one_check.setVisibility(View.VISIBLE);
            view_one.setBackgroundColor(getResources().getColor(R.color.darkblue));
        }
    }
    @OnClick(R.id.linear_previous_step_one)
    public void StepTwo(){
        linear_form_one.setVisibility(View.VISIBLE);
        linear_form_two.setVisibility(View.GONE);
        txt_one.setVisibility(View.VISIBLE);
        img_one_check.setVisibility(View.GONE);
        view_one.setBackgroundColor(getResources().getColor(R.color.lightblue));
    }
    @OnClick(R.id.linear_btn_save)
    public void save(){
        AddBusiness();
    }
    public void AddBusiness(){

        kProgressHUD = KProgressHUD.create(getActivity()).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE).setAnimationSpeed(5).setDimAmount(0.5f);
        kProgressHUD.show();

        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("user_id",user_id);
        params.put("user_unique_id",user_unique_id);
        params.put("service_id",selectedSubCategory_id);
        if(list_services.get(spinner_category.getSelectedItemPosition()).getCatName().equalsIgnoreCase("Repairs")){
            params.put("cat_id",selectedSubCategory_id);
        }else {
            params.put("cat_id",selectedCategory_id);
        }
        params.put("bus_name",edt_business_name.getText().toString().trim().toUpperCase());
        params.put("bus_add",edt_business_address.getText().toString().trim());
        params.put("bus_mobile",edt_mobile_number.getText().toString().trim());
        params.put("bus_loc",list_city.get(spinner_city.getSelectedItemPosition()).city_name);


        asyncHttpClient.post(AppConstant.BaseURL + "add_business.php", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if (kProgressHUD.isShowing()) kProgressHUD.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(new String(responseBody));
                    boolean status = jsonObject.getBoolean("status");
                    if(status){
                        Toasty.success(getActivity(),jsonObject.optString("message"),5000).show();
                        SecurePreferences.savePreferences(getActivity(),AppConstant.USERTYPE,"p");
                        Fragment fragment = getFragmentManager().findFragmentByTag("ManageBusinessFragment");
                        if(fragment!=null){
                            ManageBusinessFragment manageBusinessFragment = (ManageBusinessFragment)fragment;
                            manageBusinessFragment.ViewBusiness();
                        }
                        getFragmentManager().popBackStack();
                    }
                    else {
                        Toasty.error(getActivity(),jsonObject.optString("message"),5000).show();
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
