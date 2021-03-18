package com.reemsservices.fragment.navigationtab.wallet;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.reemsservices.R;
import com.reemsservices.helper.AppConstant;
import com.reemsservices.helper.SecurePreferences;
import com.reemsservices.model.ViewBusinessModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;
import es.dmoral.toasty.Toasty;

public class TransactionFragment extends BottomSheetDialogFragment {


    Context frag_context;

    @BindView(R.id.spinner_business)
    Spinner spinner_business;

    @BindView(R.id.edt_refCode)
    EditText edt_refCode;

    @BindView(R.id.edt_amtCoin)
    EditText edt_amtCoin;

    List<ViewBusinessModel> list_viewBusiness;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        frag_context = context;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((View) getView().getParent()).setBackgroundColor(Color.TRANSPARENT);
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.transactionfragment,container,false);
        ButterKnife.bind(this,view);

        edt_refCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(edt_refCode.length() ==  6 ){
                   transactionSpinnerApi();
                }
            }
        });

        return view;
    }
    @OnClick(R.id.btn_confirm)
    public void btn_confirm(){
        if(edt_amtCoin.getText().toString().trim().isEmpty()){
            Toasty.error(getActivity(),getResources().getString(R.string.entercoin),5000).show();
        }else if(edt_amtCoin.getText().toString().trim().equalsIgnoreCase("0")){
            Toasty.error(getActivity(),getResources().getString(R.string.coinscantbezero),5000).show();
        }else{
            transactionCompleteApi();
        }
    }

    public class CustomAdapterBusiness extends BaseAdapter {
        LayoutInflater inflate;

        public CustomAdapterBusiness() {
            inflate = (LayoutInflater.from(getActivity()));
        }

        @Override
        public int getCount() {
            return list_viewBusiness.size();
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
            txt_name.setText(list_viewBusiness.get(i).getBusName());
            return view;
        }
    }

    public void transactionSpinnerApi(){

        String defaultCoin = "0";
        String defaultBusId = "0";
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("user_id", SecurePreferences.getStringPreference(getActivity(), AppConstant.USERID));
        params.put("user_unique_id", SecurePreferences.getStringPreference(getActivity(), AppConstant.USERUNIQUEID));
        params.put("user_ref_code",edt_refCode.getText().toString().toUpperCase().trim());
        params.put("coin",defaultCoin);
        params.put("bus_id",defaultBusId);
        params.put("action","get_business_data");

        asyncHttpClient.post(AppConstant.BaseURL + "deduct_coin.php", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    JSONObject jsonObject = new JSONObject(new String(responseBody));
                    boolean status = jsonObject.getBoolean("status");
                    if(status){
                        JSONArray jsonArray = jsonObject.getJSONArray("business");
                        Gson gson = new Gson();
                        Type type = new TypeToken<List<ViewBusinessModel>>(){}.getType();
                        list_viewBusiness = gson.fromJson(jsonArray.toString(), type);
                        spinner_business.setAdapter(new CustomAdapterBusiness());
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

            }
        });
    }
    public void transactionCompleteApi(){
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("user_id", SecurePreferences.getStringPreference(getActivity(), AppConstant.USERID));
        params.put("user_unique_id", SecurePreferences.getStringPreference(getActivity(), AppConstant.USERUNIQUEID));
        params.put("user_ref_code",edt_refCode.getText().toString().toUpperCase().trim());
        params.put("coin",edt_amtCoin.getText().toString().trim());
        params.put("bus_id",list_viewBusiness.get(spinner_business.getSelectedItemPosition()).getBusId());
        params.put("action","deduct_coin");

        asyncHttpClient.post(AppConstant.BaseURL + "deduct_coin.php", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    JSONObject jsonObject = new JSONObject(new String(responseBody));
                    boolean status = jsonObject.getBoolean("status");
                    if(status){
                        Toasty.success(getActivity(),jsonObject.optString("message"),5000).show();
                        Fragment fragment = getFragmentManager().findFragmentByTag("WalletFragment");
                        if(fragment!=null){
                            WalletFragment walletFragment = (WalletFragment)fragment;
                            walletFragment.initView();
                        }
                        dismiss();
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
