package com.jiuzhang.url.domain;


import java.time.LocalDateTime;

import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="VisitInfo", description="")
@Entity
@Table(name="VISIT_INFO")
public class VisitInfo implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fromIp;

    private String fromUrl;

    private String longUrl;

    private String shortUrl;

    @Column(name = "gmtCreate", updatable = false)
    private Date gmtCreate;

    @Column(name = "gmtModified", insertable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date gmtModified;

}
