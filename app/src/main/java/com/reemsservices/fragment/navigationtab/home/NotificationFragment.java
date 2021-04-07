package com.reemsservices.fragment.navigationtab.home;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
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
import com.reemsservices.fragment.navigationtab.profile.EditProfileFragment;
import com.reemsservices.helper.AppConstant;
import com.reemsservices.helper.SecurePreferences;
import com.reemsservices.model.NotificationModel;
import com.reemsservices.model.ViewBusinessModel;
import com.snov.timeagolibrary.PrettyTimeAgo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.ParseException;
import es.dmoral.toasty.Toasty;


public class NotificationFragment extends Fragment {

    @BindView(R.id.recycle_notification)
    RecyclerView recycle_notification;

    @BindView(R.id.txt_clearNotification)
    TextView txt_clearNotification;



    List<NotificationModel> list_notify;
    NotificationAdapter notificationAdapter;

    public NotificationFragment(List<NotificationModel> list_notify) {
        this.list_notify = list_notify;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.notificationfragment,container,false);
        ButterKnife.bind(this,view);
        notifyUpdateApi();

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recycle_notification.setLayoutManager(layoutManager);
       if(list_notify.size()>0){
           notificationAdapter = new NotificationAdapter();
           recycle_notification.setAdapter(notificationAdapter);
           txt_clearNotification.setVisibility(View.VISIBLE);
       }
       else {
           txt_clearNotification.setVisibility(View.GONE);
       }

        return view;
    }
    @OnClick(R.id.img_back)
    public void back(){
        getFragmentManager().popBackStack();
    }

    class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationHolder> {
        @NonNull
        @Override
        public NotificationHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.row_notification_recycle,parent,false);
            NotificationHolder notificationHolder = new NotificationHolder(view);
            return notificationHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull NotificationHolder holder, int position) {
            NotificationModel model = list_notify.get(position);
            Glide.with(getActivity()).load(AppConstant.ImageURL + model.getCatImg()).into(holder.img_typebooking);
            holder.txt_typeofbooking.setText(model.getNotifyType());
            holder.txt_description.setText(model.getToNotifyDesc());
            holder.txt_time.setText(PrettyTimeAgo.getTimeAgo(dateToMilli(model.getNotifyCreateDate())));
//            holder.txt_time.setText(model.getNotifyCreateDate());
        }

        @Override
        public int getItemCount() {
            return list_notify.size();
        }

        class NotificationHolder extends RecyclerView.ViewHolder {

            @BindView(R.id.img_typebooking)
            ImageView img_typebooking;

            @BindView(R.id.txt_typeofbooking)
            TextView txt_typeofbooking;

            @BindView(R.id.txt_description)
            TextView txt_description;

            @BindView(R.id.txt_time)
            TextView txt_time;

            public NotificationHolder(@NonNull View itemView) {
                super(itemView);
                ButterKnife.bind(this,itemView);
            }
        }
    }
    public static long dateToMilli(String stringDate)
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long timeInMilliseconds = 0;
        try
        {
            Date mDate = sdf.parse(stringDate);
            timeInMilliseconds = mDate.getTime();
        }
        catch (ParseException | java.text.ParseException e)
        {
            e.printStackTrace();
        }
        return timeInMilliseconds;
    }
    @OnClick(R.id.txt_clearNotification)
    public void clearNotification(){
        Dialog dialog = new Dialog(getActivity());
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.custom_dialog_confirmation);
        TextView txt_massage = dialog.findViewById(R.id.txt_massage);
        TextView txt_title = dialog.findViewById(R.id.txt_title);

        txt_title.setText("Delete Notifications");
        txt_massage.setText( "Are you sure you want to delete all notifications ?");
        Button btnCancel = dialog.findViewById(R.id.btnCancel);
        Button btn_confirm = dialog.findViewById(R.id.btn_confirm);

        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notifyDeleteApi();
                dialog.dismiss();
                getFragmentManager().popBackStack();
                Fragment fragment = getFragmentManager().findFragmentByTag("HomeFragment");
                if(fragment!=null){
                    HomeFragment homeFragment = (HomeFragment) fragment;
                    homeFragment.initView();
                }
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
    public void notifyUpdateApi(){

        List<String> temp_list;
        temp_list = new ArrayList<>();

        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("user_id",SecurePreferences.getStringPreference(getActivity(),AppConstant.USERID));
        params.put("user_unique_id",SecurePreferences.getStringPreference(getActivity(),AppConstant.USERUNIQUEID));
        for(int i=0 ; list_notify.size()>i ; i++)
        {
            temp_list.add(list_notify.get(i).getNotifyId());
        }
        params.put("notify_id",temp_list);

        asyncHttpClient.post(AppConstant.BaseURL + "update_notification.php", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    JSONObject jsonObject = new JSONObject(new String(responseBody));
                    boolean status = jsonObject.getBoolean("status");
                    if(status){
                        Toasty.success(getActivity(),jsonObject.optString("message"),5000).show();
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
    public void notifyDeleteApi(){

        List<String> temp_list;
        temp_list = new ArrayList<>();

        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("user_id",SecurePreferences.getStringPreference(getActivity(),AppConstant.USERID));
        params.put("user_unique_id",SecurePreferences.getStringPreference(getActivity(),AppConstant.USERUNIQUEID));
        for(int i=0 ; list_notify.size()>i ; i++)
        {
            temp_list.add(list_notify.get(i).getNotifyId());
        }
        params.put("notify_id",temp_list);

        asyncHttpClient.post(AppConstant.BaseURL + "delete_notification.php", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    JSONObject jsonObject = new JSONObject(new String(responseBody));
                    boolean status = jsonObject.getBoolean("status");
                    if(status){
                        Toasty.success(getActivity(),jsonObject.optString("message"),5000).show();
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
