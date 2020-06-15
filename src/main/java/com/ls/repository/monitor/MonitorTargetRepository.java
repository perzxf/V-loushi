package com.ls.repository.monitor;

import com.ls.entity.monitor.MonitorTarget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author zhangchuanfei
 */
public interface MonitorTargetRepository extends JpaRepository<MonitorTarget,Integer>, JpaSpecificationExecutor<MonitorTarget> {
}
