package com.mofp.service.support.impl;

import com.mofp.service.support.AbstractBaseService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author rivasyafri
 */
public class DefaultBaseServiceImpl<T> implements AbstractBaseService<T> {

    @Autowired
    protected T repository;

    @Override
    public T getRepository() {
        return repository;
    }
}
