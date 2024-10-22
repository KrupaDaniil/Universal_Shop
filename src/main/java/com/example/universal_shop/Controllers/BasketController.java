package com.example.universal_shop.Controllers;

import com.example.universal_shop.Enum.BasketActions;
import com.example.universal_shop.Models.DTOs.BasketDTO;
import com.example.universal_shop.Models.DTOs.ProductBasketDTO;
import com.example.universal_shop.Models.Goods;
import com.example.universal_shop.Models.User;
import com.example.universal_shop.Services.GoodsService;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;

@Controller
public class BasketController {
    private final GoodsService goodsService;

    public BasketController(GoodsService goodsService) {
        this.goodsService = goodsService;
    }

    @GetMapping("/basket")
    public String basket(@AuthenticationPrincipal User user, HttpSession session, Model model) {
        BasketDTO basketDTO = (BasketDTO) session.getAttribute("formOrder");
        if (basketDTO != null) {
            model.addAttribute("basket", basketDTO);
        }
        model.addAttribute("user", user);
        return "basket";
    }

    @GetMapping("/basket/{productId}")
    public String formOrder(@PathVariable long productId, HttpSession session, Model model) {
        Goods product = goodsService.findById(productId);
        BasketDTO basketDTO = (BasketDTO) session.getAttribute("formOrder");

        List<ProductBasketDTO> goods;

        if (product == null) {
            throw new IllegalArgumentException("Product id " + productId + " not found");
        }

        if (basketDTO == null) {
            goods = new ArrayList<>();
            goods.add(new ProductBasketDTO(product, 1, product.getPrice()));
            basketDTO = new BasketDTO(goods, 1, product.getPrice());
        }
        else {
            long pr = basketDTO.getGoodsList().stream().filter(r -> r.getGoods().getId() == product.getId()).count();

            if (pr > 0) {

                basketDTO.getGoodsList().forEach(obj -> {
                    if (obj.getGoods().getId() == product.getId()) {
                        obj.setQuantity(obj.getQuantity() + 1);
                        obj.setFinalPrice(obj.getFinalPrice() + product.getPrice());
                    }
                });
                basketDTO.setCount(basketDTO.getCount() + 1);
                basketDTO.setTotalPrice(basketDTO.getGoodsList().stream().map(r -> r.getGoods().getPrice()).reduce(Double::sum).orElse(0.0));
            }
            else {
                basketDTO.getGoodsList().add(new ProductBasketDTO(product, 1, product.getPrice()));
                basketDTO.setCount(basketDTO.getCount() + 1);
                basketDTO.setTotalPrice(basketDTO.getTotalPrice() + product.getPrice());

            }
        }

        session.setAttribute("formOrder", basketDTO);
        model.addAttribute("basket", basketDTO);

        return "redirect:/basket";
    }

    @GetMapping("basket/up/{productId}/{action}")
    public String upDown(@PathVariable long productId, @PathVariable String action,
                         HttpSession session, Model model) {

        BasketDTO basketDTO = (BasketDTO) session.getAttribute("formOrder");

        if (basketDTO != null) {
            boolean yep = false;

            for (ProductBasketDTO product : basketDTO.getGoodsList()) {
                if (product.getGoods().getId() == productId) {
                    if (action.equals(BasketActions.ProductUP.toString())) {
                        product.setQuantity(product.getQuantity() + 1);
                        product.setFinalPrice(product.getFinalPrice() + product.getGoods().getPrice());
                        yep = true;
                    }
                    if (action.equals(BasketActions.ProductDOWN.toString())) {
                        if (product.getQuantity() == 1) {
                            basketDTO.getGoodsList().remove(product);
                            yep = true;
                            break;
                        }
                        if (product.getQuantity() >= 1) {
                            product.setQuantity(product.getQuantity() - 1);
                            product.setFinalPrice(product.getFinalPrice() - product.getGoods().getPrice());
                            yep = true;
                        }
                    }
                }
            }

            if (yep) {
                if (!basketDTO.getGoodsList().isEmpty())
                {
                    if (action.equals(BasketActions.ProductUP.toString())) {
                        basketDTO.setCount(basketDTO.getCount() + 1);
                        basketDTO.setTotalPrice(basketDTO.getGoodsList().stream().map(ProductBasketDTO::getFinalPrice).reduce(Double::sum).orElse(0.0));
                    }
                    if (action.equals(BasketActions.ProductDOWN.toString())) {
                        basketDTO.setCount(basketDTO.getCount() - 1);
                        basketDTO.setTotalPrice(basketDTO.getGoodsList().stream().map(ProductBasketDTO::getFinalPrice).reduce(Double::sum).orElse(0.0));
                    }
                }
                else {
                    basketDTO.setCount(0);
                    basketDTO.setTotalPrice(0);
                }
            }

            session.setAttribute("formOrder", basketDTO);
            model.addAttribute("basket", basketDTO);
        }

        return "redirect:/basket";
    }


}
