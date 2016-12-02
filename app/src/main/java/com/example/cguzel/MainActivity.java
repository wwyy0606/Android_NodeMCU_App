package com.example.cguzel.nodemcu_app;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;
import android.widget.ToggleButton;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by cguzel on 26.04.2016.
 */

public class MainActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {

    public final static String PREF_IP = "PREF_IP_ADDRESS";
    public final static String PREF_PORT = "PREF_PORT_NUMBER";
    //定义按钮和文本输入框
    //final Context context = this;
    private EditText editTextIPAddress,editTextPortNumber;
//    private Button ledOn, ledOff;
    Switch switch1,switch2,switch3,switch4,switch5,switch6,switch7,switch8;
    SharedPreferences.Editor editor;
    SharedPreferences sharedPreferences;
    //shared preference 用于保存ip地址和port，这样用户在下次打开APP时就不用再次输入他们了

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPreferences = getSharedPreferences("HTTP_HELPER_PREFS",Context.MODE_PRIVATE);
        //Context.MODE_PRIVATE：为默认操作模式,代表该文件是私有数据,只能被应用本身访问,在该模式下,写入的内容会覆盖原文件的内容
        //将数据保存至sharedpreference
        editor = sharedPreferences.edit();

        editTextIPAddress = (EditText) findViewById(R.id.edt_ip);
        editTextPortNumber = (EditText)findViewById(R.id.editTextPortnumber);

//        ledOn = (Button) findViewById(R.id.btn_ledOn);
//        ledOff = (Button) findViewById(R.id.btn_ledOff);
        switch1=(Switch) findViewById(R.id.switch1);
        switch2=(Switch) findViewById(R.id.switch2);
        switch3=(Switch) findViewById(R.id.switch3);
        switch4=(Switch) findViewById(R.id.switch4);
        switch5=(Switch) findViewById(R.id.switch5);
        switch6=(Switch) findViewById(R.id.switch6);
        switch7=(Switch) findViewById(R.id.switch7);
        switch8=(Switch) findViewById(R.id.switch8);


        switch1.setOnCheckedChangeListener(this);
        switch2.setOnCheckedChangeListener(this);
        switch3.setOnCheckedChangeListener(this);
        switch4.setOnCheckedChangeListener(this);
        switch5.setOnCheckedChangeListener(this);
        switch6.setOnCheckedChangeListener(this);
        switch7.setOnCheckedChangeListener(this);
        switch8.setOnCheckedChangeListener(this);

//        int[] onff_id = {R.id.switch1, R.id.switch2, R.id.switch3, R.id.switch4};
//        for (int id : onff_id) {
//            ToggleButton onff = (ToggleButton) findViewById(id);
//            onff.setOnCheckedChangeListener(this);
//        }


        editTextIPAddress.setText(sharedPreferences.getString(PREF_IP,""));
        editTextPortNumber.setText(sharedPreferences.getString(PREF_PORT,""));
        // get the IP address and port number from the last time the user used the app,
        // put an empty string "" is this is the first time.从上次使用APP中得到IPaddress和port

    }


    /** When the button clicks this method executes**/
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        String parameterValue = "";
        // get the Ip address
        String IpAddress = editTextIPAddress.getText().toString().trim();
        // get the port number
        String portNumber = editTextPortNumber.getText().toString().trim();

        //保存IP地址和端口号
        editor.putString(PREF_IP, IpAddress); // set the ip address value to save
        editor.putString(PREF_PORT, portNumber); // set the port number to save
        editor.commit(); // save the IP and PORT

        if (editTextIPAddress.getText().toString().equals(""))
            Toast.makeText(MainActivity.this, "Please enter the ip address and portnumber ...", Toast.LENGTH_SHORT).show();

        else {
            switch (buttonView.getId()) {
                case R.id.switch1:
                    if (isChecked) {
                        parameterValue = "11";
                    } else {
                        parameterValue = "10";
                    }
                    break;
                case R.id.switch2:
                    if (isChecked) {
                        parameterValue = "21";
                    } else {
                        parameterValue = "20";
                    }
                    break;
                case R.id.switch3:
                    if (isChecked) {
                        parameterValue = "31";
                    } else {
                        parameterValue = "30";
                    }
                    break;
                case R.id.switch4:
                    if (isChecked) {
                        parameterValue = "41";
                    } else {
                        parameterValue = "40";
                    }
                    break;
                case R.id.switch5:
                    if (isChecked) {
                        parameterValue = "51";
                    } else {
                        parameterValue = "50";
                    }
                    break;
                case R.id.switch6:
                    if (isChecked) {
                        parameterValue = "61";
                    } else {
                        parameterValue = "60";
                    }
                    break;
                case R.id.switch7:
                    if (isChecked) {
                        parameterValue = "71";
                    } else {
                        parameterValue = "70";
                    }
                    break;
                case R.id.switch8:
                    if (isChecked) {
                        parameterValue = "81";
                    } else {
                        parameterValue = "80";
                    }
                    break;
                default:
                    break;
            }
            //Connect to default port number. Ex: http://IpAddress:80
            // String serverAddress = editTextIPAddress.getText().toString() + ":" + editTextPortNumber.getText().toString();
            HttpRequestTask requestTask = new HttpRequestTask(buttonView.getContext(),IpAddress, portNumber, parameterValue);
            requestTask.execute(parameterValue);// execute HTTP request
        }
    }

