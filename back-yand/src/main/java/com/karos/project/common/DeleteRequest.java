package com.karos.project.common;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 删除请求
 *
 * @author karos
 */
@Data
public class DeleteRequest implements Serializable {
    /**
     * id
     */
    private String id;
    private List<String> ids;
    private static final long serialVersionUID = 1L;
}