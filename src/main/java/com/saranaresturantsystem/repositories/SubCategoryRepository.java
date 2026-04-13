package com.saranaresturantsystem.repositories;

import com.saranaresturantsystem.entities.SubCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


public interface SubCategoryRepository extends JpaRepository<SubCategory , Long> , JpaSpecificationExecutor<SubCategory> {
}
