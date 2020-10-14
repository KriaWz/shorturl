package com.jiuzhang.url.domain;

import java.time.LocalDateTime;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="LongToShort", description="")
@Entity
@Table(name="LONG_TO_SHORT")
public class LongToShort implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String longUrl;

    private String shortUrl;
}
