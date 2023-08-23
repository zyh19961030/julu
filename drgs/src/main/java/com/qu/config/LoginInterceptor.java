package com.qu.config;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.qu.constant.Constant;
import com.qu.modules.web.pojo.JsonRootBean;
import com.qu.util.HttpClient;
import com.qu.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.Nullable;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 登录拦截器
 *
 * @author yanrj 2021-03-18
 */

@Slf4j
public class LoginInterceptor implements HandlerInterceptor {

    private static final String URL_LOGIN = "/login";

    private static final String URL_REGISTER = "/register";
    private static final List<String> URL_LIST = Lists.newArrayList("/web/miniapp","/web/ba",
            "/j/admin",
            "/answerCheck/exportXlsDetailList",
            "/answerCheck/exportXlsCheckQuestionHistoryStatisticDeptDetailList",
            "/answerCheck/exportXlsCheckQuestionHistoryStatisticDetailList",
            "/business/qSingleDiseaseTake/departmentQuantityStatisticsExport",
            "/business/qSingleDiseaseTake/allSingleDiseaseReportStatisticByDeptExport",
            "/business/qSingleDiseaseTake/allSingleDiseaseReportStatisticExport",
            "/business/questionTemp/copyTemp");


    @Value("${system.tokenUrl}")
    private String tokenUrl;

    /**
     * 在请求处理之前进行调用（Controller方法调用之前）
     *
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("-----preHandle");
        //放过登录接口
        String requestUrl = request.getRequestURL().toString();
//        if (requestUrl.indexOf(URL_LOGIN) != -1) {
//            return true;
//        }
//        //放过注册
//        if (requestUrl.indexOf(URL_REGISTER) != -1) {
//            return true;
//        }
//        if (requestUrl.contains(URL_MINIAPP) ) {
        for (String s : URL_LIST) {
            if (requestUrl.contains(s)) {
                return true;
            }
        }


        String token = request.getHeader("token");
        if (StringUtil.isEmpty(token)) {
            log.info("token is null-----{}", "preHandle");
            log.info("----------------token is empty...");
            Result result = new Result();
            result.setCode(Constant.ERROR_CODE_60001);
            result.setMessage("token is empty...");
            result.setSuccess(false);
            result.setTimestamp(System.currentTimeMillis());
            request.setCharacterEncoding("UTF-8");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().append(JSON.toJSONString(result));
            return false;
        }

//        Data data = (Data) request.getSession().getAttribute(Constant.SESSION_USER);
//        if (data != null) {
//            log.info("getSession is null-----preHandle");
//            return true;
//        }

        String cookie = "JSESSIONID=" + token;
        String res = HttpClient.doPost(tokenUrl, cookie, null);
        JsonRootBean jsonRootBean = JSON.parseObject(res, JsonRootBean.class);
        if(jsonRootBean==null || jsonRootBean.getData()==null){
            log.info("jsonRootBean is null-----preHandle");
            Result result = new Result();
            result.setCode(Constant.ERROR_CODE_60001);
            result.setMessage("token is error...");
            result.setSuccess(false);
            result.setTimestamp(System.currentTimeMillis());
            request.setCharacterEncoding("UTF-8");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().append(JSON.toJSONString(result));
            return false;
        }
        log.info("request continue-----preHandle");
        request.getSession().setAttribute(Constant.SESSION_USER,jsonRootBean.getData());
        return true;
    }

    /**
     * 请求处理之后进行调用，但是在视图被渲染之前（Controller方法调用之后）
     *
     * @param request
     * @param response
     * @param handler
     * @param modelAndView
     * @throws Exception
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable ModelAndView modelAndView) throws Exception {
        log.info("-----{}", "postHandle");
    }

    /**
     * 在整个请求结束之后被调用，也就是在DispatcherServlet 渲染了对应的视图之后执行（主要是用于进行资源清理工作）
     *
     * @param request
     * @param response
     * @param handler
     * @param ex
     * @throws Exception
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {
        log.info("-----{}", "afterCompletion");
    }
}
