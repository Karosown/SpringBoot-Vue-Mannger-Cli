package com.karos.project.common;

import lombok.*;

@AllArgsConstructor
public enum LimitTypeCommon {
    FileUPloadLimit("FileLimit"),
    RequestLimit("RequestLimit");

    String typeName;

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    @Override
    public String toString() {
        return this.typeName;
    }
}
