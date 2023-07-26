package org.secwallet.core.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.io.Serializable;
import java.util.List;


@Mapper
public interface BaseMapper <E extends Serializable,PK>{

    int insert(E model);

    int insertSelective(E model);

    int deleteByIds(List<PK> ids);

    int deleteByPrimaryKey(PK id);

    int updateByPrimaryKey(E model);


    int updateByPrimaryKeySelective(E model);


    E findById(PK id);

    List<E> findAll(E model);

    int addBatch(List<E> list);
}
