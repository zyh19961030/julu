package com.qu.modules.web.param;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(value="AdminPrivateUpdateAnswerCheckAllTableParam", description="AdminPrivateUpdateAnswerCheckAllTableParam")
public class AdminPrivateUpdateAnswerCheckAllTableParam {
    private String name;
    private List<Integer> quId;
}
