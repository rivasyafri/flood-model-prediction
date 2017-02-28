package com.mofp.dao.support;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;

import java.io.Serializable;
import java.util.List;

@NoRepositoryBean
public interface CellAttributeRepository<T, ID extends Serializable>
        extends PagingAndSortingRepository<T, ID>, JpaRepository<T, ID>, JpaSpecificationExecutor<T> {
}
