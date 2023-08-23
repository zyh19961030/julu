package com.qu.constant;

import com.google.common.collect.Lists;

import java.util.List;

public class QuestionConstant {

	public static final Integer WRITE_FREQUENCY_PATIENT_WRITE = 0;
	public static final Integer WRITE_FREQUENCY_ALL = -1;
	public static final Integer WRITE_FREQUENCY_MONTH = 1;
	public static final Integer WRITE_FREQUENCY_QUARTER = 2;
	public static final Integer WRITE_FREQUENCY_YEAR = 3;
	public static final List<Integer> WRITE_FREQUENCY_MONTH_QUARTER_YEAR = Lists.newArrayList(1,2,3);

	public static final String QUESTION_VERSION_DEFAULT = "1.0.0";
	public static final Integer DEL_NORMAL = 0;
	public static final Integer DEL_DELETED = 1;
	public static final Integer QU_STATUS_DRAFT = 0;
	public static final Integer QU_STATUS_RELEASE = 1;

	public static final Integer CATEGORY_TYPE_NORMAL = 0;
	public static final Integer CATEGORY_TYPE_SINGLE_DISEASE = 1;
	public static final Integer CATEGORY_TYPE_CHECK = 2;
	public static final Integer CATEGORY_TYPE_REGISTER = 3;
	public static final List<Integer> CATEGORY_TYPE_NORMAL_AND_REGISTER = Lists.newArrayList(0,3);
	public static final Integer QU_STOP_NORMAL =0;
	public static final Integer QU_STOP_YES =1;
	public static final String SUB_TYPE_GROUP = "8";
	public static final String SUB_TYPE_TITLE = "9";

	public static final Integer TRACEABILITY_STATUS_NO_GENERATE = 1;
	public static final Integer TRACEABILITY_STATUS_GENERATED = 2;




}
