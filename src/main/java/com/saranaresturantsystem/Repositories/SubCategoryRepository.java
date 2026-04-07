package com.saranaresturantsystem.Repositories;

import com.saranaresturantsystem.Enities.SubCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


public interface SubCategoryRepository extends JpaRepository<SubCategory , Long> , JpaSpecificationExecutor<SubCategory> {
}
