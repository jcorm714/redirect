package com.example.redirectv2;
import android.content.Context;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import  java.util.regex.*;


public class TextParser {
    private static final String EMAIL_PATTERN = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";
    private static final String PHONE_PATTERN = "^([0-9]( |-)?)?(\\(?[0-9]{3}\\)?|[0-9]{3})( |-)?([0-9]{3}( |-)?[0-9]{4}|[a-zA-Z0-9]{7})$";
    private static TextParser instance;
    private Pattern emailPattern = Pattern.compile(EMAIL_PATTERN);
    private Pattern phonePattern = Pattern.compile(PHONE_PATTERN);

    public static TextParser getInstance()
    {
        if (instance == null)
        {
            instance =  new TextParser();
        }
        return  instance;
    }


    public List<Contact> findMatches(String content)
    {
        ArrayList<Contact> contacts = new ArrayList<>();
        Matcher matcher = phonePattern.matcher(content);
        Matcher matcher2 = emailPattern.matcher(content);
        Contact temp;
        if(matcher.find())
        {
            temp = new Contact(Contact.TYPE_PHONE, matcher.group());

            contacts.add(temp);
        }

        if(matcher2.find())
        {
            temp = new Contact(Contact.TYPE_EMAIL, matcher2.group());
            contacts.add(temp);
        }


        return contacts;
    }

    public void SaveContacts(Context c, List<Contact> contacts )
    {
        DatabaseHandler db = new DatabaseHandler(c);
        Iterator<Contact> contactItr = contacts.iterator();
        while(contactItr.hasNext())
        {
            db.addContact(contactItr.next());
        }
        db.close();
    }
}
