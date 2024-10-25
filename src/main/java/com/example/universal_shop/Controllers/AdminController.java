package com.example.universal_shop.Controllers;

import com.example.universal_shop.Models.*;
import com.example.universal_shop.Models.DTOs.*;
import com.example.universal_shop.Models.ModelsView.ImagesView;
import com.example.universal_shop.Models.ModelsView.OrdersView;
import com.example.universal_shop.Services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.IOException;
import java.util.List;

@Controller
public class AdminController {
    private final CategoriesService categoriesService;
    private final GoodsService goodsService;
    private final ImagesService imagesService;
    private final UserService userService;
    private final RoleService roleService;
    private final UserRoleService userRoleService;
    private final OrdersService ordersService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AdminController(CategoriesService categoriesService, GoodsService goodsService, ImagesService imagesService,
                           UserService userService, RoleService roleService, UserRoleService userRoleService,
                           OrdersService ordersService, PasswordEncoder passwordEncoder)
    {
        this.categoriesService = categoriesService;
        this.goodsService = goodsService;
        this.imagesService = imagesService;
        this.userService = userService;
        this.roleService = roleService;
        this.userRoleService = userRoleService;
        this.ordersService = ordersService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/admin-panel")
    public String preIndex(@AuthenticationPrincipal User user, Model model) {
        model.addAttribute("user", user);
        return "admin-panel/index";
    }


    @GetMapping("/admin-panel/")
    public String index(@AuthenticationPrincipal User user, Model model) {
        model.addAttribute("user", user);
        return "admin-panel/index";
    }

    @GetMapping("/admin-panel/bad-request-product")
    public String badRequestProduct(@AuthenticationPrincipal User user, Model model) {
        model.addAttribute("user", user);
        return "admin-panel/bad-request-product";
    }

    @GetMapping("/admin-panel/product-management")
    public String productManagement(@AuthenticationPrincipal User user, Model model) {

        List<Categories> categories = categoriesService.findAll();
        List<Goods> goods = goodsService.findAll();
        List<ImagesView> images = imagesService.findAllView();
        List<OrdersView> orders = ordersService.getAllOrders();


        model.addAttribute("categories", categories);
        model.addAttribute("goods", goods);
        model.addAttribute("images", images);
        model.addAttribute("orders", orders);

        model.addAttribute("user", user);

        return "admin-panel/product-management";
    }

    @GetMapping("/admin-panel/user-management")
    public String userManagement(@AuthenticationPrincipal User user, Model model) {
        List<User> users = userService.findAll();
        List<Role> roles = roleService.findAll();
        model.addAttribute("users", users);
        model.addAttribute("roles", roles);
        model.addAttribute("userAP_DTO", new UserAP_DTO());

        model.addAttribute("user", user);

       return "admin-panel/user-management";
    }

    @PostMapping("/admin-panel/add-category")
    public String addCategory(@ModelAttribute("categoriesDTO") CategoriesDTO categoriesDTO) {
        try {
            categoriesService.saveCategories(categoriesDTO);
            return "redirect:/admin-panel/product-management";
        } catch (IOException ex) {
            return "redirect:/admin-panel/bad-request-product";
        }
    }

    @GetMapping("/admin-panel/edit/category/{id}")
    public String editCategory(@PathVariable("id") Long id, @AuthenticationPrincipal User user, Model model) {
        model.addAttribute("user", user);

        if (categoriesService.existsById(id)) {
            Categories categories = categoriesService.findById(id);
            if (categories != null) {
                CategoriesDTO categoriesDTO = new CategoriesDTO();
                categoriesDTO.setId(categories.getId());
                categoriesDTO.setCategoryName(categories.getCategoryName());
                model.addAttribute("categories", categoriesDTO);
            }
        }

        return "admin-panel/edit/category";
    }

    @PostMapping("/admin-panel/edit/category")
    public String editCategory(@ModelAttribute("categoryDTO") CategoriesDTO categoryDTO) {
        if (categoriesService.existsById(categoryDTO.getId())) {
            Categories ct = categoriesService.findById(categoryDTO.getId());
            try {
                if (categoryDTO.getCategoryName() != null) {
                    ct.setCategoryName(categoryDTO.getCategoryName());
                }
                if (categoryDTO.getImage() != null && !categoryDTO.getImage().isEmpty()) {
                    ct.setImage(categoryDTO.getImage().getBytes());
                }

                categoriesService.editCategories(ct);

                return "redirect:/admin-panel/product-management";

            } catch (IOException ex) {
                return "redirect:/admin-panel/bad-request-product";
            }
        }
        else {
            return "redirect:/admin-panel/bad-request-product";
        }
    }

    @GetMapping("/admin-panel/delete-category/{id}")
    public String deleteCategory(@PathVariable("id") long id) {
        if (categoriesService.existsById(id)) {
            categoriesService.delete(id);
            return "redirect:/admin-panel/product-management";
        }
        else {
            return "redirect:/admin-panel/bad-request-product";
        }
    }

    @PostMapping("/admin-panel/add-product")
    public String addProduct(@ModelAttribute("goodsDTO") GoodsDTO goodsDTO) {
        try {
            goodsService.saveGoods(goodsDTO, null);
            return "redirect:/admin-panel/product-management";
        }
        catch (IllegalArgumentException ex) {
            return "redirect:/admin-panel/bad-request-product";
        }
    }

    @GetMapping("/admin-panel/edit/product/{id}")
    public String editProduct(@PathVariable("id") long id, @AuthenticationPrincipal User user, Model model) {
        model.addAttribute("user", user);
        if (goodsService.existsById(id)) {
            Goods product = goodsService.findById(id);
            if (product != null) {
                ProductEditDTO productEditDTO;
                if (product.getCategories() != null) {
                    productEditDTO = new ProductEditDTO(product.getId(), product.getProductName(), product.getPrice(), product.getBrand(),
                            product.getDescription(), product.getCategories().getId(), categoriesService.findAllCategoriesProductDTO());
                }
                else {
                    productEditDTO = new ProductEditDTO(product.getId(), product.getProductName(), product.getPrice(), product.getBrand(),
                            product.getDescription(), null, categoriesService.findAllCategoriesProductDTO());
                }

                model.addAttribute("productDTO", productEditDTO);
                return "admin-panel/edit/product";
            }
            else {
                return "redirect:/admin-panel/bad-request-product";
            }
        }
        else {
            return "redirect:/admin-panel/bad-request-product";
        }
    }

    @PostMapping("/admin-panel/edit/product")
    public String editProduct(@ModelAttribute("productDTO") ProductEditDTO productDTO) {
        if (goodsService.existsById(productDTO.getId())) {
            GoodsDTO goodsDTO = new GoodsDTO(productDTO.getProductName(), productDTO.getPrice(), productDTO.getBrand(),
                    productDTO.getDescription(), productDTO.getCategoryId());
            try {
                goodsService.saveGoods(goodsDTO, productDTO.getId());
                return "redirect:/admin-panel/product-management";
            } catch (IllegalArgumentException ex) {
                return "redirect:/admin-panel/bad-request-product";
            }
        }
        else {
            return "redirect:/admin-panel/bad-request-product";
        }
    }

    @GetMapping("/admin-panel/delete-product/{id}")
    public String deleteProduct(@PathVariable("id") long id) {
        if (goodsService.existsById(id)) {
            goodsService.delete(id);
            return "redirect:/admin-panel/product-management";
        }
        else {
            return "redirect:/admin-panel/bad-request-product";
        }
    }

    @PostMapping("/admin-panel/add-image")
    public String addImage(@ModelAttribute("imagesDTO") ImagesDTO imagesDTO) {
        try {
            imagesService.save(imagesDTO);
            return "redirect:/admin-panel/product-management";
        } catch (IllegalArgumentException | IOException ex) {
            return "redirect:/admin-panel/bad-request-product";
        }
    }

    @GetMapping("/admin-panel/edit/image/{id}")
    public String editImage(@PathVariable("id") long id, @AuthenticationPrincipal User user, Model model) {
        model.addAttribute("user", user);

        if (imagesService.existsById(id)) {
            Images img = imagesService.findById(id);
            ImageEditDTO imageEditDTO = new ImageEditDTO(img.getId(), img.getImageName(), img.getIsMainImage());
            model.addAttribute("imageDTO", imageEditDTO);
            return "admin-panel/edit/image";
        }
        else {
            return "redirect:/admin-panel/bad-request-product";
        }
    }

    @PostMapping("/admin-panel/edit/image")
    public String editImage(@ModelAttribute("imageDTO") ImageEditDTO imageDTO) {
        if (imagesService.existsById(imageDTO.getImdId())) {
            int res = imagesService.updateByImage(imageDTO);
            if (res > 0) {
                return "redirect:/admin-panel/product-management";
            }
            else {
                return "redirect:/admin-panel/bad-request-product";
            }
        }
        else {
            return "redirect:/admin-panel/bad-request-product";
        }
    }

    @GetMapping("/admin-panel/delete-image/{id}")
    public String deleteImage(@PathVariable("id") long id) {
        if (imagesService.existsById(id)) {
            imagesService.delete(id);
            return "redirect:/admin-panel/product-management";
        }
        else {
            return "redirect:/admin-panel/bad-request-product";
        }
    }

    @PostMapping("/admin-panel/add-role")
    public String addRole(@ModelAttribute("role") Role role) {
        if (!roleService.existsByRole(role.getUserRole())){
            roleService.save(role);
        }
        return "redirect:/admin-panel/user-management";
    }

    @PostMapping("/admin-panel/add-user")
    public String addUser(@ModelAttribute("userAP_DTO") UserAP_DTO user) {
        if (!userService.existsByEmail(user.getEmail())){
            User newUser = new User(user.getName(), user.getSurname(), user.getEmail(), passwordEncoder.encode(user.getPassword()),
                    user.getPhone(), user.isEnabled(), user.isLocked());
            userService.saveUser(newUser);

            Role role = roleService.findById(user.getRole_id());
            if (role != null) {
                userRoleService.save(new UserRole(newUser, role));
            }
        }
        return "redirect:/admin-panel/user-management";
    }

    @GetMapping("/admin-panel/edit/user/{id}")
    public String editUser(@PathVariable(value = "id") long id, @AuthenticationPrincipal User userAt, Model model) {
        model.addAttribute("user", userAt);
        if (userService.existsById(id)){
            User user = userService.findById(id);
            if (user != null) {
                try {
                    UserEditDTO userEditDTO = new UserEditDTO(user.getName(), user.getSurname(), user.getEmail(), user.getPhone(),
                            user.isEnabled(), user.isLocked(), user.getUserRoles(), roleService.findAll());

                    model.addAttribute("userEditDTO", userEditDTO);

                } catch (IllegalArgumentException ex) {
                    return "redirect:/admin-panel/bad-request-user";
                }
            }
        }
        else {
            return "redirect:/admin-panel/bad-request-user";
        }

        return "admin-panel/edit/user";
    }

    @PostMapping("/admin-panel/edit/user")
    public String editUser(@ModelAttribute("userEditDTO") UserEditDTO user) {

        User currentUser = userService.findByEmail(user.getEmail());

        if (currentUser != null) {
            if (user.getName() != null) {
                currentUser.setName(user.getName());
            }
            if (user.getSurname() != null) {
                currentUser.setSurname(user.getSurname());
            }
            if (user.getEmail() != null) {
                currentUser.setEmail(user.getEmail());
            }
            if (user.getPassword() != null && !user.getPassword().isBlank()) {
                if (!passwordEncoder.matches(user.getPassword(), currentUser.getPassword())) {
                    currentUser.setPassword(passwordEncoder.encode(user.getPassword()));
                }
            }
            if (user.getPhone() != null) {
                currentUser.setPhone(user.getPhone());
            }
            currentUser.setEnabled(user.isEnabled());
            currentUser.setLocked(user.isLocked());

            userService.saveUser(currentUser);

            Role role = roleService.findById(user.getRole_id());

            if (role != null) {
                if (!userRoleService.findByUser_IdAndRole_Id(currentUser.getId(), role.getId())) {
                    userRoleService.save(new UserRole(currentUser, role));
                }
            }
        }

        return "redirect:/admin-panel/user-management";
    }

    @GetMapping("/admin-panel/delete-user/{id}")
    public String deleteUser(@PathVariable(value = "id") long id) {
        if (userService.existsById(id)) {

            userService.deleteUser(id);
        }

        return "redirect:/admin-panel/user-management";
    }

    @GetMapping("/admin-panel/delete-user-role/{id}")
    public String deleteUserRole(@PathVariable(value = "id") long id) {
        if (userRoleService.existsById(id)) {
            userRoleService.deleteById(id);
        }

        return "redirect:/admin-panel/user-management";
    }

    @GetMapping("/admin-panel/delete-role/{id}")
    public String deleteRole(@PathVariable(value = "id") long id) {
        if (roleService.existsById(id)) {
            roleService.delete(id);
        }

        return "redirect:/admin-panel/user-management";
    }

    @GetMapping("/admin-panel/processing-complete/{id}")
    public String processingComplete(@PathVariable(value = "id") String id) {
        if (ordersService.existsOrderById(id)) {
            ordersService.saveCompletedOrder(id);
        }
        return "redirect:/admin-panel/product-management";
    }

    @GetMapping("/admin-panel/delete-order/{id}")
    public String deleteOrder(@PathVariable(value = "id") String id) {
        long res = 0;

        if (ordersService.existsOrderById(id)) {
            try {
                ordersService.deleteOrder(id);
            } catch (IllegalArgumentException ex) {
                return "redirect:/admin-panel/bad-request-product";
            }

        }

        return "redirect:/admin-panel/product-management";
    }


    @GetMapping("/admin-panel/image/{id}")
    public ResponseEntity<byte[]> getImage(@PathVariable long id) {
        byte[] image = imagesService.findById(id).getImage();

        if (image == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok().header("Content-Type", "image/png").body(image);
    }

    @GetMapping("/admin-panel/category-image/{id}")
    public ResponseEntity<byte[]> getCategoryImage(@PathVariable long id) {
        byte[] image = categoriesService.findById(id).getImage();

        if (image == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().header("Content-Type", "image/png").body(image);
    }


}
