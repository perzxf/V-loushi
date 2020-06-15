package com.ls.service.monitor.impl;

import com.ls.entity.monitor.MonitorType;
import com.ls.repository.monitor.MonitorTypeRepository;
import com.ls.service.monitor.MonitorTypeService;
import com.ls.util.Consts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 监测类型 service 实现类
 *
 * @author zhangchuanfei
 */
@Service("monitorTypeService")
public class MonitorTypeServiceImpl implements MonitorTypeService {

    @Autowired
    private MonitorTypeRepository monitorTypeRepository;

    @Autowired
    private RedisTemplate<Object,Object> redisTemplate;


    @Override
    public List<MonitorType> list(Integer page, Integer pageSize, Sort.Direction direction, String... properties) {
        Page<MonitorType> monitorTypePage = monitorTypeRepository.findAll(PageRequest.of(page - 1, pageSize, direction, properties));
        return monitorTypePage.getContent();
    }

    @Override
    public List listAll(Sort.Direction direction, String... properties) {
        if(redisTemplate.hasKey(Consts.ALL_ARC_TYPE_NAME)){
            return redisTemplate.opsForList().range(Consts.ALL_ARC_TYPE_NAME,0,-1);
        }else{
            return monitorTypeRepository.findAll(Sort.by(direction,properties));
        }
    }

    @Override
    public Long getCount() {
        return monitorTypeRepository.count();
    }

    @Override
    public void save(MonitorType monitorType) {
        boolean flag = false;
        if(monitorType.getTypeId()==null){
            flag = true;
        }
        monitorTypeRepository.save(monitorType);
        if(flag){
            redisTemplate.opsForList().rightPush(Consts.ALL_ARC_TYPE_NAME,monitorType);
        }
    }

    @Override
    public void delete(Integer id) {
        monitorTypeRepository.deleteById(id);
    }

    @Override
    public MonitorType getById(Integer id) {
        return monitorTypeRepository.getOne(id);
    }
}
