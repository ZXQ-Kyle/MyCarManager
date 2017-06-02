package com.kyle.mycar;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import com.kyle.mycar.Bean.MessageEvent;
import com.kyle.mycar.Bean.MsgMainFragment;
import com.kyle.mycar.Fragment.ChartFragment;
import com.kyle.mycar.Fragment.MainFragment;
import com.kyle.mycar.Fragment.MaintenanceFragment;
import com.kyle.mycar.Fragment.OilFragment;
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
import java.util.LinkedList;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private Toolbar mToolbar;
    public DrawerLayout drawer;
    public LinkedList<Fragment> mFrgBackList;

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

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        // TODO: 2017/5/27 旋转，加油等fragment不保存
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
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
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
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
                getSupportFragmentManager().beginTransaction().show(mFrgBackList.get(1)).remove(mFrgBackList.get(0)).commit();
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
            switchFrag(fromFrag.getClass(),MainFragment.class,false);
        } else if (id == R.id.nav_chart) {
            switchFrag(fromFrag.getClass(),ChartFragment.class,false);
        } else if (id == R.id.nav_oil) {

        } else if (id == R.id.nav_expenses) {

        } else if (id == R.id.nav_setting) {

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


    /** fragment 跳转逻辑封装
     * @param fromClass
     * @param toClass
     * @param isRemove 是否移除from的fragment,false hide,true remove
     * @param obj 为toFrag 赋值，用于点击条目修改已有数据时跳转
     */
    public void switchFrag(Class<? extends Fragment> fromClass, Class<? extends Fragment> toClass, boolean isRemove,Object obj) {
        if (fromClass==toClass) return;
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
            if (null!=obj){
                if (obj instanceof Oil){
                    ((OilFragment)toFrag).oil=(Oil)obj;
                }else {
                    ((MaintenanceFragment)toFrag).mt=(Maintenance) obj;
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
            if (null!=obj){
                if (obj instanceof Oil){
                    ((OilFragment)toFrag).oil=(Oil)obj;
                }else {
                    ((MaintenanceFragment)toFrag).mt=(Maintenance) obj;
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
            if (indexOf > -1){
                mFrgBackList.remove(indexOf);
                mFrgBackList.addFirst(toFrag);
            }else {
                mFrgBackList.addFirst(toFrag);
            }
        }
    }
    public void switchFrag(Class<? extends Fragment> fromClass, Class<? extends Fragment> toClass, boolean isRemove) {
        switchFrag(fromClass,toClass,isRemove,null);
    }

}
