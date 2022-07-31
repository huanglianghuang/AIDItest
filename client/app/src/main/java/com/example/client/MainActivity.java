package com.example.client;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private Button btnConnection;
    private EditText etName;
    private EditText etPwd;
    private Button btnLogin;
    private TextView tvBookInfo;
    private Button btnQuery;
    private EditText etBookName;
    private boolean islogin=false;

    private IMyBookManager bookManager = null;
    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            bookManager = IMyBookManager.Stub.asInterface(service);
            Toast.makeText(MainActivity.this, "服务连接成功！", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            bookManager = null;
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        etBookName = findViewById(R.id.etBookName);
        etName = findViewById(R.id.etName);
        etPwd = findViewById(R.id.etPwd);
        btnLogin = findViewById(R.id.btnLogin);
        btnConnection = findViewById(R.id.btnConnection);
        btnQuery = findViewById(R.id.btnQuery);
        tvBookInfo = findViewById(R.id.tvBookInfo);
        tvBookInfo.setMovementMethod(ScrollingMovementMethod.getInstance());
        connect();
    }

    private void connect(){
        btnConnection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bookManager != null) {
                    Toast.makeText(MainActivity.this, "bookManager服务连接成功！", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "尝试连接Server！", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent();
                    intent.setAction("com.example.server.bookService");
                    intent.setPackage("com.example.server");
                    bindService(intent, conn, Context.BIND_AUTO_CREATE);
                }
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bookManager == null)
                {
                    Toast.makeText(MainActivity.this, "还没有绑定服务!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(etName.getText().toString().isEmpty() || etPwd.getText().toString().isEmpty()){
                    Toast.makeText(MainActivity.this, "用户名或密码不能为空!", Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    String resStr = bookManager.login(etName.getText().toString(), etPwd.getText().toString());
                    if(resStr.compareToIgnoreCase("success") == 0){
                        islogin=true;
                        Toast.makeText(MainActivity.this, "登录成功！", Toast.LENGTH_SHORT).show();
                    } else{
                        Toast.makeText(MainActivity.this, "登录失败！", Toast.LENGTH_SHORT).show();
                        throw new RemoteException("登录失败");
                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, "登录失败！", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnQuery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bookManager == null || !islogin)
                {
                    Toast.makeText(MainActivity.this, "请先绑定服务并登录!", Toast.LENGTH_LONG).show();
                    return;
                }
                if(etBookName.getText().toString().isEmpty()){
                    Toast.makeText(MainActivity.this, "请输入您要查找的图书！", Toast.LENGTH_LONG).show();
                    return;
                }
                try {
                    Book book = bookManager.queryByName(etBookName.getText().toString());
                    if(book != null){
                        tvBookInfo.append(book.toString());
                    } else{
                        throw new RemoteException("查询失败！");
                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
                    tvBookInfo.append("查询失败！");
                }
            }
        });
    }
}