package com.electroshop.controller;

import com.electroshop.model.Feedback;
import com.electroshop.model.Order;
import com.electroshop.model.OrderItem;
import com.electroshop.model.Product;
import com.electroshop.model.Review;
import com.electroshop.model.User;
import com.electroshop.service.FeedbackService;
import com.electroshop.service.OrderService;
import com.electroshop.service.ProductService;
import com.electroshop.service.ReviewService;
import com.electroshop.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
public class UiController {

    @Autowired
    private ProductService productService;

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private FeedbackService feedbackService;

    @Autowired
    private UserService userService;

    @Autowired
    private OrderService orderService;
    
//    @Autowired
//    private PasswordEncoder passwordEncoder;

    @GetMapping("/")
    public String home() {
        return "home";
    }
    
    @GetMapping("/login")
    public String loginPage() {
        return "login";  
    }
    
    @GetMapping("/register")
    public String showRegisterPage() {
        return "register";
    }

    @PostMapping("/register")
    public String handleRegistration(@ModelAttribute User user, Model model) {
        try {
            userService.registerUser(user);
            return "redirect:/login"; // go to login page
        } catch (Exception e) {
            model.addAttribute("error", "Registration failed: " + e.getMessage());
            return "register"; // show error message
        }
    }

    @GetMapping("/products")
    public String viewProducts(Model model) {
        List<Product> products = productService.getAllProducts();
        model.addAttribute("products", products);
        return "products";
    }

    @GetMapping("/products/{id}")
    public String viewProductDetails(@PathVariable Long id, Model model) {
        Product product = productService.getProductById(id);
        List<Review> reviews = reviewService.getReviewsByProductId(id);
        model.addAttribute("product", product);
        model.addAttribute("reviews", reviews);
        return "product-details";
    }
    
    @GetMapping("/add-product")
    public String showAddProductForm() {
        return "add-product"; 
    }

    @PostMapping("/add-product")
    public String handleAddProductForm(
            @RequestParam String name,
            @RequestParam String description,
            @RequestParam double price,
            @RequestParam String category,
            @RequestParam int stock) {

        Product product = new Product();
        product.setName(name);
        product.setDescription(description);
        product.setPrice(price);
        product.setCategory(category);
        product.setStock(stock);

        productService.addProduct(product);
        return "redirect:/products";
    }

    @GetMapping("/products/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Product product = productService.getProductById(id);
        model.addAttribute("product", product);
        return "add-product";
    }

    @PostMapping("/products/update")
    public String updateProduct(
            @RequestParam Long id,
            @RequestParam String name,
            @RequestParam String description,
            @RequestParam double price,
            @RequestParam String category,
            @RequestParam int stock) {

        Product product = productService.getProductById(id);
        product.setName(name);
        product.setDescription(description);
        product.setPrice(price);
        product.setCategory(category);
        product.setStock(stock);

        productService.updateProduct(id, product); // reuse same save logic
        return "redirect:/products";
    }
    
    @PostMapping("/products/delete/{id}")
    public String deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return "redirect:/products";
    }
    
    @GetMapping("/categories/{category}")
    public String viewProductsByCategory(@PathVariable String category, Model model) {
        List<Product> products = productService.getAllProducts()
                                               .stream()
                                               .filter(p -> p.getCategory().equalsIgnoreCase(category))
                                               .toList();

        model.addAttribute("category", category);
        model.addAttribute("products", products);
        return "category-products";
    }
    
    @GetMapping("/search")
    public String searchProducts(@RequestParam("query") String query, Model model) {
        List<Product> results = productService.searchProducts(query);
        model.addAttribute("products", results);
        model.addAttribute("searchQuery", query);
        return "search-results";  // we’ll create this page
    }
    
    @GetMapping("/orders")
    public String viewOrders(Model model) {
        org.springframework.security.core.Authentication authentication =
            org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName(); // login uses email

        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(role -> role.getAuthority().equals("ROLE_ADMIN"));

        User user = userService.getUserByEmail(email); // use email

        List<Order> orders;
        if (isAdmin) {
            orders = orderService.getAllOrders();
        } else {
            orders = orderService.getOrdersByUserId(user.getId());
        }

        model.addAttribute("orders", orders);
        return "orders";
    }
    
    @PostMapping("/submit-review")
    public String submitReview(@RequestParam Long productId,
                               @RequestParam int rating,
                               @RequestParam String comment,
                               Model model) {
        try {
        	org.springframework.security.core.Authentication auth =
                    org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
            String email = auth.getName();
            User user = userService.getUserByEmail(email);

            Review review = new Review();
            review.setRating(rating);
            review.setComment(comment);
            review.setUser(user);
            review.setProduct(productService.getProductById(productId));

            reviewService.addReview(review);
            return "redirect:/products/" + productId;

        } catch (Exception e) {
            model.addAttribute("error", "Unable to submit review: " + e.getMessage());
            List<Review> reviews = reviewService.getAllReviews();
            model.addAttribute("reviews", reviews);
            return "reviews";
        }
    }

    
    @PostMapping("/reviews/delete/{id}")
    public String deleteReview(@PathVariable Long id, @RequestHeader("referer") String referer) {
        reviewService.deleteReview(id);
        return "redirect:" + referer; // redirect to same product details page
    }

    @GetMapping("/feedbacks")
    public String viewFeedbacks(Model model) {
        List<Feedback> feedbacks = feedbackService.getAllFeedback();
        model.addAttribute("feedbacks", feedbacks);
        return "feedback";
    }
    
    @PostMapping("/submit-feedback")
    public String submitFeedback(@RequestParam String message,
                                 Model model) {
        try {
        	org.springframework.security.core.Authentication auth =
                    org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();            String email = auth.getName();
            User user = userService.getUserByEmail(email);

            Feedback feedback = new Feedback();
            feedback.setMessage(message);
            feedback.setUser(user);

            feedbackService.submitFeedback(feedback);
            return "redirect:/feedbacks";
        } catch (Exception e) {
            model.addAttribute("error", "Unable to submit feedback: " + e.getMessage());
            List<Feedback> feedbacks = feedbackService.getAllFeedback();
            model.addAttribute("feedbacks", feedbacks);
            return "feedback";
        }
    }

    @PostMapping("/order-now")
    public String quickOrder(@RequestParam Long productId,
                             @RequestParam int quantity) {

    	org.springframework.security.core.Authentication auth =
                org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();            String email = auth.getName();
        String mail = auth.getName();
        User user = userService.getUserByEmail(mail);

        Product product = productService.getProductById(productId);

        Order order = new Order();
        order.setUser(user);
        order.setOrderDate(LocalDate.now());

        OrderItem item = new OrderItem();
        item.setProduct(product);
        item.setQuantity(quantity);
        item.setPrice(product.getPrice());
        item.setOrder(order);

        order.setOrderItems(List.of(item));
        order.setTotalAmount(product.getPrice() * quantity);

        orderService.placeOrder(order);

        return "redirect:/thank-you";
    }


    @GetMapping("/orders/{id}")
    public String viewOrderDetails(@PathVariable Long id, Model model) {
        Order order = orderService.getOrderById(id);  // existing method
        List<OrderItem> items = order.getOrderItems();

        model.addAttribute("order", order);
        model.addAttribute("items", items);
        return "order-details";
    }
    
    @GetMapping("/thank-you")
    public String showThankYouPage() {
        return "thank-you";
    }

}
