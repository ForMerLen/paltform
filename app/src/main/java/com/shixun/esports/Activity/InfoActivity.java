package com.shixun.esports.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.shixun.esports.R;
import com.shixun.esports.Tools.Constant;
import com.shixun.esports.Tools.StringUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InfoActivity extends AppCompatActivity implements View.OnClickListener {

    private String daiusername;
    private String username;
    private TextView name;
    private TextView id;
    private TextView price;
    private TextView ordernum;
    private TextView rank;
    private ImageButton order;

    private Message message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_info);
        Intent intent=getIntent();
        daiusername=intent.getStringExtra("daiusername");

        username=load();
       // Log.d("测试us",username);


        initView();
        info();
        initEvent();
    }

    private String load(){
        FileInputStream in=null;
        BufferedReader reader=null;
        StringBuilder content=new StringBuilder();
        try{
            in=openFileInput("yourusername");
            reader = new BufferedReader(new InputStreamReader(in));
            String line="";
            while ((line=reader.readLine())!=null){
                content.append(line);
            }
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            if(reader!=null){
                try{
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
            return content.toString();
        }
    }

    private void initEvent() {
        order.setOnClickListener(this);
    }

    private void info() {
        String TestUrlStr = Constant.URL_Info + "?username="+daiusername;
       // Log.d("测试",TestUrlStr);
        new MyAsyncTask(message).execute(TestUrlStr);
    }

    private void initView() {
        name=(TextView)findViewById(R.id.txt_username);
        id=(TextView)findViewById(R.id.txt_userid);
        price=(TextView)findViewById(R.id.txt_price);
        rank=(TextView)findViewById(R.id.txt_rank);
        ordernum=(TextView)findViewById(R.id.txt_ordernum);
        order=(ImageButton)findViewById(R.id.im_order);

        message = new Message();
    }

   Handler mHandler=new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);

            String data = msg.obj.toString();
            Log.d("测试1", data);

            try {
                if (!StringUtil.isEmpty(data)) {
                    JSONArray jsonArray = new JSONArray(data);

                    JSONObject jsonObject = (JSONObject) jsonArray.get(0);
                    name.setText(jsonObject.getString("username"));
                    id.setText(jsonObject.getString("id"));
                    price.setText(jsonObject.getString("price"));
                    rank.setText(jsonObject.getString("rank"));
                    ordernum.setText(jsonObject.getString("ordernum"));


                }
            }catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
    };

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.im_order:
                final EditText editText=new EditText(this);
                new AlertDialog.Builder(this).setView(editText).setTitle("请输入局数").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Pattern p = Pattern.compile("[0-9]*");
                        Matcher m = p.matcher(editText.getText().toString());
                        if(m.matches())
                        {
                            String s="";
                            int a=Integer.parseInt(editText.getText().toString());
                            int b=Integer.parseInt(price.getText().toString());
                            int totle = a*b;
                            String OrderUrlStr = Constant.URL_Order + "?username="+username+"&ausername="+daiusername+"&totle="+totle;
                            new NewAsyncTask(s).execute(OrderUrlStr);
                        }
                    }
                }).setNegativeButton("取消",null).show();
                break;
        }
    }


    public class MyAsyncTask extends AsyncTask<String, Integer, String>
    {

        StringBuilder response = new StringBuilder();
        Message msg=new Message();
        public MyAsyncTask(Message string) {
            string.obj=response;
            // mHandler.sendMessage(string);
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
            // Log.d("测试re",response.toString());
            msg.obj=response;
            mHandler.sendMessage(msg);
            return response.toString(); // 这里返回的结果就作为onPostExecute方法的入参
        }
    }

    public class NewAsyncTask extends AsyncTask<String, Integer, String>
    {
        private String tv;

        public NewAsyncTask(String v) {
            tv=v;
        }
        @Override
        protected void onPreExecute() {
            Log.w("WangJ", "task onPreExecute()");
        }

        /**
         * @param params 这里的params是一个数组，即AsyncTask在激活运行是调用execute()方法传入的参数
         */
        @Override
        protected String doInBackground(String... params) {

            HttpURLConnection connection = null;
            StringBuilder response = new StringBuilder();
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
            return response.toString(); // 这里返回的结果就作为onPostExecute方法的入参
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            // 如果在doInBackground方法，那么就会立刻执行本方法
            // 本方法在UI线程中执行，可以更新UI元素，典型的就是更新进度条进度，一般是在下载时候使用
        }

        /**
         * 运行在UI线程中，所以可以直接操作UI元素
         * @param s
         */
        @Override
        protected void onPostExecute(String s) {
            s.equals(tv);
            // Toast.makeText(RegisterActivity.this,s,Toast.LENGTH_SHORT).show();

        }
    }
}
