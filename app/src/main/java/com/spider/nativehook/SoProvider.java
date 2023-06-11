package com.spider.nativehook;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.FileNotFoundException;
import java.io.IOException;

public class SoProvider extends ContentProvider {
    private static final String TAG = "Ainkom->";

    @Override
    public boolean onCreate() {
        return false;
    }

    @Nullable
    @Override
    public AssetFileDescriptor openAssetFile(@NonNull Uri uri, @NonNull String mode) throws FileNotFoundException {
        Log.d(TAG, "openAssetFile: uri = " + uri);
        AssetFileDescriptor afd = null;
        Context context = getContext();
        if (context == null) {
            throw new FileNotFoundException("context 为空");
        }
        AssetManager assets = context.getAssets();
        // /assets/jniLibs...
        String assetsPath = uri.getPath().substring(8);
        try {
            afd = assets.openFd(assetsPath);
        } catch (IOException e) {
            Log.d(TAG, "openAssetFile error: " + e.getMessage());
            throw new RuntimeException(e);
        }
        return afd;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        return null;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
