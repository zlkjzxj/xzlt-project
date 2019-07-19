package com.zlkj.business.dto;

import com.zlkj.business.entity.TestCode;
import com.zlkj.business.entity.TestUser;
import lombok.Data;

import java.util.List;

/**
 * @Description TODO
 * @Author sunny
 * @Date 2019-05-27 16:08
 */
@Data
public class TestQuestionTypeDto {
    private Integer id;
    private String testTypeName;
    private String testTypeHeader;
    private String testTypeCode;
    private String testQuestionType;
    private String testQuestionTypeName;
    private String testQuestionResult;
    private String backImg;
    private String resultDesc;
    private Integer cpSign;
    private Integer openSign;
    private Integer ymlx;

    private List<TestCode> result;
    private List<TestCode> job;
    private List<TestQuestionDto> question;
    private TestUserMarkDto testUserMarkDto;
    private TestUser user;
    private String qrcode;

}
