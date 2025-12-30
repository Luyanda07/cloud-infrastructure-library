package com.cloudinfra.repository;

import com.cloudinfra.model.Template;
import com.cloudinfra.model.TemplateType;
import com.cloudinfra.model.Complexity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TemplateRepository extends JpaRepository<Template, Long> {
    
    List<Template> findByCategory(String category);
    
    List<Template> findByType(TemplateType type);
    
    List<Template> findByComplexity(Complexity complexity);
    
    @Query("SELECT t FROM Template t WHERE " +
           "LOWER(t.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(t.description) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<Template> findBySearchTerm(@Param("searchTerm") String searchTerm);
    
    @Query("SELECT t FROM Template t JOIN t.tags tag WHERE LOWER(tag) LIKE LOWER(CONCAT('%', :tag, '%'))")
    List<Template> findByTag(@Param("tag") String tag);
    
    @Query("SELECT t FROM Template t WHERE " +
           "(:category IS NULL OR t.category = :category) AND " +
           "(:type IS NULL OR t.type = :type) AND " +
           "(:complexity IS NULL OR t.complexity = :complexity) AND " +
           "(:searchTerm IS NULL OR " +
           "LOWER(t.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(t.description) LIKE LOWER(CONCAT('%', :searchTerm, '%')))")
    List<Template> findByFilters(@Param("category") String category,
                                @Param("type") TemplateType type,
                                @Param("complexity") Complexity complexity,
                                @Param("searchTerm") String searchTerm);
    
    List<Template> findTop10ByOrderByDownloadCountDesc();
    
    @Query("SELECT DISTINCT t.category FROM Template t ORDER BY t.category")
    List<String> findAllCategories();
}