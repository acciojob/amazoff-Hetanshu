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
        Integer userTime=Integer.parseInt(time.substring(0,2))*60+Integer.parseInt(time.substring(3));
        if(partnerOrdersMap.containsKey(partnerId)) {
            for (String orderId : partnerOrdersMap.get(partnerId)) {
                if(orderMap.containsKey(orderId)) {
                    if (orderMap.get(orderId).getDeliveryTime() > userTime) count++;
                }
            }
        }
        return count;
    }

    public String getLastDeliveryTimeByPartnerId(String partnerId) {
        Integer time=0;
        if(partnerOrdersMap.containsKey(partnerId)) {
            for (String orderId : partnerOrdersMap.get(partnerId)) {
                if(orderMap.containsKey(orderId)) time = Math.max(time, orderMap.get(orderId).getDeliveryTime());
            }
        }
        // convert time to string
        String hours=String.valueOf(time/60);
        String minutes=String.valueOf(time%60);

        if(hours.length()==1) hours="0"+hours;
        if(minutes.length()==1) minutes="0"+minutes;
        return hours+":"+minutes;
    }

    public void deletePartnerById(String partnerId) {

        if(partnerOrdersMap.containsKey(partnerId)) {
            for (String orderID : partnerOrdersMap.get(partnerId)) {
                if(orderPartnerMap.containsKey(orderID)) orderPartnerMap.remove(orderID);
            }
            partnerOrdersMap.remove(partnerId);
        }

        if(deliveryPartnerMap.containsKey(partnerId)) deliveryPartnerMap.remove(partnerId);
    }

    public void deleteOrderById(String orderId) {
//        deliveryPartnerMap.remove(orderPartnerMap.get(orderId));
//        orderPartnerMap.remove(orderId);

        if(orderPartnerMap.containsKey(orderId)){
            String partnerId=orderPartnerMap.get(orderId);
            HashSet<String> orders=partnerOrdersMap.get(partnerId);
            orders.remove(orderId);
            partnerOrdersMap.put(partnerId,orders);

            DeliveryPartner partner=deliveryPartnerMap.get(partnerId);
            partner.setNumberOfOrders(orders.size());
            deliveryPartnerMap.put(partnerId,partner);

            orderPartnerMap.remove(orderId);
        }

        if(orderMap.containsKey(orderId)) orderMap.remove(orderId);
    }
}
