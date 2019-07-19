package com.zlkj.business.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zlkj.admin.dto.AppParam;
import com.zlkj.admin.dto.ResultInfo;
import com.zlkj.admin.entity.Code;
import com.zlkj.admin.service.ICodeService;
import com.zlkj.admin.util.PasswordEncoder;
import com.zlkj.business.dto.TestQuestionDto;
import com.zlkj.business.dto.TestQuestionTypeDto;
import com.zlkj.business.dto.TestUserMarkDto;
import com.zlkj.business.entity.*;
import com.zlkj.business.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.*;


/**
 * @Description 人才测评接口
 * @Author sunny
 * @Date 2019-05-13 16:05
 */
@Controller
@RequestMapping("/test")
public class TestController {

    @Resource
    private ICodeService iCodeService;
    @Resource
    private ITestCodeService iTestCodeService;
    @Resource
    private ITestQuestionTypeService iTestQuestionTypeService;

    @Resource
    private ITestQuestionService iTestQuestionService;
    @Resource
    private ITestQuestionSelectService iTestQuestionSelectService;
    @Resource
    private ITestUserService iTestUserService;
    @Resource
    private ITestUserMarkService iTestUserMarkService;
    @Resource
    private ITestUserMarkResultService iTestUserMarkResultService;

    /**
     * 小程序扫码后跳转的页面
     *
     * @return
     */
    @RequestMapping("/index")
    public String index() {
        return "question/index";
    }

    /**
     * 生成二维码页面
     *
     * @return
     */
    @RequestMapping("/qrcode")
    public String qrcode() {
        return "question/qrcode";
    }

    /**
     * 获取code
     *
     * @return
     */
    @RequestMapping("/getTestCode")
    @ResponseBody
    public ResultInfo<Map<String, List<Code>>> getTestCode() {
        //性别
        QueryWrapper<Code> wrapper = new QueryWrapper<>();
        wrapper.eq("code", "gender");
        List<Code> genderList = iCodeService.list(wrapper);
        //学历
        QueryWrapper<Code> wrapper1 = new QueryWrapper<>();
        wrapper1.eq("code", "qualification");
        List<Code> qualificationList = iCodeService.list(wrapper1);
        Map<String, List<Code>> codeMap = new HashMap<>(2);
        codeMap.put("gender", genderList);
        codeMap.put("qualification", qualificationList);
        return new ResultInfo(codeMap);
    }


    /**
     * 查询测试数据
     *
     * @param appParam
     * @return
     */
    @RequestMapping("/getTestList")
    @ResponseBody
    public ResultInfo<List<TestQuestionTypeDto>> getTestInfo(AppParam appParam) {
        //测试类型
        QueryWrapper<TestQuestionType> typeQueryWrapper = new QueryWrapper<>();
        typeQueryWrapper.ne("test_question_type", "");
        List<TestQuestionType> typeList = iTestQuestionTypeService.list(typeQueryWrapper);
        List<TestQuestionTypeDto> questionTypeDtoList = new ArrayList<>();
        for (TestQuestionType type : typeList) {
            TestQuestionTypeDto dto = new TestQuestionTypeDto();
            dto.setId(type.getId());
            dto.setTestTypeName(type.getTestTypeName());
            dto.setTestTypeCode(type.getTestTypeCode());
            dto.setTestQuestionType(type.getTestQuestionType());
            dto.setTestQuestionTypeName(type.getTestQuestionTypeName());
            dto.setBackImg(type.getBackImg());
            //测试结果
            QueryWrapper<TestCode> resultWrapper = new QueryWrapper<>();
            resultWrapper.eq("code", type.getTestQuestionType());
            List<TestCode> resultList = iTestCodeService.list(resultWrapper);
            //结果对应的职业类型
            QueryWrapper<TestCode> jobWrapper = new QueryWrapper<>();
            jobWrapper.eq("code", type.getTestQuestionResult());
            List<TestCode> jobList = iTestCodeService.list(jobWrapper);
            //测试题
            QueryWrapper<TestQuestion> questionWrapper = new QueryWrapper<>();
            questionWrapper.eq("test_type_id", type.getId());
            List<TestQuestion> questionsList = iTestQuestionService.list(questionWrapper);
            List<TestQuestionDto> questionsDtoList = new ArrayList<>();
            for (TestQuestion question : questionsList) {
                QueryWrapper<TestQuestionSelect> selectQueryWrapper = new QueryWrapper<>();
                selectQueryWrapper.eq("question_id", question.getId());
                List<TestQuestionSelect> selects = iTestQuestionSelectService.list(selectQueryWrapper);
                TestQuestionDto questionDto = new TestQuestionDto();
                questionDto.setId(question.getId());
                questionDto.setQuestionAnswer(question.getQuestionAnswer());
                questionDto.setQuestionMark(question.getQuestionMark());
                questionDto.setQuestionName(question.getQuestionName());
                questionDto.setTestTypeId(question.getTestTypeId());
                questionDto.setSelectList(selects);
                questionsDtoList.add(questionDto);
            }
            dto.setResult(resultList);
            dto.setJob(jobList);
            dto.setQuestion(questionsDtoList);
            questionTypeDtoList.add(dto);
        }
        return new ResultInfo<>(questionTypeDtoList);
    }

