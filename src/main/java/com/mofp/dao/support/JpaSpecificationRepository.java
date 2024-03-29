package com.mofp.dao.support;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.io.Serializable;

@NoRepositoryBean
public interface JpaSpecificationRepository<T, ID extends Serializable>
        extends PagingAndSortingRepository<T, ID>, JpaRepository<T, ID>, JpaSpecificationExecutor<T> {
}
