package com.baseproject.base;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.baseproject.R;
import org.greenrobot.eventbus.EventBus;

/**
 * Created by yuanxiang on 2019/1/6.
 */
public abstract class BaseFragment extends Fragment {
    private boolean isRegisterEventBus = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        int i = initFragmentLayout();
        // 注册 EventBus
        if (isRegisterEventBus) {
            EventBus.getDefault().register(this);
        }
        if (i != 0) {
            return inflater.inflate(i, null);
        } else {
            return super.onCreateView(inflater, container, savedInstanceState);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();
        initListener();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (isRegisterEventBus) {
            EventBus.getDefault().unregister(this);
        }
    }

    protected abstract @LayoutRes
    int initFragmentLayout();

    protected abstract void initData();

    protected abstract void initListener();

    /**
     * 设置是否注册 EventBus，默认不注册 <br/>
     * 注意，如果为true，则fragment中必须有 @Subscribe 注解的方法
     *
     * @param isRegisterEventBus
     */
    protected void setEvenBusRegister(boolean isRegisterEventBus) {
        this.isRegisterEventBus = isRegisterEventBus;
    }

    /**
     * 默认不传参数。采用add添加fragment且加入回退栈
     */
    public void nextFragment(Fragment fragment) {
        this.nextFragment(fragment, null);
    }

    /**
     * 采用add添加fragment且加入回退栈
     *
     * @param fragment
     * @param bundle
     */
    public void nextFragment(Fragment fragment, Bundle bundle) {
        this.nextFragment(fragment, bundle, true, true);
    }

    public void nextFragment(Fragment fragment, boolean isAdd, boolean isBackStack) {
        this.nextFragment(fragment, null, isAdd, isBackStack);
    }

    /**
     * 如果是add方式应加入回退栈, replace则一般不用
     */
    public void nextFragment(Fragment fragment, Bundle bundle, boolean isAdd, boolean isBackStack) {
        FragmentTransaction ft = this.getActivity().getSupportFragmentManager().beginTransaction();
        fragment.setArguments(bundle);
        if (isAdd) {
            if (fragment.isAdded()) {
                ft.show(fragment).hide(this);
            } else {
                ft.hide(this).add(R.id.contanier, fragment);
            }
        } else {
            if (fragment.isAdded()) {
                ft.show(fragment).hide(this);
            } else
                ft.replace(R.id.contanier, fragment);
        }
        if (isBackStack) ft.addToBackStack(null);
        ft.commitAllowingStateLoss();
    }
}
