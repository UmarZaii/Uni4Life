package com.umarzaii.uni4life.Controller;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

public class FragmentController {

    private FragmentManager fragmentManager;
    private Fragment targetFragment;

    public FragmentController(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    public void startFragment(Fragment fragmentClass, int containerViewId) {
        targetFragment = fragmentClass;
        startTransaction(containerViewId);
    }

    public void startFragment(Fragment fragmentClass, int containerViewId, Bundle bundle) {
        targetFragment = fragmentClass;
        targetFragment.setArguments(bundle);
        startTransaction(containerViewId);
    }

    public void stackFragment(Fragment fragmentClass, int containerViewId, String stackName) {
        targetFragment = fragmentClass;
        startTransaction(containerViewId, stackName);
    }

    public void stackFragment(Fragment fragmentClass, int containerViewId, Bundle bundle, String stackName) {
        targetFragment = fragmentClass;
        targetFragment.setArguments(bundle);
        startTransaction(containerViewId, stackName);
    }

    public void popBackStack(String stackName) {
        fragmentManager.popBackStack (stackName, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    private void startTransaction(int containerViewId) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(containerViewId, targetFragment);
        transaction.commit();
    }

    private void startTransaction(int containerViewId, String stackName) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(containerViewId, targetFragment);
        transaction.addToBackStack(stackName);
        transaction.commit();
    }

}