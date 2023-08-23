package com.qu.modules.web.param;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel(value="AdminPrivateUpdateTableAddDelFeeParam", description="AdminPrivateUpdateTableAddDelFeeParam")
public class AdminPrivateUpdateTableAddDelFeeParam {
    private Integer quId;

    private String name;
}
