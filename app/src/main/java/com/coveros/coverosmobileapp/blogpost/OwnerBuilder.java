package com.coveros.coverosmobileapp.blogpost;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;

/**
 * @author Maria Kim
 */

public class OwnerBuilder {

    public static class Owner {
//        private String name;
        private String email;

//        public void setName(String name) {
//            this.name = name;
//        }
        public void setEmail(String email) {
            this.email = email;
        }

//        public String getName() {
//            return name;
//        }
        public String getEmail() {
            return email;
        }
        public String toString() {
            return email;
        }
    }

    public static Owner getOwner(Context context) {
        Owner owner = new Owner();

        AccountManager accountManager = AccountManager.get(context);
        Account[] accounts = accountManager.getAccountsByType("com.google");
        Account account;

        if (accounts.length > 0) {
            account = accounts[0];
        }
        else {
            account = null;
        }
        if (account == null) {
            return null;
        }
        else {
            owner.setEmail(account.name);
        }
        return owner;
    }


}
