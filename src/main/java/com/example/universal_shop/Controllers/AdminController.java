package com.example.universal_shop.Controllers;

import com.example.universal_shop.Models.*;
import com.example.universal_shop.Models.DTOs.*;
import com.example.universal_shop.Models.ModelsView.ImagesView;
import com.example.universal_shop.Services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

    @Autowired
    public AdminController(CategoriesService categoriesService, GoodsService goodsService, ImagesService imagesService,
                           UserService userService, RoleService roleService, UserRoleService userRoleService)
    {
        this.categoriesService = categoriesService;
        this.goodsService = goodsService;
        this.imagesService = imagesService;
        this.userService = userService;
        this.roleService = roleService;
        this.userRoleService = userRoleService;
    }

    @GetMapping("/admin-panel/")
    public String index() {
        return "admin-panel/index";
    }

    @GetMapping("/admin-panel/bad-request-product")
    public String badRequestProduct() {
        return "admin-panel/bad-request-product";
    }

    @GetMapping("/admin-panel/product-management")
    public String productManagement(Model model) {

        List<Categories> categories = categoriesService.findAll();
        List<Goods> goods = goodsService.findAll();
        List<ImagesView> images = imagesService.findAllView();

        model.addAttribute("categories", categories);
        model.addAttribute("goods", goods);
        model.addAttribute("images", images);

        return "admin-panel/product-management";
    }

    @GetMapping("/admin-panel/user-management")
    public String userManagement(Model model) {
        List<User> users = userService.findAll();
        List<Role> roles = roleService.findAll();
        model.addAttribute("users", users);
        model.addAttribute("roles", roles);
        model.addAttribute("userAP_DTO", new UserAP_DTO());

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
    public String editCategory(@PathVariable("id") long id, Model model) {
        if (categoriesService.existsById(id)) {
            Categories categories = categoriesService.findById(id);
            CategoriesDTO categoriesDTO = new CategoriesDTO();
            categoriesDTO.setCategoryName(categories.getCategoryName());
            model.addAttribute("categories", categoriesDTO);
        }

        return "admin-panel/edit/category";
    }

    @PostMapping("/admin-panel/edit/category/{id}")
    public String editCategory(@PathVariable("id") long id, @ModelAttribute("categoryDTO") CategoriesDTO categoryDTO) {
        if (categoriesService.existsById(id)) {
            Categories ct = categoriesService.findById(id);
            try {
                ct.setCategoryName(categoryDTO.getCategoryName());
                ct.setImage(categoryDTO.getImage().getBytes());
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



    @PostMapping("/admin-panel/add-product")
    public String addProduct(@ModelAttribute("goodsDTO") GoodsDTO goodsDTO) {
        try {
            goodsService.saveGoods(goodsDTO);
            return "redirect:/admin-panel/product-management";
        }
        catch (IllegalArgumentException ex) {
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
            User newUser = new User(user.getName(), user.getSurname(), user.getEmail(), user.getPassword(),
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
    public String editUser(@PathVariable(value = "id") long id, Model model) {
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
            currentUser.setName(user.getName());
            currentUser.setSurname(user.getSurname());
            currentUser.setEmail(user.getEmail());
            currentUser.setPassword(user.getPassword());
            currentUser.setPhone(user.getPhone());
            currentUser.setEnabled(user.isEnabled());
            currentUser.setLocked(user.isLocked());

            userService.saveUser(currentUser);
        }

        Role role = roleService.findById(user.getRole_id());

        if (role != null) {
            if (!userRoleService.findByUser_IdAndRole_Id(currentUser.getId(), role.getId())) {
                userRoleService.save(new UserRole(currentUser, role));
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
