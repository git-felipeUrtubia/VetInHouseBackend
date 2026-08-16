package org.example.backend_vet_in_house.catalog.controller;

import lombok.RequiredArgsConstructor;
import org.example.backend_vet_in_house.catalog.dto.req.CreateProductDTO;
import org.example.backend_vet_in_house.catalog.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping("/create")
    public ResponseEntity<?> saveProduct(@RequestBody CreateProductDTO req) {
        return new ResponseEntity<>(productService.saveProduct(req), HttpStatus.CREATED);
    }

    @GetMapping("/find-all")
    public ResponseEntity<?> findAllProducts() {
        return new ResponseEntity<>(productService.findAllProducts(), HttpStatus.OK);
    }

    @GetMapping("/find-all-admin")
    public ResponseEntity<?> findAllProductsAdmin() {
        return new ResponseEntity<>(productService.findAllProductsAdmin(), HttpStatus.OK);
    }

    @GetMapping("/find")
    public ResponseEntity<?> findByProductCode(@RequestParam String code) {
        return new ResponseEntity<>(productService.findProductByCode(code), HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateProductByCode(@RequestBody CreateProductDTO req) {
        return new ResponseEntity<>(productService.updateProductByCode(req), HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteProductByCode(@RequestParam String code) {
        productService.deleteProductByCode(code);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
