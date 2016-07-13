package com.android.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.utils.TextUtil;

import butterknife.ButterKnife;

/**
 * Created by liujq on 2016/5/31.
 */
public abstract class BaseFragment extends Fragment implements CommonInterface {

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View rootView = inflater.inflate(setContentView(), container, false);
        ButterKnife.bind(this, rootView);

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        initData();
        setListener();
    }

    public abstract int setContentView();


    @Override
    public void onDestroy() {
        ButterKnife.unbind(this);
        super.onDestroy();
    }

    @Override
    public Context getContext() {
        return getActivity();
    }




    public void showToast(String text) {
        if (TextUtil.isEmpty(text)) {
            return;
        }
        Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
    }

    public void goNext(Class toClass, Bundle bundle) {
        Intent intent = new Intent(getActivity(), toClass);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    public void goNext(Class toClass) {
        Intent intent = new Intent(getActivity(), toClass);
        startActivity(intent);
    }

}
