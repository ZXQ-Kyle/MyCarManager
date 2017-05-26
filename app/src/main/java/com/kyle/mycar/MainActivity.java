package com.kyle.mycar;

import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.util.ArrayMap;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.animation.AlphaAnimation;

import com.github.clans.fab.FloatingActionMenu;
import com.kyle.mycar.Bean.MsgMainFragment;
import com.kyle.mycar.Fragment.BaseFragment;
import com.kyle.mycar.Fragment.MainFragment;
import com.kyle.mycar.Fragment.MaintenanceFragment;
import com.kyle.mycar.Fragment.OilFragment;
import com.kyle.mycar.MyUtils.MyConstant;
import com.kyle.mycar.MyUtils.SpUtils;
import com.kyle.mycar.db.Dao.MtTagDao;
import com.kyle.mycar.db.Dao.OilTypeDao;
import com.kyle.mycar.db.Table.MtTag;
import com.kyle.mycar.db.Table.OilType;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public static final String OILFRAGMENT = "OilFragment";
    public static final String MAIN_FRAGMENT = "MainFragment";
    @BindView(R.id.fab_menu)
    FloatingActionMenu fabMenu;

    private Toolbar mToolbar;
    private DrawerLayout drawer;
    public ArrayList<BaseFragment> mFrgBackList;
    public ArrayMap<String,BaseFragment> mFrgMap = new ArrayMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);

        initData();
        initView();
        new Thread() {
            @Override
            public void run() {
                initDb();
            }
        }.start();
    }

    private void initDb() {
        if (!SpUtils.getboolean(this, MyConstant.First_IN)) {
            OilTypeDao typeDao = OilTypeDao.getInstance(this);
            String[] strings = getResources().getStringArray(R.array.spinner_oil);
            for (int i = 0; i < strings.length; i++) {
                int add = typeDao.add(new OilType(strings[i]));
            }

            MtTagDao tagDao = MtTagDao.getInstance(this);
            String[] stringArray = getResources().getStringArray(R.array.tags);
            for (int i = 0; i < stringArray.length; i++) {
                tagDao.add(new MtTag(stringArray[i]));
            }

            SpUtils.putboolean(this, MyConstant.First_IN, true);
        }

    }


    private void initData() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, mToolbar, R.string
                .navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void initView() {
        MainFragment fragment = new MainFragment();
        if (mFrgBackList == null) {
            mFrgBackList = new ArrayList();
        }
        mFrgBackList.add(0,fragment);
        mFrgMap.put(MAIN_FRAGMENT,fragment);
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_content, fragment, MAIN_FRAGMENT).commit();
        getSupportActionBar().setTitle(R.string.history);
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Logger.d(mFrgBackList.size());
            if (mFrgBackList.size()>1){
                getSupportFragmentManager().beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).show(mFrgBackList.get(1))
                        .remove(mFrgBackList.get(0)).commit();
                mFrgBackList.remove(0);
                getFabMenu();
            }else {
                super.onBackPressed();
            }
        }
    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//        //noinspection SimplifiableIfStatement
//        if (id == android.R.id.home) {
//            Toast.makeText(MainActivity.this, "hahahhahaha", Toast.LENGTH_SHORT).show();
//            return true;
//        }
//        if (id == R.id.action_settings) {
//            Toast.makeText(MainActivity.this, "22222222222222", Toast.LENGTH_SHORT).show();
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();
        BaseFragment fragment = null;
        if (id == R.id.nav_history) {
            mToolbar.setTitle(R.string.history);

        } else if (id == R.id.nav_statistics) {
            mToolbar.setTitle(R.string.statistics);
        } else if (id == R.id.nav_oil) {

        } else if (id == R.id.nav_expenses) {

        } else if (id == R.id.nav_setting) {

        } else if (id == R.id.nav_about) {

        }
        fabMenu.setVisibility(View.VISIBLE);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //  浮动按钮点击事件
    @OnClick({R.id.fab_1, R.id.fab_2})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.fab_1:
                OilFragment fragment = new OilFragment();
                getSupportFragmentManager().beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .add(R.id.fl_content,fragment,OILFRAGMENT).hide
                        (mFrgMap.get(MAIN_FRAGMENT)).commit();
                mFrgBackList.add(0,fragment);
                setToolbarColor(R.string.oil, getResources().getColor(R.color.colorCyan), getResources().getColor(R
                        .color.colorCyanDark));

                break;
            case R.id.fab_2:
                MaintenanceFragment mtFragment = new MaintenanceFragment();
                getSupportFragmentManager().beginTransaction().add(R.id.fl_content,mtFragment)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).hide
                        (mFrgMap.get(MAIN_FRAGMENT)).commit();
                mFrgBackList.add(0,mtFragment);
                setToolbarColor(R.string.maintenance, getResources().getColor(R.color.colorPurple), getResources()
                        .getColor(R.color.colorPurpleDark));
                break;
//            case R.id.fab_3:
////                ExpenseFragment exFragment = new ExpenseFragment();
////                getSupportFragmentManager().beginTransaction().replace(R.id.fl_content, exFragment)
////                        .addToBackStack(null).commit();
//                mToolbar.setTitle(R.string.expense);
//                mToolbar.setBackgroundColor(getResources().getColor(R.color.colorAmber));
//
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                    Window window = getWindow();
//                    window.setStatusBarColor(getResources().getColor(R.color.colorAmberDark));
//                    window.setNavigationBarColor(getResources().getColor(R.color.colorAmberDark));
//                }
//                break;
        }

        fabMenu.close(true);
        fabMenu.setVisibility(View.GONE);

    }

    public void setToolbarColor(int toolbarString, int color, int color2) {
        mToolbar.setTitle(toolbarString);
        mToolbar.setBackgroundColor(color);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.setStatusBarColor(color2);
            window.setNavigationBarColor(color2);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void msg(MsgMainFragment msg) {
        if (msg.getFlag()==MsgMainFragment.UPDATE_AN_NEW_ONE_DATA){
            getFabMenu();
            mFrgBackList.remove(0);
        }
    }


    public void getFabMenu() {
        AlphaAnimation animation =new AlphaAnimation(0,1);
        animation.setDuration(1000);
        fabMenu.setVisibility(View.VISIBLE);
        fabMenu.startAnimation(animation);
        setToolbarColor(R.string.history, getResources().getColor(R.color.colorPrimary), getResources().getColor(R
                .color.colorPrimaryDark));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
