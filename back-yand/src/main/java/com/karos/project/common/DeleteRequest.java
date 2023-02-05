package com.karos.project.common;

import lombok.Data;

import java.io.Serializable;

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
    private Object id;

    private static final long serialVersionUID = 1L;
}