    /**
     * 用户注册
     *
     * @param user
     * @return
     */
    @RequestMapping("/add")
    public @ResponseBody
    ResultInfo<TestUser> add(@RequestBody TestUser user) {
        if (StringUtils.isEmpty(user.getPhone()) || StringUtils.isEmpty(user.getUserPass())) {
            return new ResultInfo<>("1001", "电话号码、密码不能为空");
        }
        QueryWrapper<TestUser> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("phone", user.getPhone());
        TestUser oldUser = iTestUserService.getOne(userQueryWrapper);
        if (oldUser != null) {
            return new ResultInfo<>("1002", "此电话号码已被注册");
        }
        Map<String, String> map = PasswordEncoder.enCodePassWord(user.getPhone(), user.getUserPass());
        user.setSalt(map.get(PasswordEncoder.SALT));
        user.setUserPass(map.get(PasswordEncoder.PASSWORD));
        boolean b = iTestUserService.save(user);
        if (b) {
            return new ResultInfo<>(user);
        }
        return new ResultInfo<>("1003", "注册失败");
    }

    /**
     * 用户登陆
     *
     * @param user
     * @return
     */
    @RequestMapping("/login")
    public @ResponseBody
    ResultInfo<Map<String, Object>> login(@RequestBody TestUser user) {
        if (StringUtils.isEmpty(user.getPhone()) || StringUtils.isEmpty(user.getUserPass())) {
            return new ResultInfo<>("1001", "电话号码、密码不能为空");
        }
        QueryWrapper<TestUser> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("phone", user.getPhone());
        TestUser testUser = iTestUserService.getOne(userQueryWrapper);
        if (testUser != null) {
            String encodePass = PasswordEncoder.enCodePassWord1(user.getPhone(), user.getUserPass(), testUser.getSalt());
            if (!encodePass.equals(testUser.getUserPass())) {
                return new ResultInfo<>("1003", "电话号码或密码错误");
            }
            //测试类型
            QueryWrapper<TestQuestionType> typeQueryWrapper = new QueryWrapper<>();
            typeQueryWrapper.select("id", "test_type_name", "test_type_code", "test_question_type");
            typeQueryWrapper.ne("test_question_type", "");
            List<TestQuestionType> typeList = iTestQuestionTypeService.list(typeQueryWrapper);
            List<TestQuestionTypeDto> questionTypeDtoList = new ArrayList<>();
            for (TestQuestionType type : typeList) {
                TestQuestionTypeDto dto = new TestQuestionTypeDto();
                QueryWrapper<TestUserMark> markQueryWrapper = new QueryWrapper<>();
                markQueryWrapper.eq("user_id", testUser.getId());
                markQueryWrapper.eq("question_type_id", type.getId());
                TestUserMark mark = iTestUserMarkService.getOne(markQueryWrapper);
                dto.setId(type.getId());
                dto.setTestTypeName(type.getTestTypeName());
                dto.setTestTypeCode(type.getTestTypeCode());
                dto.setTestQuestionType(type.getTestQuestionType());
                //判断是否测评过
                if (mark != null) {
                    dto.setCpSign(1);
                } else {
                    dto.setCpSign(0);
                }
                questionTypeDtoList.add(dto);
            }
            Map map = new HashMap(2);
            map.put("userInfo", testUser);
            map.put("questionInfo", questionTypeDtoList);
            return new ResultInfo<>(map);
        }
        return new ResultInfo<>("1002", "电话号码不存在");

    }

