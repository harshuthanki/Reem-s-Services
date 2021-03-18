package com.reemsservices.fragment.servicefragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.reemsservices.LoginActivity;
import com.reemsservices.R;
import com.reemsservices.helper.AppConstant;
import com.reemsservices.helper.SecurePreferences;
import com.reemsservices.model.GetBusinessModel;
import com.smarteist.autoimageslider.SliderView;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;

public class ServiceFragment extends Fragment {

    @BindView(R.id.imageSlider)
    SliderView imageSlider;

    @BindView(R.id.recycle_services_info)
    RecyclerView recycle_services_info;

    @BindView(R.id.txt_servicename)
    TextView txt_servicename;

    @BindView(R.id.recycle_review)
    RecyclerView recycle_review;

    @BindView(R.id.img_serviceperson)
    ImageView img_serviceperson;

    @BindView(R.id.txt_personname)
    TextView txt_personname;

    @BindView(R.id.txt_servicerating)
    TextView txt_servicerating;

    @BindView(R.id.linear_service)
    LinearLayout linear_service;

    @BindView(R.id.recycle_hours)
    RecyclerView recycle_hours;

    @BindView(R.id.txt_review)
    TextView txt_review;

    GetBusinessModel getBusinessModel;
    List<GetBusinessModel> list_business;
    List<GetBusinessModel> list_service;
    List<GetBusinessModel> list_review;
    List<String> days;
    List<String> times;
    RecycleHoursAdapter recycleHoursAdapter;
    ServiceAdapter serviceAdapter;
    Integer sum = 0;
    Boolean isSelectService = false;
    String service_id="",bus_id="";
    public ServiceFragment(GetBusinessModel getBusinessModel) {
        this.getBusinessModel = getBusinessModel;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.servicedetailfragment, container, false);
        ButterKnife.bind(this, view);

        list_service = getBusinessModel.getList_service();
        list_review = getBusinessModel.getList_review();

        list_business = new ArrayList<>();
        list_business.add(getBusinessModel);
        txt_servicename.setText(getBusinessModel.getBusName());
        Glide.with(getActivity()).load(AppConstant.ImageURL + getBusinessModel.getUserProfile()).into(img_serviceperson);
        txt_personname.setText(getBusinessModel.getBusName());
        txt_servicerating.setText(getBusinessModel.getTotalRating());

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recycle_hours.setLayoutManager(layoutManager);

        String timeArray[] = getBusinessModel.getSchedule().split(",");
        times = new ArrayList<>(Arrays.asList(timeArray));


        days = new ArrayList<>();
        days.add("Monday:");
        days.add("Tuesday:");
        days.add("Wednesday:");
        days.add("Thursday:");
        days.add("Friday:");
        days.add("Saturday");
        days.add("Sunday");

        recycleHoursAdapter = new RecycleHoursAdapter();
        recycle_hours.setAdapter(recycleHoursAdapter);


        recycle_services_info.setLayoutManager(new LinearLayoutManager(getActivity()));
        serviceAdapter = new ServiceAdapter();

        if (list_service.size() > 0) {
            linear_service.setVisibility(View.VISIBLE);
            recycle_services_info.setAdapter(serviceAdapter);
        } else {
            linear_service.setVisibility(View.GONE);
        }

        imageSlider.setSliderAdapter(new SliderAdapterExample());

        recycle_review.setLayoutManager(new LinearLayoutManager(getActivity()));

        if(list_review.size()>0){
            txt_review.setVisibility(View.VISIBLE);
            ReviewAdapter reviewAdapter = new ReviewAdapter();
            recycle_review.setAdapter(reviewAdapter);
        }
        else {
            txt_review.setVisibility(View.GONE);
        }

