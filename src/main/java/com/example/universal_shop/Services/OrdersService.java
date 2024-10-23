package com.example.universal_shop.Services;

import com.example.universal_shop.Models.Orders;
import com.example.universal_shop.Repo.IOrdersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class OrdersService {
    private final IOrdersRepository ordersRepository;

    @Autowired
    public OrdersService(IOrdersRepository ordersRepository) {
        this.ordersRepository = ordersRepository;
    }

    public void saveOrder(Orders orders) {
        ordersRepository.save(orders);
    }

    public void saveCompletedOrder(String id) {
        Orders orders = ordersRepository.findById(id).orElse(null);

        if (orders == null) {
            throw new IllegalArgumentException("Order from id " + id + " not found");
        }

        orders.setProcessed(true);

        ordersRepository.save(orders);
    }

    public List<Orders> getAllOrders() {
        return ordersRepository.findAll();
    }

    public Set<Orders> getAllUserOrders(long userId) {
        return ordersRepository.findByUser_id(userId);
    }

    public Orders getOrderById(String id) {
        return ordersRepository.findById(id).orElse(null);
    }

    public Orders getOrderByOrderId(String orderId) {
        return ordersRepository.findByOrderIdentifier(orderId).orElse(null);
    }

    public long deleteOrder(String id) {
        return ordersRepository.deleteById(id);
    }

    public boolean existsOrderById(String id) {
        return ordersRepository.existsById(id);
    }

    public boolean existsOrderByOrderId(String orderId) {
        return ordersRepository.existsByOrderIdentifier(orderId);
    }

    public boolean existsCompletedOrderById(String id) {
        return ordersRepository.existsByProcessedAndId(id);
    }

    public boolean existsCompletedOrderByOrderId(String orderId) {
        return ordersRepository.existsByProcessedAndOrderIdentifier(orderId);
    }



}
