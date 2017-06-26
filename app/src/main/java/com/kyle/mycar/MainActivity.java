package com.kyle.mycar;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.transition.Fade;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.feedback.FeedbackAgent;
import com.kyle.mycar.Bean.MessageEvent;
import com.kyle.mycar.Fragment.AboutFragment;
import com.kyle.mycar.Fragment.ChartFragment;
import com.kyle.mycar.Fragment.MainFragment;
import com.kyle.mycar.Fragment.QueryExpenseFragment;
import com.kyle.mycar.Fragment.QueryOilFragment;
import com.kyle.mycar.Fragment.SettingFragment;
import com.kyle.mycar.MyUtils.MyConstant;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public DrawerLayout drawer;
    public LinkedList<Fragment> mFrgBackList;
    public ExecutorService mThreadPool = Executors.newFixedThreadPool(5);
    private Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setEnterTransition(new Fade().setDuration(300));
            getWindow().setExitTransition(new Fade().setDuration(300));
        }
        setContentView(R.layout.activity_main);
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

        initNav();
        initView();
//        if (NetWorkUtils.isWifiByType(this)) {
            checkUpdate();
//        }
    }

    private void checkUpdate() {
        mThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                PackageInfo info = null;
                int present = 0;
                int target = 0;
                try {
                    info = getPackageManager().getPackageInfo(getPackageName(), 0);
                    present = info.versionCode;

                    AVObject avObject = new AVQuery("versionCode").getFirst();
                    target = avObject.getInt("versionCode");

                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }

                if (target > present) {
                    //开启更新
                    EventBus.getDefault().post(new MessageEvent(MyConstant.APP_UPDATE));
                }
            }
        });
    }

    private void initNav() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        menu = navigationView.getMenu();
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);
        CircleImageView headImage = (CircleImageView) headerView.findViewById(R.id.nav_header_head);
        initHeadImage(headImage);
    }

    public void initHeadImage(CircleImageView headImage) {
        FileInputStream is = null;
        try {
            is = openFileInput("head.png");
            headImage.setImageBitmap(BitmapFactory.decodeStream(is));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            headImage.setImageResource(R.drawable.head);
        } finally {
            if (null != is) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void initView() {
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
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
                Fragment fragment = mFrgBackList.get(0);
                if (fragment instanceof MainFragment) {
                    menu.getItem(0).setChecked(true);
                } else if (fragment instanceof ChartFragment) {
                    menu.getItem(1).setChecked(true);
                } else if (fragment instanceof QueryOilFragment) {
                    menu.getItem(2).setChecked(true);
                } else if (fragment instanceof QueryExpenseFragment) {
                    menu.getItem(3).setChecked(true);
                }
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
            switchFrag(fromFrag.getClass(), QueryOilFragment.class, false);
        } else if (id == R.id.nav_expense) {
            switchFrag(fromFrag.getClass(), QueryExpenseFragment.class, false);
        } else if (id == R.id.nav_setting) {
            switchFrag(fromFrag.getClass(), SettingFragment.class, false);
        } else if (id == R.id.nav_about) {
            switchFrag(fromFrag.getClass(), AboutFragment.class, false);
        } else if (id == R.id.nav_back) {
            FeedbackAgent agent = new FeedbackAgent(this);
            agent.startDefaultThreadActivity();
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void msg(MessageEvent msg) {
        switch (msg.getFlag()) {
            case MyConstant.UPDATE_HEAD_IMAGE:
                initNav();
                break;

            case MyConstant.OPEN_DRAWER:
                drawer.openDrawer(GravityCompat.START);
                break;
            case MyConstant.APP_UPDATE:

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle(R.string.app_update)
                        .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
//                                //打开网页更新
//                                Intent intent = new Intent();
//                                intent.setAction("android.intent.action.VIEW");
//                                Uri content_url = Uri.parse("http://fir.im/carM");
//                                intent.setData(content_url);
//                                startActivity(intent);
                                //开启服务更新



                            }
                        }).setNegativeButton(R.string.cancel, null).show();



                break;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    //fragment 中的申请也是反馈到父activity的方法中
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[]
            grantResults) {
        if (requestCode == SettingFragment.RQ_CODE_REQUEST_PERMISSIONS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager
                    .PERMISSION_GRANTED) {
                Fragment fragment = mFrgBackList.get(0);
                if (fragment instanceof SettingFragment) {
                    ((SettingFragment) fragment).goPickPicture();
                }
            } else {
                Toast.makeText(this, R.string.permission_fail, Toast.LENGTH_LONG).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    /**
     * fragment 跳转逻辑封装
     *
     * @param fromClass
     * @param toClass
     * @param isRemove  是否移除from的fragment,false hide,true remove
     */
    public void switchFrag(Class<? extends Fragment> fromClass, Class<? extends Fragment> toClass, boolean isRemove) {
        if (fromClass == toClass) return;
        Fragment fromFrag = getSupportFragmentManager().findFragmentByTag(fromClass.getSimpleName());
        Fragment toFrag = getSupportFragmentManager().findFragmentByTag(toClass.getSimpleName());
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        if (null == fromFrag) try {
            fromFrag = fromClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (null == toFrag) {
            try {
                toFrag = toClass.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        switchFrag(fromFrag, toFrag, isRemove, transaction);
    }

    /**
     * @param fromFrag
     * @param toFrag
     * @param isRemove    是否移除from的fragment,false hide,true remove
     * @param transaction 可以为null；
     */
    public void switchFrag(Fragment fromFrag, Fragment toFrag, boolean isRemove, FragmentTransaction transaction) {
        if (fromFrag == toFrag || null == toFrag) return;
        if (null == transaction) {
            transaction = getSupportFragmentManager().beginTransaction();
        }
        boolean hidden = toFrag.isHidden();
        transaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out);
        if (isRemove) {
            if (hidden) {
                transaction.remove(fromFrag).show(toFrag).commit();
                mFrgBackList.remove(0);
            } else {
                transaction.remove(fromFrag).add(R.id.fl_content, toFrag, toFrag.getClass().getSimpleName()).commit();
                mFrgBackList.remove(0);
            }
        } else {
            if (hidden) {
                transaction.hide(fromFrag).show(toFrag).commit();
            } else {
                transaction.hide(fromFrag).add(R.id.fl_content, toFrag, toFrag.getClass().getSimpleName()).commit();
            }
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

    public void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public void showKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);
    }

    public void showSnackBar(int strRes) {
        this.showSnackBar(strRes, Snackbar.LENGTH_SHORT);
    }

    public void showSnackBar(int strRes, int duration) {
        Snackbar.make(getWindow().getDecorView(), strRes, duration).show();
    }

//
//    @Override
//    public void onClick(View v) {
//        //点击更换头像
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager
//                .PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest
//                    .permission.WRITE_EXTERNAL_STORAGE}, RQ_CODE_REQUEST_PERMISSIONS);
//        } else {
//            goPickPicture();
//        }
//    }
//
//    public void goPickPicture() {
//        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        startActivityForResult(intent, RQ_CODE_PICK_HEAD_IMAGE);
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[]
//            grantResults) {
//        if (requestCode == RQ_CODE_REQUEST_PERMISSIONS) {
//            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager
//                    .PERMISSION_GRANTED) {
//                Snackbar.make(getWindow().getDecorView(), "权限获取成功", Snackbar.LENGTH_LONG).show();
//                goPickPicture();
//            } else {
//                Snackbar.make(getWindow().getDecorView(), "权限获取失败", Snackbar.LENGTH_LONG).show();
//            }
//        }
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (null == data) return;
//        if (requestCode == RQ_CODE_PICK_HEAD_IMAGE) {
//            Uri uri = data.getData();
//            //剪裁
//            //构建隐式Intent来启动裁剪程序
//            Intent intent = new Intent("com.android.camera.action.CROP");
//            //设置数据uri和类型为图片类型
//            intent.setDataAndType(uri, "image/*");
//            //显示View为可裁剪的
//            intent.putExtra("crop", true);
//            //裁剪的宽高的比例为1:1
//            intent.putExtra("aspectX", 1);
//            intent.putExtra("aspectY", 1);
//            //输出图片的宽高均为150
//            intent.putExtra("outputX", 150);
//            intent.putExtra("outputY", 150);
//            //裁剪之后的数据是通过Intent返回
//            intent.putExtra("return-data", true);
//            startActivityForResult(intent, RQ_CODE_CROP_PICTURE);
//        }
//        if (requestCode == RQ_CODE_CROP_PICTURE) {
//            Bundle extras = data.getExtras();
//            if (extras != null) {
//                //获取到裁剪后的图像
//                Bitmap bm = extras.getParcelable("data");
//                try {
//                    //打开文件输出流
//                    FileOutputStream fos = openFileOutput("head.png", Context.MODE_PRIVATE);
//                    //将bitmap压缩后写入输出流(参数依次为图片格式、图片质量和输出流)
//                    bm.compress(Bitmap.CompressFormat.PNG, 85, fos);
//                    //刷新输出流
//                    fos.flush();
//                    //关闭输出流
//                    fos.close();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//                SpUtils.putboolean(this.getApplicationContext(), "hasHeadImage", true);
//
//                NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
//                View headerView = navigationView.getHeaderView(0);
//                CircleImageView headImage = (CircleImageView) headerView.findViewById(R.id.nav_header_head);
////                Glide.with(this).load(bm).into(headImage);
//                headImage.setImageBitmap(bm);
//            }
//        }
//    }


}
