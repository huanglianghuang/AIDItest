package com.example.server;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.widget.Toast;

public class AIDLService extends Service {
    MyBookManager bookManager = new MyBookManager();
    public AIDLService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        Toast.makeText(this,"client 连接成功", Toast.LENGTH_LONG).show();
        return bookManager;
    }

    public class MyBookManager extends IMyBookManager.Stub {

        @Override
        public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString) throws RemoteException {

        }

        @Override
        public String login(String userName, String pwd) throws RemoteException {
            if (userName.equalsIgnoreCase("admain") && pwd.equalsIgnoreCase("123456")) {
                return "success";
            }
            return "failed";
        }

        @Override
        public Book queryByName(String bookName) throws RemoteException {
            Book book = new Book();
            book.setName(bookName);
            book.setPrice(100);
            return book;
        }
    }
}