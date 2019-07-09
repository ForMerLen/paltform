package com.shixun.esports.Activity;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.shixun.esports.Fragment.HomepageFragment;
import com.shixun.esports.Fragment.MessageFragment;
import com.shixun.esports.Fragment.MineFragment;
import com.shixun.esports.R;
import com.shixun.esports.Tools.Constant;
import com.shixun.esports.Tools.StringUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class HomeActivity extends FragmentActivity implements View.OnClickListener {
    private LinearLayout mTabWeixin;
    private LinearLayout mTabFri;
    private LinearLayout mTabMe;

    private ImageButton mImgWeixin;
    private ImageButton mImgFri;
    private ImageButton mImgMe;

    private Fragment mtab1;
    private Fragment mtab2;
    private Fragment mtab3;

    //保存activity传递过来的数据
    private String iusername;
    Handler mHandler;
    private Message message;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        initView();
        initEvent();

        Intent intent=getIntent();
        iusername=intent.getStringExtra("用户名");
        setSelect(0);
    }

    private void initEvent() {
        mTabWeixin.setOnClickListener(this);
        mTabFri.setOnClickListener(this);
        mTabMe.setOnClickListener(this);
    }

    private void initView() {
        mTabWeixin = (LinearLayout)findViewById(R.id.id_tab_ui);
        mTabFri = (LinearLayout)findViewById(R.id.id_tab_fri);
        mTabMe = (LinearLayout)findViewById(R.id.id_tab_me);

        mImgWeixin = (ImageButton) findViewById(R.id.id_tab_ui_img);
        mImgFri = (ImageButton) findViewById(R.id.id_tab_fri_img);
        mImgMe = (ImageButton) findViewById(R.id.id_tab_me_img);

        message = new Message();


    }

    @SuppressLint("HandlerLeak")
    private void setSelect(int i)
    {

        FragmentManager fm = getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();

        hideFragment(transaction);
        //设置内容区域
        switch (i)
        {
            case 0:

                if(mtab1==null){
                    mtab1 = new HomepageFragment();
                    transaction.add(R.id.id_content,mtab1);
                }else
                {
                    transaction.show(mtab1);
                }
                mImgWeixin.setImageResource(R.drawable.homepage);
                break;
            case 1:
                if(mtab2==null){
                    mtab2 = new MessageFragment();
                    transaction.add(R.id.id_content,mtab2);
                }else
                {
                    transaction.show(mtab2);
                }
                mImgFri.setImageResource(R.drawable.message);
                break;
            case 2:
                mine();
                if(mtab3==null){
                    mtab3 = new MineFragment();
                    transaction.add(R.id.id_content,mtab3);
                }else
                {
                    transaction.show(mtab3);

                }

                mImgMe.setImageResource(R.drawable.mine);
                break;
        }
        transaction.commit();
    }

    private void mine() {

        String MineUrlStr = Constant.URL_Mine + "?username="+iusername;
        new MyAsyncTask(message).execute(MineUrlStr);
    }

    private void hideFragment(FragmentTransaction transaction) {
        if(mtab1 !=null)
        {
            transaction.hide(mtab1);
        }
        if(mtab2 !=null)
        {
            transaction.hide(mtab2);
        }
        if(mtab3 !=null)
        {
            transaction.hide(mtab3);
        }

    }

    @Override
    public void onClick(View view) {

        resetImg();
        switch (view.getId())
        {
            case R.id.id_tab_ui:
                setSelect(0);
                break;
            case R.id.id_tab_fri:
                setSelect(1);
                break;
            case R.id.id_tab_me:
                setSelect(2);
                break;
        }
    }

    private void resetImg() {
        mImgWeixin.setImageResource(R.drawable.homepage_u);
        mImgFri.setImageResource(R.drawable.message_u);
        mImgMe.setImageResource(R.drawable.mine_u);
    }

    public void setHandler(Handler handler) {
        mHandler = handler;
    }



    public class MyAsyncTask extends AsyncTask<String, Integer, String>
    {


        StringBuilder response = new StringBuilder();
        Message msg=new Message();
        public MyAsyncTask(Message string) {
            string.obj=response;
//            mHandler.sendMessage(string);
        }

        @Override
        protected void onPreExecute() {

        }

        /**
         * @param params 这里的params是一个数组，即AsyncTask在激活运行是调用execute()方法传入的参数
         */
        @Override
        protected String doInBackground(String... params) {

            HttpURLConnection connection = null;

            try {
                URL url = new URL(params[0]); // 声明一个URL,注意如果用百度首页实验，请使用https开头，否则获取不到返回报文
                connection = (HttpURLConnection) url.openConnection(); // 打开该URL连接
                connection.setRequestMethod("GET"); // 设置请求方法，“POST或GET”，我们这里用GET，在说到POST的时候再用POST
                connection.setConnectTimeout(80000); // 设置连接建立的超时时间
                connection.setReadTimeout(80000); // 设置网络报文收发超时时间
                InputStream in = connection.getInputStream();  // 通过连接的输入流获取下发报文，然后就是Java的流处理
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            //Bundle bundle=new Bundle();
            //bundle.putString("data",response.toString());
            msg.obj=response;
            mHandler.sendMessage(msg);
            return response.toString(); // 这里返回的结果就作为onPostExecute方法的入参
        }
    }
}
