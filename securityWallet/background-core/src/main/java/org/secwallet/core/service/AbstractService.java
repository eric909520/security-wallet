package org.secwallet.core.service;


import org.secwallet.core.exception.CTException;
import org.secwallet.core.util.bean.AppUtil;
import org.secwallet.core.util.iterator.CollectionWrapperIterator;
import org.secwallet.core.mapper.BaseMapper;
import org.secwallet.uid.UidGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

/**
 *
 * Platform Abstract Common Service
 * @param <E>
 * @param <PK>
 */
public abstract class AbstractService<E extends Serializable,PK extends Serializable> implements BaseService<E,PK> {

    @Value("${cthl.batch.count}")
    private int batchCount;

    @Autowired
    public BaseMapper<E, PK> baseMapper;


    @Transactional
    public int insert(E model) {
        return baseMapper.insert(model);
    }


    @Transactional
    public int insertSelective(E model) {
        return baseMapper.insertSelective(model);
    }


    @Transactional
    public int deleteByIds(List<PK> ids) {
        return baseMapper.deleteByIds(ids);
    }


    @Transactional
    public int deleteByPrimaryKey(PK id) {
        return baseMapper.deleteByPrimaryKey(id);
    }


    @Transactional
    public int updateByPrimaryKey(E model) {
        return baseMapper.updateByPrimaryKey(model);
    }


    @Transactional
    public int updateByPrimaryKeySelective(E model) {
        return baseMapper.updateByPrimaryKeySelective(model);
    }


    public E findById(PK id) {
        return baseMapper.findById(id);
    }


    public List<E> findAll(E model) {
        return baseMapper.findAll(model);
    }



    @Transactional
    public void addBatch(List<E> list) throws CTException {
        try {
            Iterator<List<E>> iterator = new CollectionWrapperIterator<List<E>>(list, batchCount);
            while (iterator.hasNext()) {
                baseMapper.addBatch(iterator.next());
            }
        } catch (ReflectiveOperationException e) {
            throw new CTException(e.getMessage(),e);
        }
    }

    public long genId() {
        UidGenerator uidGenerator= (UidGenerator) AppUtil.getBean("cachedUidGenerator");
        return  uidGenerator.getUID();
    }

}
