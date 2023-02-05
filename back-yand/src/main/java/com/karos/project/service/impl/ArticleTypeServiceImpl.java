package com.karos.project.service.impl;

import cn.hutool.core.lang.hash.Hash;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.karos.project.model.entity.Article;
import com.karos.project.model.entity.ArticleType;
import com.karos.project.model.vo.article.ArticleTypeVo;
import com.karos.project.model.vo.article.ArticleVo;
import com.karos.project.service.ArticleTypeService;
import com.karos.project.mapper.ArticletypeMapper;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
* @author 30398
* @description 针对表【articletype】的数据库操作Service实现
* @createDate 2023-02-01 03:24:40
*/
@Service
public class ArticleTypeServiceImpl extends ServiceImpl<ArticletypeMapper, ArticleType> implements ArticleTypeService{


    @Override
    public List<ArticleTypeVo> allList() {
        HashMap<Integer,ArticleTypeVo> listMap=new HashMap<>();
            List<ArticleTypeVo> list = list().stream().map(v->{
                //获取当前的v的Vo
                ArticleType current=v;
                ArticleTypeVo currentVo=new ArticleTypeVo();
                BeanUtils.copyProperties(v,currentVo);
                //找到父亲，在父亲里面添加vo
                Integer rId=0;
                listMap.putIfAbsent(current.getId(),currentVo);
                Integer fId=current.getFId();
                if (fId==0){
                        rId=current.getId();
                }
                else{
                    List<ArticleTypeVo> children = listMap.get(fId).getChildren();
                    if (children==null){
                        children=new ArrayList<>();
                        listMap.get(fId).setChildren(children);
                        children = listMap.get(fId).getChildren();
                        }
                    children.add(currentVo);
                }
                //将父亲添加到listMap中，返回父亲
                if (rId==0) return null;
                ArticleTypeVo articleTypeVo = listMap.get(rId);
                if (articleTypeVo.getChildren()==null||articleTypeVo.getChildren().size()<=0)articleTypeVo.setChildren(null);
                else articleTypeVo.setChildren(
                        articleTypeVo.getChildren().stream().collect(Collectors.toSet()).
                                stream().collect(Collectors.toList())
                );
                return articleTypeVo.getId()==v.getId()?articleTypeVo:null;
            }).collect(Collectors.toSet()).stream().collect(Collectors.toList());
            list.remove(null);
            return list;
    }
}




