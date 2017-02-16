package com.example.mygreendaotest;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.mygreendaotest.bean.DaoMaster;
import com.example.mygreendaotest.bean.DaoSession;
import com.example.mygreendaotest.bean.User;
import com.example.mygreendaotest.bean.UserDao;

import java.util.List;

/**
 * Created by gcy on 2017/2/15 0015.
 */
public class Util {
    private UserDao mUserDao;
    private CoordinatorLayout mFuLayout;

    public Util(Context context, CoordinatorLayout fuLayout) {
        mFuLayout = fuLayout;
        DaoMaster.DevOpenHelper devOpenHelper = new DaoMaster.DevOpenHelper(context, "valentine.db", null);
        DaoMaster daoMaster = new DaoMaster(devOpenHelper.getWritableDb());
        DaoSession daoSession = daoMaster.newSession();
        mUserDao = daoSession.getUserDao();
    }

    public void addUser(String username, String phone) {
        //id自增
        User user = new User(null, username, phone);
        mUserDao.insert(user);
    }

    public void deleteByUser(User user) {
        if (user == null) {
            Snackbar.make(mFuLayout, "该用户不存在", 1).show();
        } else {
            mUserDao.deleteByKey(user.getId());
        }
    }

    public void deleteById(long id) {
//        这里我是查询id小于等于10的数据，where中的参数可以有多个，就是说可以添加多个查询条件
//        List<User> userList = mUserDao.queryBuilder().where(UserDao.Properties.Id.le(10)).build().list();
//        for (User user : userList) {
//            mUserDao.delete(user);
//        }
//        将表中所有数据一次删除
//        userDao.deleteAll();
        User user = mUserDao.queryBuilder().where(UserDao.Properties.Id.eq(id)).build().unique();
        if (user == null) {
            Snackbar.make(mFuLayout, "该用户不存在", 1).show();
        } else {
            mUserDao.deleteByKey(user.getId());
        }
    }

    public void updateByUser(User user, String username, String phonenum) {
        if (user == null) {
            Snackbar.make(mFuLayout, "该用户不存在", 1).show();
        } else {
            user.setUsername(username);
            user.setPhone(phonenum);
            mUserDao.update(user);
        }
    }

    public void updateById(long id, String theOtherUserName) {
//      一个是id要大于等于10，同是还要满足username like %90%，注意最后的unique表示只查询一条数据出来即可。
//        User user = mUserDao.queryBuilder()
//                .where(UserDao.Properties.Id.ge(10), UserDao.Properties.Username.like("%90%")).build().unique();
        User user = mUserDao.queryBuilder().where(UserDao.Properties.Id.eq(id)).build().unique();
        if (user == null) {
            Snackbar.make(mFuLayout, "该用户不存在", 1).show();
        } else {
            user.setUsername(theOtherUserName);
            mUserDao.update(user);
        }
    }

    public List<User> selectAll() {
//        between表示查询id介于2到13之间的数据，limit表示查询5条数据。
//        List<User> list = mUserDao.queryBuilder()
//                .where(UserDao.Properties.Id.between(2, 13)).limit(5).build().list();
        List<User> list = mUserDao.queryBuilder().orderAsc(UserDao.Properties.Username).build().list();
        return list;
    }
}
