package com.zlkj.business.dto;

import com.zlkj.business.entity.TestQuestionSelect;
import lombok.Data;

import java.util.List;

/**
 * @Description TODO
 * @Author sunny
 * @Date 2019-05-27 16:08
 */
@Data
public class TestQuestionDto {
    private Integer id;
    private String testTypeId;
    private String questionName;
    private String questionAnswer;
    private String questionMark;

    private List<TestQuestionSelect> selectList;
}
