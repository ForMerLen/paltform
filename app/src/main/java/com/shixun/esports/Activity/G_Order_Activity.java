package com.shixun.esports.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.shixun.esports.R;
import com.shixun.esports.Tools.Constant;
import com.shixun.esports.Tools.StringUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class G_Order_Activity extends AppCompatActivity{

    private String username;
    private int flag=0;
    private ListView lv;
    private Button cancel;
    private String Str;

    List<HashMap<String, String>> list = new ArrayList<HashMap<String,String>>();
    private Message message=new Message();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.g_order);

        username=load();
        initView();
        Log.d("测试1", username);
        order();
    }

    private void initView() {
        list.clear();
        lv=(ListView)findViewById(R.id.g_order_lv);

    }

    private void order() {
        String TestUrlStr = Constant.URL_G_Order + "?username="+username+"&flag="+flag;
        new MyAsyncTask(message).execute(TestUrlStr);
    }

    Handler mHandler=new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);

            String data = msg.obj.toString();

            try {
                if(!StringUtil.isEmpty(data)) {
                    JSONArray jsonArray = new JSONArray(data);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                        String str="";
                        HashMap<String, String> map = new HashMap<String, String>();
                        map.put("time", jsonObject.getString("time"));
                        map.put("username", jsonObject.getString("username"));

                        map.put("orderid",jsonObject.getString("orderid"));
                        if("0".equals(jsonObject.getString("status"))) {
                            str = "待接单";
                        }else if("1".equals(jsonObject.getString("status"))){
                            str="已接单";
                        }else if("2".equals(jsonObject.getString("status"))){
                            str="已完成";
                        }else {
                            str="已取消";
                        }
                        map.put("status",str);

                        map.put("totle",jsonObject.getString("totle"));
                        list.add(map);

                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            String[] str = new String[]{"time","username","orderid","status","totle"};
            int[] rid = new int[]{R.id.g_order_time,R.id.g_order_username,R.id.g_order_id,R.id.g_order_status,R.id.g_order_totle};
            SimpleAdapter simpleAdapter=new SimpleAdapter(G_Order_Activity.this,list,R.layout.g_order_lv,str,rid){
                public View getView(int position, View convertView, ViewGroup parent) {
                    final int p=position;
                    final View view=super.getView(position, convertView, parent);
                    cancel = (Button)view.findViewById(R.id.g_order_btn);
                    Str=list.get(p).get("status");
                    if(Str.equals("已接单")) {
                        cancel.setText("订单完成");
                    }
                    else if(Str.equals("已完成")) {
                        cancel.setText("已完成");
                        cancel.setEnabled(false);
                    }
                    else if(Str.equals("已取消")) {
                        cancel.setText("已取消");
                        cancel.setEnabled(false);
                    }
                    else if(Str.equals("待接单")){
                        cancel.setText("接单");
                    }
                    cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Str=list.get(p).get("status");
                            if(Str.equals("待接单")) {
                                Toast.makeText(G_Order_Activity.this, "接单成功", Toast.LENGTH_SHORT).show();
                                flag = 1;
                                int status = 1;
                                String g_order_id = list.get(p).get("orderid");
                                list.clear();
                                String TestUrlStr = Constant.URL_G_Order + "?username="+username+"&orderid="+g_order_id +"&status="+status+"&flag="+flag;
                                System.out.println(TestUrlStr);
                                new MyAsyncTask(message).execute(TestUrlStr);
                            }
                            else if (Str.equals("已接单")) {
                                Toast.makeText(G_Order_Activity.this, "订单完成", Toast.LENGTH_SHORT).show();
                                flag = 1;
                                int status = 2;
                                String g_order_id = list.get(p).get("orderid");
                                list.clear();
                                String TestUrlStr = Constant.URL_G_Order + "?username="+username+"&orderid="+g_order_id +"&status="+status+"&flag="+flag;
                                new MyAsyncTask(message).execute(TestUrlStr);
                            }
                        }
                    });
                    return view;
                }
            };
            lv.setAdapter(simpleAdapter);

        }
    };

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
            Log.d("测试2", response.toString());
            return response.toString(); // 这里返回的结果就作为onPostExecute方法的入参
        }
    }
}
