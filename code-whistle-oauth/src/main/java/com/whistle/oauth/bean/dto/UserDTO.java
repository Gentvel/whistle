package com.whistle.oauth.bean.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * <p>
 * 
 * </p>
 *
 * @author Gentvel
 * @since 2022-12-14
 */
@Data
public class UserDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("uid")
    private String uid;
    @NotBlank(message = "用户名不能为空")
    @ApiModelProperty("用户名（邮箱/手机号）")
    private String userName;
    @ApiModelProperty("昵称")
    private String nickName;
    @NotBlank(message = "密码不能为空")
    @ApiModelProperty("密码")
    private String password;
    @ApiModelProperty("性别")
    private String gender;
    @ApiModelProperty("年龄")
    private String age;
    @ApiModelProperty("出生年月日")
    private LocalDate birth;
    @ApiModelProperty("身份证")
    private String idCard;
    @ApiModelProperty("联系地址")
    private String address;
    @ApiModelProperty("邮箱")
    private String email;
    @ApiModelProperty("手机号")
    private String mobile;
    @ApiModelProperty("是否管理员")
    private String isAdmin;
    @ApiModelProperty("状态")
    private String status;

}
