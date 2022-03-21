package com.whistle.code.simulate.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.whistle.code.simulate.entity.PersonInfo;
import org.apache.ibatis.annotations.Mapper;

/**
 * 个人信息表(PersonInfo)表数据库访问层
 *
 * @author makejava
 * @since 2022-03-16 20:29:11
 */
@Mapper
public interface PersonInfoDao extends BaseMapper<PersonInfo> {

}

