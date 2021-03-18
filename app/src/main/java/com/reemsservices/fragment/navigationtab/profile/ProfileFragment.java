package com.reemsservices.fragment.navigationtab.profile;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.reemsservices.MainActivity;
import com.reemsservices.R;
import com.reemsservices.helper.AppConstant;
import com.reemsservices.helper.SecurePreferences;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ProfileFragment extends Fragment {

    @BindView(R.id.txt_user_name)
    TextView txt_user_name;

    @BindView(R.id.txt_user_mobile)
    TextView txt_user_mobile;

    @BindView(R.id.img_profile_pic)
    ImageView img_profile_pic;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profilefragment, container, false);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }
    @OnClick(R.id.txt_logout)
    public void txt_logout(){
        SecurePreferences.clearSecurepreference(getContext());
        Intent intent =new Intent(getActivity(),MainActivity.class);
        startActivity(intent);
        getActivity().finish();
    }
    @OnClick(R.id.linear_edit)
    public void linear_edit(){
        AppConstant.addFragment(getFragmentManager(),new EditProfileFragment(),"EditProfileFragment");
    }
    @OnClick(R.id.linear_manage_business)
    public void linear_manage_business(){
        AppConstant.addFragment(getFragmentManager(),new ManageBusinessFragment(),"ManageBusinessFragment");
    }
    public void initView(){
        Glide.with(getActivity()).load(AppConstant.ImageURL + SecurePreferences.getStringPreference(getActivity(),AppConstant.USERPROFILE)).into(img_profile_pic);
        txt_user_name.setText(SecurePreferences.getStringPreference(getContext(),AppConstant.USERNAME));
        txt_user_mobile.setText("+91 " + SecurePreferences.getStringPreference(getContext(),AppConstant.USERMOB));
    }
    @OnClick(R.id.linear_share)
    public void share(){
        Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        String app_url =  "https://play.google.com/store/apps/details?id=app.com.reemsservices";
        shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, app_url);
        startActivity(Intent.createChooser(shareIntent, "Share via"));
    }
    @OnClick(R.id.linear_contact_us)
    public void contact(){
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:9999999999"));
        startActivity(intent);
    }
}
