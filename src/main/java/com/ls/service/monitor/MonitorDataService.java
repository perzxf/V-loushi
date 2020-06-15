package com.ls.service.monitor;

import com.ls.entity.monitor.MonitorData;
import org.springframework.data.domain.Sort;

import java.util.List;

/**
 * @author zhangchuanfei
 */
public interface MonitorDataService {

    List<MonitorData> list(MonitorData s_article, Integer page, Integer pageSize, Sort.Direction desc, String... properties);
}
