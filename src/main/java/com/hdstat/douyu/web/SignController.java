package com.hdstat.douyu.web;

import com.hdstat.common.BaseRest;
import com.hdstat.douyu.model.AppUser;
import com.hdstat.douyu.service.HiveService;
import com.hdstat.util.JsonUtils;
import com.hdstat.common.DataJson;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/sign")
@Api(description = "登录API")
public class SignController extends BaseRest {

    @Autowired
    private HiveService hiveService;

    @ApiOperation(value = "123")
    @GetMapping(value = "/getAPI")
    public String getApi() {
        return hiveService.getHiveData();
    }

    @ApiOperation(value = "登录系统", notes = "登录系统")
    @PostMapping(value = "/signIn", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String signIn(HttpServletRequest request, HttpServletResponse response, String loginName, String password) {

        response.setContentType("text/html;charset=utf-8");
        Map<String, Object> resMap = new HashMap<String, Object>();
        resMap.put(RESULT_CODE, FAILED);
        String passwd = request.getParameter("password");
        try {

            HttpSession httpSession = request.getSession();
            AppUser user = (AppUser) httpSession.getAttribute(LOGIN_INFO);

            if (user != null && loginName.equals(user.getLoginName())) {
                resMap.put(RESULT_CODE, SUCCESS);
                resMap.put(RESULT_MESG, "用户已登陆");
                resMap.put("accessGranted", true);
                return JsonUtils.transObject2Json(resMap);
            }

            if ("admin".equals(loginName)) {
                if ("123".equals(passwd)) {
                    resMap.put(RESULT_CODE, SUCCESS);
                    resMap.put(RESULT_MESG, "登录成功");
                    resMap.put("accessGranted", true);
                    AppUser appUser = new AppUser();
                    appUser.setLoginName("admin");
                    appUser.setPassword("123");
                    httpSession.setAttribute(LOGIN_INFO, appUser);
                } else {
                    resMap.put(RESULT_MESG, "密码错误");
                }
                return JsonUtils.transObject2Json(resMap);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return JsonUtils.transObject2Json(resMap);
    }


    @ApiOperation(value = "获取用户登录信息", notes = "获取用户登录信息")
    @PostMapping(value = "/getLoginInfo", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String getLoginInfo(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("text/html;charset=utf-8");
        Map<String, Object> resMap = new HashMap<String, Object>();
        resMap.put(RESULT_CODE, FAILED);

        HttpSession session = request.getSession();
        if (session.getAttribute(LOGIN_INFO) == null) {
            resMap.put(RESULT_MESG, "用户未登录");
            return JsonUtils.transObject2Json(resMap);
        }

        AppUser appUser = (AppUser) session.getAttribute(LOGIN_INFO);

        resMap.put(RESULT_CODE, SUCCESS);
        resMap.put(LOGIN_INFO, appUser);

        return JsonUtils.transObject2Json(resMap);
    }

}
