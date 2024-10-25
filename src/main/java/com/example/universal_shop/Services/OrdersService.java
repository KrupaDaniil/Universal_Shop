package com.example.universal_shop.Services;

import com.example.universal_shop.Models.Goods;
import com.example.universal_shop.Models.ModelsView.OrdersView;
import com.example.universal_shop.Models.ModelsView.UserOrdersView;
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
        Orders orders = getOrderById(id);

        if (orders == null) {
            throw new IllegalArgumentException("Order from id " + id + " not found");
        }

        orders.setProcessed(true);

        ordersRepository.save(orders);
    }

    public void saveCanceledOrder(String orderId) {
        Orders orders = getOrderByOrderId(orderId);

        if (orders == null) {
            throw new IllegalArgumentException("Order from id " + orderId + " not found");
        }

        orders.setCanceledUser(true);

        ordersRepository.save(orders);
    }

    public List<OrdersView> getAllOrders() {
        List<Orders> orders = ordersRepository.findAll();

        if (!orders.isEmpty()) {
            List<OrdersView> ordersViews = new ArrayList<>();

            for (Orders order : orders) {
                ordersViews.add(new OrdersView(order.getId(), userRepository.findById(order.getUser_id()).orElse(null),
                        convertToGoodsMap(order.getProduct()), order.getQuantity(), order.getPrice(), order.isProcessed(),
                        order.isCanceledUser(), order.getOrderIdentifier()));
            }

            return ordersViews;
        }

        return null;
    }

    public List<UserOrdersView> getAllUserOrders(long userId) {
        List<Orders>  userOrders = ordersRepository.findByUser_id(userId);

        if (userOrders.isEmpty()) {
            return null;
        }

        List<UserOrdersView> userOrdersViews = new ArrayList<>();;

        for (Orders order : userOrders) {

            userOrdersViews.add(new UserOrdersView(convertToGoodsMap(order.getProduct()), order.getQuantity(), order.getPrice(),
                    order.isProcessed(), order.isCanceledUser(), order.getOrderIdentifier()));
        }

        return userOrdersViews;
    }

    public Orders getOrderById(String id) {
        return ordersRepository.findById(id).orElse(null);
    }

    public Orders getOrderByOrderId(String orderId) {
        return ordersRepository.findByOrderIdentifier(orderId).orElse(null);
    }

    public void deleteOrder(String id) {
        ordersRepository.deleteById(id);
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

    private Map<Goods, Long> convertToGoodsMap(Map<Long, Long> userGoods) {
        Map<Goods, Long> goodsMap = new HashMap<>();

        for (Map.Entry<Long, Long> entry: userGoods.entrySet()) {
            goodsMap.put(goodsRepository.findById(entry.getKey()).orElse(null), entry.getValue());
        }

        return goodsMap;
    }


}
