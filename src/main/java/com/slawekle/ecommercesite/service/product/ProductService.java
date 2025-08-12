package com.slawekle.ecommercesite.service.product;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.slawekle.ecommercesite.dtos.ImageDto;
import com.slawekle.ecommercesite.dtos.ProductDto;
import com.slawekle.ecommercesite.model.Cart;
import com.slawekle.ecommercesite.model.CartItem;
import com.slawekle.ecommercesite.model.Category;
import com.slawekle.ecommercesite.model.Image;
import com.slawekle.ecommercesite.model.OrderItem;
import com.slawekle.ecommercesite.model.Product;
import com.slawekle.ecommercesite.repository.CartItemRepository;
import com.slawekle.ecommercesite.repository.CategoryRepository;
import com.slawekle.ecommercesite.repository.ImageRepository;
import com.slawekle.ecommercesite.repository.OrderItemRepository;
import com.slawekle.ecommercesite.repository.ProductRepository;
import com.slawekle.ecommercesite.request.AddProductRequest;
import com.slawekle.ecommercesite.request.ProductUpdateRequest;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService implements IProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final CartItemRepository cartItemRepository;
    private final OrderItemRepository orderItemRepository;
    private final ImageRepository imageRepository;
    private final ModelMapper modelMapper;

    @Override
    public Product addProduct(AddProductRequest productRequest) {
        if(isProductExists(productRequest.getName(), productRequest.getBrand())){
            throw new EntityExistsException("Product with name " + productRequest.getName() + " and brand " + productRequest.getBrand() + " already exists.");
        } 
        Category category = Optional.ofNullable(categoryRepository.findByName(productRequest.getCategory().getName()))
                .orElseGet(() -> {
                    Category newCategory = new Category(productRequest.getCategory().getName());
                    return categoryRepository.save(newCategory);
                });
        productRequest.setCategory(category);
        return productRepository.save(createProduct(productRequest, category));
    }

    private boolean isProductExists(String name, String brand) {
        return productRepository.existsByNameAndBrand(name, brand);
    }

    private Product createProduct(AddProductRequest request, Category category) {
        return new Product(
                request.getName(),
                request.getBrand(),
                request.getPrice(),
                request.getInventory(),
                request.getDescription(),
                category
        );
    }

    @Override
    public Product updateProduct(ProductUpdateRequest product, Long productId) {
        return productRepository.findById(productId)
                .map(existingProduct -> updateExistingProduct(product, existingProduct))
                .map(productRepository::save)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + productId));
    }

    private Product updateExistingProduct(ProductUpdateRequest productRequest, Product product) {
        product.setName(productRequest.getName());
        product.setBrand(productRequest.getBrand());
        product.setPrice(productRequest.getPrice());
        product.setInventory(productRequest.getInventory());
        product.setDescription(productRequest.getDescription());
        Category category = categoryRepository.findByName(productRequest.getCategory().getName());
        product.setCategory(category);
        return productRepository.save(product);

    }
    
    @Override
    public void deleteProductById(Long productId) {
        productRepository.findById(productId)
                .ifPresentOrElse(product -> {
                    List<CartItem> cartItems = cartItemRepository.findByProductId(productId);
                    cartItems.forEach(cartItem -> {
                        Cart cart = cartItem.getCart();
                        cart.removeItem(cartItem);
                        cartItemRepository.delete(cartItem);
                    });

                    List<OrderItem> ordersItems = orderItemRepository.findByProductId(productId);
                    ordersItems.forEach(ordersItem -> {
                        ordersItem.setProduct(null);
                        orderItemRepository.save(ordersItem);
                    });

                    Optional.ofNullable(product.getCategory())
                            .ifPresent(category -> category.getProducts().remove(product));
                    product.setCategory(null);

                    productRepository.deleteById(productId);

                }, () -> {
                    throw new EntityNotFoundException("Product not found with id: " + productId);
                });
    }

    @Override
    public Product getProductById(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + productId));
    }


    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public List<Product> getProductsByCategoryNameAndBrand(String category, String brand) {
        return productRepository.findProductsByCategoryAndBrand(categoryRepository.findByName(category), brand);
    }

    @Override
    public List<Product> getProductsByCategoryName(String category) {
        return productRepository.findProductsByCategory(categoryRepository.findByName(category));
    }

    @Override
    public List<Product> getProductsByBrandAndName(String brand, String name) {
        return productRepository.findProductsByBrandAndName(brand, name);
    }

    @Override
    public List<Product> getProductsByBrand(String brand) {
        return productRepository.findProductsByBrand(brand);
    }

    @Override
    public List<Product> getProductsByName(String name) {
        return productRepository.findProductsByName(name);
    }

    @Override    
    public List<ProductDto> getConvertedProducts(List<Product> products) {
        return products.stream()
                .map(this::convertToDto)
                .toList();
    }

    @Override
    public ProductDto convertToDto(Product product) {
        ProductDto productDto = modelMapper.map(product, ProductDto.class);
        List<Image> images = imageRepository.findImageByProductId(product.getId());
        List<ImageDto> imageDtos = images.stream()
                .map(image -> modelMapper.map(image, ImageDto.class))
                .toList();
        productDto.setImages(imageDtos);

            // Explicitly set category name (or object) if needed
    if (product.getCategory() != null) {
        productDto.setCategory(product.getCategory());
        // or, if ProductDto has a CategoryDto:
        // productDto.setCategory(modelMapper.map(product.getCategory(), CategoryDto.class));
    }
        return productDto;
    }
}
