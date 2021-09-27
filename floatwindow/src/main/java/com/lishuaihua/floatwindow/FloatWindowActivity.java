package com.lishuaihua.floatwindow;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.lishuaihua.baselib.util.permission.PermissionUtils;

@Route(path = "/float/float_window")
public class FloatWindowActivity extends AppCompatActivity {

    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_float_window);

        mHandler = new Handler();

        findViewById(R.id.btn_check_permission).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean permission = FloatWindowParamManager.checkPermission(getApplicationContext());
                if (permission ) {
                    Toast.makeText(FloatWindowActivity.this, R.string.has_float_permission, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), FloatWindowService.class);
                    intent.setAction(FloatWindowService.ACTION_CHECK_PERMISSION_AND_TRY_ADD);
                    startService(intent);
                } else {
                    Toast.makeText(FloatWindowActivity.this, R.string.no_float_permission, Toast.LENGTH_SHORT).show();
                    showOpenPermissionDialog();
                }
            }
        });

        findViewById(R.id.btn_stop_service).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), FloatWindowService.class);
                stopService(intent);
            }
        });

        findViewById(R.id.btn_full_screen_touch_able).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), FloatWindowService.class);
                intent.setAction(FloatWindowService.ACTION_FULL_SCREEN_TOUCH_ABLE);
                startService(intent);
            }
        });

        findViewById(R.id.btn_full_screen_touch_disable).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), FloatWindowService.class);
                intent.setAction(FloatWindowService.ACTION_FULL_SCREEN_TOUCH_DISABLE);
                startService(intent);
            }
        });

        findViewById(R.id.btn_not_full_touch_able).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), FloatWindowService.class);
                intent.setAction(FloatWindowService.ACTION_NOT_FULL_SCREEN_TOUCH_ABLE);
                startService(intent);
            }
        });

        findViewById(R.id.btn_not_full_touch_disable).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), FloatWindowService.class);
                intent.setAction(FloatWindowService.ACTION_NOT_FULL_SCREEN_TOUCH_DISABLE);
                startService(intent);
            }
        });

        findViewById(R.id.btn_input).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), FloatWindowService.class);
                intent.setAction(FloatWindowService.ACTION_INPUT);
                startService(intent);
            }
        });

        findViewById(R.id.btn_anim).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), FloatWindowService.class);
                intent.setAction(FloatWindowService.ACTION_ANIM);
                startService(intent);
            }
        });

        findViewById(R.id.btn_touch_follow).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), FloatWindowService.class);
                intent.setAction(FloatWindowService.ACTION_FOLLOW_TOUCH);
                startService(intent);
            }
        });


    }

    private void showOpenPermissionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.no_float_permission);
        builder.setMessage(R.string.go_t0_open_float_ask);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                PermissionUtils.requestOverlayPermission(FloatWindowActivity.this,1000);

                Intent intent = new Intent(getApplicationContext(), FloatWindowService.class);
                intent.setAction(FloatWindowService.ACTION_CHECK_PERMISSION_AND_TRY_ADD);
                startService(intent);
            }
        });

        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Intent intent = new Intent(getApplicationContext(), FloatWindowService.class);
        stopService(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode== 0&&requestCode==1000){

        }
    }
}
