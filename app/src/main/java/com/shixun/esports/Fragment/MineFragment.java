package com.shixun.esports.Fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.shixun.esports.Activity.G_Order_Activity;
import com.shixun.esports.Activity.HomeActivity;
import com.shixun.esports.Activity.OrderActivity;
import com.shixun.esports.Activity.Setting;
import com.shixun.esports.R;
import com.shixun.esports.Tools.StringUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MineFragment extends Fragment {
    private TextView username;
    private TextView userid;
    private String flag="";
    private HomeActivity mActivity;
    private ImageView g_order;
    private ImageView x_order;
    private ImageButton setting;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.mine,container,false);

        userid=(TextView)view.findViewById(R.id.UID);
        username=(TextView)view.findViewById(R.id.UserName);
        g_order=(ImageView)view.findViewById(R.id.g_order);
        x_order=(ImageView)view.findViewById(R.id.x_order);
        setting = (ImageButton)view.findViewById(R.id.setting);


        g_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ("0".equals(flag)) {
                    new AlertDialog.Builder(getActivity()).setTitle("普通用户不提供此功能").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    }).show();
                }else
                {
                    Intent intent=new Intent(getActivity().getApplicationContext(), G_Order_Activity.class);
                    startActivity(intent);
                }
            }
        });

        x_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity().getApplicationContext(), OrderActivity.class);
                startActivity(intent);
            }
        });

        setting.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent=new Intent(getActivity().getApplicationContext(), Setting.class);
                startActivity(intent);
            }
        });

        return view;
    }



    public Handler mHandler=new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            String data = msg.obj.toString();

            try {
                if(!StringUtil.isEmpty(data)) {
                    JSONArray jsonArray = new JSONArray(data);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                        username.setText(jsonObject.getString("username"));
                        userid.setText("UID："+jsonObject.getString("userid"));
                        flag=jsonObject.getString("status");
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = (HomeActivity) activity;
        mActivity.setHandler(mHandler);
    }
}
