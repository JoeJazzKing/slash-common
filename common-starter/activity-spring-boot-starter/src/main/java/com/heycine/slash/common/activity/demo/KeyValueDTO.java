package com.heycine.slash.common.activity.demo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@ApiModel("KeyValueDTO")
@NoArgsConstructor
public class KeyValueDTO {

    @ApiModelProperty(value = "键")
    private String id;

    @ApiModelProperty(value = "值")
    private String name;

    @ApiModelProperty(value = "状态(false：禁用，true：激活)")
    private Boolean status = true;

    public KeyValueDTO(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public KeyValueDTO(String id, String name, Boolean status) {
        this.id = id;
        this.name = name;
        this.status = status;
    }

    public KeyValueDTO(String id, Boolean status) {
        this.id = id;
        this.status = status;
    }

}
