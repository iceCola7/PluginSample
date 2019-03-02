package com.cxz.pluginlib;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;

import java.io.File;
import java.lang.reflect.Method;

import dalvik.system.DexClassLoader;

/**
 * @author chenxz
 * @date 2019/3/2
 * @desc
 */
public class PluginManager {

    public static volatile PluginManager instance;

    public static PluginManager getInstance() {
        if (instance == null) {
            synchronized (PluginManager.class) {
                if (instance == null) {
                    instance = new PluginManager();
                }
            }
        }
        return instance;
    }

    private Context mContext;
    private PluginApk mPluginApk;

    public void init(Context context) {
        mContext = context.getApplicationContext();
    }

    public PluginApk getPluginApk() {
        return mPluginApk;
    }

    /**
     * 加载 APK 文件
     */
    public void loadApk(String apkPath) {
        PackageInfo packageInfo = mContext.getPackageManager().getPackageArchiveInfo(apkPath,
                PackageManager.GET_ACTIVITIES | PackageManager.GET_SERVICES);
        if (packageInfo == null) return;
        // 创建 classloader
        DexClassLoader classLoader = createDexClassLoader(apkPath);
        // 创建 AssetManager
        AssetManager assetManager = createAssetManager(apkPath);
        // 创建 resources
        Resources resources = createResources(assetManager);
        mPluginApk = new PluginApk(packageInfo, resources, classLoader);
    }

    private Resources createResources(AssetManager assetManager) {
        Resources resources = mContext.getResources();
        return new Resources(assetManager, resources.getDisplayMetrics(), resources.getConfiguration());
    }

    private AssetManager createAssetManager(String apkPath) {
        try {
            AssetManager assetManager = AssetManager.class.newInstance();
            Method method = AssetManager.class.getDeclaredMethod("addAssetPath", String.class);
            method.invoke(assetManager, apkPath);
            return assetManager;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private DexClassLoader createDexClassLoader(String apkPath) {
        File file = mContext.getDir("dex", Context.MODE_PRIVATE);
        return new DexClassLoader(apkPath, file.getAbsolutePath(), null, mContext.getClassLoader());
    }

}
