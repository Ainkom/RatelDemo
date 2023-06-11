package com.spider.nativehook;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.spider.nativehook.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    // Used to load the 'nativehook' library on application startup.


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * A native method that is implemented by the 'nativehook' native library,
     * which is packaged with this application.
     */
}