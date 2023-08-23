package com.qu.modules.web.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qu.modules.web.entity.Answer;
import com.qu.modules.web.param.*;
import org.jeecg.common.api.vo.Result;

public interface IAdminPrivateService extends IService<Answer> {


    boolean updateTableDate(AdminPrivateParam adminPrivateParam);

    boolean updateTableDrugFee(AdminPrivateUpdateTableDrugFeeParam adminPrivateUpdateTableDrugFeeParam);

    Result updateOptionValue(AdminPrivateUpdateOptionValueParam adminPrivateUpdateOptionValueParam);

    Result updateTableAddDel(AdminPrivateUpdateTableAddDelFeeParam param);

    Result updateTableAddAnswerStatus(AdminPrivateUpdateTableDrugFeeParam param);

    Result updateQuestionTableAddAnswerStatus(AdminPrivateUpdateTableDrugFeeParam param);

    Result updateQuestionAnswerStatus(AdminPrivateUpdateTableDrugFeeParam param);

    Result updateQSingleDiseaseTakeAnswerStatus(AdminPrivateUpdateTableDrugFeeParam param);

    Result updateAnswerStatus(AdminPrivateUpdateTableDrugFeeParam param);

    Result updateAnswerCheckCaseId(AdminPrivateUpdateTableDrugFeeParam param);

    Result updateAnswerOutTime(AdminPrivateUpdateTableDrugFeeParam param);

    Result updateAnswerCheckPassStatus(AdminPrivateUpdateTableDrugFeeParam param);

    Result updateTableAddMark(AdminPrivateUpdateTableDrugFeeParam param);

    Result updateAnswerCheckStatisticDetail(AdminPrivateUpdateTableDrugFeeParam param);

    Result updateAnswerCheckAllTable(AdminPrivateUpdateAnswerCheckAllTableParam param,Boolean tbrFlag);

    Result updateRegisterAnswerCheckAllTable(AdminPrivateUpdateAnswerAllTableParam param,Boolean tbrFlag);

    Result updateAnswerCheckStatisticDetailBySubtable(AdminPrivateUpdateAnswerCheckAllTableParam param);

    Result selectQuestionAllTable(AdminPrivateUpdateAnswerCheckAllTableParam param);

    Result selectRegisterQuestionAllTable(AdminPrivateUpdateAnswerAllTableParam param);

    Result addQuestionSubject(AdminPrivateUpdateTableDrugFeeParam param);



}
