package com.reemsservices.fragment.navigationtab.profile.addbusiness;

import android.Manifest;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nguyenhoanglam.imagepicker.model.Config;
import com.nguyenhoanglam.imagepicker.model.Image;
import com.nguyenhoanglam.imagepicker.ui.imagepicker.ImagePicker;
import com.reemsservices.R;
import com.reemsservices.fragment.navigationtab.profile.ManageBusinessFragment;
import com.reemsservices.helper.AppConstant;
import com.reemsservices.helper.SecurePreferences;
import com.reemsservices.model.AddServiceModel;
import com.reemsservices.model.CityModel;
import com.reemsservices.model.ViewBusinessModel;
import com.smarteist.autoimageslider.SliderView;
import com.smarteist.autoimageslider.SliderViewAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;
import es.dmoral.toasty.Toasty;
import id.zelory.compressor.Compressor;

import static android.app.Activity.RESULT_OK;

public class EditBusinessFragment extends Fragment {

    private static final int PICK_IMAGE = 100;
    @BindView(R.id.recycle_hours)
    RecyclerView recycle_hours;

    @BindView(R.id.imageSlider)
    SliderView imageSlider;

    @BindView(R.id.txt_business_name)
    TextView txt_business_name;

    @BindView(R.id.edt_business_address)
    EditText edt_business_address;

    @BindView(R.id.edt_business_name)
    EditText edt_business_name;

    @BindView(R.id.edt_mobile_number)
    EditText edt_mobile_number;

    @BindView(R.id.recycle_edit_services)
    RecyclerView recycle_edit_services;

    @BindView(R.id.spinner_city)
    Spinner spinner_city;

    @BindView(R.id.img_pic)
    ImageView img_pic;

    @BindView(R.id.linear_serviceHeading)
    LinearLayout linear_serviceHeading;

    ViewBusinessModel viewBusinessModel;
    List<String> slider_list;

    List<String> days;
    List<String> times;
    List<ViewBusinessModel> serviceList , tempList;
    List<ViewBusinessModel> list_viewBusiness;
    File file, file1, file2;
    String code;
    RecycleHoursAdapter recycleHoursAdapter;
    serviceAdapter serviceAdapter;
    SliderAdapterExample sliderAdapterExample;
    List<CityModel> list_city;

    private KProgressHUD kProgressHUD;

    public EditBusinessFragment(ViewBusinessModel viewBusinessModel, List<ViewBusinessModel> list_viewBusiness, List<CityModel> list_city) {
        this.viewBusinessModel = viewBusinessModel;
        serviceList = new ArrayList<>();
        serviceList.addAll(this.viewBusinessModel.getServices());
        this.list_city = list_city;
        this.list_viewBusiness = list_viewBusiness;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.editbusinessfragment, container, false);
        ButterKnife.bind(this, view);
        txt_business_name.setText(viewBusinessModel.getBusName());
        edt_business_name.setText(viewBusinessModel.getBusName());
        edt_business_address.setText(viewBusinessModel.getBusAdd());
        edt_mobile_number.setText(viewBusinessModel.getBusMobile());

        Glide.with(getActivity()).load(AppConstant.ImageURL + SecurePreferences.getStringPreference(getActivity(), AppConstant.USERPROFILE)).into(img_pic);

        slider_list = new ArrayList<>();

        if (serviceList == null) {
            serviceList = new ArrayList<>();
        }

        tempList = new ArrayList<>();

        slider_list.add(viewBusinessModel.getBusImgOne());
        slider_list.add(viewBusinessModel.getBusImgTwo());
        slider_list.add(viewBusinessModel.getBusImgThree());

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recycle_hours.setLayoutManager(layoutManager);

        imageSlider.setSliderAdapter(new SliderAdapterExample());

        spinner_city.setAdapter(new CustomAdapterCity());
        for (int i = 0; i < list_city.size(); i++) {
            if (list_city.get(i).city_name.equalsIgnoreCase(viewBusinessModel.getBusLoc())) {
                spinner_city.setSelection(i, true);
            }
        }


        spinner_city.getSelectedItem();
        list_city.get(spinner_city.getSelectedItemPosition());

        String timeArray[] = viewBusinessModel.getSchedule().split(",");
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

        RecyclerView.LayoutManager layoutManager1 = new LinearLayoutManager(getActivity());
        recycle_edit_services.setLayoutManager(layoutManager1);

