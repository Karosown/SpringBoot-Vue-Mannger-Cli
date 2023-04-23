package com.karos.project.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.karos.project.common.BaseResponse;
import com.karos.project.common.ResultUtils;
import com.karos.project.model.dto.alumni.AlumniQueryRequest;
import com.karos.project.model.entity.Alumni;
import com.karos.project.model.vo.alumni.AlumniVO;
import com.karos.project.service.AlumniService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/alumni")
public class AlumniController {

    @Autowired
    private AlumniService alumniService;

    @PostMapping("/")
    @ApiOperation(value = "新增校友预约信息")
    public BaseResponse<AlumniVO> addAlumni(@RequestBody Alumni alumni) {

        Alumni alumni1 = alumniService.saveAlumni(alumni);
        AlumniVO alumniVO=new AlumniVO();
        BeanUtils.copyProperties(alumni,alumni1);
        return ResultUtils.success(alumniVO);
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "根据ID获取校友预约信息")
    public BaseResponse<Alumni> getAlumniById(@PathVariable Long id) {
        return ResultUtils.success(alumniService.getAlumniById(id));
    }



    /**
     * userName：根据姓名模糊查询
     * entranceYear：根据入学年份查询
     * major：根据专业模糊查询
     * company：根据当前工作或学习单位模糊查询
     * city：根据当前工作地点（省/市）查询
     * availableOnEventDay：根据是否当天能回学校参加活动查询
     */
    @GetMapping("/")
    @ApiOperation(value = "分页获取校友预约信息")
    public BaseResponse<Page<AlumniVO>> getAlumniByPage(AlumniQueryRequest alumniQueryRequest) {
        long current = 1;
        long size = 10;
        Alumni alumniQuery = new Alumni();
        if (alumniQueryRequest != null) {
            BeanUtils.copyProperties(alumniQueryRequest, alumniQuery);
            current = alumniQueryRequest.getCurrent();
            size = alumniQueryRequest.getPageSize();
        }
        QueryWrapper<Alumni> queryWrapper = new QueryWrapper<>(alumniQuery);
        Page<Alumni> alumniPage = alumniService.page(new Page<>(current, size), queryWrapper);
        Page<AlumniVO> alumniVOPage = new PageDTO<>(alumniPage.getCurrent(), alumniPage.getSize(), alumniPage.getTotal());
        List<AlumniVO> alumniVOList = alumniPage.getRecords().stream().map(alumni -> {
            AlumniVO alumniVO = new AlumniVO();
            BeanUtils.copyProperties(alumni, alumniVO);
            return alumniVO;
        }).collect(Collectors.toList());
        alumniVOPage.setRecords(alumniVOList);
        return ResultUtils.success(alumniVOPage);
    }

    @PutMapping("/{id}")
    @ApiOperation(value = "根据ID更新校友预约信息")
    public BaseResponse<Alumni> updateAlumniById(@PathVariable Long id,
                                                         @RequestBody Alumni alumni) {
        return ResultUtils.success(alumniService.updateAlumniById(id, alumni));
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "根据ID删除校友预约信息")
    public BaseResponse<Boolean> deleteAlumniById(@PathVariable Long id) {
        return ResultUtils.success( alumniService.deleteAlumniById(id));
    }
}
