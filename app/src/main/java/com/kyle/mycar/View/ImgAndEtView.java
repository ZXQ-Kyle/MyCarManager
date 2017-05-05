package com.kyle.mycar.View;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.kyle.mycar.MyUtils.Tint;
import com.kyle.mycar.R;

/**
 * 自定义控件
 * Created by Zhang on 2017/5/4.
 */

public class ImgAndEtView extends LinearLayoutCompat {

    private ImageView iv;
    private EditText et;

    public ImgAndEtView(Context context, AttributeSet attrs) {
        super(context, attrs);
        //取得属性集合
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ImgAndEtView);
        ColorStateList colorStateList = typedArray.getColorStateList(R.styleable.ImgAndEtView_drawableColor);
        Drawable drawable = typedArray.getDrawable(R.styleable.ImgAndEtView_iconLeft);
        String hint = typedArray.getString(R.styleable.ImgAndEtView_hint);
        //关闭资源
        typedArray.recycle();
        View view = View.inflate(context, R.layout.img_and_et_view, this);

        iv = (ImageView) findViewById(R.id.iv_my);
        et = (EditText) findViewById(R.id.et_my);

        setDrawableColor(drawable, colorStateList);

        et.setBackground(Tint.tintDrawable(et.getBackground(), colorStateList));
        et.setMaxLines(1);
        et.setHint(hint);
    }

    public void setDrawableColor(Drawable drawable, ColorStateList color) {
//        iv.setBackground();
        iv.setImageDrawable(Tint.tintDrawable(drawable, color).mutate());

    }

    public String getText() {
        return et.getText().toString().trim();
    }

}
