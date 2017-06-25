package com.kyle.mycar.Fragment;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.kyle.mycar.Bean.MessageEvent;
import com.kyle.mycar.MyUtils.MyConstant;
import com.kyle.mycar.R;
import org.greenrobot.eventbus.EventBus;
import java.io.FileOutputStream;
import butterknife.BindView;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingFragment extends BaseFragment implements View.OnClickListener {

    public static final int RQ_CODE_PICK_HEAD_IMAGE = 99;
    public static final int RQ_CODE_REQUEST_PERMISSIONS = 98;
    private static final int RQ_CODE_CROP_PICTURE = 97;

    @BindView(R.id.head)
    CircleImageView head;
    @BindView(R.id.setting_head_tv)
    TextView settingHeadTv;
    Unbinder unbinder;

    @Override
    public View initView() {
        noEventBus = true;
        View view = View.inflate(mActivity, R.layout.fragment_setting, null);
        View view1 = view.findViewById(R.id.setting_1);
        View view2 = view.findViewById(R.id.setting_2);
        View view3 = view.findViewById(R.id.setting_3);
        ImageView iv1 = (ImageView) view1.findViewById(R.id.it_iv_setting);
        TextView tv1 = (TextView) view1.findViewById(R.id.it_tv_setting);
        ImageView iv2 = (ImageView) view2.findViewById(R.id.it_iv_setting);
        TextView tv2 = (TextView) view2.findViewById(R.id.it_tv_setting);
        ImageView iv3 = (ImageView) view3.findViewById(R.id.it_iv_setting);
        TextView tv3 = (TextView) view3.findViewById(R.id.it_tv_setting);

        iv1.setImageResource(R.drawable.ic_car);
        iv2.setImageResource(R.drawable.oilcan);
        iv3.setImageResource(R.drawable.ic_credit_card);
//        Glide.with(this).load(R.drawable.ic_menu_camera).into(iv1);
//        Glide.with(this).load(R.drawable.ic_menu_camera).into(iv2);
//        Glide.with(this).load(R.drawable.ic_menu_camera).into(iv3);

        String[] strings = getResources().getStringArray(R.array.settings);
        tv1.setText(strings[0]);
        tv2.setText(strings[1]);
        tv3.setText(strings[2]);

        view1.setOnClickListener(this);
        view2.setOnClickListener(this);
        view3.setOnClickListener(this);
        return view;
    }

    @Override
    public void initData() {
//        initToolbar(R.string.setting, 2,0,null);
        mToolbar.setVisibility(View.GONE);
        mActivity.initHeadImage(head);
        head.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.setting_1:
                mActivity.switchFrag(this,new SettingCarManagerFragment(),false,null);
                break;
            case R.id.setting_2:
                mActivity.switchFrag(this, new SettingOilTypeFrag(), false, null);
                break;
            case R.id.setting_3:
                mActivity.switchFrag(this, new SettingTagFrag(), false, null);
                break;
            case R.id.head:
                //点击更换头像
                if (ActivityCompat.checkSelfPermission(mActivity.getApplicationContext(), Manifest.permission
                        .READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(mActivity, new String[]{Manifest.permission
                            .READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            RQ_CODE_REQUEST_PERMISSIONS);
                } else {
                    goPickPicture();
                }
                break;
        }
    }

    public void goPickPicture() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, RQ_CODE_PICK_HEAD_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
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
                head.setImageBitmap(bm);
                try {
                    //打开文件输出流
                    FileOutputStream fos = mActivity.getApplicationContext().openFileOutput("head.png", Context
                            .MODE_PRIVATE);
                    //将bitmap压缩后写入输出流(参数依次为图片格式、图片质量和输出流)
                    bm.compress(Bitmap.CompressFormat.PNG, 85, fos);
                    //刷新输出流
                    fos.flush();
                    //关闭输出流
                    fos.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
//                SpUtils.putboolean(mActivity.getApplicationContext(), "hasHeadImage", true);
                EventBus.getDefault().post(new MessageEvent(MyConstant.UPDATE_HEAD_IMAGE));

            }
        }
    }
}