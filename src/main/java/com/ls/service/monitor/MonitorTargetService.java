package com.ls.service.monitor;

import com.ls.entity.monitor.MonitorTarget;
import org.springframework.data.domain.Sort.Direction;

import java.util.List;

/**
 * @author zhangchuanfei
 */
public interface MonitorTargetService {

    public List<MonitorTarget> list(MonitorTarget monitorTarget, String nickname,  Integer page, Integer pageSize, Direction direction, String... properties);


    public Long getCount(MonitorTarget monitorTarget, String nickname);


    public void save(MonitorTarget monitorTarget);


    public void delete(Integer id);


    public MonitorTarget getById(Integer id);
}