    /**
     * 进主页面获取测试题
     *
     * @return
     */
    @RequestMapping("/getTestInfo")
    public @ResponseBody
    ResultInfo<Map<String, Object>> getTestInfo() {
        //测试类型
        QueryWrapper<TestQuestionType> typeQueryWrapper = new QueryWrapper<>();
        typeQueryWrapper.select("id", "test_type_name", "test_type_code", "test_type_header", "test_question_type", "test_question_type_name", "back_img", "ymlx");
        typeQueryWrapper.ne("test_question_type", "");
        List<TestQuestionType> typeList = iTestQuestionTypeService.list(typeQueryWrapper);
        List<TestQuestionTypeDto> questionTypeDtoList = new ArrayList<>();
        for (TestQuestionType type : typeList) {
            TestQuestionTypeDto dto = new TestQuestionTypeDto();
            dto.setId(type.getId());
            dto.setTestTypeName(type.getTestTypeName());
            dto.setTestTypeHeader(type.getTestTypeHeader());
            dto.setTestTypeCode(type.getTestTypeCode());
            dto.setTestQuestionType(type.getTestQuestionType());
            dto.setTestQuestionTypeName(type.getTestQuestionTypeName());
            dto.setBackImg(type.getBackImg());
            dto.setOpenSign(type.getOpenSign());
            dto.setYmlx(type.getYmlx());
            questionTypeDtoList.add(dto);
        }
        //logo
        QueryWrapper<TestCode> codeWrapper = new QueryWrapper<>();
        codeWrapper.eq("code", "testlogo");
        TestCode logo = iTestCodeService.getOne(codeWrapper);
        Map<String, Object> map = new HashMap<>(2);
        map.put("questionTypeList", questionTypeDtoList);
        map.put("logo", logo.getCodeDesc());
        return new ResultInfo<>(map);

    }

    /**
     * 根据id获取测试题信息
     *
     * @return
     */
    @RequestMapping("/getTypeInfo")
    public @ResponseBody
    ResultInfo<TestQuestionTypeDto> getTypeInfo(@RequestBody TestQuestionType testQuestionType) {
        //测试类型
        QueryWrapper<TestQuestionType> wrapper = new QueryWrapper<>();
        wrapper.select("id", "test_question_type", "test_question_result").eq("id", testQuestionType.getId());
        TestQuestionType type = iTestQuestionTypeService.getOne(wrapper);
        TestQuestionTypeDto dto = new TestQuestionTypeDto();
        dto.setId(type.getId());
        //测试结果
        QueryWrapper<TestCode> resultWrapper = new QueryWrapper<>();
        resultWrapper.eq("code", type.getTestQuestionType());
        List<TestCode> resultList = iTestCodeService.list(resultWrapper);
        //结果对应的职业类型
        QueryWrapper<TestCode> jobWrapper = new QueryWrapper<>();
        jobWrapper.eq("code", type.getTestQuestionResult());
        List<TestCode> jobList = iTestCodeService.list(jobWrapper);
        //测试题
        QueryWrapper<TestQuestion> questionWrapper = new QueryWrapper<>();
        questionWrapper.eq("test_type_id", type.getId());
        List<TestQuestion> questionsList = iTestQuestionService.list(questionWrapper);
        List<TestQuestionDto> questionsDtoList = new ArrayList<>();
        for (TestQuestion question : questionsList) {
            QueryWrapper<TestQuestionSelect> selectQueryWrapper = new QueryWrapper<>();
            selectQueryWrapper.eq("question_id", question.getId());
            List<TestQuestionSelect> selects = iTestQuestionSelectService.list(selectQueryWrapper);
            TestQuestionDto questionDto = new TestQuestionDto();
            questionDto.setId(question.getId());
            questionDto.setQuestionAnswer(question.getQuestionAnswer());
            questionDto.setQuestionMark(question.getQuestionMark());
            questionDto.setQuestionName(question.getQuestionName());
            questionDto.setTestTypeId(question.getTestTypeId());
            questionDto.setSelectList(selects);
            questionsDtoList.add(questionDto);
        }
        dto.setResult(resultList);
        dto.setJob(jobList);
        dto.setQuestion(questionsDtoList);
        //logo
        QueryWrapper<TestCode> codeWrapper = new QueryWrapper<>();
        codeWrapper.eq("code", "testqrcode");
        TestCode logo = iTestCodeService.getOne(codeWrapper);
        dto.setQrcode(logo.getCodeDesc());
        return new ResultInfo<>(dto);

    }

