package com.example.tp1springbatch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;


public class ProductItemProcessor implements ItemProcessor<Product,Product> {
    private static final Logger logger=  LoggerFactory.getLogger(ProductItemProcessor.class);

    @Override
    public Product process(Product product) throws Exception {
        String name=product.name().toUpperCase();
        double price = product.price();
        Product transformedProduct = new Product(name,price);
        logger.info("Processing product "+name +" with price "+price+" "+transformedProduct);
        return transformedProduct;
    }
}
