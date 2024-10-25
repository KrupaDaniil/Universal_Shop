package com.example.universal_shop.Controllers;


import com.example.universal_shop.Enum.UserRoles;
import com.example.universal_shop.Models.DTOs.BasketDTO;
import com.example.universal_shop.Models.DTOs.ProductBasketDTO;
import com.example.universal_shop.Models.DTOs.UserOrderDTO;
import com.example.universal_shop.Models.ModelsView.UserOrdersView;
import com.example.universal_shop.Models.Orders;
import com.example.universal_shop.Models.Role;
import com.example.universal_shop.Models.User;
import com.example.universal_shop.Models.UserRole;
import com.example.universal_shop.Services.OrdersService;
import com.example.universal_shop.Services.UserRoleService;
import com.example.universal_shop.Services.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.*;

@Controller
public class OrderController {
    private final OrdersService ordersService;
    private final UserService userService;
    private final UserRoleService userRoleService;

    @Autowired
    public OrderController(OrdersService ordersService, UserService userService, UserRoleService userRoleService) {
        this.ordersService = ordersService;
        this.userService = userService;
        this.userRoleService = userRoleService;
    }

    @PostMapping("/place/order")
    public String placeOrder(HttpSession session, @ModelAttribute("userOrderDTO") UserOrderDTO userOrderDTO) {
        BasketDTO basketDTO = (BasketDTO) session.getAttribute("formOrder");
        User user = userService.findByEmail(userOrderDTO.getUserEmail());

        if (basketDTO != null) {
            Orders orders = new Orders();

            if (user != null ) {
                orders.setUser_id(user.getId());
            }
            else {
                User unknownUser = new User();
                unknownUser.setEmail(userOrderDTO.getUserEmail());
                Role role = new Role();
                role.setUserRole(UserRoles.ROLE_ANONYMOUS.toString());
                userService.saveUser(unknownUser);
                UserRole userRole = new UserRole(unknownUser, role);
                userRoleService.save(userRole);
            }


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

            if (ordersService.existsOrderByOrderId(u_id)) {
                basketDTO = null;
                session.setAttribute("formOrder", basketDTO);
            }

            return "redirect:/";
        }
        else {
            return "redirect:/error/fail-create-order";
        }
    }

    private String generateOrderIdentifier() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 10).toUpperCase();
    }

    @GetMapping("/management/orders")
    public String getUserOrders(@AuthenticationPrincipal User user, Model model) {
        if (user != null) {
            model.addAttribute("user", user);
            List<UserOrdersView> orders = ordersService.getAllUserOrders(user.getId());
            model.addAttribute("userOrders", orders);
        }

        return "management/orders";
    }

    @GetMapping("/management/orders/{orderId}")
    public String canceledOrder(@PathVariable String orderId) {
        ordersService.saveCanceledOrder(orderId);

        return "redirect:/management/orders";
    }


}
