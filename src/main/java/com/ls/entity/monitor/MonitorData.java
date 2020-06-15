package com.ls.entity.monitor;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 监测数据表
 *
 * @author zhangchuanfei
 */
@Entity
@Table(name="monitor_data")
@JsonIgnoreProperties(value={"hibernateLazyInitializer","hander","fieldHandler"})
public class MonitorData implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer mdId; //主键ID

    private String  popularFeelingEvent; //舆情事件

    private Integer eventUrl; //事件链接

    private Integer eventStatus; //事件状态

    private Integer eventType; //事件类型

    private Integer joinReport; //是否加入报告

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date    eventDate; //事件发布日期

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date    monitorTime; //监测日期

    @ManyToOne
    @JoinColumn(name="mtId")
    private MonitorTarget monitorTarget; //监测目标ID

    public Integer getMdId() {
        return mdId;
    }

    public void setMdId(Integer mdId) {
        this.mdId = mdId;
    }

    public String getPopularFeelingEvent() {
        return popularFeelingEvent;
    }

    public void setPopularFeelingEvent(String popularFeelingEvent) {
        this.popularFeelingEvent = popularFeelingEvent;
    }

    public Integer getEventUrl() {
        return eventUrl;
    }

    public void setEventUrl(Integer eventUrl) {
        this.eventUrl = eventUrl;
    }

    public Integer getEventStatus() {
        return eventStatus;
    }

    public void setEventStatus(Integer eventStatus) {
        this.eventStatus = eventStatus;
    }

    public Integer getEventType() {
        return eventType;
    }

    public void setEventType(Integer eventType) {
        this.eventType = eventType;
    }

    public Integer getJoinReport() {
        return joinReport;
    }

    public void setJoinReport(Integer joinReport) {
        this.joinReport = joinReport;
    }

    public Date getEventDate() {
        return eventDate;
    }

    public void setEventDate(Date eventDate) {
        this.eventDate = eventDate;
    }

    public Date getMonitorTime() {
        return monitorTime;
    }

    public void setMonitorTime(Date monitorTime) {
        this.monitorTime = monitorTime;
    }

    public MonitorTarget getMonitorTarget() {
        return monitorTarget;
    }

    public void setMonitorTarget(MonitorTarget monitorTarget) {
        this.monitorTarget = monitorTarget;
    }
}
