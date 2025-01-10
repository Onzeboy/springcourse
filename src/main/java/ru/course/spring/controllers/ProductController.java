package ru.course.spring.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
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
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class ProductController {

    @Autowired
    private  ProductRepository productRepository;

    @Autowired
    private ProductService productService;

    @GetMapping("/products")
    public String getProducts(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("Is Authenticated: " + authentication.isAuthenticated());
        List<Product> productPage = productService.getProducts();

        // Преобразование продуктов с изображениями в формат Base64
        productPage.forEach(product -> {
            if (product.getProductImageData() != null) {
                String base64Image = Base64.getEncoder().encodeToString(product.getProductImageData());
                product.setBase64Image("data:image/jpeg;base64," + base64Image);
            }
        });

        model.addAttribute("products", productPage);;
        return "/mainPage";
    }

    @GetMapping("/admin/add")
    public String showAddProductForm(Model model) {
        model.addAttribute("categories", ProductCategory.values());
        model.addAttribute("product", new Product());
        return "/admin/addProduct";
    }

    @PostMapping("/admin/addproduct")
    public String addProduct(
            @ModelAttribute Product product,
            @RequestParam("imageFile") MultipartFile imageFile,
            Model model) {
        // Определите допустимые типы контента
        List<String> allowedContentTypes = Arrays.asList("image/jpeg", "image/png", "image/gif");

        // Максимальный допустимый размер файла (например, 5 МБ)
        long maxFileSize = 5 * 1024 * 1024; // 5 МБ

        try {
            if (!imageFile.isEmpty()) {
                String contentType = imageFile.getContentType();
                String originalFilename = StringUtils.cleanPath(imageFile.getOriginalFilename());

                // Проверка типа файла
                if (!allowedContentTypes.contains(contentType)) {
                    model.addAttribute("errorMessage", "Недопустимый тип изображения. Разрешены только JPEG, PNG и GIF.");
                    return "redirect:/admin/add"; // Перенаправление на страницу добавления с ошибкой
                }

                // Проверка размера файла
                if (imageFile.getSize() > maxFileSize) {
                    model.addAttribute("errorMessage", "Размер изображения превышает 5 МБ.");
                    return "redirect:/admin/add"; // Перенаправление на страницу добавления с ошибкой
                }

                // Дополнительная проверка расширения файла (опционально)
                String fileExtension = StringUtils.getFilenameExtension(originalFilename);
                if (fileExtension == null ||
                        (!fileExtension.equalsIgnoreCase("jpg") &&
                                !fileExtension.equalsIgnoreCase("jpeg") &&
                                !fileExtension.equalsIgnoreCase("png") &&
                                !fileExtension.equalsIgnoreCase("gif"))) {
                    model.addAttribute("errorMessage", "Недопустимое расширение файла изображения.");
                    return "redirect:/admin/add"; // Перенаправление на страницу добавления с ошибкой
                }

                // Преобразуем файл в массив байтов и сохраняем
                byte[] imageData = imageFile.getBytes();
                product.setProductImageData(imageData);
            } else {
                model.addAttribute("errorMessage", "Пожалуйста, загрузите изображение продукта.");
                 return "redirect:/admin/add";
            }

            // Отладочная информация
            if (product.getProductImageData() != null) {
                System.out.println("Тип данных в productImageData: " + product.getProductImageData().getClass().getName());
                System.out.println("Размер productImageData: " + product.getProductImageData().length);
            } else {
                System.out.println("Изображение не загружено.");
            }

            // Сохранение продукта в репозитории
            productRepository.save(product);
            model.addAttribute("successMessage", "Товар успешно добавлен!");

        } catch (IOException e) {
            model.addAttribute("errorMessage", "Ошибка при сохранении изображения. Попробуйте снова.");
            e.printStackTrace();
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Произошла неизвестная ошибка. Попробуйте позже.");
            e.printStackTrace();
        }

        return "redirect:/admin/add";
    }

    @PostMapping("/admin/editProduct")
    public String editProduct(
            @RequestParam("productId") Long productId,
            @RequestParam("name") String name,
            @RequestParam("description") String description,
            @RequestParam("quantity") Integer quantity,
            @RequestParam("price") Integer price,
            Model model
    ) {
        try {
            // Поиск продукта по ID
            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new IllegalArgumentException("Продукт не найден: " + productId));

            // Обновление полей продукта
            product.setProductName(name);
            product.setProductDescription(description);
            product.setProductQuantity(quantity);
            product.setProductPriceCent(price);

            // Сохранение изменений
            productRepository.save(product);

            // Добавление сообщения в модель
            model.addAttribute("message", "Продукт успешно обновлен!");

            return "redirect:/products"; // Перенаправление на страницу продуктов
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "redirect:/products"; // Перенаправление на страницу продуктов с ошибкой
        } catch (Exception e) {
            model.addAttribute("error", "Ошибка при обновлении продукта: " + e.getMessage());
            return "redirect:/products"; // Перенаправление на страницу продуктов с ошибкой
        }
    }

    /**
     * Обрабатывает запрос на удаление продукта.
     * URL: /products/deleteProduct
     * Метод: POST
     */
    @PostMapping("/admin/deleteProduct")
    public String deleteProduct(
            @RequestParam("productId") Long productId,
            Model model
    ) {
        try {
            // Поиск продукта по ID
            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new IllegalArgumentException("Продукт не найден: " + productId));

            // Удаление продукта
            productRepository.delete(product);

            // Добавление сообщения в модель
            model.addAttribute("message", "Продукт успешно удален!");

            return "redirect:/products"; // Перенаправление на страницу продуктов
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "redirect:/products"; // Перенаправление на страницу продуктов с ошибкой
        } catch (Exception e) {
            model.addAttribute("error", "Ошибка при удалении продукта: " + e.getMessage());
            return "redirect:/products"; // Перенаправление на страницу продуктов с ошибкой
        }
    }

}