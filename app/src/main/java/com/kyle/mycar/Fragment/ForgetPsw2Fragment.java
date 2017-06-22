package com.kyle.mycar.Fragment;


import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.GetCallback;
import com.avos.avoscloud.SaveCallback;
import com.kyle.mycar.Bean.UserInfo;
import com.kyle.mycar.R;
import com.labo.kaji.fragmentanimations.CubeAnimation;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class ForgetPsw2Fragment extends Fragment {


    Unbinder unbinder;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.et_psw_forget)
    EditText etPsw;
    @BindView(R.id.et_psw_forget2)
    EditText etPsw2;
    @BindView(R.id.btn_confirm)
    Button btnConfirm;


    private String phone;
    private static final String ARG_PARAM1 = "param1";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            phone = getArguments().getString(ARG_PARAM1);
        }
    }

    public static ForgetPsw2Fragment newInstance(String phone) {
        ForgetPsw2Fragment fragment = new ForgetPsw2Fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, phone);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_forget_psw2, container, false);
        unbinder = ButterKnife.bind(this, view);
        toolbar.setTitle("重置密码");
        return view;
    }


    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        return CubeAnimation.create(CubeAnimation.LEFT, enter, 500);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.btn_confirm)
    public void onViewClicked() {
        final String psw = etPsw.getText().toString().trim();
        if (TextUtils.isEmpty(psw)){
//            Toast.makeText(getActivity(), R.string.err_empty_psw, Toast.LENGTH_SHORT).show();
            etPsw.setError(getString(R.string.err_empty_psw));
            return;
        }else if (psw.length()<6){
//            Toast.makeText(getActivity(), R.string.err_psw_short, Toast.LENGTH_SHORT).show();
            etPsw.setError(getString(R.string.err_psw_short));
            return;
        }
        final String psw2 = etPsw2.getText().toString().trim();
        if (TextUtils.equals(psw,psw2)){
            AVQuery<UserInfo> query=new AVQuery<>("UserInfo");
            query.whereEqualTo("phone",phone);
            query.getFirstInBackground(new GetCallback<UserInfo>() {
                @Override
                public void done(UserInfo userInfo, AVException e) {
                    if (null==e){
                        userInfo.setPsw(psw2);
                        userInfo.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(AVException e) {
                                if (null==e){
                                    Toast.makeText(getActivity(), R.string.change_psw_sucess,Toast.LENGTH_SHORT).show();
                                }else {
                                    Toast.makeText(getActivity(), R.string.save_fail, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }else {
                        Toast.makeText(getActivity(), R.string.err_unexist_acc,Toast.LENGTH_SHORT).show();

                    }
                }
            });

        }else {
            Toast.makeText(getActivity(), R.string.err_psw_unequal, Toast.LENGTH_SHORT).show();
        }

    }
}
