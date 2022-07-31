// IMyBookManager.aidl
package com.example.client;
import com.example.client.Book;
// Declare any non-default types here with import statements
// 引入一个序列化的parcelable类
parcelable Book;
interface IMyBookManager {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,
            double aDouble, String aString);

    String login(String userName, String pwd);

    Book queryByName(String bookName);
}