        linear_serviceHeading.setVisibility(View.VISIBLE);
        serviceAdapter = new serviceAdapter();
        recycle_edit_services.setAdapter(serviceAdapter);

        sliderAdapterExample = new SliderAdapterExample();
        imageSlider.setSliderAdapter(sliderAdapterExample);

        recycle_edit_services.setNestedScrollingEnabled(false);

        return view;
    }

    @OnClick(R.id.img_back)
    public void img_back() {
        getFragmentManager().popBackStack();
    }

    public class SliderAdapterExample extends SliderViewAdapter<SliderAdapterExample.SliderAdapterVH> {

        @Override
        public SliderAdapterExample.SliderAdapterVH onCreateViewHolder(ViewGroup parent) {
            View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_slider_layout_item, null);
            return new SliderAdapterExample.SliderAdapterVH(inflate);
        }

        @Override
        public void onBindViewHolder(SliderAdapterExample.SliderAdapterVH viewHolder, int position) {
            if (slider_list.get(position).contains("/data/user/")) {
                File imgFile = new File(slider_list.get(position));
                if (imgFile.exists()) {
                    viewHolder.imageViewBackground.setImageURI(Uri.fromFile(imgFile));
                } else {
                    Glide.with(viewHolder.itemView).load(slider_list.get(position)).into(viewHolder.imageViewBackground);
                }
            } else {
                Glide.with(viewHolder.itemView).load(AppConstant.ImageURL + slider_list.get(position)).into(viewHolder.imageViewBackground);
            }

            viewHolder.linear_edit_businessImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (position == 0) {
                        code = "100";
                        selectPic();
                    } else if (position == 1) {
                        if (file == null) {
                            Toasty.error(getActivity(), "Please Select first image", 5000).show();
                        } else {
                            code = "101";
                            selectPic();
                        }
                    } else {
                        if (file == null) {
                            Toasty.error(getActivity(), "Please Select first image", 5000).show();
                        } else if (file1 == null) {
                            Toasty.error(getActivity(), "Please Select second image", 5000).show();
                        } else {
                            code = "102";
                            selectPic();
                        }
                    }
                }
            });
        }

        @Override
        public int getCount() {
//slider view count could be dynamic size
            return slider_list.size();
        }

        class SliderAdapterVH extends SliderViewAdapter.ViewHolder {

            View itemView;
            ImageView imageViewBackground;

            @BindView(R.id.linear_edit_businessImg)
            LinearLayout linear_edit_businessImg;

            public SliderAdapterVH(View itemView) {

                super(itemView);
                imageViewBackground = itemView.findViewById(R.id.iv_auto_image_slider);
                this.itemView = itemView;
                ButterKnife.bind(this, itemView);
            }
        }
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


    class RecycleHoursAdapter extends RecyclerView.Adapter<RecycleHoursAdapter.RecycleHoursHolder> {
        @NonNull
        @Override
        public RecycleHoursHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.row_hours_recycle, parent, false);
            RecycleHoursHolder recycleHoursHolder = new RecycleHoursHolder(view);
            return recycleHoursHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull RecycleHoursHolder holder, final int position) {

            holder.txt_days.setText(days.get(position));
            holder.txt_days_hours.setText(times.get(position));

            holder.linear_edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Dialog dialog = new Dialog(getActivity());
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    if (dialog.getWindow() != null)
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.setCancelable(false);
                    dialog.setContentView(R.layout.custom_dialog);

                    TextView txt_message = dialog.findViewById(R.id.txt_message);
                    Button btnCancel = dialog.findViewById(R.id.btnCancel);
                    Button btnSave = dialog.findViewById(R.id.btn_save);
                    TextView txt_openingTime = dialog.findViewById(R.id.txt_openingTime);
                    TextView txt_closingTime = dialog.findViewById(R.id.txt_closingTime);
                    EditText edt_openTime = dialog.findViewById(R.id.edt_openTime);
                    EditText edt_closeTime = dialog.findViewById(R.id.edt_closeTime);
                    RadioButton radio_247 = dialog.findViewById(R.id.radio_247);
                    RadioButton radio_closed = dialog.findViewById(R.id.radio_shop_closed);
                    RadioButton radio_setTimeManual = dialog.findViewById(R.id.radio_manual_setTime);
                    LinearLayout linear_set_timing = dialog.findViewById(R.id.linear_set_timing);

                    linear_set_timing.setVisibility(View.GONE);
                    radio_247.setChecked(true);

                    txt_message.setText(getResources().getString(R.string.settiming));
                    btnCancel.setText(getString(R.string.cancle));
                    btnSave.setText(getString(R.string.save));


                    radio_setTimeManual.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                            if (b) {
                                linear_set_timing.setVisibility(View.VISIBLE);
                            } else {
                                linear_set_timing.setVisibility(View.GONE);
                            }
                        }
                    });


                    edt_openTime.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            setTime(edt_openTime);
                        }
                    });

                    edt_closeTime.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            setTime(edt_closeTime);
                        }
                    });

                    btnSave.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (radio_setTimeManual.isChecked()) {
                                if (edt_openTime.getText().toString().isEmpty()) {
                                    Toasty.error(getActivity(), "Please set Opening Time", 5000).show();
                                } else if (edt_closeTime.getText().toString().isEmpty()) {
                                    Toasty.error(getActivity(), "Please set Closing Time", 5000).show();
                                } else {
                                    dialog.dismiss();
                                    saveTiming(edt_openTime.getText().toString(), edt_closeTime.getText().toString(), radio_247.isChecked(), radio_closed.isChecked(), position);
                                }
                            } else {
                                dialog.dismiss();
                                saveTiming(edt_openTime.getText().toString(), edt_closeTime.getText().toString(), radio_247.isChecked(), radio_closed.isChecked(), position);
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
            });

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

            @BindView(R.id.linear_edit)
            LinearLayout linear_edit;

            public RecycleHoursHolder(@NonNull View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }
    }

    public void saveTiming(String openTime, String closeTime, Boolean open247, Boolean closed, int position) {
        if (open247) {
            times.set(position, "Open 24 hours");
        } else if (closed) {
            times.set(position, "Closed");
        } else {
            times.set(position, openTime + " - " + closeTime);
        }

        recycleHoursAdapter.notifyDataSetChanged();
    }

    public void setTime(EditText editText) {
        final Calendar c = Calendar.getInstance();
        Calendar currentDate = Calendar.getInstance();

        new TimePickerDialog(getActivity(), R.style.DialogTheme, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                c.set(Calendar.HOUR_OF_DAY, hourOfDay);
                c.set(Calendar.MINUTE, minute);
                editText.setText(changeFormat(hourOfDay + ":" + minute + ":00"));
            }
        }, currentDate.get(Calendar.HOUR_OF_DAY), currentDate.get(Calendar.MINUTE), true).show();
    }

    public String changeFormat(String time) {
        String inputPattern = "HH:mm:ss";
        String outputPattern = "h:mm a";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(time)
            ;
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }

    private void selectPic() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 12345);
        } else {
            ImagePicker.with(getActivity())                         //  Initialize ImagePicker with activity or fragment context
                    .setToolbarColor("#386084")         //  Toolbar color
                    .setStatusBarColor("#0C3E65")       //  StatusBar color (works with SDK >= 21  )
                    .setToolbarTextColor("#FFFFFF")     //  Toolbar text color (Title and Done button)
                    .setToolbarIconColor("#FFFFFF")     //  Toolbar icon color (Back and Camera button)
                    .setProgressBarColor("#f56214")     //  ProgressBar color
                    .setBackgroundColor("#F2F2F2")      //  Background color
                    .setCameraOnly(false)               //  Camera mode
                    .setMultipleMode(false)//  Select multiple images or single image
                    .setFolderMode(true)                //  Folder mode
                    .setShowCamera(true)                //  Show camera button
                    .setFolderTitle(getResources().getString(R.string.profile_albums))           //  Folder title (works with FolderMode = true)
                    .setImageTitle(getResources().getString(R.string.gallery))         //  Image title (works with FolderMode = false)
                    .setDoneTitle(getResources().getString(R.string.profile_done))               //  Done button title
                    .setSavePath(getResources().getString(R.string.app_name))         //  Image capture folder name
                    .setAlwaysShowDoneButton(true)      //  Set always show done button in multiple mode
                    .setRequestCode(PICK_IMAGE)                //  Set request code, default Config.RC_PICK_IMAGES
                    .setKeepScreenOn(true)              //  Keep screen on when selecting images
                    .start();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Config.RC_PICK_IMAGES && resultCode == RESULT_OK && data != null) {
            ArrayList<Image> images = data.getParcelableArrayListExtra(Config.EXTRA_IMAGES);
            try {
                if (code.equals("100")) {
                    File temp = new File(images.get(0).getPath());
                    file = new Compressor(getActivity()).compressToFile(temp);
                    slider_list.set(0, file.getPath());
                    sliderAdapterExample.notifyDataSetChanged();
                } else if (code.equals("101")) {
                    File temp1 = new File(images.get(0).getPath());
                    file1 = new Compressor(getActivity()).compressToFile(temp1);
                    slider_list.set(1, file1.getPath());
                    sliderAdapterExample.notifyDataSetChanged();
                } else {
                    File temp2 = new File(images.get(0).getPath());
                    file2 = new Compressor(getActivity()).compressToFile(temp2);
                    slider_list.set(2, file2.getPath());
                    sliderAdapterExample.notifyDataSetChanged();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @OnClick(R.id.btn_add)
    public void add() {
        Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (dialog.getWindow() != null)
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.custom_dialog_addservice);

        TextView txt_message = dialog.findViewById(R.id.txt_message);
        EditText edt_service_name = dialog.findViewById(R.id.edt_serviceName);
        EditText edt_service_price = dialog.findViewById(R.id.edt_servicePrice);
        Button btnCancel = dialog.findViewById(R.id.btnCancel);
        Button btn_save = dialog.findViewById(R.id.btn_save);

        txt_message.setText(getResources().getString(R.string.addservice));
        btnCancel.setText(getString(R.string.cancle));
        btn_save.setText(getString(R.string.save));

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edt_service_name.getText().toString().isEmpty()) {
                    Toasty.error(getActivity(), "Service name can't be empty", 5000).show();
                } else if (edt_service_price.getText().toString().isEmpty()) {
                    Toasty.error(getActivity(), "Service price can't be empty", 5000).show();
                } else {
                    if (!insertService(edt_service_name.getText().toString().trim(), edt_service_price.getText().toString().trim())) {
                        dialog.dismiss();
                    }
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

    boolean insertService(String name, String price) {
        boolean flag = false;
        for (ViewBusinessModel model : viewBusinessModel.getServices()) {
            if (model.getServiceName().equalsIgnoreCase(name)) {
                flag = true;
            }
        }

        if (!flag) {
            ViewBusinessModel viewBusinessModel = new ViewBusinessModel();
            viewBusinessModel.setServiceName(name);
            viewBusinessModel.setServicePrice(price);
            serviceList.add(viewBusinessModel);
            tempList.add(viewBusinessModel);
            serviceAdapter.notifyDataSetChanged();
        } else {
            Toasty.error(getActivity(), "Service Already Exist", 5000).show();
        }

        return flag;

    }

    class serviceAdapter extends RecyclerView.Adapter<serviceAdapter.serviceHolder> {
        @NonNull
        @Override
        public serviceHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.row_edit_services, parent, false);
            serviceHolder serviceHolder = new serviceHolder(view);
            return serviceHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull serviceHolder holder, final int position) {
            ViewBusinessModel businessModel = serviceList.get(position);

            holder.txt_servicename.setText(businessModel.getServiceName());
            holder.txt_servicePrice.setText(getResources().getString(R.string.rupee) + " " + businessModel.getServicePrice());

            holder.img_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Dialog dialog = new Dialog(getActivity());
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    if (dialog.getWindow() != null)
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.setCancelable(false);
                    dialog.setContentView(R.layout.custom_dialog_delete_service);

                    TextView txt_message = dialog.findViewById(R.id.txt_message);
                    TextView txt_title = dialog.findViewById(R.id.txt_title);
                    Button btnCancel = dialog.findViewById(R.id.btnCancel);
                    Button btn_yes = dialog.findViewById(R.id.btn_yes);

                    txt_message.setText(getResources().getString(R.string.deleteconfirmmsg));
                    txt_title.setText(getResources().getString(R.string.deleteconfirm));
                    btnCancel.setText(getString(R.string.cancle));
                    btn_yes.setText(getString(R.string.yes));

                    btn_yes.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            deleteApi(serviceList.get(0).getServiceName(), serviceList.get(0).getServicePrice());
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
            return serviceList.size();
        }

        class serviceHolder extends RecyclerView.ViewHolder {

            @BindView(R.id.txt_serviceName)
            TextView txt_servicename;

            @BindView(R.id.txt_servicePrice)
            TextView txt_servicePrice;

            @BindView(R.id.img_delete)
            ImageView img_delete;

            public serviceHolder(@NonNull View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }
    }

    public void updateAPI() {
        String temptime = "";

        for (int i = 0; i < times.size(); i++) {

            if (i == 0) {
                temptime = times.get(i);
            } else {
                temptime = temptime + "," + times.get(i);
            }
        }

        String service = "";
        kProgressHUD = KProgressHUD.create(getActivity()).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE).setAnimationSpeed(5).setDimAmount(0.5f);
        kProgressHUD.show();
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        JSONObject jobject = new JSONObject();
        JSONArray jsonArray = new JSONArray();

        try {
            for (int i = 0; i < tempList.size(); i++) {
                if (tempList.get(i).getServiceId() == null) {
                    JSONObject services = new JSONObject();
                    services.put("service_name", tempList.get(i).getServiceName());
                    services.put("service_price", tempList.get(i).getServicePrice());
                    jsonArray.put(i, services);
                }
            }

            jobject.put("services", jsonArray);
            service = jobject.toString();

        } catch (JSONException e) {
            e.printStackTrace();
        }
        tempList = new ArrayList<>();
        params.put("user_id", SecurePreferences.getStringPreference(getActivity(), AppConstant.USERID));
        params.put("user_unique_id", SecurePreferences.getStringPreference(getActivity(), AppConstant.USERUNIQUEID));
        params.put("bus_id", viewBusinessModel.getBusId());
        params.put("bus_mobile", edt_mobile_number.getText().toString().trim());
        params.put("services", service);
        try {
            if (file != null) {
                params.put("bus_img_one", file);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            if (file1 != null) {
                params.put("bus_img_two", file1);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            if (file2 != null) {
                params.put("bus_img_three", file2);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        params.put("bus_name", edt_business_name.getText().toString().trim());
        params.put("bus_add", edt_business_address.getText().toString().trim());
        params.put("bus_loc", list_city.get(spinner_city.getSelectedItemPosition()).city_name);
        params.put("schedule", temptime);
        params.put("services", service);


        asyncHttpClient.post(AppConstant.BaseURL + "update_business.php", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if (kProgressHUD.isShowing()) kProgressHUD.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(new String(responseBody));
                    boolean status = jsonObject.getBoolean("status");
                    if (status) {
                        getFragmentManager().popBackStack();
                        Fragment fragment = getFragmentManager().findFragmentByTag("ManageBusinessFragment");
                        if(fragment != null){
                            ManageBusinessFragment manageBusinessFragment = (ManageBusinessFragment) fragment ;
                            manageBusinessFragment.ViewBusiness();
                            Toasty.success(getActivity(), jsonObject.optString("message"), 5000).show();
                        }

                    } else {
                        Toasty.error(getActivity(), jsonObject.optString("message"), 5000).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                if (kProgressHUD.isShowing()) kProgressHUD.dismiss();
            }
        });
    }

    @OnClick(R.id.btn_save)
    public void save() {
        Dialog dialog = new Dialog(getActivity());
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        if (dialog.getWindow() != null)
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.custom_dialog_confirmation);

        Button btnCancel = dialog.findViewById(R.id.btnCancel);
        Button btn_confirm = dialog.findViewById(R.id.btn_confirm);

        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateAPI();
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

    public void deleteApi(String service_name, String service_price) {

        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("user_id", SecurePreferences.getStringPreference(getActivity(), AppConstant.USERID));
        params.put("user_unique_id", SecurePreferences.getStringPreference(getActivity(), AppConstant.USERUNIQUEID));
        params.put("service_name", service_name);
        params.put("service_price", service_price);

        asyncHttpClient.post(AppConstant.BaseURL + "delete_service.php", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    JSONObject jsonObject = new JSONObject(new String(responseBody));
                    boolean status = jsonObject.getBoolean("status");
                    if (status) {

                        JSONArray jsonArray = jsonObject.getJSONArray("services");
                        Gson gson = new Gson();
                        Type type = new TypeToken<List<ViewBusinessModel>>() {
                        }.getType();
                        serviceList = gson.fromJson(jsonArray.toString(), type);
                        serviceAdapter.notifyDataSetChanged();
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
