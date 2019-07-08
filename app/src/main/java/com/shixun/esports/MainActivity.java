package com.shixun.esports;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.shixun.esports.Activity.HomeActivity;
import com.shixun.esports.Activity.RegisterActivity;
import com.shixun.esports.Tools.Constant;
import com.shixun.esports.Tools.StringUtil;
import com.shixun.esports.Tools.Verification;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageButton login;
    private String realCode;
    private Button register;
    private EditText username;
    private EditText password;
    private TextView result;
    Handler mHandler;

    SharedPreferences sprfMain;
    SharedPreferences.Editor editorMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sprfMain= PreferenceManager.getDefaultSharedPreferences(this);
        editorMain=sprfMain.edit();
        if(sprfMain.getBoolean("main",false)){
            Intent intent=new Intent(MainActivity.this,HomeActivity.class);
            startActivity(intent);
            finish();
        }

        setContentView(R.layout.login);

        initView();
        initEvent();


    }

    private void initEvent() {
        login.setOnClickListener(this);
        register.setOnClickListener(this);
    }

    private void initView() {
        login = (ImageButton) findViewById(R.id.login);
        register = (Button)findViewById(R.id.register);
        username=(EditText)findViewById(R.id.id_username);
        password=(EditText)findViewById(R.id.id_passwrod);
        result=(TextView)findViewById(R.id.textView);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.login:
                if (!StringUtil.isEmpty(username.getText().toString() )&& !StringUtil.isEmpty(password.getText().toString())) {
                    login(username.getText().toString(),password.getText().toString());
                    mHandler = new Handler(){
                        @Override
                        public void handleMessage(@NonNull Message msg) {
                            super.handleMessage(msg);
                            result.setText(msg.obj.toString());
                            if(result.getText().toString().equals("message:登陆成功")){
                                save(username.getText().toString());
                                Intent intent = new Intent();
                                intent.setClass(MainActivity.this, HomeActivity.class);
                                intent.putExtra("用户名",username.getText().toString());
                                MainActivity.this.startActivity(intent);
                                editorMain.putBoolean("main",true);
                                editorMain.commit();
                                finish();
                            }else
                            {
                                result.setText("账号密码不一致");
                            }

                        }
                    };
                } else{
                    Toast.makeText(MainActivity.this,"账号密码不能为空",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.register:
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, RegisterActivity.class);
                MainActivity.this.startActivity(intent);
                break;
        }
    }

    public void save(String s){
        FileOutputStream out=null;
        BufferedWriter writer=null;
        try{
            out=openFileOutput("yourusername", Context.MODE_PRIVATE);
            writer=new BufferedWriter(new OutputStreamWriter(out));
            writer.write(s);
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            try{
                if(writer!=null)
                    writer.close();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    private void login(String account, String password) {
        String loginUrlStr = Constant.URL_Login + "?account=" + account + "&password=" + password;
        Log.d("ceshi",loginUrlStr);
        new MyAsyncTask(result).execute(loginUrlStr);

    }

    public  class MyAsyncTask extends AsyncTask<String, Integer, String>
    {
        private TextView tv; // 举例一个UI元素，后边会用到
        Message msg=new Message();

        public MyAsyncTask(TextView v) {
            tv = v;
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

            tv.setText(s);
            msg.obj=tv.getText().toString();
            mHandler.sendMessage(msg);
        }

    }

}
