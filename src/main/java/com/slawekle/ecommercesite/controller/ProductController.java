package com.slawekle.ecommercesite.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.slawekle.ecommercesite.dtos.ProductDto;
import com.slawekle.ecommercesite.model.Product;
import com.slawekle.ecommercesite.request.AddProductRequest;
import com.slawekle.ecommercesite.request.ProductUpdateRequest;
import com.slawekle.ecommercesite.response.ApiResponse;
import com.slawekle.ecommercesite.service.product.IProductService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/products")
public class ProductController {
    private final IProductService iProductService;

    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllProducts() {
        List<Product> products = iProductService.getAllProducts();
        List<ProductDto> convertedProducts = iProductService.getConvertedProducts(products);
        return ResponseEntity.ok(new ApiResponse("Products retrieved successfully", convertedProducts));
    }

    @GetMapping("/product/{productId}/product")
    public ResponseEntity<ApiResponse> getProductById(@PathVariable Long productId) {
        Product product = iProductService.getProductById(productId);
        ProductDto productDto = iProductService.convertToDto(product);
        return ResponseEntity.ok(new ApiResponse("Product retrieved successfully", productDto));
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addProduct(@RequestBody AddProductRequest productRequest) {
        Product product = iProductService.addProduct(productRequest);
        ProductDto productDto = iProductService.convertToDto(product);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse("Product created successfully", productDto));
    }

    @PutMapping("/product/{id}/update")
    public ResponseEntity<ApiResponse> updateProduct(@PathVariable Long id,
            @RequestBody ProductUpdateRequest productRequest) {
        Product product = iProductService.updateProduct(productRequest, id);
        ProductDto productDto = iProductService.convertToDto(product);
        return ResponseEntity.ok(new ApiResponse("Product updated successfully", productDto));
    }

    @DeleteMapping("/product/{id}/delete")
    public ResponseEntity<ApiResponse> deleteProduct(@PathVariable Long id) {
        iProductService.deleteProductById(id);
        return ResponseEntity.ok(new ApiResponse("Product deleted successfully", null));
    }

    @GetMapping("/products/by/brand-and-name")
    public ResponseEntity<ApiResponse> getProductsByBrandAndName(@RequestParam String brandName,
            @RequestParam String productName) {
        List<Product> products = iProductService.getProductsByBrandAndName(brandName, productName);
        List<ProductDto> convertedProducts = iProductService.getConvertedProducts(products);
        return ResponseEntity.ok(new ApiResponse("Products retrieved successfully", convertedProducts));
    }

    @GetMapping("/products/by/category-and-brand")
    public ResponseEntity<ApiResponse> getProductsByCategoryAndBrand(@RequestParam String category,
            @RequestParam String brand) {
        List<Product> products = iProductService.getProductsByCategoryNameAndBrand(category, brand);
        List<ProductDto> convertedProducts = iProductService.getConvertedProducts(products);
        return ResponseEntity.ok(new ApiResponse("Products retrieved successfully", convertedProducts));
    }

    @GetMapping("/products/by/category")
    public ResponseEntity<ApiResponse> getProductsByCategory(@RequestParam String category) {
        List<Product> products = iProductService.getProductsByCategoryName(category);
        List<ProductDto> convertedProducts = iProductService.getConvertedProducts(products);
        return ResponseEntity.ok(new ApiResponse("Products retrieved successfully", convertedProducts));
    }

    @GetMapping("/products/by/brand")
    public ResponseEntity<ApiResponse> getProductsByBrand(@RequestParam String brand) {
        List<Product> products = iProductService.getProductsByBrand(brand);
        List<ProductDto> convertedProducts = iProductService.getConvertedProducts(products);
        return ResponseEntity.ok(new ApiResponse("Products retrieved successfully", convertedProducts));
    }

    @GetMapping("/products/by/name")
    public ResponseEntity<ApiResponse> getProductsByName(@RequestParam String name) {
        List<Product> products = iProductService.getProductsByName(name);
        List<ProductDto> convertedProducts = iProductService.getConvertedProducts(products);
        return ResponseEntity.ok(new ApiResponse("Products retrieved successfully", convertedProducts));
    }

}
