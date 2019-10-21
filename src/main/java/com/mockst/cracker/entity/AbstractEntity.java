package com.mockst.cracker.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @Auther: zhiwei
 * @Date: 2019/10/18 20:20
 * @Description:
 */
@Data
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class AbstractEntity {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid",strategy = "uuid")
    @Column(length = 32)
    protected String id;

    @CreatedDate
    @Column(columnDefinition = "datetime comment '创建时间'")
    protected LocalDateTime createdDate;

    @LastModifiedDate
    @Column(columnDefinition = "datetime comment '更新时间'")
    protected LocalDateTime updatedDate;

}
