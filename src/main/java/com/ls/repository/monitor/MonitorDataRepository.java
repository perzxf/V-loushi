package com.ls.repository.monitor;

import com.ls.entity.monitor.MonitorData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author zhangchuanfei
 */
public interface MonitorDataRepository extends JpaRepository<MonitorData,Integer>, JpaSpecificationExecutor<MonitorData> {
}
