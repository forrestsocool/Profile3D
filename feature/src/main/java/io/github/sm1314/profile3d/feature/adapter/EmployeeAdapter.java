package io.github.sm1314.profile3d.feature.adapter;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.view.SimpleDraweeView;
import com.github.promeg.pinyinhelper.Pinyin;

import java.util.List;

import cc.solart.turbo.BaseTurboAdapter;
import cc.solart.turbo.BaseViewHolder;
import io.github.sm1314.profile3d.feature.MyApplication;
import io.github.sm1314.profile3d.feature.R;
import io.github.sm1314.profile3d.feature.activity.ProfileActivity;
import io.github.sm1314.profile3d.feature.model.Employee;

public class EmployeeAdapter extends BaseTurboAdapter<Employee, BaseViewHolder> {

    private static final int AVATAR_BOLDER_WIDTH = 2;
    private static final int AVATAR_DELAY = 500;

    public EmployeeAdapter(Context context) {
        super(context);
    }


    public EmployeeAdapter(Context context, List<Employee> data) {
        super(context, data);
    }

    @Override
    protected int getDefItemViewType(int position) {
        Employee employee = getItem(position);
        return employee.getId() == null? 1 : 0;
    }

    @Override
    protected BaseViewHolder onCreateDefViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 0)
            return new EmployeeHolder(inflateItemView(R.layout.item_empl, parent));
        else
            return new PinnedHolder(inflateItemView(R.layout.item_pinned_header, parent));
    }


    @Override
    protected void convert(BaseViewHolder holder, final Employee item) {
        if (holder instanceof EmployeeHolder) {
            ((EmployeeHolder) holder).empl_posi.setText(item.getPosition().getName());
            ((EmployeeHolder) holder).empl_name.setText(item.getName());
            Uri uri=Uri.parse(item.getAvatarUrl(MyApplication.BASE_URL,96));
            ((EmployeeHolder) holder).empl_avatar.setImageURI(uri);
            //圆角图片
            RoundingParams rp = new RoundingParams();
            //设置边框颜色 宽度
            rp.setBorder(Color.WHITE,AVATAR_BOLDER_WIDTH);
            //设置圆角
            rp.setRoundAsCircle(true);
            GenericDraweeHierarchy hierarchy = GenericDraweeHierarchyBuilder.newInstance(((EmployeeHolder) holder).empl_avatar.getResources())
                    //设置圆形圆角参数；RoundingParams.asCircle()是将图像设置成圆形
                    .setRoundingParams(rp)
                    //设置淡入淡出动画持续时间(单位：毫秒ms)
                    .setFadeDuration(AVATAR_DELAY)
                    .setPlaceholderImage(R.drawable.placeholder)
                    //构建
                    .build();
            ((EmployeeHolder) holder).empl_avatar.setHierarchy(hierarchy);

            //设置点击效果
            ((EmployeeHolder) holder).itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Toast.makeText(view.getContext(),"Clicked",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(view.getContext(),ProfileActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("employee",item);
                    intent.putExtras(bundle);
                    //构建一个intent,传入FirstActivity.this作为上下文，传入SecondActivity.class作为目标活动，然后通过startActivity()方法执行Intent
                    view.getContext().startActivity(intent);
                }
            });
        }else {
            String letter = item.getName();
            ((PinnedHolder) holder).empl_tip.setText(letter);
        }
    }

    public int getLetterPosition(String letter){
        for (int i = 0 ; i < getData().size(); i++){
            if(getData().get(i).getId() !=null && Pinyin.toPinyin(getData().get(i).getName(),"").substring(0,1).equals(letter)){
                return i;
            }
        }
        return -1;
    }

    class EmployeeHolder extends BaseViewHolder {
        SimpleDraweeView empl_avatar;
        TextView empl_name, empl_posi;

        public EmployeeHolder(View view) {
            super(view);
            empl_name = findViewById(R.id.empl_name);
            empl_posi = findViewById(R.id.empl_posi);
            empl_avatar = findViewById(R.id.my_image_view);
        }
    }


    class PinnedHolder extends BaseViewHolder {
        TextView empl_tip;

        public PinnedHolder(View view) {
            super(view);
            empl_tip = findViewById(R.id.empl_tip);
        }
    }
}
