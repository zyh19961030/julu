package com.qu.modules.web.vo;

import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * @auther zhangyihao
 * @date 2021-12-29 16:14
 */
public class ReportFailureRecordParameterPageVo {

    @ApiModelProperty(value = "总条数")
    private long total;

    @ApiModelProperty(value = "数据")
    private List<ReportFailureRecordParameterVo> reportFailureRecordParameterVoList;

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public List<ReportFailureRecordParameterVo> getReportFailureRecordParameterVoList() {
        return reportFailureRecordParameterVoList;
    }

    public void setReportFailureRecordParameterVoList(List<ReportFailureRecordParameterVo> reportFailureRecordParameterVoList) {
        this.reportFailureRecordParameterVoList = reportFailureRecordParameterVoList;
    }
}
