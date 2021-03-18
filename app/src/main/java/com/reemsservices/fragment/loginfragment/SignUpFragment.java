package com.reemsservices.fragment.loginfragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.kaopiz.kprogresshud.KProgressHUD;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.RequestParams;
import com.reemsservices.MainActivity;
import com.reemsservices.R;
import com.reemsservices.fragment.loginfragment.SignInFragment;
import com.reemsservices.helper.AppConstant;
import com.reemsservices.helper.SecurePreferences;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;
import es.dmoral.toasty.Toasty;

public class SignUpFragment extends Fragment {

    @BindView(R.id.img_back)
    ImageView img_back;

    @BindView(R.id.txt_login)
    TextView txt_login;

    @BindView(R.id.edt_username)
    EditText edt_username;

    @BindView(R.id.edt_mobile_number)
    EditText edt_mobile_number;

    @BindView(R.id.edt_email)
    EditText edt_email;

    @BindView(R.id.check_policy)
    CheckBox check_policy;

    private KProgressHUD kProgressHUD;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate( R.layout.signupfragment,container,false);
        ButterKnife.bind(this,view);


        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().popBackStack();
            }
        });

        txt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppConstant.addLoginFragment(getFragmentManager(),new SignInFragment(),"SignUpFragment");
            }
        });

        return view;
    }
    @OnClick(R.id.btn_sign_up)
    public void btn_sign_up(){

        if(edt_username.getText().toString().isEmpty()){
          Toasty.error(getActivity(),"User name can't be empty",5000).show();
        }
        else if(edt_mobile_number.getText().toString().isEmpty()){
            Toasty.error(getActivity(),"Mobile number can't be empty",5000).show();
        }
        else if(edt_mobile_number.getText().toString().trim().length()!=10){
            Toasty.error(getActivity(),"Please enter correct mobile number",5000).show();
        }
        else if(edt_email.getText().toString().isEmpty()){
            Toasty.error(getActivity(),"Email can't be empty",5000).show();
        }
        else if(!check_policy.isChecked()){
            Toasty.error(getContext(),"Please check Privacy Policy",5000).show();
        }
        else {
            Register();
        }
    }
    public void Register() {

        kProgressHUD = KProgressHUD.create(getActivity()).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE).setAnimationSpeed(5).setDimAmount(0.5f);
        kProgressHUD.show();

        AsyncHttpClient asyncHttpClient =new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("user_name",edt_username.getText().toString());
        params.put("user_email",edt_email.getText().toString().trim());
        params.put("user_mobile",edt_mobile_number.getText().toString().trim());

        asyncHttpClient.post(AppConstant.BaseURL + "register_user.php", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if (kProgressHUD.isShowing()) kProgressHUD.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(new String(responseBody));
                    boolean status = jsonObject.getBoolean("status");

                    if(status){
                         Toasty.success(getActivity(),jsonObject.optString("message"),5000,true).show();

                        getFragmentManager().popBackStack();
                    }
                    else {
                         Toasty.error(getActivity(),jsonObject.optString("message"),5000,true).show();
                    }
                } catch (JSONException e) {
                    if (kProgressHUD.isShowing()) kProgressHUD.dismiss();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });

    }
}
