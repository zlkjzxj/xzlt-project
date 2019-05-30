package com.zlkj.admin.config;

import com.alibaba.fastjson.JSONObject;
import com.google.code.kaptcha.Constants;
import com.zlkj.admin.dto.ResultInfo;
import com.zlkj.admin.dto.UserInfo;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 自定义验证码校验过滤器
 */
public class CustomFormAuthenticationFilter extends FormAuthenticationFilter {
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        // 在这里进行验证码的校验
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        HttpSession session = httpServletRequest.getSession();
//        if (isAjax(request)) {
//            httpServletResponse.setCharacterEncoding("UTF-8");
//            httpServletResponse.setContentType("application/json");
//            ResultInfo<String> resultInfo = new ResultInfo<>();
//            resultInfo.setData("1");
//            resultInfo.setCode("403");
//            resultInfo.setMsg("登录认证失效，请重新登录!");
//            httpServletResponse.getWriter().write(JSONObject.toJSON(resultInfo).toString());
//        } else {
//            //saveRequestAndRedirectToLogin(request, response);
//            /**
//             * @Mark 非ajax请求重定向为登录页面
//             */
//            httpServletResponse.sendRedirect("/login");
//        }

        // 取出验证码  
        String validateCode = (String) session.getAttribute(Constants.KAPTCHA_SESSION_KEY);
        // 取出页面的验证码  
        // 输入的验证和session中的验证进行对比  
        String code = httpServletRequest.getParameter("code");
        if (code != null && validateCode != null && !code.equals(validateCode)) {
            // 如果校验失败，将验证码错误失败信息，通过shiroLoginFailure设置到request中
            // 自定义登录异常
            httpServletRequest.setAttribute("shiroLoginFailure", "kaptchaValidateFailed");
            // 拒绝访问，不再校验账号和密码  
            return true;
        }
        return super.onAccessDenied(request, response);
    }

//    private boolean isAjax(ServletRequest request) {
//        String header = ((HttpServletRequest) request).getHeader("X-Requested-With");
//        if ("XMLHttpRequest".equalsIgnoreCase(header)) {
//            return Boolean.TRUE;
//        }
//        return Boolean.FALSE;
//    }

    /*@Override
    protected boolean onLoginSuccess(AuthenticationToken token, Subject subject, ServletRequest request, ServletResponse response) throws Exception {
        UserInfo user = (UserInfo) SecurityUtils.getSubject().getPrincipal();

        if (user != null) {
            HttpServletRequest req = (HttpServletRequest) request;
            String clientType = req.getParameter("isPhone");
            if (StringUtils.isEmpty(clientType)) {
                return super.onLoginSuccess(token, subject, request, response);
            } else { // 根据类型跳转指定页面
                //其他情况自己想要跳转的页面
                WebUtils.getAndClearSavedRequest(request);
                String fallbackUrl = "/enterprise";
                WebUtils.redirectToSavedRequest(request, response, fallbackUrl);
            }
        }
        return false;
    }*/

}