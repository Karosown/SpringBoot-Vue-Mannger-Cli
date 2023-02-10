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
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.karos.project.common.BaseResponse;
import com.karos.project.common.DeleteRequest;
import com.karos.project.common.ResultUtils;
import com.karos.project.model.dto.common.CommonSaveRequest;
import com.karos.project.model.entity.Common;
import com.karos.project.service.CommonService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/common")
public class CommonController {
    @Resource
    CommonService commonService;

    @ApiOperationSupport(author = "Karos")
    @ApiOperation(value = "获得属性列表接口")
    @GetMapping("/attribute/list")
    public BaseResponse<List<String>> getAttributelist(){
        List<Common> list = commonService.list();
        return ResultUtils.success(list.stream().map(v->{
            String s=v.getAttribute();
            return s;
        }).collect(Collectors.toList()));
    }

    @ApiOperationSupport(author = "Karos")
    @ApiOperation(value = "获得值列表接口")
    @GetMapping("/value/list")
    public BaseResponse<List<String>> getValuelist(){
        List<Common> list = commonService.list();
        return ResultUtils.success(list.stream().map(v->{
            String s=v.getValue();
            return s;
        }).collect(Collectors.toList()));
    }

    @ApiOperationSupport(author = "Karos")
    @ApiOperation(value = "获得所有所有属性和其值")
    @GetMapping("/list")
    public BaseResponse<List<Common>> getlist(){
        QueryWrapper<Common> queryWrapper=new QueryWrapper<>();
        queryWrapper.orderByAsc("createdTime");
        List<Common> list = commonService.list(queryWrapper);
        return ResultUtils.success(list);
    }

    @ApiOperationSupport(author = "Karos")
    @ApiOperation(value = "保存所有所有属性和其值")
    @PostMapping("/save")
    public BaseResponse<Boolean> save(@RequestBody CommonSaveRequest commonSaveRequest){
        boolean b = commonService.saveOrUpdateBatch(commonSaveRequest.getCommonList());
        return ResultUtils.success(b);
    }

    @ApiOperationSupport(author = "Karos")
    @ApiOperation(value = "删除某些属性")
    @PostMapping("/delete")
    public BaseResponse<Boolean> delete(@RequestBody DeleteRequest deleteRequest){
        boolean b = commonService.removeByIds( (List<String>)deleteRequest.getIds());
        return ResultUtils.success(b);
    }

    @ApiOperationSupport(author = "Karos")
    @ApiOperation(value = "获得某个属性的值")
    @GetMapping("/get")
    public BaseResponse<String> getAttribute(@RequestParam("attribute") String attribute){
        Common byId = commonService.getById(attribute);
        return ResultUtils.success(byId!=null?byId.getValue():null);
    }
}
