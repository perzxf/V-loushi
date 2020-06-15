package com.ls.entity.monitor;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author zhangchuanfei
 */
@Entity
@Table(name="keyword_statistics")
@JsonIgnoreProperties(value={"hibernateLazyInitializer","hander","fieldHandler"})
public class KeywordStatistics implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer keywordId; //主键ID

    private String  keywords; //关键词

    private Integer keywordNum; //关键词的个数

    @ManyToOne
    @JoinColumn(name="typeId")
    private MonitorType monitorType; //监测类型ID

    public Integer getKeywordId() {
        return keywordId;
    }

    public void setKeywordId(Integer keywordId) {
        this.keywordId = keywordId;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public Integer getKeywordNum() {
        return keywordNum;
    }

    public void setKeywordNum(Integer keywordNum) {
        this.keywordNum = keywordNum;
    }

    public MonitorType getMonitorType() {
        return monitorType;
    }

    public void setMonitorType(MonitorType monitorType) {
        this.monitorType = monitorType;
    }
}
