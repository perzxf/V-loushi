package com.ls.entity.monitor;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

/**
 * 监测类型表
 *
 * @author zhangchuanfei
 */
@Entity
@Table(name="monitor_type")
@JsonIgnoreProperties(value={"hibernateLazyInitializer","hander","fieldHandler"})
public class MonitorType implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer typeId;  //主键id

    @NotEmpty(message = "类型名称不能为空！")
    @Column(length = 200)
    private String  typeName;   //类型名称

    @Column(length = 255)
    private String  typeDescribe;  //类型描述

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getTypeDescribe() {
        return typeDescribe;
    }

    public void setTypeDescribe(String typeDescribe) {
        this.typeDescribe = typeDescribe;
    }
}
