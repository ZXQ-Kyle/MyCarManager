package com.kyle.mycar;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.bumptech.glide.Glide;
import com.kyle.mycar.Bean.MessageEvent;
import com.kyle.mycar.Bean.MsgMainFragment;
import com.kyle.mycar.Fragment.ChartFragment;
import com.kyle.mycar.Fragment.MainFragment;
import com.kyle.mycar.Fragment.MaintenanceFragment;
import com.kyle.mycar.Fragment.OilFragment;
import com.kyle.mycar.Fragment.SettingFragment;
import com.kyle.mycar.MyUtils.MyConstant;
import com.kyle.mycar.MyUtils.SpUtils;
import com.kyle.mycar.db.Dao.MtTagDao;
import com.kyle.mycar.db.Dao.OilTypeDao;
import com.kyle.mycar.db.Table.Maintenance;
import com.kyle.mycar.db.Table.MtTag;
import com.kyle.mycar.db.Table.Oil;
import com.kyle.mycar.db.Table.OilType;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View
        .OnClickListener {

    public static final int RQ_CODE_PICK_HEAD_IMAGE = 99;
    public static final int RQ_CODE_REQUEST_PERMISSIONS = 98;
    private static final int RQ_CODE_CROP_PICTURE = 97;
    private Toolbar mToolbar;
    public DrawerLayout drawer;
    public LinkedList<Fragment> mFrgBackList;
    public ExecutorService mThreadPool = Executors.newFixedThreadPool(5);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | WindowManager.LayoutParams
                    .FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View
                    .SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
            window.setNavigationBarColor(getResources().getColor(R.color.b2));
        }

        initData();
        initView();
        if (!SpUtils.getboolean(this.getApplicationContext(), MyConstant.First_IN)) {
            mThreadPool.execute(new initDb(this.getApplicationContext()));
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        // TODO: 2017/5/27 旋转，加油等fragment不保存
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    private void initData() {
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);
        CircleImageView headImage = (CircleImageView) headerView.findViewById(R.id.nav_header_head);

        initHeadImage(headImage);

        headImage.setOnClickListener(this);
    }

    public void initHeadImage(CircleImageView headImage) {
        boolean image = SpUtils.getboolean(this.getApplicationContext(), "hasHeadImage");
        FileInputStream is = null;
        try {
            is = openFileInput("head.png");
            if (image && null != is) {
                headImage.setImageBitmap(BitmapFactory.decodeStream(is));
            } else {
                headImage.setImageResource(R.drawable.head);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void initView() {
        MainFragment fragment = new MainFragment();
        if (mFrgBackList == null) {
            mFrgBackList = new LinkedList<>();
        }
        mFrgBackList.add(0, fragment);
        getSupportFragmentManager().beginTransaction().add(R.id.fl_content, fragment, MainFragment.class
                .getSimpleName()).commit();
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (mFrgBackList.size() > 1) {
                getSupportFragmentManager().beginTransaction().show(mFrgBackList.get(1)).remove(mFrgBackList.get(0))
                        .commit();
                mFrgBackList.remove(0);
            } else {
                super.onBackPressed();
            }
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        Fragment fromFrag = mFrgBackList.get(0);

        int id = item.getItemId();
        if (id == R.id.nav_history) {
            switchFrag(fromFrag.getClass(), MainFragment.class, false);
        } else if (id == R.id.nav_chart) {
            switchFrag(fromFrag.getClass(), ChartFragment.class, false);
        } else if (id == R.id.nav_oil) {

        } else if (id == R.id.nav_setting) {
            switchFrag(fromFrag.getClass(), SettingFragment.class, false);
        } else if (id == R.id.nav_about) {

        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void msg(MsgMainFragment msg) {
        if (msg.getFlag() == MsgMainFragment.UPDATE_AN_NEW_ONE_DATA) {
            mFrgBackList.remove(0);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void msg(MessageEvent msg) {
        switch (msg.getFlag()) {
            case MyConstant.SET_TOOLBAR:
                break;

            case MyConstant.OPEN_DRAWER:
                drawer.openDrawer(GravityCompat.START);
                break;

        }

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


    /**
     * fragment 跳转逻辑封装
     *
     * @param fromClass
     * @param toClass
     * @param isRemove  是否移除from的fragment,false hide,true remove
     * @param obj       为toFrag 赋值，用于点击条目修改已有数据时跳转
     */
    public void switchFrag(Class<? extends Fragment> fromClass, Class<? extends Fragment> toClass, boolean isRemove,
                           Object obj) {
        if (fromClass == toClass) return;
        Fragment fromFrag = getSupportFragmentManager().findFragmentByTag(fromClass.getSimpleName());
        Fragment toFrag = getSupportFragmentManager().findFragmentByTag(toClass.getSimpleName());
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        if (fromFrag == null) try {
            fromFrag = fromClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (toFrag == null) {
            try {
                toFrag = toClass.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (null != obj) {
                if (obj instanceof Oil) {
                    ((OilFragment) toFrag).oil = (Oil) obj;
                } else {
                    ((MaintenanceFragment) toFrag).mt = (Maintenance) obj;
                }
            }
            if (isRemove) {
                transaction.remove(fromFrag).add(R.id.fl_content, toFrag, toClass.getSimpleName()).commit();
                mFrgBackList.remove(0);
            } else {
                transaction.hide(fromFrag).add(R.id.fl_content, toFrag, toClass.getSimpleName()).commit();
            }
            //add frag,保存到回退栈
            mFrgBackList.addFirst(toFrag);
        } else {
            if (null != obj) {
                if (obj instanceof Oil) {
                    ((OilFragment) toFrag).oil = (Oil) obj;
                } else {
                    ((MaintenanceFragment) toFrag).mt = (Maintenance) obj;
                }
            }
            if (isRemove) {
                transaction.remove(fromFrag).show(toFrag).commit();
                mFrgBackList.remove(0);
            } else {
                transaction.hide(fromFrag).show(toFrag).commit();
            }
            //show frag，保存到回退栈
            int indexOf = mFrgBackList.indexOf(toFrag);
            if (indexOf > -1) {
                mFrgBackList.remove(indexOf);
                mFrgBackList.addFirst(toFrag);
            } else {
                mFrgBackList.addFirst(toFrag);
            }
        }
    }

    public void switchFrag(Class<? extends Fragment> fromClass, Class<? extends Fragment> toClass, boolean isRemove) {
        switchFrag(fromClass, toClass, isRemove, null);
    }

    @Override
    public void onClick(View v) {
        //点击更换头像
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager
                .PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest
                    .permission.WRITE_EXTERNAL_STORAGE}, RQ_CODE_REQUEST_PERMISSIONS);
        } else {
            goPickPicture();
        }
    }

    public void goPickPicture() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, RQ_CODE_PICK_HEAD_IMAGE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[]
            grantResults) {
        if (requestCode == RQ_CODE_REQUEST_PERMISSIONS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager
                    .PERMISSION_GRANTED) {
                Snackbar.make(getWindow().getDecorView(), "权限获取成功", Snackbar.LENGTH_LONG).show();
                goPickPicture();
            } else {
                Snackbar.make(getWindow().getDecorView(), "权限获取失败", Snackbar.LENGTH_LONG).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (null == data) return;
        if (requestCode == RQ_CODE_PICK_HEAD_IMAGE) {
            Uri uri = data.getData();
            //剪裁
            //构建隐式Intent来启动裁剪程序
            Intent intent = new Intent("com.android.camera.action.CROP");
            //设置数据uri和类型为图片类型
            intent.setDataAndType(uri, "image/*");
            //显示View为可裁剪的
            intent.putExtra("crop", true);
            //裁剪的宽高的比例为1:1
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
            //输出图片的宽高均为150
            intent.putExtra("outputX", 150);
            intent.putExtra("outputY", 150);
            //裁剪之后的数据是通过Intent返回
            intent.putExtra("return-data", true);
            startActivityForResult(intent, RQ_CODE_CROP_PICTURE);
        }
        if (requestCode == RQ_CODE_CROP_PICTURE) {
            Bundle extras = data.getExtras();
            if (extras != null) {
                //获取到裁剪后的图像
                Bitmap bm = extras.getParcelable("data");
                try {
                    //打开文件输出流
                    FileOutputStream fos = openFileOutput("head.png", Context.MODE_PRIVATE);
                    //将bitmap压缩后写入输出流(参数依次为图片格式、图片质量和输出流)
                    bm.compress(Bitmap.CompressFormat.PNG, 85, fos);
                    //刷新输出流
                    fos.flush();
                    //关闭输出流
                    fos.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                SpUtils.putboolean(this.getApplicationContext(), "hasHeadImage", true);

                NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
                View headerView = navigationView.getHeaderView(0);
                CircleImageView headImage = (CircleImageView) headerView.findViewById(R.id.nav_header_head);
//                Glide.with(this).load(bm).into(headImage);
                headImage.setImageBitmap(bm);
            }
        }
    }


    private static class initDb implements Runnable {
        private Context mContext;

        public initDb(Context context) {
            mContext = context;
        }

        @Override
        public void run() {
            OilTypeDao typeDao = OilTypeDao.getInstance(mContext);
            String[] strings = mContext.getResources().getStringArray(R.array.spinner_oil);
            for (int i = 0; i < strings.length; i++) {
                int add = typeDao.add(new OilType(strings[i]));
            }

            MtTagDao tagDao = MtTagDao.getInstance(mContext);
            String[] stringArray = mContext.getResources().getStringArray(R.array.tags);
            for (int i = 0; i < stringArray.length; i++) {
                tagDao.add(new MtTag(stringArray[i]));
            }

            SpUtils.putboolean(mContext, MyConstant.First_IN, true);
        }
    }

}