//    public void onchecked(View view) {
//        String parameterValue="";
//        // get the Ip address
//        String IpAddress =editTextIPAddress.getText().toString().trim();
//        // get the port number
//        String portNumber = editTextPortNumber.getText().toString().trim();
//
//        //保存IP地址和端口号
//        editor.putString(PREF_IP,IpAddress); // set the ip address value to save
//        editor.putString(PREF_PORT,portNumber); // set the port number to save
//        editor.commit(); // save the IP and PORT
//
//        if (editTextIPAddress.getText().toString().equals(""))
//            Toast.makeText(MainActivity.this,"Please enter the ip address and portnumber ...", Toast.LENGTH_SHORT).show();
//
//        else {
//
//
//            if (view == ledOn)
//                parameterValue = "1";
//
//            else
//                parameterValue = "0";
//
//            //Connect to default port number. Ex: http://IpAddress:80
//           // String serverAddress = editTextIPAddress.getText().toString() + ":" + editTextPortNumber.getText().toString();
//            HttpRequestTask requestTask = new HttpRequestTask(view.getContext(),IpAddress,portNumber,parameterValue);
//            requestTask.execute(parameterValue);// execute HTTP request
//        }
//    }


    private class HttpRequestTask extends AsyncTask<String,Void, String> {

        private String IpAddress,portnumber;
        private String serverResponse = "";
        private AlertDialog dialog;
        private Context context;
        //* @param context the application context, needed to create the dialog
        //private String parameter;
        private String parameterValue;


        public HttpRequestTask(Context context,String IpAddress,String portNumber,String parameterValue) {
            this.IpAddress = IpAddress;
            this.parameterValue = parameterValue;
            this.portnumber=portNumber;// * @param portNumber the port number of the ip address// this.context = context;
            this.context = context;
          //  this.parameter = parameter;



            dialog = new AlertDialog.Builder(this.context)
                    .setTitle("HTTP Response from Ip Address:")
                    .setCancelable(true)
                    .show();
        }

        //doinbackground 发送请求给IP地址
        @Override
        protected String doInBackground(String... params) {
            dialog.setMessage("Data sent , waiting response from server...");

            if (!dialog.isShowing())
                dialog.show();

            //String val = params[0];
            final String url = "http://" + IpAddress +":"+portnumber+ "/led/" +parameterValue;
            Log.i("msg",url);

            //httpclient 发送请求
            try {
                HttpClient client = new DefaultHttpClient();//创建DefaultHttpClient对象
                HttpGet getRequest = new HttpGet();//创建一个HTTP GET 对象
                getRequest.setURI(new URI(url));// set the URL of the GET request
                HttpResponse response = client.execute(getRequest);// execute the request

                //get the ip address server's reply
                InputStream inputStream = null;
                inputStream = response.getEntity().getContent();
                BufferedReader bufferedReader =
                        new BufferedReader(new InputStreamReader(inputStream));
                // 定义bufferedreader输入流来读取服务器的响应，将socket对应的输入流包装成bufferedreader

                serverResponse = bufferedReader.readLine();
                Log.i("msg",serverResponse);
                inputStream.close();// Close the connection

            } catch (URISyntaxException e) {
                e.printStackTrace();//URL syntax error
                serverResponse = e.getMessage();
            } catch (ClientProtocolException e) {
                e.printStackTrace();//HTTP error
                serverResponse = e.getMessage();
            } catch (IOException e) {
                e.printStackTrace();//IO error
                serverResponse = e.getMessage();
            }

            return serverResponse;// return the server's reply/response text
        }

        @Override
        //HTTP请求返回给ip地址后，函数执行,这个函数用来设置从服务端收到的回复为对话框的内容
        //* 同时如果没有显示完全的话就不显示对话框
        protected void onPostExecute(String s) {
            dialog.setMessage(serverResponse);

            if (!dialog.isShowing())
                dialog.show();
        }

        @Override
        // HTTP的请求发送给ip地址前执行,设置并显示对话框内容
        protected void onPreExecute() {
            dialog.setMessage("Sending data to server, please wait...");

            if (!dialog.isShowing())
                dialog.show();
    }
    }
}
