package com.marton.aidl;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by marton on 16/4/19.
 */
public class Book implements Parcelable {

    public int mBookId;
    public String mBookName;

    public Book(int bookId, String bookName){
        this.mBookId = bookId;
        this.mBookName = bookName;
    }

    private Book(Parcel source){
        mBookId = source.readInt();
        mBookName = source.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mBookId);
        dest.writeString(mBookName);
    }

    public static final Creator<Book> CREATOR = new Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel source) {
            return new Book(source);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("bookId : ").append(mBookId).append(" bookName : ").append(mBookName);
        return sb.toString();
    }
}
