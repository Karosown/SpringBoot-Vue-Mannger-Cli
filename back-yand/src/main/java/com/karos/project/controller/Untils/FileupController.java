/**
 * Title
 *
 * @ClassName: fileup
 * @Description:
 * @author: Karos
 * @date: 2022/12/15 10:00
 * @Blog: https://www.wzl1.top/
 */

package com.karos.project.controller.Untils;

import cn.hutool.core.img.ImgUtil;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import cn.katool.qiniu.impl.QiniuServiceImpl;
import com.karos.project.annotation.AllLimitCheck;
import com.karos.project.common.BaseResponse;
import cn.katool.io.ImageUtils;
import com.karos.project.common.ErrorCode;
import com.karos.project.common.ResultUtils;
import com.karos.project.exception.BusinessException;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.Date;

@RestController
@RequestMapping("/file")
@AllLimitCheck(mustText = "文件上传")
@Slf4j
public class FileupController {

    @Resource
    QiniuServiceImpl qiniuService;
    @PostMapping("/upload")
    @AllLimitCheck(mustText = "文件上传",limitMaxNUM = 1000,limitMaxExpTime = 5000)
    @ApiOperationSupport(author = "Karos")
    @ApiOperation(value = "文件上传")
    public BaseResponse fileUpload(@RequestParam("file")MultipartFile file){
        String originalFilename = file.getOriginalFilename();
        File tempFile = null;
        String url=null;
        try {
            tempFile = File.createTempFile("temp", originalFilename.substring(originalFilename.lastIndexOf('.')));
            file.transferTo(tempFile);
            url = qiniuService.uploadFile(tempFile, "/file", file.getOriginalFilename(), true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResultUtils.success( url);
    }



    @PostMapping("/i2b/img")
    @AllLimitCheck(mustText = "图片转Base64编码")
    @ApiOperationSupport(author = "Karos")
    @ApiOperation(value = "图片转Base64编码")
    public BaseResponse img2base64(@RequestParam("image")MultipartFile image){
        String originalFilename = image.getOriginalFilename();
        File tempFile = null;
        String encode = null;
        try {
            tempFile = File.createTempFile("temp", originalFilename.substring(originalFilename.lastIndexOf('.')));
            image.transferTo(tempFile);
            encode=ImageUtils.img2base64(tempFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResultUtils.success( encode);
    }

    @GetMapping ("/i2b/src")
    @AllLimitCheck(mustText = "url图片转Base64编码")
    @ApiOperationSupport(author = "Karos")
    @ApiOperation(value = "url图片转base64编码")
    public BaseResponse img2base64Url(@RequestParam("url")String src){
        File tempFile= null;
        String encode = null;
        try {
            tempFile = File.createTempFile("temp",src.substring(src.lastIndexOf('.')));
            Image image = ImgUtil.getImage(new URL(src));
            ImgUtil.write(image,tempFile);
            encode=ImageUtils.img2base64(tempFile);
        } catch (IOException e) {
            e.printStackTrace();
            throw new BusinessException(ErrorCode.OPERATION_ERROR);
        }
        log.info("URL:{} BASE64{}",src,encode.substring(0,10));
        return ResultUtils.success( encode);
    }
}
