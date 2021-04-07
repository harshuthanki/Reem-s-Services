package com.reemsservices.fragment.navigationtab.wallet;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.reemsservices.helper.AppConstant;
import com.reemsservices.helper.SecurePreferences;
import com.reemsservices.model.TransactionModel;
import com.reemsservices.model.ViewBusinessModel;
import com.reemsservices.model.WalletModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;
import es.dmoral.toasty.Toasty;


public class WalletFragment extends Fragment {

    @BindView(R.id.txt_user_coins)
    TextView txt_user_coins;

    @BindView(R.id.txt_user_unique_code)
    TextView txt_user_unique_code;

    @BindView(R.id.txt_coinSpentHeading)
    TextView txt_coinSpentHeading;

    @BindView(R.id.tablayout)
    TabLayout tablayout;

    @BindView(R.id.edt_coin_claim)
    EditText edt_coin_claim;

    @BindView(R.id.recycle_coinYouSpent)
    RecyclerView recycle_coinYouSpent;

    ViewBusinessModel viewBusinessModel;
    List<ViewBusinessModel> list_viewBusiness;
    List<WalletModel> list_wallet;
    List<TransactionModel> list_transaction;
    private KProgressHUD kProgressHUD;

    coinsYouSpentAdapter coinsYouSpentAdapter;
    coinsYouGeneratedAdapter coinsYouGeneratedAdapter;


//    @BindView(R.id.btn_add_coins)
//    FloatingActionButton btn_add_coins;

    @BindView(R.id.btn_add_coins)
    ImageView btn_add_coins;

