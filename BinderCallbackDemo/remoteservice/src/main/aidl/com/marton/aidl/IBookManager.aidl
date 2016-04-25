// IBookManager.aidl
package com.marton.aidl;

// Declare any non-default types here with import statements
import com.marton.aidl.Book;
import com.marton.aidl.IOnNewBookArrivedListener;

interface IBookManager {

    List<Book> getBookList();
    void addBook(in Book book);
    void registerListner(IOnNewBookArrivedListener listener);
    void unregisterListener(IOnNewBookArrivedListener listener);
}