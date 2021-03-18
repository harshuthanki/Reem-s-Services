package com.reemsservices.fragment.servicefragment;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.reemsservices.R;
import com.reemsservices.helper.AppConstant;
import com.reemsservices.helper.SecurePreferences;
import com.reemsservices.model.GetBusinessModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;
import es.dmoral.toasty.Toasty;

public class BookNowFragment extends Fragment {

    @BindView(R.id.txt_date)
    TextView txt_date;

    @BindView(R.id.txt_time)
    TextView txt_time;

    @BindView(R.id.txt_services)
    TextView txt_services;

    @BindView(R.id.edt_note)
    EditText edt_note;

    @BindView(R.id.recycle_services_info)
    RecyclerView recycle_services_info;

    GetBusinessModel getBusinessModel;
    List<GetBusinessModel> list_service;
    ServiceAdapter serviceAdapter;
    private String date_str;
    Integer sum = 0;
    String time ,busId;
    List<String> list_serviceId;

    public BookNowFragment(List<GetBusinessModel> list_service, String bus_id)
    {
        this.list_service = list_service;
        this.busId = bus_id;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.booknowfragment, container, false);
        ButterKnife.bind(this, view);

        list_serviceId = new ArrayList<>();
        final Calendar cldr = Calendar.getInstance();
        int day = cldr.get(Calendar.DAY_OF_MONTH);
        int month = cldr.get(Calendar.MONTH) + 1;
        int year = cldr.get(Calendar.YEAR);
        date_str = year + "-" + month + "-" + day;
        txt_date.setText(day + " / " + month + " / " + year);

        if(list_service.size()>0)
        {
            for(int i = 0 ; i <list_service.size() ; i ++)
            {
                list_serviceId.add(list_service.get(i).getServiceId());
            }
        }
//        String currentDateTimeString = java.text.DateFormat.getTimeInstance().format(new Date());
        txt_time.setText("Select time");

        recycle_services_info.setLayoutManager(new LinearLayoutManager(getActivity()));
        serviceAdapter = new ServiceAdapter();

