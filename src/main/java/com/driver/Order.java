package com.driver;

public class Order {

    private String id;
    private int deliveryTime;

    public Order(String id, String deliveryTime) {

        // The deliveryTime has to converted from string to int and then stored in the attribute
        //deliveryTime  = HH*60 + MM
        this.id=id;
//        String[] arrOfStr=deliveryTime.split(":");
//        this.deliveryTime=Integer.parseInt(arrOfStr[0])*60+Integer.parseInt(arrOfStr[1]);
        this.deliveryTime=Integer.valueOf(deliveryTime.substring(0,2))*60+Integer.valueOf(deliveryTime.substring(3));
    }

    public String getId() {
        return id;
    }

    public int getDeliveryTime() {return deliveryTime;}
}
