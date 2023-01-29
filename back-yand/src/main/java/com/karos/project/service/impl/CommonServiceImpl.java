package com.karos.project.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.karos.project.model.entity.Common;
import com.karos.project.service.CommonService;
import com.karos.project.mapper.CommonMapper;
import org.springframework.stereotype.Service;

/**
* @author 30398
* @description 针对表【common(普通配置表)】的数据库操作Service实现
* @createDate 2023-01-17 07:53:55
*/
@Service
public class CommonServiceImpl extends ServiceImpl<CommonMapper, Common>
    implements CommonService {

}




