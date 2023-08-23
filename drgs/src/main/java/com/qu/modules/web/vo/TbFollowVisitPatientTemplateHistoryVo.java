package com.qu.modules.web.vo;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @Description: 随访患者记录表
 * @Author: jeecg-boot
 * @Date:   2023-02-23
 * @Version: V1.0
 */
@Data
@TableName("tb_follow_visit_patient_record")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="TbFollowVisitPatientTemplateHistoryVo", description="TbFollowVisitPatientTemplateHistoryVo")
public class TbFollowVisitPatientTemplateHistoryVo {

    @ApiModelProperty(value = "id")
	private Integer id;

    @ApiModelProperty(value = "随访次数_第N次随访")
    private Integer followVisitNumber;

    @ApiModelProperty(value = "距时间起点的整数")
    private Integer dateStartNumber;

    @ApiModelProperty(value = "距时间起点 1天 2周 3月 4年")
    private Integer dateStartType;

    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "预计开始随访日期")
    private Date followVisitTime;

    @ApiModelProperty(value = "随访关联内容的问卷id")
    private Integer questionId;

    @ApiModelProperty(value = "随访关联内容的问卷名字")
    private String questionName;

    @ApiModelProperty(value = "答案id")
    private Integer answerId;

    @ApiModelProperty(value = "状态 1待填报 2已填报 3随访被终止")
    private Integer status;

}
