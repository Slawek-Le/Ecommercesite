package com.slawekle.ecommercesite.service.product;

import java.util.List;

import com.slawekle.ecommercesite.dtos.ProductDto;
import com.slawekle.ecommercesite.model.Product;
import com.slawekle.ecommercesite.request.AddProductRequest;
import com.slawekle.ecommercesite.request.ProductUpdateRequest;

public interface IProductService {
    Product addProduct(AddProductRequest product);
    Product updateProduct(ProductUpdateRequest product, Long productId);
    Product getProductById(Long productId);
    void deleteProductById(Long productId);
    
    List<Product> getAllProducts();
    List<Product> getProductsByCategoryNameAndBrand(String categoryName, String brand);
    List<Product> getProductsByCategoryName(String categoryName);
    List<Product> getProductsByBrandAndName(String brand, String productName);
    List<Product> getProductsByBrand(String brand);
    List<Product> getProductsByName(String name);
    ProductDto convertToDto(Product product);
    List<ProductDto> getConvertedProducts(List<Product> products);
    
}