    /*  *//**
     * 进主页面获取测试题
     *
     * @return
     *//*
    @RequestMapping("/getTestInfo")
    public @ResponseBody
    ResultInfo<List<TestQuestionTypeDto>> getTestInfo() {
        //测试类型
        QueryWrapper<TestQuestionType> typeQueryWrapper = new QueryWrapper<>();
        typeQueryWrapper.ne("test_question_type", "");
        List<TestQuestionType> typeList = iTestQuestionTypeService.list(typeQueryWrapper);
        List<TestQuestionTypeDto> questionTypeDtoList = new ArrayList<>();
        for (TestQuestionType type : typeList) {
            TestQuestionTypeDto dto = new TestQuestionTypeDto();
            dto.setId(type.getId());
            dto.setTestTypeName(type.getTestTypeName());
            dto.setTestTypeCode(type.getTestTypeCode());
            dto.setTestQuestionType(type.getTestQuestionType());
            dto.setTestQuestionTypeName(type.getTestQuestionTypeName());
            dto.setBackImg(type.getBackImg());
            dto.setResultDesc(type.getResultDesc());

            //测试结果
            QueryWrapper<TestCode> resultWrapper = new QueryWrapper<>();
            resultWrapper.eq("code", type.getTestQuestionType());
            List<TestCode> resultList = iTestCodeService.list(resultWrapper);
            //结果对应的职业类型
            QueryWrapper<TestCode> jobWrapper = new QueryWrapper<>();
            jobWrapper.eq("code", type.getTestQuestionResult());
            List<TestCode> jobList = iTestCodeService.list(jobWrapper);
            //测试题
            QueryWrapper<TestQuestion> questionWrapper = new QueryWrapper<>();
            questionWrapper.eq("test_type_id", type.getId());
            List<TestQuestion> questionsList = iTestQuestionService.list(questionWrapper);
            List<TestQuestionDto> questionsDtoList = new ArrayList<>();
            for (TestQuestion question : questionsList) {
                QueryWrapper<TestQuestionSelect> selectQueryWrapper = new QueryWrapper<>();
                selectQueryWrapper.eq("question_id", question.getId());
                List<TestQuestionSelect> selects = iTestQuestionSelectService.list(selectQueryWrapper);
                TestQuestionDto questionDto = new TestQuestionDto();
                questionDto.setId(question.getId());
                questionDto.setQuestionAnswer(question.getQuestionAnswer());
                questionDto.setQuestionMark(question.getQuestionMark());
                questionDto.setQuestionName(question.getQuestionName());
                questionDto.setTestTypeId(question.getTestTypeId());
                questionDto.setSelectList(selects);
                questionsDtoList.add(questionDto);
            }
            if (questionsDtoList.isEmpty()) {
                dto.setOpenSign(0);
            }
            dto.setResult(resultList);
            dto.setJob(jobList);
            dto.setQuestion(questionsDtoList);

            questionTypeDtoList.add(dto);
        }
        return new ResultInfo<>(questionTypeDtoList);

    }*/

