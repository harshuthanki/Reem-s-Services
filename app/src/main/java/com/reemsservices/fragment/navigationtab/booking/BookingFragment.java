package com.reemsservices.fragment.navigationtab.booking;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.reemsservices.fragment.navigationtab.wallet.GenerateCoinsFragment;
import com.reemsservices.fragment.navigationtab.wallet.WalletFragment;
import com.reemsservices.helper.AppConstant;
import com.reemsservices.helper.SecurePreferences;
import com.reemsservices.model.AddServiceModel;
import com.reemsservices.model.ViewBookingModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;
import es.dmoral.toasty.Toasty;

public class BookingFragment extends Fragment {

    @BindView(R.id.recycle_booking)
    RecyclerView recycle_booking;

    @BindView(R.id.recycle_booking_p)
    RecyclerView recycle_booking_p;

    @BindView(R.id.tablayout)
    TabLayout tablayout; 

    String partner = "p";
    List<ViewBookingModel> list_booking;
    BookingAdapter bookingAdapter;
    BookingPartnerAdapter bookingPartnerAdapter;


    private KProgressHUD kProgressHUD;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bookingfragment,container,false);
        ButterKnife.bind(this,view);
        list_booking = new ArrayList<>();
        callApi();
        initView();

        return view;
    }
    public void initView(){
        tablayout.addTab(tablayout.newTab().setText(getResources().getString(R.string.yourBooking)));
        tablayout.addTab(tablayout.newTab().setText(getResources().getString(R.string.businessBooking)));

        if(SecurePreferences.getStringPreference(getActivity(), AppConstant.USERTYPE).equalsIgnoreCase(partner)){
            tablayout.setVisibility(View.VISIBLE);
        }
        else {
            tablayout.setVisibility(View.GONE);
        }

        tablayout.setTabTextColors(Color.parseColor("#727272"), Color.parseColor("#112d4e"));

        tablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(tab.getPosition()==0){
                    recycle_booking_p.setVisibility(View.GONE);
                    bookingAdapter = new BookingAdapter();
                    recycle_booking.setVisibility(View.VISIBLE);
                    recycle_booking.setAdapter(bookingAdapter);
                    bookingAdapter.notifyDataSetChanged();

                }
                else if(tab.getPosition()==1){
                    recycle_booking.setVisibility(View.GONE);
                    bookingPartnerAdapter = new BookingPartnerAdapter();
                    recycle_booking_p.setVisibility(View.VISIBLE);
                    recycle_booking_p.setAdapter(bookingPartnerAdapter);
                    bookingPartnerAdapter.notifyDataSetChanged();

                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false);
        recycle_booking.setLayoutManager(layoutManager);

        RecyclerView.LayoutManager layoutManager1 = new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false);
        recycle_booking_p.setLayoutManager(layoutManager1);

    }
    class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.BookingHolder> {
        @NonNull
        @Override
        public BookingHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.row_booking_recycle,parent,false);
            BookingHolder bookingHolder = new BookingHolder(view);
            return bookingHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull BookingHolder holder, int position) {
            ViewBookingModel model = list_booking.get(position);
            Glide.with(getActivity()).load(AppConstant.ImageURL + model.getCatImg()).into(holder.img_typebooking);
            holder.txt_typeofbooking.setText(model.getBusName());
            holder.txt_dateofbooking.setText(AppConstant.dateFormate(model.getBookingDateTime()));
            holder.txt_timeofbooking.setText(AppConstant.timeFormate(model.getBookingDateTime()));

            if(model.getStatus().equalsIgnoreCase("0")){
                holder.linear_btn_cancle.setVisibility(View.VISIBLE);
                holder.txt_status.setText("Pending");
            }else if(model.getStatus().equalsIgnoreCase("1")){
                holder.linear_btn_cancle.setVisibility(View.GONE);
                holder.txt_status.setText("Booking Accepted");
                holder.txt_status.setTextColor(getResources().getColor(R.color.green));
            }else{
                holder.linear_btn_cancle.setVisibility(View.GONE);
                holder.txt_status.setText("Booking Rejected");
                holder.txt_status.setTextColor(getResources().getColor(R.color.red));
            }

            holder.linear_btn_cancle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Dialog dialog = new Dialog(getActivity());
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.setCancelable(false);
                    dialog.setContentView(R.layout.custom_dialog_confirmation);
                    TextView txt_massage = dialog.findViewById(R.id.txt_massage);

                    txt_massage.setText(getResources().getString(R.string.cancelBooking)+" " + model.getBusName() + "?");
                    Button btnCancel = dialog.findViewById(R.id.btnCancel);
                    Button btn_confirm = dialog.findViewById(R.id.btn_confirm);

                    btn_confirm.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            cancelBooking(model.getBookingId());
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
            return list_booking.size();
        }

        class BookingHolder extends RecyclerView.ViewHolder {

            @BindView(R.id.img_typebooking)
            ImageView img_typebooking;

            @BindView(R.id.txt_typeofbooking)
            TextView txt_typeofbooking;

            @BindView(R.id.txt_dateofbooking)
            TextView txt_dateofbooking;

            @BindView(R.id.txt_timeofbooking)
            TextView txt_timeofbooking;

            @BindView(R.id.txt_status)
            TextView txt_status;

            @BindView(R.id.linear_btn_cancle)
            LinearLayout linear_btn_cancle;

            public BookingHolder(@NonNull View itemView) {
                super(itemView);
                ButterKnife.bind(this,itemView);
            }
        }
    }

    class BookingPartnerAdapter extends RecyclerView.Adapter<BookingPartnerAdapter.BookingPartnerHolder> {
        @NonNull
        @Override
        public BookingPartnerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.row_booking_p_recycle,parent,false);
            BookingPartnerHolder bookingPartnerHolder = new BookingPartnerHolder(view);
            return bookingPartnerHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull BookingPartnerHolder holder, int position) {
            ViewBookingModel model = list_booking.get(position);
            Glide.with(getActivity()).load(AppConstant.ImageURL + model.getCatImg()).into(holder.img_typebooking);
            holder.txt_typeofbooking.setText(model.getBusName());
            holder.txt_dateofbooking.setText(AppConstant.dateFormate(model.getBookingDateTime()));
            holder.txt_timeofbooking.setText(AppConstant.timeFormate(model.getBookingDateTime()));

            if(model.getStatus().equalsIgnoreCase("0"))
            {
                holder.card_service.setVisibility(View.VISIBLE);
            }
            else
            {
                holder.card_service.setVisibility(View.GONE);
            }

            holder.card_service.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    List<ViewBookingModel> tempList = new ArrayList<>();
                    for(int i = 0; i<list_booking.size() ; i++){
                        if(model.getBookingId()== list_booking.get(i).getBookingId()){
                            tempList.add(list_booking.get(i));
                        }
                    }

                    BookingAcceptRejectFragment bookingAcceptRejectFragment = new BookingAcceptRejectFragment(tempList,model);
                    bookingAcceptRejectFragment.show(getFragmentManager(), "BookingAcceptRejectFragment");
                }
            });

        }

        @Override
        public int getItemCount() {
            return list_booking.size();
        }

        class BookingPartnerHolder extends RecyclerView.ViewHolder {

            @BindView(R.id.img_typebooking)
            ImageView img_typebooking;

            @BindView(R.id.txt_typeofbooking)
            TextView txt_typeofbooking;

            @BindView(R.id.txt_dateofbooking)
            TextView txt_dateofbooking;

            @BindView(R.id.txt_timeofbooking)
            TextView txt_timeofbooking;

            @BindView(R.id.card_service)
            CardView card_service;

            public BookingPartnerHolder(@NonNull View itemView) {
                super(itemView);
                ButterKnife.bind(this,itemView);
            }
        }
    }


    public void callApi(){
        kProgressHUD = KProgressHUD.create(getActivity()).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE).setAnimationSpeed(5).setDimAmount(0.5f);
        kProgressHUD.show();

        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("user_id",SecurePreferences.getStringPreference(getContext(),AppConstant.USERID));
        params.put("user_unique_id",SecurePreferences.getStringPreference(getContext(),AppConstant.USERUNIQUEID));

        asyncHttpClient.post(AppConstant.BaseURL + "view_booking.php", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if (kProgressHUD.isShowing()) kProgressHUD.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(new String(responseBody));
                    boolean status = jsonObject.getBoolean("status");
                    if(status){
                        JSONArray jsonArray = jsonObject.getJSONArray("booking_c");
                        Gson gson = new Gson();
                        Type type = new TypeToken<List<ViewBookingModel>>() {}.getType();
                        list_booking = gson.fromJson(jsonArray.toString(), type);
                        bookingAdapter = new BookingAdapter();
                        recycle_booking.setAdapter(bookingAdapter);
                        bookingAdapter.notifyDataSetChanged();
                        recycle_booking_p.setVisibility(View.GONE);
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
    public void cancelBooking(String bookingId){

        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("user_id",SecurePreferences.getStringPreference(getContext(),AppConstant.USERID));
        params.put("user_unique_id",SecurePreferences.getStringPreference(getContext(),AppConstant.USERUNIQUEID));
        params.put("booking_id",bookingId);

        asyncHttpClient.post(AppConstant.BaseURL + "delete_booking.php", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    JSONObject jsonObject = new JSONObject(new String(responseBody));
                    boolean status = jsonObject.getBoolean("status");
                    if(status){
                        Toasty.success(getActivity(),jsonObject.optString("message"),5000).show();
                        initView();
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
