package com.reemsservices.fragment.navigationtab.booking;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.reemsservices.MainActivity;
import com.reemsservices.R;
import com.reemsservices.helper.AppConstant;
import com.reemsservices.helper.SecurePreferences;
import com.reemsservices.model.ViewBookingModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;
import es.dmoral.toasty.Toasty;

public class BookingAcceptRejectFragment  extends BottomSheetDialogFragment {

    Context frag_context;

    @BindView(R.id.img_typebooking)
    ImageView img_typebooking;

    @BindView(R.id.txt_typeofbooking)
    TextView txt_typeofbooking;

    @BindView(R.id.txt_dateofbooking)
    TextView txt_dateofbooking;

    @BindView(R.id.txt_timeofbooking)
    TextView txt_timeofbooking;

    @BindView(R.id.txt_services)
    TextView txt_services;

    @BindView(R.id.txt_headingtotalAmt)
    TextView txt_headingtotalAmt;

    @BindView(R.id.txt_totalAmt)
    TextView txt_totalAmt;

    @BindView(R.id.recycle_services)
    RecyclerView recycle_services;

    List<ViewBookingModel> list_booking;
    ViewBookingModel model;

    public BookingAcceptRejectFragment(List<ViewBookingModel> list_booking, ViewBookingModel model) {
        this.list_booking = list_booking;
        this.model = model;
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
        View view = inflater.inflate(R.layout.bookingacceptrejectfragment,container,false);
        ButterKnife.bind(this,view);

        Glide.with(getActivity()).load(AppConstant.ImageURL + model.getCatImg()).into(img_typebooking);
        txt_typeofbooking.setText(model.getBusName());
        txt_dateofbooking.setText(AppConstant.dateFormate(model.getBookingDateTime()));
        txt_timeofbooking.setText(AppConstant.timeFormate(model.getBookingDateTime()));

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recycle_services.setLayoutManager(layoutManager);

        if(!model.getServiceId().equalsIgnoreCase("0")){
            ServiceAdapter serviceAdapter = new ServiceAdapter();
            recycle_services.setAdapter(serviceAdapter);
            txt_totalAmt.setText(model.getTotalAmt());
        }
        else {
            txt_totalAmt.setVisibility(View.GONE);
            txt_headingtotalAmt.setVisibility(View.GONE);
            txt_services.setVisibility(View.GONE);
            recycle_services.setVisibility(View.GONE);
        }

        return view;
    }
    class ServiceAdapter extends RecyclerView.Adapter<ServiceAdapter.ServiceHolder> {
        @NonNull
        @Override
        public ServiceHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.row_servicename_recycle,parent,false);
            ServiceHolder serviceHolder = new ServiceHolder(view);
            return serviceHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull ServiceHolder holder, int position) {
            ViewBookingModel model = list_booking.get(position);
            holder.txt_serviceName.setText(model.getServiceName());
        }

        @Override
        public int getItemCount() {
            return list_booking.size();
        }

        class ServiceHolder extends RecyclerView.ViewHolder {

            @BindView(R.id.txt_serviceName)
            TextView txt_serviceName;

            public ServiceHolder(@NonNull View itemView) {
                super(itemView);
                ButterKnife.bind(this,itemView);
            }
        }
    }
    @OnClick(R.id.btn_accept)
    public void accepted(){
        callAPI("1");
    }
    @OnClick(R.id.btn_reject)
    public void reject(){
        callAPI("2");
    }
    public void callAPI(String status){

        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("user_id", SecurePreferences.getStringPreference(getActivity(),AppConstant.USERID));
        params.put("user_unique_id", SecurePreferences.getStringPreference(getActivity(),AppConstant.USERUNIQUEID));
        params.put("booking_id", model.getBookingId());
        params.put("status", status);

        asyncHttpClient.post(AppConstant.BaseURL + "booking_ar.php", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    JSONObject jsonObject = new JSONObject(new String(responseBody));
                    boolean status = jsonObject.getBoolean("status");
                    if(status){
                        Toasty.success(getActivity(),jsonObject.optString("message"),5000).show();
                        dismiss();
                        ((MainActivity)getActivity()).linear_booking();
                    }else {
                        Toasty.error(getActivity(),jsonObject.optString("message"),5000).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toasty.error(getActivity(),"failure",5000).show();
            }
        });

    }
}