    /**
     * 保存测试结果
     *
     * @return
     */
    @RequestMapping("/saveResult")
    public @ResponseBody
    @Transactional
    ResultInfo<TestUserMarkDto> saveResult(@RequestBody TestUserMarkDto userMarkDto) {
        TestUserMark userMark = new TestUserMark();
        userMark.setQuestionTypeId(userMarkDto.getQuestionTypeId());
        userMark.setUserName(userMarkDto.getUserName());
        userMark.setUserId(userMarkDto.getUserId());
        userMark.setTestJob(userMarkDto.getTestJob());
        userMark.setCpSign(1);
        iTestUserMarkService.save(userMark);
        List<TestUserMarkResult> results = userMarkDto.getResults();
        if (results.size() > 0) {
            for (TestUserMarkResult result : results) {
                result.setQuestionTypeId(userMarkDto.getQuestionTypeId());
                result.setUserId(userMarkDto.getUserId());
                iTestUserMarkResultService.save(result);
            }
        }
        userMarkDto.setId(userMark.getId());
        return new ResultInfo<>(userMarkDto);
    }

    /**
     * 进主页面获取测试题
     *
     * @return
     */
    @RequestMapping("/getResultInfo")
    public @ResponseBody
    ResultInfo<TestQuestionTypeDto> getResultInfo(@RequestBody TestUserMark userMark) {
        TestQuestionTypeDto typeDto = new TestQuestionTypeDto();
        TestUserMarkDto userMarkDto = new TestUserMarkDto();
        QueryWrapper<TestQuestionType> wrapper = new QueryWrapper<>();
        wrapper.select("id", "test_question_type", "test_question_result", "ymlx", "result_desc").eq("id", userMark.getQuestionTypeId());
        TestQuestionType type = iTestQuestionTypeService.getOne(wrapper);
        typeDto.setYmlx(type.getYmlx());
        typeDto.setResultDesc(type.getResultDesc());
        //userMark
        QueryWrapper<TestUserMark> markWrapper = new QueryWrapper<>();
        markWrapper.eq("user_id", userMark.getUserId()).eq("question_type_id", userMark.getQuestionTypeId());
        TestUserMark mark = iTestUserMarkService.getOne(markWrapper);
        if (mark != null) {
            //markResult
            QueryWrapper<TestUserMarkResult> markResultQueryWrapper = new QueryWrapper<>();
            markResultQueryWrapper.eq("user_id", userMark.getUserId()).eq("question_type_id", userMark.getQuestionTypeId());
            markResultQueryWrapper.orderByDesc("value");
            List<TestUserMarkResult> results = iTestUserMarkResultService.list(markResultQueryWrapper);
            //测试结果
            QueryWrapper<TestCode> resultWrapper = new QueryWrapper<>();
            resultWrapper.eq("code", type.getTestQuestionType());
            List<TestCode> resultList = iTestCodeService.list(resultWrapper);
            //结果对应的职业类型
            QueryWrapper<TestCode> jobWrapper = new QueryWrapper<>();
            jobWrapper.eq("code", type.getTestQuestionResult());
            List<TestCode> jobList = iTestCodeService.list(jobWrapper);
            typeDto.setResult(resultList);
            typeDto.setJob(jobList);
           /* //xun huan paixu result
            if ("2".equals(userMark.getQuestionTypeId())) {
                List<TestCode> jobList1 = new ArrayList<>();
                for (TestUserMarkResult result : results) {
                    for (TestCode code : jobList) {
                        if (result.getCode().equals(code.getCodeDesc1())) {
                            jobList1.add(code);
                            continue;
                        }
                    }
                }
                typeDto.setJob(jobList1);
            } else {
                typeDto.setJob(jobList);
            }*/
            //测试题
            userMarkDto.setUserId(mark.getUserId());
            userMarkDto.setQuestionTypeId(mark.getQuestionTypeId());
            userMarkDto.setResults(results);
            userMarkDto.setTestResult(mark.getTestResult());
            userMarkDto.setTestJob(mark.getTestJob());
            typeDto.setTestUserMarkDto(userMarkDto);
        }
        //logo
        QueryWrapper<TestCode> codeWrapper = new QueryWrapper<>();
        codeWrapper.eq("code", "testqrcode");
        TestCode logo = iTestCodeService.getOne(codeWrapper);
        typeDto.setQrcode(logo.getCodeDesc());
        return new ResultInfo<>(typeDto);

    }
}
