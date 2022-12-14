package com.whistle.oauth.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;

import java.io.Serial;
import java.time.LocalDate;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author Gentvel
 * @since 2022-12-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("pri_rbac_user")
public class PriRbacUser implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(value = "uid", type = IdType.ASSIGN_ID)
    private String uid;

    private String userName;

    private String nickName;

    private String password;

    private String gender;

    private String age;

    private LocalDate birth;

    private String idCard;

    private String address;

    private String email;

    private String mobile;

    private String isAdmin;

    private String status;

    private String createdBy;

    private LocalDateTime createdTime;

    private String updatedBy;

    private LocalDateTime updatedTime;


}
