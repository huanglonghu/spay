package com.example.godcode.ui.base;

import android.Manifest;
import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.widget.Toast;
import com.example.godcode.R;
import com.example.godcode.bean.LoginResponse;
import com.example.godcode.greendao.entity.LoginResult;
import com.example.godcode.greendao.gen.DaoMaster;
import com.example.godcode.greendao.gen.DaoSession;
import com.example.godcode.greendao.option.LoginResultOption;
import com.example.godcode.http.HttpUtil;
import com.example.godcode.service.PushIntentService;
import com.example.godcode.utils.GsonUtil;
import com.example.godcode.utils.LogUtil;
import com.example.godcode.utils.RandomUtil;
import com.example.godcode.utils.RemberPsd;
import com.example.godcode.utils.SharepreferenceUtil;
import com.tencent.bugly.crashreport.CrashReport;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import me.weyye.hipermission.HiPermission;
import me.weyye.hipermission.PermissionCallback;
import me.weyye.hipermission.PermissionItem;

public class GodCodeApplication extends Application {
    private DaoSession mDaoSession;
    private SQLiteDatabase db;
    private static GodCodeApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        CrashReport.initCrashReport(getApplicationContext(), "529d5e8f8e", true);
        requestPermission();
        instance = this;
        setDatabase();
        initWindowSize();
        initUmeng();
//        LoginResult loginResult = LoginResultOption.getInstance().getLoginResult();
//        if (loginResult != null) {
//            Constant.uniquenessToken = loginResult.getUniquenessToken();
//        } else {
//            String random = RandomUtil.getInstance().getRandom();
//            Constant.uniquenessToken = random;
//        }
    }

    private void requestPermission() {
        List<PermissionItem> permissonItems = new ArrayList<PermissionItem>();
        permissonItems.add(new PermissionItem(Manifest.permission.CAMERA, "照相机", R.drawable.permission_ic_camera));
        permissonItems.add(new PermissionItem(Manifest.permission.WRITE_EXTERNAL_STORAGE, "SD卡", R.drawable.permission_ic_storage));
        permissonItems.add(new PermissionItem(Manifest.permission.READ_CONTACTS, "联系人", R.drawable.permission_ic_contacts));
        HiPermission.create(this)
                .permissions(permissonItems)
                .checkMutiPermission(new PermissionCallback() {
                    @Override
                    public void onClose() {
                    }

                    @Override
                    public void onFinish() {
                    }

                    @Override
                    public void onDeny(String permission, int position) {
                    }

                    @Override
                    public void onGuarantee(String permission, int position) {

                    }
                });
    }

    private void initUmeng() {
        UMConfigure.init(this, "5b336c2bb27b0a6a4c00005c", "Umeng", UMConfigure.DEVICE_TYPE_PHONE,
                "6ecad4e50e60e9777228adc9d2e4ca91");
        PushAgent mPushAgent = PushAgent.getInstance(this);
        mPushAgent.register(new IUmengRegisterCallback() {
            @Override
            public void onSuccess(String deviceToken) {
                //注册成功会返回device token
                Constant.diviceToken = deviceToken;
                LogUtil.log("--------dd-----" + deviceToken);
            }

            @Override
            public void onFailure(String s, String s1) {
                LogUtil.log(s+"-------faile--------"+s1);
            }
        });
        mPushAgent.setPushIntentServiceClass(PushIntentService.class);
    }

    public void initWindowSize() {
        DisplayMetrics metric = new DisplayMetrics();
        WindowManager mWindowManager = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        mWindowManager.getDefaultDisplay().getMetrics(metric);
        windownWidth = metric.widthPixels;
        windowHeight = metric.heightPixels;
        RemberPsd.getInstance().init(getApplicationContext());
    }

    private int windownWidth;
    private int windowHeight;

    public int getWindownWidth() {
        return windownWidth;
    }

    public int getWindowHeight() {
        return windowHeight;
    }

    public static GodCodeApplication getInstance() {
        return instance;
    }
    /**
     * 设置greenDao
     */
    private void setDatabase() {
        // 通过 DaoMaster 的内部类 DevOpenHelper，你可以得到一个便利的 SQLiteOpenHelper 对象。
        // 可能你已经注意到了，你并不需要去编写「CREATE TABLE」这样的 SQL 语句，因为 greenDAO 已经帮你做了。
        // 注意：默认的 DaoMaster.DevOpenHelper 会在数据库升级时，删除所有的表，意味着这将导致数据的丢失。
        // 所以，在正式的项目中，你还应该做一层封装，来实现数据库的安全升级。
        DaoMaster.DevOpenHelper mHelper = new DaoMaster.DevOpenHelper(this, "notes-db", null);

        db = mHelper.getWritableDatabase();
        // 注意：该数据库连接属于 DaoMaster，所以多个 Session 指的是相同的数据库连接。
        DaoMaster mDaoMaster = new DaoMaster(db);
        mDaoSession = mDaoMaster.newSession();
    }

    public DaoSession getDaoSession() {
        return mDaoSession;
    }

    public SQLiteDatabase getDb() {
        return db;
    }

}
