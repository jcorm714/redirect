package com.example.redirectv2;

public class Contact {
    public static final int TYPE_EMAIL = 10012;
    public static final int TYPE_PHONE = 10023;

    private int id;
    private String contactInfo;
    private int contactType;

    public Contact(int type, String value) {
        this.contactType = type;
        this.contactInfo = value;
    }

    public Contact()
    {
        this(-1,null);
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(String newContact)
    {
        this.contactInfo = newContact;
    }

    public void setContactType(int type)
    {
        this.contactType = type;
    }

    public int getContactType()
    {
        return this.contactType;
    }

}
