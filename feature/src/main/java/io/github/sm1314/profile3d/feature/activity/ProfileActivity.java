package io.github.sm1314.profile3d.feature.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.request.RequestOptions;
import com.intrusoft.library.FrissonView;

import java.util.Calendar;
import java.util.Date;

import io.github.sm1314.profile3d.feature.GlideApp;
import io.github.sm1314.profile3d.feature.MyApplication;
import io.github.sm1314.profile3d.feature.R;
import io.github.sm1314.profile3d.feature.model.Employee;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class ProfileActivity extends BaseActivity {
    private static int AVATAR_SIZE = 512;
    private static int AVATAR_BOLDER_WIDTH = 2;
    private static int AVATAR_DELAY = 2300;
    private Employee employee;
    private FrissonView frissonView;
    private Bitmap avatarBitmap;
    private Handler mHandler;
    private ImageButton imageButtonClose;
    private LinearLayout llEmplInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        //设置彩色statusbar
        setStatusBar(R.color.colorPrimary);
        //设置返回按钮
        imageButtonClose = findViewById(R.id.view_back);
        imageButtonClose.setClickable(true);
        imageButtonClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProfileActivity.this.finish();
            }
        });
        //获取传参
        Intent intent = this.getIntent();
        employee = (Employee) intent.getParcelableExtra("employee");
        frissonView = findViewById(R.id.wave_head);
        llEmplInfo = findViewById(R.id.ll_info);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable("employee", employee);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        employee = (Employee) savedInstanceState.getParcelable("employee");
    }

    @Override
    protected void onStart() {
        super.onStart();

        setUpAvatar();

        Handler mHandler = new Handler(this.getMainLooper());
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                setUpContent();
                imageButtonClose.bringToFront();
                llEmplInfo.bringToFront();
            }
        }, 1500);
        //frissonView.bringToFront();
    }

    @Override
    protected void onStop() {

        super.onStop();
    }

    public void setUpContent()
    {
        ((TextView)findViewById(R.id.tv_name)).setText(employee.getName());
        Calendar cal = Calendar.getInstance();
        cal.setTime(employee.getArmyday());
        ((TextView)findViewById(R.id.tv_armyday)).setText(cal.get(Calendar.YEAR) + "年" + cal.get(Calendar.MONTH) + "月");
        cal.setTime(employee.getWorkday());
        ((TextView)findViewById(R.id.tv_workday)).setText(cal.get(Calendar.YEAR) + "年" + cal.get(Calendar.MONTH) + "月");
        ((TextView)findViewById(R.id.tv_birthday)).setText(String.valueOf(getAgeByBirth(employee.getBirthday())));
        ((TextView)findViewById(R.id.tv_nation)).setText("民族： " + employee.getNation());
        ((TextView)findViewById(R.id.tv_location)).setText("籍贯： " + employee.getLocation());
        ((TextView)findViewById(R.id.tv_party)).setText("政治面貌： " + employee.getParty());
        ((TextView)findViewById(R.id.tv_department)).setText(employee.getDepartment().getName());
        ((TextView)findViewById(R.id.tv_position)).setText(employee.getPosition().getName());
    }
    public void setUpAvatar()
    {
        //开始加载图片
        Uri uri=Uri.parse(employee.getAvatarUrl(MyApplication.BASE_URL,AVATAR_SIZE));

        //获取GenericDraweeHierarchy对象
        //圆角图片
        //RoundingParams rp = new RoundingParams();
//        RoundingParams rp = RoundingParams.fromCornersRadius(180f);
//        rp.setPadding(0);
//        //设置边框颜色 宽度
//        rp.setBorder(Color.WHITE,AVATAR_BOLDER_WIDTH);
//        //设置圆角
//        rp.setRoundAsCircle(false);
//        GenericDraweeHierarchy hierarchy = GenericDraweeHierarchyBuilder.newInstance(draweeView.getResources())
//                //设置圆形圆角参数；RoundingParams.asCircle()是将图像设置成圆形
//                .setRoundingParams(rp)
//                //设置淡入淡出动画持续时间(单位：毫秒ms)
//                .setFadeDuration(AVATAR_DELAY)
//                .setActualImageScaleType(ScalingUtils.ScaleType.CENTER_INSIDE)
//                .setPlaceholderImage(R.drawable.placeholder)
//                //构建
//                .build();
//        draweeView.setHierarchy(hierarchy);

        ImageView imageView = (ImageView) findViewById(R.id.iv_avatar);
        RequestOptions requestOptions = new RequestOptions();
//        requestOptions = requestOptions.transforms(new CenterCrop(), new RoundedCorners(128)).;
        //GlideApp.with(this)
//                .applyDefaultRequestOptions(requestOptions)
//                .load(uri)
//                .placeholder(R.drawable.placeholder)
//                into(imageView);
        //setCrossFadeEnabled
        GlideApp.with(this).load(uri).circleCrop().placeholder(employee.getSex().contains("女")? R.drawable.women : R.drawable.man).circleCrop().transition(withCrossFade(1000)).into(imageView);
        //Glide.with(this)
    }

    private static int getAgeByBirth(Date birthday) {
        int age = 0;
        try {
            Calendar now = Calendar.getInstance();
            now.setTime(new Date());// 当前时间

            Calendar birth = Calendar.getInstance();
            birth.setTime(birthday);

            if (birth.after(now)) {//如果传入的时间，在当前时间的后面，返回0岁
                age = 0;
            } else {
                age = now.get(Calendar.YEAR) - birth.get(Calendar.YEAR);
                if (now.get(Calendar.DAY_OF_YEAR) > birth.get(Calendar.DAY_OF_YEAR)) {
                    age += 1;
                }
            }
            return age;
        } catch (Exception e) {//兼容性更强,异常后返回数据
            return 0;
        }
    }
}
