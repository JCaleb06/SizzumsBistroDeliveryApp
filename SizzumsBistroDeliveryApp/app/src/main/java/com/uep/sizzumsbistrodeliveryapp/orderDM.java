package com.uep.sizzumsbistrodeliveryapp;

public class orderDM {
    String customer,address,contactnumber,ordernumber,status;
    int totalpayment;

    public orderDM(String status,String customer, String address, String contactnumber, int totalpayment, String ordernumber){
        this.customer = customer;
        this.status = status;
        this.address = address;
        this.contactnumber = contactnumber;
        this.totalpayment = totalpayment;
        this.ordernumber = ordernumber;
    }

    public String getStatus() {
        return status;
    }

    public String getOrdernumber() {
        return ordernumber;
    }

    public String getCustomer() {
        return customer;
    }
    public String getAddress() {
        return address;
    }
    public int getTotalpayment() {
        return totalpayment;
    }
    public String getContactnumber() {
        return contactnumber;
    }
}
