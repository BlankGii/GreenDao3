package com.example.mygreendaotest;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;

import com.example.mygreendaotest.bean.User;
import com.example.mygreendaotest.swipemenulistview.SwipeMenu;
import com.example.mygreendaotest.swipemenulistview.SwipeMenuCreator;
import com.example.mygreendaotest.swipemenulistview.SwipeMenuItem;
import com.example.mygreendaotest.swipemenulistview.SwipeMenuListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    //数据库
    private Util util;
    //对话框
    private AlertDialog mAlertDialogADD;
    private AlertDialog mAlertDialogUPDATE;
    private EditText editText1;
    private EditText editText2;
    private EditText u_editText1;
    private EditText u_editText2;
    private View dialog;
    private View dialog2;
    //更新-上一对象
    private User lastUser;
    //展示
    @Bind(R.id.listView)
    SwipeMenuListView mListView;
    private List<User> data = new ArrayList<>();
    private MyAdapter mMyAdapter;
    //design
    @Bind(R.id.fuLayout)
    CoordinatorLayout fuLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        util = new Util(this, fuLayout);
        dialog = LayoutInflater.from(MainActivity.this).inflate(R.layout.dialog,
                (ViewGroup) findViewById(R.id.dialog));
        dialog2 = LayoutInflater.from(MainActivity.this).inflate(R.layout.dialog2,
                (ViewGroup) findViewById(R.id.dialog));
        editText1 = (EditText) dialog.findViewById(R.id.et_1);
        editText2 = (EditText) dialog.findViewById(R.id.et_2);
        u_editText1 = (EditText) dialog2.findViewById(R.id.et_1);
        u_editText2 = (EditText) dialog2.findViewById(R.id.et_2);
        data = util.selectAll();
        mMyAdapter = new MyAdapter(this, data);
        mListView.setAdapter(mMyAdapter);
        SwipeMenuCreator creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                SwipeMenuItem openItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                openItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9,
                        0xCE)));
                // set item width
                openItem.setWidth(dp2px(90));
                // set item title
                openItem.setTitle("Hello");
                // set item title fontsize
                openItem.setTitleSize(18);
                // set item title font color
                openItem.setTitleColor(Color.WHITE);
                // add to menu
                menu.addMenuItem(openItem);
                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                        0x3F, 0x25)));
                // set item width
                deleteItem.setWidth(dp2px(90));
                // set a icon
                deleteItem.setIcon(R.drawable.ic_delete);
                // add to menu
                menu.addMenuItem(deleteItem);
            }
        };
        mListView.setMenuCreator(creator);
        mListView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public void onMenuItemClick(int position, SwipeMenu menu, int index) {
                User user = data.get(position);
                switch (index) {
                    case 0:
                        // open
                        snackbarShow("I just say Hello,man");
                        break;
                    case 1:
                        // delete
                        util.deleteByUser(user);
                        checkDataFromDao();
                        break;
                }
            }
        });
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                lastUser = data.get(position);
                u_editText1.setText(lastUser.getUsername());
                u_editText2.setText(lastUser.getPhone());
                //移动光标
                u_editText1.setSelection(lastUser.getUsername().length());
                u_editText2.setSelection(lastUser.getPhone().length());
                mAlertDialogUPDATE.show();
                return false;
            }
        });
        createDialog1();
        createDialog2();
    }

    private void createDialog1() {
        mAlertDialogADD = new AlertDialog.Builder(this).setTitle("增加数据").setView(dialog).setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String inputUserName = editText1.getText() + "";
                String inputPhone = editText2.getText() + "";
                if (!inputUserName.equals("")) {
                    util.addUser(inputUserName, inputPhone);
                } else {
                    snackbarShow("userName为空不能添加");
                }
                checkDataFromDao();
                editText1.setText("");
                editText2.setText("");
                //获取焦点
                editText1.requestFocus();
                //失去焦点
//                editText1.clearFocus();
            }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                mAlertDialogADD.dismiss();
            }
        }).create();
    }

    private void createDialog2() {
        mAlertDialogUPDATE = new AlertDialog.Builder(this).setTitle("修改数据").setView(dialog2).setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String inputUserName = u_editText1.getText() + "";
                String inputPhone = u_editText2.getText() + "";
                if (!inputUserName.equals("")) {
                    util.updateByUser(lastUser, inputUserName, inputPhone);
                } else {
                    snackbarShow("userName为空不能添加");
                }
                checkDataFromDao();
                u_editText1.setText("");
                u_editText2.setText("");
            }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                mAlertDialogUPDATE.dismiss();
            }
        }).create();
    }

    private void checkDataFromDao() {
        data.clear();
        data.addAll(util.selectAll());
        mMyAdapter.notifyDataSetChanged();
    }

    @OnClick(R.id.fab)
    void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:
                mAlertDialogADD.show();
                break;
        }
    }

    private void snackbarShow(String msg) {
        Snackbar.make(fuLayout, msg, Snackbar.LENGTH_SHORT).show();
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }

}
