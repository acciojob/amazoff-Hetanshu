package com.driver;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class OrderRepository {

    Map<String,Order> orderMap=new HashMap<>();
    Map<String,DeliveryPartner> deliveryPartnerMap=new HashMap<>();
    Map<String, List<String>> partnerOrdersMap=new HashMap<>();
    Map<String,String> orderPartnerMap=new HashMap<>();
    public void addOrder(Order order) {
        orderMap.put(order.getId(),order);
    }

    public void addPartner(String partnerId) {
        DeliveryPartner partner=new DeliveryPartner(partnerId);
        deliveryPartnerMap.put(partnerId,partner);
    }

    public void addOrderPartnerPair(String orderId, String partnerId) {
        if(orderMap.containsKey(orderId) && deliveryPartnerMap.containsKey(partnerId)){
            DeliveryPartner partner=deliveryPartnerMap.get(partnerId);
            partner.setNumberOfOrders(deliveryPartnerMap.get(partnerId).getNumberOfOrders()+1);
            deliveryPartnerMap.put(partnerId,partner);

            List<String> orderIDList;
            if(partnerOrdersMap.containsKey(partnerId)) orderIDList=partnerOrdersMap.get(partnerId);
            else orderIDList=new ArrayList<>();
            orderIDList.add(orderId);
            partnerOrdersMap.put(partnerId,orderIDList);

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
        return deliveryPartnerMap.get(partnerId).getNumberOfOrders();
    }

    public List<String> getOrdersByPartnerId(String partnerId) {
        return partnerOrdersMap.get(partnerId);
    }

    public List<String> getAllOrders() {
        List<String> allOrdersList=new ArrayList<>();

        for(String orderID:orderMap.keySet()) allOrdersList.add(orderID);
        return allOrdersList;
    }

    public Integer getCountOfUnassignedOrders() {
        Integer count=0;

        for(String OrderID:orderMap.keySet()){
            if(!orderPartnerMap.containsKey(OrderID)) count++;
        }
        return count;
    }
}
