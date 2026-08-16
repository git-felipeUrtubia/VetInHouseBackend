package org.example.backend_vet_in_house.catalog.service;

import lombok.RequiredArgsConstructor;
import org.example.backend_vet_in_house.catalog.dto.req.CreateProductDTO;
import org.example.backend_vet_in_house.catalog.dto.res.ProductPanelAdminResDTO;
import org.example.backend_vet_in_house.catalog.dto.res.ProductResDTO;
import org.example.backend_vet_in_house.catalog.model.Categoria;
import org.example.backend_vet_in_house.catalog.model.Product;
import org.example.backend_vet_in_house.catalog.repository.ProductRepository;
import org.example.backend_vet_in_house.shared.exception.catalog.InsufficientStockException;
import org.example.backend_vet_in_house.shared.exception.catalog.ProductAlreadyExistsException;
import org.example.backend_vet_in_house.shared.exception.catalog.ProductNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public String saveProduct(CreateProductDTO req) {
        boolean prod = productRepository.findProductByCode(req.code()).isPresent();

        if(prod) {
            throw new ProductAlreadyExistsException("Product already exists");
        }

        if(req.stock() <= 0) {
            throw new InsufficientStockException("Product: " + req.name() + " debe contener al menos 1 stock");
        }

        productRepository.save(Product.builder()
                .code(req.code())
                .name(req.name())
                .description(req.description())
                .price(req.price())
                .priceOffer(req.priceOffer())
                .image(req.image())
                .stock(req.stock())
                .categoria(Categoria.valueOf(req.categoria()))
                .build()
        );
        return "Product saved with successfully!";
    }

    public List<ProductResDTO> findAllProducts() {
        return productRepository.findAll().stream()
                .map(p -> new ProductResDTO(
                        p.getCode(),
                        p.getName(),
                        p.getDescription(),
                        p.getPrice(),
                        p.getPriceOffer(),
                        p.getImage(),
                        p.getCategoria().name()
                ))
                .toList();
    }

    public ProductResDTO findProductByCode(String code) throws ProductNotFoundException {
        Product prod = productRepository.findProductByCode(code)
                .orElseThrow(() -> new ProductNotFoundException("Producto con code: " + code + " no encontrado."));

        return new ProductResDTO(
                prod.getCode(),
                prod.getName(),
                prod.getDescription(),
                prod.getPrice(),
                prod.getPriceOffer(),
                prod.getImage(),
                prod.getCategoria().name()
        );
    }

    public void deleteProductByCode(String code) {
        productRepository.deleteProductByCode(code);
    }

    public ProductResDTO updateProductByCode(CreateProductDTO req) {

        Product prod = productRepository.findProductByCode(req.code())
                        .orElseThrow(() -> new ProductNotFoundException("Product " + req.code() + " not found"));

        prod.setName(req.name());
        prod.setDescription(req.description());
        prod.setPrice(req.price());
        prod.setPriceOffer(req.priceOffer());
        prod.setImage(req.image());
        prod.setCategoria(Categoria.valueOf(req.categoria()));
        prod.setStock(req.stock());

        productRepository.save(prod);

        return new ProductResDTO(
                prod.getCode(),
                prod.getName(),
                prod.getDescription(),
                prod.getPrice(),
                prod.getPriceOffer(),
                prod.getImage(),
                prod.getCategoria().name()
        );

    }

    public List<ProductPanelAdminResDTO> findAllProductsAdmin() {
        return productRepository.findAll().stream()
                .map(p -> new ProductPanelAdminResDTO(
                        p.getCode(),
                        p.getName(),
                        p.getDescription(),
                        p.getPrice(),
                        p.getPriceOffer(),
                        p.getImage(),
                        p.getCategoria().name(),
                        p.getStock()
                ))
                .toList();
    }

}
