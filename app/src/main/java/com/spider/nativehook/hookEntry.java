package com.spider.nativehook;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.virjar.ratel.api.RatelToolKit;
import com.virjar.ratel.api.rposed.IRposedHookLoadPackage;
import com.virjar.ratel.api.rposed.RC_MethodHook;
import com.virjar.ratel.api.rposed.RposedHelpers;
import com.virjar.ratel.api.rposed.callbacks.RC_LoadPackage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class hookEntry implements IRposedHookLoadPackage {
    private static final String TAG = "Ainkom->";

    @Override
    public void handleLoadPackage(RC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        if (lpparam.packageName.equals("com.spider.nativedome")) {
            showToast("hook com.spider.nativedome start");
            RposedHelpers.findAndHookMethod("java.lang.Runtime", lpparam.classLoader, "loadLibrary0", Class.class, String.class, new RC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    super.beforeHookedMethod(param);
                    Log.d(TAG, "beforeHookedMethod: so 文件加载之前 " + param.args[1]);
                }

                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    super.afterHookedMethod(param);
                    Log.d(TAG, "afterHookedMethod: so 文件加载之后 " + param.args[1]);
                    if (param.args[1].equals("nativedome")) {
                        loadSoFile("librate.so", "content://com.spider.nativehook/assets/jniLibs/arm64-v8a/libnativehook.so");
                    }
                }
            });
        }
    }

    private void loadSoFile(String newFileName, String soDir) {
        try {
            // 先在目标 app 的私有目录下创建一个空的 so 文件来待接收我们的插件 so 文件
            // 完成此操作得拿到目标 app 的 context
            Context sContext = RatelToolKit.sContext;
            if (sContext == null) {
                Log.d(TAG, "writeSoFile: sContext 获取失败");
                return;
            }
            File newFile = new File(sContext.getFilesDir(), newFileName);
            // 我们得拿到插件 app 的 so 文件打开对象，使用那个共享方法
            Uri uri = Uri.parse(soDir);
            // 拿到插件 app 的 so 文件
            ContentResolver contentResolver = sContext.getContentResolver();
            AssetFileDescriptor r = contentResolver.openAssetFileDescriptor(uri, "r", null);
            // 拿到的 r 是打开后的对象，接下来写入即可
            if (r == null) {
                Log.d(TAG, String.format("writeSoFile: 没有存在目录为%s的文件", soDir));
                return;
            }
            if (r.getLength() > Integer.MAX_VALUE) {
                Log.d(TAG, "writeSoFile: 目标文件太大，出现错误");
                return;
            }
            int length = (int) r.getLength();
            FileInputStream inputStream = r.createInputStream();
            // 把插件 app 中 so 文件的内容复制到目标 app 的私有目录下
            byte[] bytes = new byte[length];
            inputStream.read(bytes, 0, length);
            FileOutputStream fileOutputStream = new FileOutputStream(newFile);
            fileOutputStream.write(bytes);
            inputStream.close();
            fileOutputStream.close();
            r.close();
            System.load(newFile.getAbsolutePath());
            Log.d(TAG, "writeSoFile: 加载完成");
        } catch (Exception e) {
            Log.d(TAG, "writeSoFile: error " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void showToast(String msg) {
        new Handler(Looper.getMainLooper()).post(
                new Runnable() {
                    @Override
                    public void run() {
                        Context sContext = RatelToolKit.sContext;
                        Toast.makeText(sContext, msg, Toast.LENGTH_LONG).show();
                    }
                }
        );
    }
}
