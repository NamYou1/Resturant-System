package com.saranaresturantsystem.repositories.product;

import com.saranaresturantsystem.entities.product.SubCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


public interface SubCategoryRepository extends JpaRepository<SubCategory , Long> , JpaSpecificationExecutor<SubCategory> {
}
