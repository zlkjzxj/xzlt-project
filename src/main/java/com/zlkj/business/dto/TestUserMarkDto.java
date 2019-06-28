package com.zlkj.business.dto;

import com.zlkj.business.entity.TestUser;
import com.zlkj.business.entity.TestUserMarkResult;
import lombok.Data;
import org.aspectj.weaver.ast.Test;

import java.util.List;

/**
 * @Description TODO
 * @Author sunny
 * @Date 2019-05-27 16:08
 */
@Data
public class TestUserMarkDto extends TestUser {
    private Integer id;
    private String userName;
    private String userId;
    private String questionTypeId;
    private String questionTypeName;
    private String testResult;
    private String testJob;
    private Integer cpSign;

    private List<TestUserMarkResult> results;

}
