package com.ls.entity.monitor;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ls.entity.User;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

/**
 * 监测目标表
 *
 * @author zhangchuanfei
 */
@Entity
@Table(name="monitor_target")
@JsonIgnoreProperties(value={"hibernateLazyInitializer","hander","fieldHandler"})
public class MonitorTarget implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer  mtId;  //主键ID

    @NotEmpty(message = "监测主题不能为空！")
    @Column(length = 200)
    private String  monitorMotif; //监测主题

    @NotEmpty(message = "监测目标名称不能为空！")
    @Column(length = 200)
    private String  monitorTargetName; //监测目标名称

    @ManyToOne
    @JoinColumn(name="typeId")
    private MonitorType monitorType; //监测类型ID

    @ManyToOne
    @JoinColumn(name="keywordId")
    private KeywordStatistics keywordStatistics;   //监测关键词ID

    @ManyToOne
    @JoinColumn(name="userId")
    private User user;  //用户ID

    public Integer getMtId() {
        return mtId;
    }

    public void setMtId(Integer mtId) {
        this.mtId = mtId;
    }

    public String getMonitorMotif() {
        return monitorMotif;
    }

    public void setMonitorMotif(String monitorMotif) {
        this.monitorMotif = monitorMotif;
    }

    public String getMonitorTargetName() {
        return monitorTargetName;
    }

    public void setMonitorTargetName(String monitorTargetName) {
        this.monitorTargetName = monitorTargetName;
    }

    public MonitorType getMonitorType() {
        return monitorType;
    }

    public void setMonitorType(MonitorType monitorType) {
        this.monitorType = monitorType;
    }

    public KeywordStatistics getKeywordStatistics() {
        return keywordStatistics;
    }

    public void setKeywordStatistics(KeywordStatistics keywordStatistics) {
        this.keywordStatistics = keywordStatistics;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
