package com.shixun.esports.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.shixun.esports.R;
import com.shixun.esports.Tools.Constant;
import com.shixun.esports.Tools.StringUtil;
import com.shixun.esports.Tools.Verification;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView iv_code;
    private EditText username;
    private EditText password;
    private EditText passwrodagain;
    private EditText et_phoneCode;
    private ImageButton register;
    private ImageButton back;
    private String realcode;
    private String result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        initView();
        initEvent();

        iv_code.setImageBitmap(Verification.getInstance().createBitmap());
        realcode = Verification.getInstance().getCode().toLowerCase();


    }

    private void initEvent() {
        iv_code.setOnClickListener(this);
        register.setOnClickListener(this);
        back.setOnClickListener(this);
    }

    private void initView() {
        iv_code = (ImageView)findViewById(R.id.iv_code);
        username=(EditText)findViewById(R.id.Username);
        password=(EditText)findViewById(R.id.Password);
        passwrodagain=(EditText)findViewById(R.id.PwCon);
        et_phoneCode=(EditText)findViewById(R.id.et_phoneCodes);
        register=(ImageButton)findViewById(R.id.register);
        back = (ImageButton)findViewById(R.id.back);
    }

    public void onClick(View view){
        switch (view.getId()) {
            case R.id.iv_code:
                iv_code.setImageBitmap(Verification.getInstance().createBitmap());
                realcode = Verification.getInstance().getCode().toLowerCase();
            case R.id.register:
                String phoneCode = et_phoneCode.getText().toString().toLowerCase();
                if(StringUtil.isEmpty(username.getText().toString()) || StringUtil.isEmpty(password.getText().toString()))
                {
                    Toast.makeText(RegisterActivity.this,"账号密码不能为空",Toast.LENGTH_SHORT).show();
                }else if(!password.getText().toString().equals(passwrodagain.getText().toString()))
                {
                    Toast.makeText(RegisterActivity.this,"两次密码不一致",Toast.LENGTH_SHORT).show();
                }else if(!phoneCode.equals(realcode))
                {
                    et_phoneCode.setText("");
                    iv_code.setImageBitmap(Verification.getInstance().createBitmap());
                    realcode = Verification.getInstance().getCode().toLowerCase();
                }else
                {
                    register(username.getText().toString(), password.getText().toString());
                }
                break;
            case R.id.back:
                finish();
                break;
        }
    }

    private void register(String account, String password) {
        String registerUrlStr = Constant.URL_Register + "?account=" + account + "&password=" + password;
        Log.d("测试",registerUrlStr);
        new MyAsyncTask(result).execute(registerUrlStr);
    }

    public static class MyAsyncTask extends AsyncTask<String, Integer, String>
    {
        private String tv; // 举例一个UI元素，后边会用到

        public MyAsyncTask(String v) {
            tv = v;
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

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