    String partner = "p";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.walletfragment, container, false);
        ButterKnife.bind(this, view);
        initView();

        return view;
    }

    public void initView() {
        ViewBusiness();

        tablayout.removeAllTabs();
        tablayout.setVisibility(View.GONE);
        tablayout.addTab(tablayout.newTab().setText(getResources().getString(R.string.coinsyouspent)));
        tablayout.addTab(tablayout.newTab().setText(getResources().getString(R.string.coinsyougenerated)));

        if (SecurePreferences.getStringPreference(getActivity(), AppConstant.USERTYPE).equalsIgnoreCase(partner)) {
            btn_add_coins.setVisibility(View.VISIBLE);
            tablayout.setVisibility(View.VISIBLE);
            txt_coinSpentHeading.setVisibility(View.GONE);
        } else {
            btn_add_coins.setVisibility(View.GONE);
            tablayout.setVisibility(View.GONE);
            txt_coinSpentHeading.setVisibility(View.VISIBLE);
        }


        tablayout.setTabTextColors(Color.parseColor("#727272"), Color.parseColor("#112d4e"));

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recycle_coinYouSpent.setLayoutManager(layoutManager);

        list_transaction = new ArrayList<>();
        coinsYouSpentAdapter = new coinsYouSpentAdapter();
        recycle_coinYouSpent.setAdapter(coinsYouSpentAdapter);


        tablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    coinsYouSpentAdapter = new coinsYouSpentAdapter();
                    recycle_coinYouSpent.setAdapter(coinsYouSpentAdapter);
                } else if (tab.getPosition() == 1) {
                    coinsYouGeneratedAdapter = new coinsYouGeneratedAdapter();
                    recycle_coinYouSpent.setAdapter(coinsYouGeneratedAdapter);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @OnClick(R.id.img_help)
    public void help() {
        WalletHelpFragment walletHelpFragment = new WalletHelpFragment();
        walletHelpFragment.show(getFragmentManager(), "WalletHelpFragment");
    }

    @OnClick(R.id.btn_add_coins)
    public void addCoins() {
        GenerateCoinsFragment generateCoinsFragment = new GenerateCoinsFragment(list_viewBusiness, viewBusinessModel);
        generateCoinsFragment.show(getFragmentManager(), "GenerateCoinsFragment");
    }

    @OnClick(R.id.btn_coupon)
    public void btn_coupon() {
        TransactionFragment transactionFragment = new TransactionFragment();
        transactionFragment.show(getFragmentManager(), "TransactionFragment");
    }

    @OnClick(R.id.btn_claim_now)
    public void btn_claim_now() {
        if (edt_coin_claim.getText().toString().trim().toUpperCase().isEmpty()) {
            Toasty.error(getActivity(), getResources().getString(R.string.enterrefcode), 5000).show();
        } else {
            claimCoinApi();
        }

    }

    class coinsYouSpentAdapter extends RecyclerView.Adapter<coinsYouSpentAdapter.coinsYouSpentHolder> {
        @NonNull
        @Override
        public coinsYouSpentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.row_coinsspent_recycle, parent, false);
            coinsYouSpentHolder coinsYouSpentHolder = new coinsYouSpentHolder(view);
            return coinsYouSpentHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull coinsYouSpentHolder holder, int position) {

            TransactionModel transactionModel = list_transaction.get(position);
            Glide.with(getActivity()).load(AppConstant.ImageURL + transactionModel.getCatImg()).into(holder.img_business);
            holder.txt_name_of_business.setText(transactionModel.getBusName());
            holder.txt_date.setText(AppConstant.dateFormate(transactionModel.getTransDate()));
            holder.txt_time.setText(AppConstant.timeFormate(transactionModel.getTransDate()));
            holder.txt_totalSpentCoins.setText(transactionModel.getTransCoin());
        }

        @Override
        public int getItemCount() {
            return list_transaction.size();
        }

        class coinsYouSpentHolder extends RecyclerView.ViewHolder {

            @BindView(R.id.img_business)
            ImageView img_business;

            @BindView(R.id.txt_name_of_business)
            TextView txt_name_of_business;

            @BindView(R.id.txt_date)
            TextView txt_date;

            @BindView(R.id.txt_time)
            TextView txt_time;

            @BindView(R.id.txt_totalSpentCoins)
            TextView txt_totalSpentCoins;

            public coinsYouSpentHolder(@NonNull View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }
    }

    class coinsYouGeneratedAdapter extends RecyclerView.Adapter<coinsYouGeneratedAdapter.coinsYouGeneratedHolder> {
        @NonNull
        @Override
        public coinsYouGeneratedHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.row_coinsgenerated_recycle, parent, false);
            coinsYouGeneratedHolder coinsYouGeneratedHolder = new coinsYouGeneratedHolder(view);
            return coinsYouGeneratedHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull coinsYouGeneratedHolder holder, int position) {
            WalletModel walletModel = list_wallet.get(position);
            Glide.with(getActivity()).load(AppConstant.ImageURL + walletModel.getCatImg()).into(holder.img_business);
            holder.txt_name_of_business.setText(walletModel.getBusName());
            holder.txt_date.setText(AppConstant.dateFormate(walletModel.getCreatedDate()));
            holder.txt_time.setText(AppConstant.timeFormate(walletModel.getCreatedDate()));
            holder.txt_totalGeneratedCoins.setText(walletModel.getCoin());
            holder.txt_coupon.setText(walletModel.getCodeGenerate());
        }

        @Override
        public int getItemCount() {
            return list_wallet.size();
        }

        class coinsYouGeneratedHolder extends RecyclerView.ViewHolder {

            @BindView(R.id.img_business)
            ImageView img_business;

            @BindView(R.id.txt_name_of_business)
            TextView txt_name_of_business;

            @BindView(R.id.txt_date)
            TextView txt_date;

            @BindView(R.id.txt_time)
            TextView txt_time;

            @BindView(R.id.txt_coupon)
            TextView txt_coupon;

            @BindView(R.id.txt_totalGeneratedCoins)
            TextView txt_totalGeneratedCoins;

            public coinsYouGeneratedHolder(@NonNull View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }
    }

    public void ViewBusiness() {

        kProgressHUD = KProgressHUD.create(getActivity()).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE).setAnimationSpeed(5).setDimAmount(0.5f);
        kProgressHUD.show();

        String user_id = SecurePreferences.getStringPreference(getActivity(), AppConstant.USERID);
        String user_unique_id = SecurePreferences.getStringPreference(getActivity(), AppConstant.USERUNIQUEID);
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("user_id", user_id);
        params.put("user_unique_id", user_unique_id);
        params.put("user_type",SecurePreferences.getStringPreference(getActivity(),AppConstant.USERTYPE));

        asyncHttpClient.post(AppConstant.BaseURL + "wallet.php", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if (kProgressHUD.isShowing()) kProgressHUD.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(new String(responseBody));
                    boolean status = jsonObject.getBoolean("status");
                    if (status) {
                        txt_user_coins.setText(jsonObject.getString("user_coin"));
                        txt_user_unique_code.setText(jsonObject.getString("user_ref_code"));

                        JSONArray jsonArray = jsonObject.getJSONArray("business_view");
                        Gson gson = new Gson();
                        Type type = new TypeToken<List<ViewBusinessModel>>() {
                        }.getType();
                        list_viewBusiness = gson.fromJson(jsonArray.toString(), type);

                        jsonArray = jsonObject.getJSONArray("transaction_array");
                        type = new TypeToken<List<TransactionModel>>() {
                        }.getType();
                        list_transaction = gson.fromJson(jsonArray.toString(), type);

                        coinsYouSpentAdapter.notifyDataSetChanged();
                        jsonArray = jsonObject.getJSONArray("coin_created");
                        type = new TypeToken<List<WalletModel>>() {
                        }.getType();
                        list_wallet = gson.fromJson(jsonArray.toString(), type);

                    }
                    else {
                        Toasty.error(getActivity(),jsonObject.optString("message"),5000).show();
                    }
                } catch (JSONException e) {
                    if (kProgressHUD.isShowing()) kProgressHUD.dismiss();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toasty.error(getActivity(), "fail", 5000).show();
            }
        });
    }

    public void claimCoinApi() {

        kProgressHUD = KProgressHUD.create(getActivity()).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE).setAnimationSpeed(5).setDimAmount(0.5f);
        kProgressHUD.show();

        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("user_id", SecurePreferences.getStringPreference(getActivity(), AppConstant.USERID));
        params.put("user_unique_id", SecurePreferences.getStringPreference(getActivity(), AppConstant.USERUNIQUEID));
        params.put("code_generate", edt_coin_claim.getText().toString().trim().toUpperCase());

        asyncHttpClient.post(AppConstant.BaseURL + "verify_coin.php", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if (kProgressHUD.isShowing()) kProgressHUD.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(new String(responseBody));
                    boolean status = jsonObject.getBoolean("status");
                    if (status) {
                        Toasty.success(getActivity(), jsonObject.optString("message"), 5000).show();
                        txt_user_coins.setText(String.valueOf(jsonObject.getInt("user_coin")));
                        rating();
                        ViewBusiness();
                    } else {
                        Toasty.error(getActivity(), jsonObject.optString("message"), 5000).show();
                    }
                } catch (JSONException e) {
                    if (kProgressHUD.isShowing()) kProgressHUD.dismiss();
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }

    public void rating() {
        Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (dialog.getWindow() != null)
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.custom_rating);

        TextView txt_reviewBusHeader = dialog.findViewById(R.id.txt_reviewBusHeader);
        RatingBar ratingBar = dialog.findViewById(R.id.rating);
        EditText edt_busReview = dialog.findViewById(R.id.edt_busReview);
        Button btnCancel = dialog.findViewById(R.id.btnCancel);
        Button btn_save = dialog.findViewById(R.id.btn_save);

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                txt_reviewBusHeader.setVisibility(View.VISIBLE);
                edt_busReview.setVisibility(View.VISIBLE);
            }
        });

        btnCancel.setText(getString(R.string.cancle));
        btn_save.setText(getString(R.string.save));

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ratingApi(ratingBar.getRating(),edt_busReview.getText().toString().trim());
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
    public void ratingApi(float rating, String busReview){

        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("user_id",SecurePreferences.getStringPreference(getActivity(),AppConstant.USERID));
        params.put("user_unique_id",SecurePreferences.getStringPreference(getActivity(),AppConstant.USERUNIQUEID));
        params.put("code",edt_coin_claim.getText().toString().trim());
        params.put("rating",rating);
        params.put("bus_review",busReview);

        asyncHttpClient.post(AppConstant.BaseURL + "rating.php", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    JSONObject jsonObject = new JSONObject(new String(responseBody));
                    boolean status = jsonObject.getBoolean("status");
                    if(status){
                        Toasty.success(getActivity(),jsonObject.optString("message"),5000).show();
                    }else {
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
