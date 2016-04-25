// IOnNewBookArrivedListener.aidl
package com.marton.aidl;

// Declare any non-default types here with import statements

import com.marton.aidl.Book;

interface IOnNewBookArrivedListener {

    void onNewBookArrived(in Book newBook);
}
