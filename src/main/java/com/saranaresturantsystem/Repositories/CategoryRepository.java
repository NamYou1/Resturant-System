package com.saranaresturantsystem.Repositories;

import com.saranaresturantsystem.DTO.Response.CategoryResponse;
import com.saranaresturantsystem.Enities.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Map;

public interface CategoryRepository extends JpaRepository<Category , Long> , JpaSpecificationExecutor<Category> {

}
