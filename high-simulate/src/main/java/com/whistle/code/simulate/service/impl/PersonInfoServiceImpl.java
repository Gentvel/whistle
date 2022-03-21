package com.whistle.code.simulate.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.whistle.code.simulate.dao.PersonInfoDao;
import com.whistle.code.simulate.entity.PersonInfo;
import com.whistle.code.simulate.service.PersonInfoService;
import org.springframework.stereotype.Service;

/**
 * 个人信息表(PersonInfo)表服务实现类
 *
 * @author makejava
 * @since 2022-03-16 20:29:17
 */
@Service("personInfoService")
public class PersonInfoServiceImpl extends ServiceImpl<PersonInfoDao, PersonInfo> implements PersonInfoService {

}

