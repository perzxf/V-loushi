package com.ls.repository.monitor;

import com.ls.entity.monitor.MonitorType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 监测类型 Repository
 *
 * @author zhangchuanfei
 */
public interface MonitorTypeRepository extends JpaRepository<MonitorType,Integer>, JpaSpecificationExecutor<MonitorType> {
}
