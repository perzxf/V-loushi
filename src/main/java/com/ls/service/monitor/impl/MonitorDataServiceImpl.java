package com.ls.service.monitor.impl;

import com.ls.entity.monitor.MonitorData;
import com.ls.repository.monitor.MonitorDataRepository;
import com.ls.service.monitor.MonitorDataService;
import com.ls.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * @author zhangchuanfei
 */
@Service("monitorDataService")
public class MonitorDataServiceImpl implements MonitorDataService {
    @Autowired
    private MonitorDataRepository monitorDataRepository;

    @Override
    public List<MonitorData> list(MonitorData s_article, Integer page, Integer pageSize, Sort.Direction direction, String... properties) {
        Page<MonitorData> pageArticle = monitorDataRepository.findAll(new Specification<MonitorData>() {
            @Override
            public Predicate toPredicate(Root<MonitorData> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
                return getPredicate(root, cb, s_article);
            }
        }, PageRequest.of(page-1,pageSize,direction,properties));
        return pageArticle.getContent();
    }

    private Predicate getPredicate(Root<MonitorData> root, CriteriaBuilder cb, MonitorData s_article) {
        Predicate predicate = cb.conjunction();


        if (s_article != null) {
            if (StringUtil.isNotEmpty(s_article.getPopularFeelingEvent())) {
                predicate.getExpressions().add(cb.like(root.get("popularFeelingEvent"), "%" + s_article.getPopularFeelingEvent() + "%"));
            }

            if (s_article.getMonitorTarget() != null && s_article.getMonitorTarget().getMtId() != null) {
                predicate.getExpressions().add(cb.equal(root.get("monitorTarget").get("mtId"), s_article.getMonitorTarget().getMtId()));
            }
        }
        return predicate;
    }
}
