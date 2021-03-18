package com.reemsservices.fragment.loginfragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.kaopiz.kprogresshud.KProgressHUD;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.reemsservices.LoginActivity;
import com.reemsservices.MainActivity;
import com.reemsservices.R;
import com.reemsservices.helper.AppConstant;
import com.reemsservices.helper.SecurePreferences;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;
import es.dmoral.toasty.Toasty;

public class SignInFragment extends Fragment {

    @BindView(R.id.txt_register)
    TextView txt_register;

    @BindView(R.id.btn_sign_in)
    Button btn_signin;

    @BindView(R.id.edt_mobile_number)
    EditText edt_mobile_number;

    private KProgressHUD kProgressHUD;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.signinfragment, container, false);
        ButterKnife.bind(this, view);

        txt_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppConstant.addLoginFragment(getFragmentManager(), new SignUpFragment(), "SignUpFragment");
            }
        });

        return view;
    }

    @OnClick(R.id.btn_sign_in)
    public void brn_sign_in() {
        if (edt_mobile_number.getText().toString().isEmpty()) {
            Toasty.error(getActivity(), "Mobile number can't be empty", 5000).show();
        } else if (edt_mobile_number.getText().toString().trim().length() != 10) {
            Toasty.error(getActivity(), "Please enter correct mobile number", 5000).show();
        } else {
            Login();
        }
    }

    public void Login() {

        kProgressHUD = KProgressHUD.create(getActivity()).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE).setAnimationSpeed(5).setDimAmount(0.5f);
        kProgressHUD.show();

        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("user_mobile", edt_mobile_number.getText().toString());

        asyncHttpClient.post(AppConstant.BaseURL + "login_user.php", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if (kProgressHUD.isShowing()) kProgressHUD.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(new String(responseBody));
                    boolean status = jsonObject.getBoolean("status");
                    if (status) {
                        AppConstant.addLoginFragment(getFragmentManager(), new OTPFragment(edt_mobile_number.getText().toString()), "OTPFragment");
                        SecurePreferences.savePreferences(getActivity(),AppConstant.USERID,jsonObject.getString("user_id"));
                        SecurePreferences.savePreferences(getActivity(),AppConstant.USERUNIQUEID,jsonObject.getString("user_unique_id"));
                    } else {
                        Toasty.error(getActivity(), jsonObject.optString("message"), 5000, true).show();
                    }
                } catch (JSONException e) {
                    if (kProgressHUD.isShowing()) kProgressHUD.dismiss();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(getActivity(), "Fail", Toast.LENGTH_SHORT).show();
            }
        });
    }
}