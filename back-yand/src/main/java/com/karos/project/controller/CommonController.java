/**
 * Title
 *
 * @ClassName: CommonController
 * @Description:
 * @author: 巫宗霖
 * @date: 2023/1/17 8:40
 * @Blog: https://www.wzl1.top/
 */

package com.karos.project.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.karos.project.common.BaseResponse;
import com.karos.project.common.DeleteRequest;
import com.karos.project.common.ResultUtils;
import com.karos.project.model.dto.common.CommonSaveRequest;
import com.karos.project.model.entity.Common;
import com.karos.project.service.CommonService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/common")
public class CommonController {
    @Resource
    CommonService commonService;

    @GetMapping("/attribute/list")
    public BaseResponse<List<String>> getAttributelist(){
        List<Common> list = commonService.list();
        return ResultUtils.success(list.stream().map(v->{
            String s=v.getAttribute();
            return s;
        }).collect(Collectors.toList()));
    }

    @GetMapping("/value/list")
    public BaseResponse<List<String>> getValuelist(){
        List<Common> list = commonService.list();
        return ResultUtils.success(list.stream().map(v->{
            String s=v.getValue();
            return s;
        }).collect(Collectors.toList()));
    }

    @GetMapping("/list")
    public BaseResponse<List<Common>> getlist(){
        QueryWrapper<Common> queryWrapper=new QueryWrapper<>();
        queryWrapper.orderByAsc("createdTime");
        List<Common> list = commonService.list(queryWrapper);
        return ResultUtils.success(list);
    }

    @PostMapping("/save")
    public BaseResponse<Boolean> save(@RequestBody CommonSaveRequest commonSaveRequest){
        boolean b = commonService.saveOrUpdateBatch(commonSaveRequest.getCommonList());
        return ResultUtils.success(b);
    }

    @PostMapping("/delete")
    public BaseResponse<Boolean> delete(@RequestBody DeleteRequest deleteRequest){
        boolean b = commonService.removeById((String) deleteRequest.getId());
        return ResultUtils.success(b);
    }
}