        return view;
    }

    public class SliderAdapterExample extends SliderViewAdapter<SliderAdapterExample.SliderAdapterVH> {

        @Override
        public SliderAdapterVH onCreateViewHolder(ViewGroup parent) {
            View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_slider_layout_item, null);
            return new SliderAdapterVH(inflate);
        }

        @Override
        public void onBindViewHolder(SliderAdapterVH viewHolder, int position) {
            viewHolder.linear_edit_businessImg.setVisibility(View.GONE);
            GetBusinessModel getBusinessModel = list_business.get(position);
            Glide.with(viewHolder.itemView).load(AppConstant.ImageURL + getBusinessModel.getBusImgOne()).into(viewHolder.imageViewBackground);
            Glide.with(viewHolder.itemView).load(AppConstant.ImageURL + getBusinessModel.getBusImgTwo()).into(viewHolder.imageViewBackground);
            Glide.with(viewHolder.itemView).load(AppConstant.ImageURL + getBusinessModel.getBusImgThree()).into(viewHolder.imageViewBackground);
        }

        @Override
        public int getCount() {
//slider view count could be dynamic size
            return list_business.size();
        }

        class SliderAdapterVH extends SliderViewAdapter.ViewHolder {

            View itemView;
            ImageView imageViewBackground;
            LinearLayout linear_edit_businessImg;

            public SliderAdapterVH(View itemView) {
                super(itemView);
                imageViewBackground = itemView.findViewById(R.id.iv_auto_image_slider);
                linear_edit_businessImg = itemView.findViewById(R.id.linear_edit_businessImg);
                this.itemView = itemView;
            }
        }
    }

    @OnClick(R.id.img_back)
    public void img_back() {
        getFragmentManager().popBackStack();
    }

    class RecycleHoursAdapter extends RecyclerView.Adapter<RecycleHoursAdapter.RecycleHoursHolder> {
        @NonNull
        @Override
        public RecycleHoursAdapter.RecycleHoursHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.row_viewhours_recycle, parent, false);
            RecycleHoursHolder recycleHoursHolder = new RecycleHoursHolder(view);
            return recycleHoursHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull RecycleHoursAdapter.RecycleHoursHolder holder, final int position) {
            holder.txt_days.setText(days.get(position));
            holder.txt_days_hours.setText(times.get(position));
        }

        @Override
        public int getItemCount() {
            return times.size();
        }

        class RecycleHoursHolder extends RecyclerView.ViewHolder {

            @BindView(R.id.txt_days)
            TextView txt_days;

            @BindView(R.id.txt_days_hours)
            TextView txt_days_hours;

            public RecycleHoursHolder(@NonNull View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }
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

                if (model.isSelected) {
                    holder.ic_tik.setImageResource(R.drawable.ic_tik);
                    sum = sum + Integer.valueOf(list_service.get(position).getServicePrice());
                    isSelectService = true;
                } else {
                    holder.ic_tik.setImageResource(R.drawable.ic_checkbox);
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

    class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewHolder> {
        @NonNull
        @Override
        public ReviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.row_review_recycle, parent, false);
            ReviewHolder reviewHolder = new ReviewHolder(view);
            return reviewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull ReviewHolder holder, int position) {
            GetBusinessModel getBusinessModel = list_review.get(position);
            Glide.with(holder.itemView).load(AppConstant.ImageURL + getBusinessModel.getUserProfile()).into(holder.img_reviewperson);
            holder.img_reviewperson.setImageResource(R.drawable.img_person);
            holder.txt_reviewername.setText(getBusinessModel.getUserName());
            holder.txt_reviewrating.setText(getBusinessModel.getRating());
            holder.txt_review.setText(" \" " + getBusinessModel.getBusReview() + " \" ");
        }

        @Override
        public int getItemCount() {
            return list_review.size();
        }

        class ReviewHolder extends RecyclerView.ViewHolder {

            @BindView(R.id.img_reviewperson)
            ImageView img_reviewperson;

            @BindView(R.id.txt_reviewername)
            TextView txt_reviewername;

            @BindView(R.id.img_rating)
            ImageView img_rating;

            @BindView(R.id.txt_reviewrating)
            TextView txt_reviewrating;

            @BindView(R.id.txt_review)
            TextView txt_review;

            public ReviewHolder(@NonNull View itemView) {
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
        isSelectService = false;
        service_id=model.getServiceId();
        bus_id=model.getBusId();
        serviceAdapter.notifyDataSetChanged();
    }

    @OnClick(R.id.linear_call_now)
    public void call() {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + getBusinessModel.getBusMobile()));
        startActivity(intent);
    }

    @OnClick(R.id.linear_book_now)
    public void bookNow() {
        if (isSelectService)
        {
            AppConstant.addFragment(getFragmentManager(), new BookNowFragment(list_service,getBusinessModel.getBusId()), "BookNowFragment");
        }
        else if (!SecurePreferences.getBooleanPreference(getActivity(), AppConstant.IS_LOGIN))
        {
            Intent i = new Intent(getActivity(), LoginActivity.class);
            startActivity(i);
        }
        else
        {
            if (list_service.size() > 0) {
                Toasty.error(getActivity(), "Please select any service to book!", 5000).show();
            } else if (!SecurePreferences.getBooleanPreference(getActivity(), AppConstant.IS_LOGIN)) {
                Intent i = new Intent(getActivity(), LoginActivity.class);
                startActivity(i);
            } else {
                AppConstant.addFragment(getFragmentManager(), new BookNowFragment(list_service,getBusinessModel.getBusId()), "BookNowFragment");
            }
        }

    }
}
