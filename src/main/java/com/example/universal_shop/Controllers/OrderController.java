package com.example.universal_shop.Controllers;


import com.example.universal_shop.Models.DTOs.BasketDTO;
import com.example.universal_shop.Models.DTOs.ProductBasketDTO;
import com.example.universal_shop.Models.DTOs.UserOrderDTO;
import com.example.universal_shop.Models.Orders;
import com.example.universal_shop.Models.User;
import com.example.universal_shop.Services.OrdersService;
import com.example.universal_shop.Services.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.*;

@Controller
public class OrderController {
    private final OrdersService ordersService;
    private final UserService userService;

    @Autowired
    public OrderController(OrdersService ordersService, UserService userService) {
        this.ordersService = ordersService;
        this.userService = userService;
    }

    @GetMapping("/order")
    public String order(@AuthenticationPrincipal User user, Model model) {
        if (user != null) {
            model.addAttribute("user", user);
        }
        return "order";
    }

    @PostMapping("/place/order")
    public String placeOrder(HttpSession session, @ModelAttribute("userOrderDTO") UserOrderDTO userOrderDTO) {
        BasketDTO basketDTO = (BasketDTO) session.getAttribute("formOrder");
        User user = userService.findByEmail(userOrderDTO.getUserEmail());

        if (user != null && basketDTO != null) {
            Orders orders = new Orders();

            orders.setUser_id(user.getId());

            Map<Long, Long> goodsId = new HashMap<>();
            for (ProductBasketDTO products : basketDTO.getGoodsList()) {
                goodsId.put(products.getGoods().getId(), products.getQuantity());
            }

            orders.setProduct(goodsId);
            orders.setQuantity(basketDTO.getCount());
            orders.setPrice(basketDTO.getTotalPrice());
            orders.setProcessed(false);
            String u_id = generateOrderIdentifier();
            while (ordersService.existsOrderByOrderId(u_id)) {
                u_id = generateOrderIdentifier();
            }
            orders.setOrderIdentifier(u_id);

            ordersService.saveOrder(orders);

            return "redirect:/";
        }
        else {
            return "redirect:/error/fail-create-order";
        }
    }

    private String generateOrderIdentifier() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 10).toUpperCase();
    }

}
