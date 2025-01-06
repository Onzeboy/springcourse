package ru.course.spring.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.course.spring.pojo.Product;
import ru.course.spring.pojo.ProductCategory;
import ru.course.spring.repository.ProductRepository;
import ru.course.spring.services.ProductService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.stream.Collectors;

@Controller
public class ProductController {

    @Autowired
    private  ProductRepository productRepository;

    @Autowired
    private ProductService productService;

    @GetMapping("/products")
    public String getProducts(@RequestParam(defaultValue = "0") int page,
                              @RequestParam(defaultValue = "12") int size,
                              Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("Is Authenticated: " + authentication.isAuthenticated());
        Page<Product> productPage = productService.getProducts(page, size);

        // Преобразование продуктов с изображениями в формат Base64
        productPage.getContent().forEach(product -> {
            if (product.getProductImageData() != null) {
                String base64Image = Base64.getEncoder().encodeToString(product.getProductImageData());
                product.setBase64Image("data:image/jpeg;base64," + base64Image);
            }
        });

        model.addAttribute("products", productPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", productPage.getTotalPages());
        model.addAttribute("totalItems", productPage.getTotalElements());
        model.addAttribute("size", size);
        return "mainPage";
    }

    @GetMapping("/products/loadMore")
    @ResponseBody
    public Page<Product> loadMoreProducts(@RequestParam(defaultValue = "0") int page,
                                          @RequestParam(defaultValue = "12") int size) {
        return productService.getProducts(page, size);
    }
    @GetMapping("/add")
    public String showAddProductForm(Model model) {
        model.addAttribute("categories", ProductCategory.values());
        model.addAttribute("product", new Product());
        return "admin/addProduct";
    }

    @PostMapping("/addproduct")
    public String addProduct(
            @ModelAttribute Product product,
            @RequestParam("imageFile") MultipartFile imageFile,
            Model model) {
        try {
            if (!imageFile.isEmpty()) {
                byte[] imageData = imageFile.getBytes(); // Преобразуем файл в массив байтов
                product.setProductImageData(imageData); // Сохраняем массив байтов в объекте
            }

            // Отладочная информация
            System.out.println("Тип данных в productImageData: " + product.getProductImageData().getClass().getName());
            System.out.println("Размер productImageData: " + (product.getProductImageData() != null ? product.getProductImageData().length : "null"));
            productRepository.save(product);
            model.addAttribute("successMessage", "Товар успешно добавлен!");
        } catch (IOException e) {
            model.addAttribute("errorMessage", "Ошибка при сохранении изображения. Попробуйте снова.");
            e.printStackTrace();
        }
        return "admin/addProduct";
    }
}