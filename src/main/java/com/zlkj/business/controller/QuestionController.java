package com.zlkj.business.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @Description TODO
 * @Author sunny
 * @Date 2019-05-13 16:05
 */
@Controller
@RequestMapping("/question")
public class QuestionController {
    @Resource
    private ICodeService iCodeService;
    @Resource
    private ITestCodeService iTestCodeService;
    @Resource
    private ITestQuestionTypeService iTestQuestionTypeService;

    @Resource
    private ITestUserService iTestUserService;
    @Resource
    private ITestUserMarkService iTestUserMarkService;
    @Resource
    private ITestUserMarkResultService iTestUserMarkResultService;


    @RequestMapping("/*")
    public void toHtml() {

    }

    /**
     * 根据公司id查出公司和公司的所有项目
     *
     * @param testUser
     * @return
     */
    @RequestMapping("/getTestUserList")
    @ResponseBody
    public ResultInfo<List<TestUser>> getTestUserList(TestUser testUser, Integer page, Integer limit) {
        //测试类型
        QueryWrapper<TestUser> wrapper = new QueryWrapper<>();
//        wrapper.ne("test_question_type", "");
        IPage<TestUser> pageObj = iTestUserService.page(new Page<>(page, limit), wrapper);
        return new ResultInfo<>(pageObj.getRecords(), pageObj.getTotal());
    }

    /**
     * @param userId
     * @return
     */
    @RequestMapping("/getTestList")
    @ResponseBody
    public ResultInfo<List<TestUserMarkDto>> getTestInfo(Integer userId) {
        QueryWrapper<TestUserMark> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId);
//        List<TestUserMarkDto> list = iTestUserMarkService.getUserMarkList(userId);
        List<TestUserMark> list = iTestUserMarkService.list(wrapper);
        List<TestUserMarkDto> markDtoList = new ArrayList<>();
        for (TestUserMark mark : list) {
            TestUserMarkDto dto = new TestUserMarkDto();
            TestUser user = iTestUserService.getById(mark.getUserId());
            dto.setUserName(user.getUserName());
            dto.setUserId(mark.getUserId());
            dto.setQuestionTypeId(mark.getQuestionTypeId());
            dto.setTestJob(mark.getTestJob());
//            QueryWrapper<TestUserMarkResult> resultQueryWrapper = new QueryWrapper<>();
//            resultQueryWrapper.eq("user_id", mark.getId());
//            resultQueryWrapper.eq("question_type_id", mark.getQuestionTypeId());
//            List<TestUserMarkResult> results = iTestUserMarkResultService.list(resultQueryWrapper);
            List<TestQuestionType> typeList = iTestQuestionTypeService.list();
            for (TestQuestionType type : typeList) {
                if (mark.getQuestionTypeId().equals(type.getId() + "")) {
                    dto.setQuestionTypeName(type.getTestTypeName());
                    break;
                }
            }
//            dto.setResults(results);
            markDtoList.add(dto);
        }
        return new ResultInfo<>(markDtoList);
    }

    /**
     * @return
     */
    @RequestMapping("/getResultDetail")
    @ResponseBody
    public ResultInfo<Map<String, Object>> getResultDetail(Integer userId, Integer questionTypeId) {
        TestUser user = iTestUserService.getById(userId);
        //测试类型
        QueryWrapper<TestQuestionType> typeQueryWrapper = new QueryWrapper<>();
        typeQueryWrapper.ne("test_question_type", "");
        TestQuestionType type = iTestQuestionTypeService.getById(questionTypeId);
        TestQuestionTypeDto dto = new TestQuestionTypeDto();
        dto.setUser(user);
        dto.setId(type.getId());
        dto.setTestTypeName(type.getTestTypeName());
        dto.setTestTypeCode(type.getTestTypeCode());
        dto.setTestQuestionType(type.getTestQuestionType());
        dto.setTestQuestionTypeName(type.getTestQuestionTypeName());
        dto.setResultDesc(type.getResultDesc());
        //测试结果
        QueryWrapper<TestCode> resultWrapper = new QueryWrapper<>();
        resultWrapper.eq("code", type.getTestQuestionType());
        List<TestCode> resultList = iTestCodeService.list(resultWrapper);
        //结果对应的职业类型
        QueryWrapper<TestCode> jobWrapper = new QueryWrapper<>();
        jobWrapper.eq("code", type.getTestQuestionResult());
        List<TestCode> jobList = iTestCodeService.list(jobWrapper);

        TestUserMarkDto markDto = new TestUserMarkDto();
        markDto.setUserName(user.getUserName());
        QueryWrapper<TestUserMarkResult> resultQueryWrapper = new QueryWrapper<>();
        resultQueryWrapper.eq("user_id", userId);
        resultQueryWrapper.eq("question_type_id", questionTypeId);
        List<TestUserMarkResult> results = iTestUserMarkResultService.list(resultQueryWrapper);
        markDto.setResults(results);
        dto.setResult(resultList);
        dto.setJob(jobList);
        dto.setTestUserMarkDto(markDto);
        //性别
        QueryWrapper<Code> wrapper = new QueryWrapper<>();
        wrapper.eq("code", "gender");
        List<Code> genderList = iCodeService.list(wrapper);
        //学历
        QueryWrapper<Code> wrapper1 = new QueryWrapper<>();
        wrapper1.eq("code", "qualification");
        List<Code> qualificationList = iCodeService.list(wrapper1);
        Map<String, Object> map = new HashMap<>();
        map.put("dto", dto);
        map.put("genders", genderList);
        map.put("qualifications", qualificationList);
        return new ResultInfo<>(map);
    }

}
