package io.github.sm1314.profile3d.feature.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

import io.github.sm1314.profile3d.feature.MainActivity;
import io.github.sm1314.profile3d.feature.MyApplication;
import io.github.sm1314.profile3d.feature.R;
import io.github.sm1314.profile3d.feature.activity.ProfileActivity;
import io.github.sm1314.profile3d.feature.model.Employee;
import io.github.sm1314.tagcloudlib.view.TagsAdapter;


/**
 * Created by moxun on 16/3/4.
 */
public class ViewTagsAdapter extends TagsAdapter {
    private List<Employee> dataTotalSet = new ArrayList<>();
    private List<Employee> dataCurrSet = new ArrayList<>();

    public ViewTagsAdapter(List<Employee> list) {
        dataTotalSet.clear();
        dataCurrSet.clear();
        for(Employee e: list)
        {
            if(e.getPosition().getName().contains("在编"))
            {
                dataTotalSet.add(e);
            }
        }
        refreshData();
    }

    @Override
    public int getCount() {
        return dataCurrSet.size();
    }

    public void refreshData()
    {
        if(dataTotalSet.size() <= 50)
        {
            dataCurrSet = dataTotalSet;
            return;
        }

        Random random = new Random();
        Object[] values = new Object[50];
        HashSet<Integer> hashSet = new HashSet<Integer>();

        // 生成随机数字并存入HashSet
        while(hashSet.size() < values.length){
            hashSet.add(random.nextInt(dataTotalSet.size()));
        }

        values = hashSet.toArray();
        for(Object o : values)
        {
            dataCurrSet.add(dataTotalSet.get((Integer)o));
        }
    }

    @Override
    public View getView(final Context context, final int position, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.tag_item_view, parent, false);
        final Employee employee = dataCurrSet.get(position);

        //设置名字
        TextView tvName = (TextView)view.findViewById(R.id.tv_star_name);
        tvName.setText(employee.getName());
        tvName.setSelected(true);

        //开始加载图片
        Uri uri=Uri.parse(employee.getAvatarUrl(MyApplication.BASE_URL,96));
        SimpleDraweeView draweeView= (SimpleDraweeView) view.findViewById(R.id.my_image_view);
        draweeView.setImageURI(uri);

        //获取GenericDraweeHierarchy对象
        //        //圆角图片
        RoundingParams rp = new RoundingParams();
        //设置边框颜色 宽度
        rp.setBorder(Color.WHITE,2);
        //设置圆角
        rp.setRoundAsCircle(true);
        GenericDraweeHierarchy hierarchy = GenericDraweeHierarchyBuilder.newInstance(draweeView.getResources())
                //设置圆形圆角参数；RoundingParams.asCircle()是将图像设置成圆形
                .setRoundingParams(rp)
                //设置淡入淡出动画持续时间(单位：毫秒ms)
                //.setFadeDuration(5000)
                .setPlaceholderImage(R.drawable.placeholder)
                //构建
                .build();
        draweeView.setHierarchy(hierarchy);

        //设置点击回调
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float currAlpha = v.getAlpha();
                if(currAlpha < 0.5)
                    return;

                Intent intent = new Intent(context,ProfileActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable("employee",employee);
                intent.putExtras(bundle);
                //构建一个intent,传入FirstActivity.this作为上下文，传入SecondActivity.class作为目标活动，然后通过startActivity()方法执行Intent
                context.startActivity(intent);

//                v.setAlpha(0);
//                Log.e("Click", "Tag " + position + " clicked.");
//                Toast.makeText(context, "Tag " + position + " clicked", Toast.LENGTH_SHORT).show();
//                v.setAlpha(currAlpha);
            }
        });
        return view;
    }

    @Override
    public Object getItem(int position) {
        return dataCurrSet.get(position);
    }

    @Override
    public int getPopularity(int position) {
        return position % 5;
    }

    @Override
    public void onThemeColorChanged(View view, int themeColor, float alpha) {
 //       view.setAlpha(alpha);
        if(alpha < 0.2 && view.getAlpha() > 0.2)
        {
            view.setAlpha(0.2f);
        }
        else if(alpha < 0.75 && view.getAlpha() > 0.75)
        {
            view.setAlpha(0.5f);
        }
        else if(alpha > 0.8 && view.getAlpha() < 0.8)
        {
            view.setAlpha(0.8f);
        }
//        Log.d("fff", String.valueOf(view.getAlpha()));
//        view.findViewById(R.id.profile_view).setAlpha(alpha);
//
//        int color = Color.argb((int) ((1 - alpha) * 255), 255, 255, 255);
//        //((ImageView) view.findViewById(R.id.iv)).setColorFilter(color);
//        SimpleDraweeView draweeView= (SimpleDraweeView) view.findViewById(R.id.my_image_view);
//        draweeView.setColorFilter(color);
    }
}
