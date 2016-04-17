// IBookManager.aidl
package com.marton.aidl;

// Declare any non-default types here with import statements
import com.marton.aidl.Book;

interface IBookManager {

    List<Book> getBookList();
    void addBook(in Book book);
}
