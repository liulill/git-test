package com.springmvc.dao;

import com.springmvc.entity.GroundHistor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface GroundHistorMapper {

    /**
     * 按id查询
     * @param groundHistorMeteoDataTabId
     * @return
     */
    GroundHistor selectByPrimaryKey(String groundHistorMeteoDataTabId);

    /**
     * 每次随机获取number条数据
     * @param number
     * @return
     */
    List<GroundHistor> getAll(long number);

    /**
     * 获取总数据条数
     * @return
     */
    long getCount();

    /**
     * 分段获取数据
     * @param map
     * @return
     */
    List<GroundHistor> getLimit(Map<String,Long> map);

    void insert(GroundHistor groundHistor);
}