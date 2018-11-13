package com.example.godcode.greendao.option;

import com.example.godcode.greendao.entity.User;
import com.example.godcode.greendao.entity.VersionMsg;
import com.example.godcode.greendao.gen.UserDao;
import com.example.godcode.greendao.gen.VersionMsgDao;
import com.example.godcode.ui.base.GodCodeApplication;
import com.example.godcode.utils.LogUtil;

import java.util.List;

import static com.example.godcode.BR.user;

/**
 * Created by Administrator on 2018/10/31.
 */

public class VersionMsgOption {
    private VersionMsgOption() {
    }

    private static VersionMsgOption defaultInstance;

    public static VersionMsgOption getInstance() {

        VersionMsgOption userOption = defaultInstance;
        if (defaultInstance == null) {
            synchronized (UserOption.class) {
                if (defaultInstance == null) {
                    userOption = new VersionMsgOption();
                    defaultInstance = userOption;
                }
            }
        }
        return userOption;
    }


    public VersionMsg querryVersion() {
        VersionMsgDao versionMsgDao = GodCodeApplication.getInstance().getDaoSession().getVersionMsgDao();
        List<VersionMsg> versionMsgs = versionMsgDao.loadAll();
        if (versionMsgs.size() > 0) {
            return versionMsgs.get(0);
        } else {
            return null;
        }

    }

    public void updateVersion(String versionCode, String versionDes) {
        VersionMsgDao versionMsgDao = GodCodeApplication.getInstance().getDaoSession().getVersionMsgDao();
        versionMsgDao.deleteAll();
        VersionMsg versionMsg = new VersionMsg(versionCode, versionDes);
        versionMsgDao.insert(versionMsg);
    }
}
