package com.reemsservices.fragment.navigationtab.profile;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.karumi.dexter.Dexter;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nguyenhoanglam.imagepicker.model.Config;
import com.nguyenhoanglam.imagepicker.model.Image;
import com.nguyenhoanglam.imagepicker.ui.imagepicker.ImagePicker;
import com.reemsservices.R;
import com.reemsservices.helper.AppConstant;
import com.reemsservices.helper.SecurePreferences;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;
import es.dmoral.toasty.Toasty;
import id.zelory.compressor.Compressor;

import static android.app.Activity.RESULT_OK;

public class EditProfileFragment extends Fragment {

    private static final int PICK_IMAGE = 100;
    @BindView(R.id.edt_username)
    EditText edt_username;

    @BindView(R.id.edt_mobile_number)
    EditText edt_mobile_number;

    @BindView(R.id.edt_email)
    EditText edt_email;

    @BindView(R.id.img_profile_pic)
    ImageView img_profile_pic;

    File file;
    private KProgressHUD kProgressHUD;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.editprofilefragment,container,false);
        ButterKnife.bind(this,view);

        Glide.with(getActivity()).load(AppConstant.ImageURL + SecurePreferences.getStringPreference(getActivity(),AppConstant.USERPROFILE)).into(img_profile_pic);
        edt_username.setText(SecurePreferences.getStringPreference(getContext(),AppConstant.USERNAME));
        edt_mobile_number.setText(SecurePreferences.getStringPreference(getContext(),AppConstant.USERMOB));
        edt_email.setText(SecurePreferences.getStringPreference(getContext(),AppConstant.USEREMAIL));


        return view;
    }
    @OnClick(R.id.img_back)
    public void img_back(){
        getFragmentManager().popBackStack();
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
                    .setMultipleMode(false)             //  Select multiple images or single image
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
    @OnClick(R.id.img_profile_pic)
    public void img_profile_pic(){
        selectPic();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Config.RC_PICK_IMAGES && resultCode == RESULT_OK && data != null) {
            ArrayList<Image> images = data.getParcelableArrayListExtra(Config.EXTRA_IMAGES);
            File temp = new File(images.get(0).getPath());
            try {
                file = new Compressor(getActivity()).compressToFile(temp);
                Glide.with(getActivity()).load(file).into(img_profile_pic);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public void EditProfile(){

        kProgressHUD = KProgressHUD.create(getActivity()).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE).setAnimationSpeed(5).setDimAmount(0.5f);
        kProgressHUD.show();

        String user_mobile = SecurePreferences.getStringPreference(getActivity(),AppConstant.USERMOB);
        String user_id = SecurePreferences.getStringPreference(getActivity(),AppConstant.USERID);
        String user_unique_id = SecurePreferences.getStringPreference(getActivity(),AppConstant.USERUNIQUEID);
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("user_id",user_id);
        params.put("user_unique_id",user_unique_id);
        params.put("user_name",edt_username.getText().toString());
        params.put("user_mobile",user_mobile);
        params.put("user_email",edt_email.getText().toString());
        try {
            if(file!=null){
                params.put("user_profile",file);
            }
            else {
                params.put("user_profile","");
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        asyncHttpClient.post(AppConstant.BaseURL + "update_profile.php", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if (kProgressHUD.isShowing()) kProgressHUD.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(new String(responseBody));
                    boolean status = jsonObject.getBoolean("status");

                    if(status){
                        Toasty.success(getActivity(),jsonObject.optString("message"),5000).show();
                        SecurePreferences.savePreferences(getActivity(), AppConstant.USERID, jsonObject.getString("user_id"));
                        SecurePreferences.savePreferences(getActivity(), AppConstant.USERNAME, jsonObject.getString("user_name"));
                        SecurePreferences.savePreferences(getActivity(), AppConstant.USERPROFILE, jsonObject.getString("user_profile"));
                        SecurePreferences.savePreferences(getActivity(), AppConstant.USEREMAIL, jsonObject.getString("user_email"));
                        SecurePreferences.savePreferences(getActivity(), AppConstant.USERMOB, jsonObject.getString("user_mobile"));
                        SecurePreferences.savePreferences(getActivity(), AppConstant.USERTYPE, jsonObject.getString("user_type"));
                        SecurePreferences.savePreferences(getActivity(), AppConstant.USERUNIQUEID, jsonObject.getString("user_unique_id"));
                        Fragment fragment = getFragmentManager().findFragmentByTag("ProfileFragment");
                        if(fragment!=null){
                            ProfileFragment profileFragment = (ProfileFragment)fragment;
                            profileFragment.initView();
                            getFragmentManager().popBackStack();
                        }
                    }
                } catch (JSONException e) {
                    if (kProgressHUD.isShowing()) kProgressHUD.dismiss();
                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toasty.error(getActivity(),"failure android",5000).show();
            }
        });
    }
    @OnClick(R.id.btn_save)
    public void btn_save(){
        EditProfile();
    }
}
