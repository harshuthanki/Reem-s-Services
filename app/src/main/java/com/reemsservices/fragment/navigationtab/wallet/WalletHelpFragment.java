package com.reemsservices.fragment.navigationtab.wallet;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.reemsservices.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class WalletHelpFragment extends BottomSheetDialogFragment {

    Context frag_context;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        frag_context = context;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((View) getView().getParent()).setBackgroundColor(Color.TRANSPARENT);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.wallethelpfragment,container,false);
        ButterKnife.bind(this,view);
        return view;
    }
}
