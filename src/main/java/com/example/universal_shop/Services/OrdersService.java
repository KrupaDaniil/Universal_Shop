package com.example.universal_shop.Services;

import com.example.universal_shop.Models.Goods;
import com.example.universal_shop.Models.ModelsView.OrdersView;
import com.example.universal_shop.Models.Orders;
import com.example.universal_shop.Repo.IGoodsRepository;
import com.example.universal_shop.Repo.IOrdersRepository;
import com.example.universal_shop.Repo.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class OrdersService {
    private final IOrdersRepository ordersRepository;
    private final IGoodsRepository goodsRepository;
    private final IUserRepository userRepository;

    @Autowired
    public OrdersService(IOrdersRepository ordersRepository, IGoodsRepository goodsRepository, IUserRepository userRepository) {
        this.ordersRepository = ordersRepository;
        this.goodsRepository = goodsRepository;
        this.userRepository = userRepository;
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

    public List<OrdersView> getAllOrders() {
        List<Orders> orders = ordersRepository.findAll();

        if (!orders.isEmpty()) {
            List<OrdersView> ordersViews = new ArrayList<>();

            OrdersView ordersView = new OrdersView();

            for (Orders order : orders) {
                Map<Goods, Long> goodsMap = new HashMap<>();
                ordersView.setId(order.getId());
                ordersView.setUser(userRepository.findById(order.getUser_id()).orElse(null));
                for (Map.Entry<Long, Long> entry: order.getProduct().entrySet()) {
                    goodsMap.put(goodsRepository.findById(entry.getKey()).orElse(null), entry.getValue());
                }
                ordersView.setProducts(goodsMap);
                ordersView.setQuantity(order.getQuantity());
                ordersView.setPrice(order.getPrice());
                ordersView.setProcessed(order.isProcessed());
                ordersView.setOrderIdentifier(order.getOrderIdentifier());

                ordersViews.add(ordersView);
            }

            return ordersViews;
        }

        return null;
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
