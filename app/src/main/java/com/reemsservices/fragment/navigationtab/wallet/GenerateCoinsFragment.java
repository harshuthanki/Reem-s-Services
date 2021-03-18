package com.reemsservices.fragment.navigationtab.wallet;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.gson.JsonObject;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.reemsservices.R;
import com.reemsservices.fragment.navigationtab.profile.addbusiness.AddBusinessFragment;
import com.reemsservices.helper.AppConstant;
import com.reemsservices.helper.SecurePreferences;
import com.reemsservices.model.ViewBusinessModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;
import es.dmoral.toasty.Toasty;

public class GenerateCoinsFragment extends BottomSheetDialogFragment {

    String[] amount;

    Context frag_context;

    @BindView(R.id.spinner_business)
    Spinner spinner_business;

    @BindView(R.id.spinner_coins)
    Spinner spinner_coins;

    ViewBusinessModel viewBusinessModel;
    List<ViewBusinessModel> list_viewBusiness;

    public GenerateCoinsFragment(List<ViewBusinessModel> list_viewBusiness, ViewBusinessModel viewBusinessModel) {
        this.list_viewBusiness = list_viewBusiness;
        this.viewBusinessModel = viewBusinessModel;
    }


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
        View view = inflater.inflate(R.layout.generatecoinfragment,container,false);
        ButterKnife.bind(this,view);

        amount = getResources().getStringArray(R.array.amount);

        spinner_business.setAdapter(new CustomAdapterBusiness());
        spinner_coins.setAdapter(new CustomAdapterAmtCoins());
        return view;
    }
    @OnClick(R.id.btn_generate)
    public void btn_generate(){
        generateCoinApi();

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



    public class CustomAdapterAmtCoins extends BaseAdapter {
        LayoutInflater inflate;

        public CustomAdapterAmtCoins() {
            inflate = (LayoutInflater.from(getActivity()));
        }

        @Override
        public int getCount() {
            return amount.length;
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
            txt_name.setText(amount[i]);
            return view;
        }
    }
    public void generateCoinApi(){
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("user_id", SecurePreferences.getStringPreference(getActivity(), AppConstant.USERID));
        params.put("user_unique_id",SecurePreferences.getStringPreference(getActivity(),AppConstant.USERUNIQUEID));
        params.put("bus_id",list_viewBusiness.get(spinner_business.getSelectedItemPosition()).getBusId());
//        params.put("bus_name",list_viewBusiness.get(spinner_business.getSelectedItemPosition()).getBusName());
        params.put("coin",amount[spinner_coins.getSelectedItemPosition()]);

        asyncHttpClient.post(AppConstant.BaseURL + "generate_coins.php", params, new AsyncHttpResponseHandler() {
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
}
