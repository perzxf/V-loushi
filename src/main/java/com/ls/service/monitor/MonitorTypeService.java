package com.ls.service.monitor;

import com.ls.entity.monitor.MonitorType;
import org.springframework.data.domain.Sort.Direction;

import java.util.List;

/**
 * 监测类型 service
 * @author zhangchuanfei
 */
public interface MonitorTypeService {
    /**
     * 分页查询监测类型
     * @param page  当前页
     * @param pageSize 每页记录数
     * @param direction 排序规则
     * @param properties 排序字段
     * @return
     */
    public List<MonitorType> list(Integer page, Integer pageSize, Direction direction , String... properties );

    /**
     * 查询监测类型
     * @param direction 排序规则
     * @param properties 排序字段
     * @return
     */
    public List listAll(Direction direction , String... properties );

    /**
     * 获取总记录数
     */
    public Long getCount();

    /**
     * 添加或修改
     */
    public void save(MonitorType monitorType);

    /**
     * 根据ID删除
     */
    public void delete(Integer id);

    /**
     * 根据ID查询
     */
    public MonitorType getById(Integer id);

}
