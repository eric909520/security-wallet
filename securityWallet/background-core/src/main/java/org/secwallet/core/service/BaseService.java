package org.secwallet.core.service;


import org.secwallet.core.exception.CTException;

import java.io.Serializable;
import java.util.List;


public interface BaseService<E extends Serializable,PK extends Serializable>{


     int insert(E model);

    int insertSelective(E model);

    int deleteByIds(List<PK> ids);

    int deleteByPrimaryKey(PK id);

    int updateByPrimaryKey(E model);


    int updateByPrimaryKeySelective(E model);


    E findById(PK id);

    List<E> findAll(E model);


    void addBatch(List<E> list) throws CTException;
}
