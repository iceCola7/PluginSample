package com.cxz.pluginlib;

import android.app.Activity;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Bundle;

/**
 * @author chenxz
 * @date 2019/3/2
 * @desc 代理Activity，管理插件Activity的生命周期
 */
public class ProxyActivity extends Activity {

    private String mClassName;
    private PluginApk mPluginApk;
    private IPlugin mIPlugin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mClassName = getIntent().getStringExtra("className");
        mPluginApk = PluginManager.getInstance().getPluginApk();

        launchPluginActivity();

    }

    private void launchPluginActivity() {
        if (mPluginApk == null) {
            throw new RuntimeException("请先加载插件apk");
        }
        try {
            // 这个clazz就是Activity的实例对象，但是这个实例对象没有生命周期，没有上下文环境
            Class<?> clazz = mPluginApk.mDexClassLoader.loadClass(mClassName);
            Object object = clazz.newInstance();
            if (object instanceof IPlugin) {
                mIPlugin = (IPlugin) object;
                mIPlugin.attach(this);
                Bundle bundle = new Bundle();
                bundle.putInt("FROM", IPlugin.FROM_EXTERNAL);
                mIPlugin.onCreate(bundle);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Resources getResources() {
        return mPluginApk != null ? mPluginApk.mResources : super.getResources();
    }

    @Override
    public AssetManager getAssets() {
        return mPluginApk != null ? mPluginApk.mAssetManager : super.getAssets();
    }

    @Override
    public ClassLoader getClassLoader() {
        return mPluginApk != null ? mPluginApk.mDexClassLoader : super.getClassLoader();
    }

}
