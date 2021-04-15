package com.reemsservices.fragment.navigationtab.home;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.reemsservices.R;
import com.reemsservices.helper.AppConstant;
import com.reemsservices.helper.SecurePreferences;
import com.reemsservices.model.SearchModel;
import com.reemsservices.model.ViewBookingModel;

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

public class SearchFragment extends Fragment {

    @BindView(R.id.recycle_search)
    RecyclerView recycle_search;

    @BindView(R.id.edt_search)
    EditText edt_search;

    @BindView(R.id.linear_searchContent)
    LinearLayout linear_searchContent;

    List<SearchModel> list_search;
    SearchAdapter searchAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.searchfragment,container,false);
        ButterKnife.bind(this,view);
        initView();
        return view;
    }
    @OnClick(R.id.img_back)
    public void back(){
        getFragmentManager().popBackStack();
    }
    public void initView(){

        list_search = new ArrayList<>();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recycle_search.setLayoutManager(layoutManager);

        edt_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(edt_search.getText().toString().isEmpty()){
                    linear_searchContent.setVisibility(View.VISIBLE);
                    list_search=new ArrayList<>();
                    searchAdapter = new SearchAdapter();
                }
                else {
                    callApi();
                    linear_searchContent.setVisibility(View.GONE);

                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
    class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchHolder> {
        @NonNull
        @Override
        public SearchHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.row_search_recycle,parent,false);
            SearchHolder searchHolder = new SearchHolder(view);
            return searchHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull SearchHolder holder, int position) {
            SearchModel searchModel = list_search.get(position);
            Glide.with(getActivity()).load(AppConstant.ImageURL + searchModel.getCatImg()).into(holder.img_catImg);
            holder.txt_nameOfBusSer.setText(searchModel.getServiceName());
            holder.txt_cat.setText(searchModel.getCatName());
        }

        @Override
        public int getItemCount() {
            return list_search.size();
        }

        class SearchHolder extends RecyclerView.ViewHolder {

            @BindView(R.id.img_catImg)
            ImageView img_catImg;

            @BindView(R.id.txt_nameOfBusSer)
            TextView txt_nameOfBusSer;

            @BindView(R.id.txt_cat)
            TextView txt_cat;

            public SearchHolder(@NonNull View itemView) {
                super(itemView);
                ButterKnife.bind(this,itemView);
            }
        }
    }

    public void callApi(){
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("user_id", SecurePreferences.getStringPreference(getActivity(), AppConstant.USERID));
        params.put("user_unique_id", SecurePreferences.getStringPreference(getActivity(), AppConstant.USERUNIQUEID));
        params.put("search_char", edt_search.getText().toString().trim());

        asyncHttpClient.post(AppConstant.BaseURL + "search.php", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    JSONObject jsonObject = new JSONObject(new String(responseBody));
                    boolean status = jsonObject.getBoolean("status");
                    if(status){
                        JSONArray jsonArray = jsonObject.getJSONArray("service_details");
                        Gson gson = new Gson();
                        Type type = new TypeToken<List<SearchModel>>() {}.getType();
                        list_search = gson.fromJson(jsonArray.toString(), type);
                        searchAdapter = new SearchAdapter();
                        recycle_search.setAdapter(searchAdapter);
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