        if (list_service.size() > 0) {
            txt_services.setVisibility(View.VISIBLE);
            recycle_services_info.setAdapter(serviceAdapter);
        } else {
            txt_services.setVisibility(View.GONE);
        }
        return view;
    }

    @OnClick(R.id.img_back)
    public void img_back() {
        getFragmentManager().popBackStack();
    }

    @OnClick(R.id.txt_date)
    public void txt_date() {
        final Calendar cldr = Calendar.getInstance();
        int day = cldr.get(Calendar.DAY_OF_MONTH);
        int month = cldr.get(Calendar.MONTH);
        int year = cldr.get(Calendar.YEAR);
        DatePickerDialog picker = new DatePickerDialog(getActivity(), R.style.DialogTheme, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                date_str = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                txt_date.setText(dayOfMonth + " / " + (monthOfYear + 1) + " / " + year);
            }
        }, year, month, day);
        picker.getDatePicker().setMinDate(System.currentTimeMillis());
        picker.show();
    }

    @OnClick(R.id.txt_time)
    public void txt_time() {
        final Calendar c = Calendar.getInstance();
        Calendar currentDate = Calendar.getInstance();

        new TimePickerDialog(getActivity(), R.style.DialogTheme, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                c.set(Calendar.HOUR_OF_DAY, hourOfDay);
                c.set(Calendar.MINUTE, minute);

                time = hourOfDay + ":" + minute;

                if (hourOfDay > 12)
                {
                    txt_time.setText((hourOfDay - 12) + ":" + minute + " PM");
                } else if (hourOfDay == 12) {
                    txt_time.setText("12" + ":" + minute + " PM");
                } else if (hourOfDay < 12) {
                    if (hourOfDay != 0) {
                        txt_time.setText(String.valueOf(hourOfDay) + ":" + minute + " AM");
                    } else {
                        txt_time.setText("12" + ":" + minute + " AM");
                    }
                }
            }
        }, currentDate.get(Calendar.HOUR_OF_DAY), currentDate.get(Calendar.MINUTE), true).show();
    }

    class ServiceAdapter extends RecyclerView.Adapter<ServiceAdapter.ServiceHolder> {

        @NonNull
        @Override
        public ServiceHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.row_servicesinfo_recycle, parent, false);
            ServiceHolder serviceHolder = new ServiceHolder(view);
            return serviceHolder;
        }

        @SuppressLint("ResourceType")
        @Override
        public void onBindViewHolder(@NonNull ServiceHolder holder, int position) {

            if (position == list_service.size()) {

                holder.txt_service_name.setText("Total:");
                holder.txt_service_name.setGravity(Gravity.CENTER);
                holder.txt_totalPrice.setVisibility(View.VISIBLE);
                holder.txt_totalPrice.setText(getResources().getString(R.string.rupee) + sum);
                holder.txt_totalPrice.setGravity(Gravity.CENTER);
                holder.txt_service_price.setVisibility(View.GONE);
                holder.ic_tik.setVisibility(View.GONE);
                holder.view.setVisibility(View.GONE);
            } else {
                GetBusinessModel model = list_service.get(position);
                holder.txt_totalPrice.setVisibility(View.GONE);
                holder.txt_service_name.setText(model.getServiceName());
                holder.txt_service_price.setText(getResources().getString(R.string.rupee) + model.getServicePrice());
                holder.txt_service_price.setGravity(Gravity.END);

                holder.ic_tik.setVisibility(View.GONE);

                if (model.isSelected) {
                    holder.ic_tik.setVisibility(View.GONE);
                    sum = sum + Integer.valueOf(list_service.get(position).getServicePrice());
                } else {
                    holder.ic_tik.setVisibility(View.GONE);
                    holder.linear_service.setVisibility(View.GONE);
                }

                holder.linear_service.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        selectService(position);
                    }
                });
            }
        }

        @Override
        public int getItemCount() {
            Integer size = list_service.size();
            return size + 1;
        }

        class ServiceHolder extends RecyclerView.ViewHolder {

            @BindView(R.id.txt_service_name)
            TextView txt_service_name;


            @BindView(R.id.view)
            View view;

            @BindView(R.id.txt_service_price)
            TextView txt_service_price;

            @BindView(R.id.txt_totalPrice)
            TextView txt_totalPrice;

            @BindView(R.id.ic_tik)
            ImageView ic_tik;

            @BindView(R.id.linear_service)
            LinearLayout linear_service;

            public ServiceHolder(@NonNull View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }
    }

    public void selectService(int position) {
        GetBusinessModel model = list_service.get(position);

        if (model.isSelected) {
            model.isSelected = false;
        } else {
            model.isSelected = true;
        }
        sum = 0;
        serviceAdapter.notifyDataSetChanged();
    }
    @OnClick(R.id.btn_book_now)
    public void book(){
        callApi();
    }

    public void callApi()
    {
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        params.put("user_id", SecurePreferences.getStringPreference(getActivity(), AppConstant.USERID));
        params.put("user_unique_id", SecurePreferences.getStringPreference(getActivity(), AppConstant.USERUNIQUEID));
        params.put("bus_id", busId);
        if (list_service.size()>0)
        {
            params.put("service_id",list_serviceId);
            params.put("total_amt",sum);
        }
        else
        {
//            list_serviceId =null;
            params.put("service_id","");
            params.put("total_amt","0");
        }
        params.put("booking_date_time",date_str +" "+time);

        if(edt_note.getText().toString().isEmpty())
        {
            params.put("book_note","No note");
        }
        else{
            params.put("book_note",edt_note.getText().toString().trim());
        }

        asyncHttpClient.post(AppConstant.BaseURL + "booking.php", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    JSONObject jsonObject = new JSONObject(new String(responseBody));
                    boolean status = jsonObject.getBoolean("status");
                    if(status)
                    {
                        Toasty.success(getActivity(),jsonObject.optString("message"),5000).show();
                    }
                    else {
                        Toasty.error(getActivity(),jsonObject.optString("message"),5000).show();
                    }
                } catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }
}
