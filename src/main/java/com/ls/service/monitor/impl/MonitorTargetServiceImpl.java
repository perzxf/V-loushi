package com.ls.service.monitor.impl;

import com.ls.entity.monitor.MonitorTarget;
import com.ls.repository.monitor.MonitorTargetRepository;
import com.ls.service.monitor.MonitorTargetService;
import com.ls.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * @author zhangchuanfei
 */
@Service("monitorTargetService")
public class MonitorTargetServiceImpl implements MonitorTargetService {

    @Autowired
    private MonitorTargetRepository monitorTargetRepository;

    @Autowired
    private RedisTemplate<Object,Object> redisTemplate;


    private RedisSerializer redisSerializer = new StringRedisSerializer();



    @Override
    public List<MonitorTarget> list(MonitorTarget monitorTarget, String nickname, Integer page, Integer pageSize, Sort.Direction direction, String... properties) {
        Page<MonitorTarget> pageArticle = monitorTargetRepository.findAll(new Specification<MonitorTarget>() {
            @Override
            public Predicate toPredicate(Root<MonitorTarget> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
                return getPredicate(root, cb,  nickname, monitorTarget);
            }
        }, PageRequest.of(page-1,pageSize,direction,properties));
        return pageArticle.getContent();
    }

    @Override
    public Long getCount(MonitorTarget monitorTarget, String nickname) {
        Long count = monitorTargetRepository.count(new Specification<MonitorTarget>() {
            @Override
            public Predicate toPredicate(Root<MonitorTarget> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
                return getPredicate(root, cb, nickname, monitorTarget);
            }
        });
        return null;
    }

    private Predicate getPredicate(Root<MonitorTarget> root, CriteriaBuilder cb, String nickname, MonitorTarget monitorTarget) {
        Predicate predicate = cb.conjunction();

        if (StringUtil.isNotEmpty(nickname)) {            //昵称
            predicate.getExpressions().add(cb.like(root.get("user").get("nickname"), "%" + nickname + "%"));
        }
        if (monitorTarget != null) {
            if (StringUtil.isNotEmpty(monitorTarget.getMonitorMotif())) {         //监测主题
                predicate.getExpressions().add(cb.like(root.get("monitorMotif"), "%" + monitorTarget.getMonitorMotif() + "%"));
            }
            if (StringUtil.isNotEmpty(monitorTarget.getMonitorTargetName())) {         //监测目标名称
                predicate.getExpressions().add(cb.like(root.get("monitorTargetName"), "%" + monitorTarget.getMonitorTargetName() + "%"));
            }
            if (monitorTarget.getUser() != null && monitorTarget.getUser().getUserId() != null) {               //用户
                predicate.getExpressions().add(cb.equal(root.get("user").get("userId"), monitorTarget.getUser().getUserId()));
            }
        }
        return predicate;
    }

    @Override
    public void save(MonitorTarget monitorTarget) {

        redisTemplate.setKeySerializer(redisSerializer);
        redisTemplate.opsForValue().set("article_"+monitorTarget.getMtId(),monitorTarget);

        monitorTargetRepository.save(monitorTarget);
    }

    @Override
    public void delete(Integer id) {
        redisTemplate.delete("article_"+id);
        monitorTargetRepository.deleteById(id);
    }

    @Override
    public MonitorTarget getById(Integer id) {
        if(redisTemplate.hasKey("article_"+id)){
            return (MonitorTarget)redisTemplate.opsForValue().get("article_"+id);
        }else{

            MonitorTarget article = monitorTargetRepository.getOne(id);

            redisTemplate.setKeySerializer(redisSerializer);
            redisTemplate.opsForValue().set("article_" + article.getMtId(), article);

            return article;
        }
    }
}
