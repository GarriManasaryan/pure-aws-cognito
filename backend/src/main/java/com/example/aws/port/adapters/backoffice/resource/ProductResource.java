package com.example.aws.port.adapters.backoffice.resource;

import com.example.aws.application.UserService;
import com.example.aws.domain.Product;
import com.example.aws.port.adapters.persistence.PostgresqlProducts;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;

import java.net.http.HttpResponse;
import java.util.List;

@RestController
public class ProductResource {

    private final PostgresqlProducts postgresqlProducts;
    private final UserService userService;

    public ProductResource(PostgresqlProducts postgresqlProducts, UserService userService) {
        this.postgresqlProducts = postgresqlProducts;
        this.userService = userService;
    }

    @PostMapping("/api/products")
    public void save(@RequestBody Product product){
        postgresqlProducts.save(product);
    }

    @GetMapping("/backoffice/products")
    public List<Product> all(){
        return postgresqlProducts.all();
    }

    @GetMapping("/api/products")
    public List<Product> allBack(HttpServletResponse response){
        return postgresqlProducts.all();
    }

}
