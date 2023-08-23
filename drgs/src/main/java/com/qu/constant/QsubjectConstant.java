package com.qu.constant;

import com.google.common.collect.Lists;

import java.util.List;

public class QsubjectConstant {


	public static final String SUB_TYPE_CHOICE = "1";
	public static final String SUB_TYPE_MULTIPLE_CHOICE = "2";
	public static final String SUB_TYPE_DATE = "3";
	public static final String SUB_TYPE_TIME = "4";
	public static final String SUB_TYPE_SINGLE_CHOICE_BOX = "5";
	public static final String SUB_TYPE_RESULT = "6";
	public static final String SUB_TYPE_GROUP = "8";
	public static final String SUB_TYPE_GROUP_SCORE = "15";
	public static final String SUB_TYPE_TITLE = "9";
	public static final String SUB_TYPE_CHOICE_SCORE = "13";
	public static final String SUB_TYPE_SINGLE_CHOICE_BOX_SCORE = "14";
	public static final String SUB_TYPE_HOSPITAL_USER = "18";
	public static final String SUB_TYPE_QUANTITY_STATISTICS = "19";
	public static final String SUB_TYPE_SCORE_COUNT = "20";
	public static final String SUB_TYPE_RESULT_EVALUATE = "21";
	//是否开启痕迹 改0:不开启1:开启
	public static final Integer MARK_OPEN = 1;
	public static final Integer MARK_CLOSE = 0;
	public static final String MARK_LENGTH = "0";
	public static final String MARK_TYPE = "TEXT";

	public static final String COLUMN_TYPE_DEFAULT = "字符串";
	public static final String COLUMN_TYPE_DATABASE_DEFAULT = "varchar";


    public static final Integer DEL_NORMAL = 0;
    public static final Integer DEL_DELETED = 1;

    public static final Integer WRITE_METABASE_YES = 1;
    public static final Integer WRITE_METABASE_NO = 0;

    public static final List<String> QUANTITY_STATISTICS_LIST =  Lists.newArrayList(SUB_TYPE_CHOICE,SUB_TYPE_SINGLE_CHOICE_BOX,SUB_TYPE_CHOICE_SCORE,SUB_TYPE_SINGLE_CHOICE_BOX_SCORE);
    public static final List<String> SCORE_COUNT_LIST =  Lists.newArrayList(SUB_TYPE_CHOICE_SCORE,SUB_TYPE_SINGLE_CHOICE_BOX_SCORE);
    public static final List<String> RESULT_EVALUATE_LIST =  Lists.newArrayList(SUB_TYPE_RESULT,SUB_TYPE_QUANTITY_STATISTICS,SUB_TYPE_SCORE_COUNT);



}
