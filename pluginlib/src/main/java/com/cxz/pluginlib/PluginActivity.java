package com.cxz.pluginlib;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * @author chenxz
 * @date 2019/3/2
 * @desc
 */
public class PluginActivity extends Activity implements IPlugin {

    private int mFrom = FROM_INTERNAL;

    private Activity mActivity;

    @Override
    public void attach(Activity activity) {
        mActivity = activity;
    }

    @Override
    public void setContentView(int layoutResID) {
        if (mFrom == FROM_INTERNAL) {
            super.setContentView(layoutResID);
        } else {
            mActivity.setContentView(layoutResID);
        }
    }

    @Override
    public void onCreate(Bundle saveInstanceSate) {
        if (saveInstanceSate != null) {
            mFrom = saveInstanceSate.getInt("FROM");
            if (mFrom == FROM_INTERNAL) {
                super.onCreate(saveInstanceSate);
                mActivity = this;
            }
        }
    }

    @Override
    public void onStart() {
        if (mFrom == FROM_INTERNAL) {
            super.onStart();
        }
    }

    @Override
    public void onRestart() {
        if (mFrom == FROM_INTERNAL) {
            super.onRestart();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (mFrom == FROM_INTERNAL) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onResume() {
        if (mFrom == FROM_INTERNAL) {
            super.onResume();
        }
    }

    @Override
    public void onPause() {
        if (mFrom == FROM_INTERNAL) {
            super.onPause();
        }
    }

    @Override
    public void onStop() {
        if (mFrom == FROM_INTERNAL) {
            super.onStop();
        }
    }

    @Override
    public void onDestroy() {
        if (mFrom == FROM_INTERNAL) {
            super.onDestroy();
        }
    }
}
