package com.driver;

import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class OrderRepository {

    Map<String,Order> orderMap=new HashMap<>();
    Map<String,DeliveryPartner> deliveryPartnerMap=new HashMap<>();
    Map<String, HashSet<String>> partnerOrdersMap=new HashMap<>();
    Map<String,String> orderPartnerMap=new HashMap<>();
    public void addOrder(Order order) {
        orderMap.put(order.getId(),order);
    }

    public void addPartner(String partnerId) {
        DeliveryPartner partner=new DeliveryPartner(partnerId);
        deliveryPartnerMap.put(partnerId,partner);
    }

    public void createOrderPartnerPair(String orderId, String partnerId) {
        if(orderMap.containsKey(orderId) && deliveryPartnerMap.containsKey(partnerId)){

            HashSet<String> orderIDSet;
            if(partnerOrdersMap.containsKey(partnerId)) orderIDSet=partnerOrdersMap.get(partnerId);
            else orderIDSet=new HashSet<>();
            orderIDSet.add(orderId);
            partnerOrdersMap.put(partnerId,orderIDSet);

            DeliveryPartner partner=deliveryPartnerMap.get(partnerId);
            partner.setNumberOfOrders(partnerOrdersMap.get(partnerId).size());
            deliveryPartnerMap.put(partnerId,partner);

            orderPartnerMap.put(orderId,partnerId);
        }
    }

    public Order getOrderById(String orderId) {
        return orderMap.get(orderId);
    }

    public DeliveryPartner getPartnerById(String partnerId) {
        return deliveryPartnerMap.get(partnerId);
    }

    public Integer getOrderCountByPartnerId(String partnerId) {
        Integer count=0;
        if(deliveryPartnerMap.containsKey(partnerId)) count=deliveryPartnerMap.get(partnerId).getNumberOfOrders();
        return count;
    }

    public List<String> getOrdersByPartnerId(String partnerId) {
        HashSet<String> orderList=new HashSet<>();
        if(partnerOrdersMap.containsKey(partnerId)) orderList=partnerOrdersMap.get(partnerId);
        return new ArrayList<>(orderList);
    }

    public List<String> getAllOrders() {
        return new ArrayList<>(orderMap.keySet());
    }

    public Integer getCountOfUnassignedOrders() {
        Integer count=0;

        for(String OrderID:orderMap.keySet()){
            if(!orderPartnerMap.containsKey(OrderID)) count++;
        }
        return count;
    }

    public Integer getOrdersLeftAfterGivenTimeByPartnerId(String time, String partnerId) {
        Integer count=0;
        int userTime=Integer.parseInt(time.substring(0,2))*60+Integer.parseInt(time.substring(3));
        for(String orderId:partnerOrdersMap.get(partnerId)){
            if(orderMap.get(orderId).getDeliveryTime()>userTime) count++;
        }
        return count;
    }

    public String getLastDeliveryTimeByPartnerId(String partnerId) {
        int time=0;
        for(String orderId:partnerOrdersMap.get(partnerId)){
            int deliveryTime=orderMap.get(orderId).getDeliveryTime();
            time=Math.max(time,deliveryTime);
        }
        return String.valueOf(time/60)+":"+String.valueOf(time%60);
    }

    public void deletePartnerById(String partnerId) {

        for(String orderID:partnerOrdersMap.get(partnerId)){
            orderPartnerMap.remove(orderID);
        }
        partnerOrdersMap.remove(partnerId);
        deliveryPartnerMap.remove(partnerId);
    }

    public void deleteOrderById(String orderId) {
        deliveryPartnerMap.remove(orderPartnerMap.get(orderId));
        orderPartnerMap.remove(orderId);
        orderMap.remove(orderId);
    }
}
