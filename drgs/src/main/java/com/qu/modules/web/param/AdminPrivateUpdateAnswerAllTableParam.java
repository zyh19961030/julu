package com.qu.modules.web.param;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(value="AdminPrivateUpdateAnswerAllTableParam", description="AdminPrivateUpdateAnswerAllTableParam")
public class AdminPrivateUpdateAnswerAllTableParam {
    private String name;
    private String type;
    private List<Integer> quId;
}
