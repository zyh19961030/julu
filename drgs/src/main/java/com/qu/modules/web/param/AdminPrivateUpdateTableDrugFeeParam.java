package com.qu.modules.web.param;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel(value="处理药品费用入参", description="处理药品费用入参")
public class AdminPrivateUpdateTableDrugFeeParam {
    private String name;
}
