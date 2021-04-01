package com.reemsservices.fragment.navigationtab.home;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.reemsservices.MainActivity;
import com.reemsservices.R;
import com.reemsservices.fragment.navigationtab.profile.addbusiness.AddBusinessFragment;
import com.reemsservices.fragment.servicefragment.ServiceProviderListFragment;
import com.reemsservices.helper.AppConstant;
import com.reemsservices.helper.SecurePreferences;
import com.reemsservices.model.CityModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SelectCityFragment extends BottomSheetDialogFragment {

    Context frag_context;
    List<CityModel> cityList;

    @BindView(R.id.spinner_city)
    Spinner spinner_city;

    public SelectCityFragment(List<CityModel> cityList) {
        this.cityList = cityList;
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
        View view = inflater.inflate(R.layout.selectcityfragment,container,false);
        ButterKnife.bind(this,view);

        spinner_city.setAdapter(new CustomAdapterCity());

        return view;
    }
    public class CustomAdapterCity extends BaseAdapter {
        LayoutInflater inflate;

        public CustomAdapterCity() {
            inflate = (LayoutInflater.from(getActivity()));
        }

        @Override
        public int getCount() {
            return cityList.size();
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
            txt_name.setText(cityList.get(i).city_name);
            return view;
        }
    }
    @OnClick(R.id.btn_save)
    public void save(){
        spinner_city.getSelectedItem();
       String cityName = cityList.get(spinner_city.getSelectedItemPosition()).city_name;
       SecurePreferences.savePreferences(getActivity(), AppConstant.USERSELECTEDCITY,cityName);
       ((MainActivity)getActivity()).linear_home();
        Fragment fragment = getFragmentManager().findFragmentByTag("ServiceProviderListFragment");
       if(fragment!=null){
           ServiceProviderListFragment serviceProviderListFragment = (ServiceProviderListFragment) fragment;
           serviceProviderListFragment.callServiceInfoApi();
       }
       dismiss();
    }
}
