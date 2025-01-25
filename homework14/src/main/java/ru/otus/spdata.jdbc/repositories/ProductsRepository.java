package ru.otus.spdata.jdbc.repositories;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import ru.otus.spdata.jdbc.entities.Product;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface ProductsRepository extends CrudRepository<Product, Long> {
    List<Product> findAll();

    @Query("UPDATE products set price = :price where id = :id RETURNING *")
    Product updatePrice(@Param("id") Long id, @Param("price") BigDecimal price);

    void deleteById(Long id);
    Optional<Product> findById(Long id);
}
