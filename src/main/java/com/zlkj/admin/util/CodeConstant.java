package com.zlkj.admin.util;


import com.zlkj.admin.dto.CodeDto;
import com.zlkj.admin.entity.Code;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 常量工具类
 *
 * @author sunny
 */
public class CodeConstant {
    /**
     * 封装code的map
     */
    public static Map<String, List<CodeDto>> cpCodeMap = new LinkedHashMap<>();
    public static List<Code> progressCodeList = new ArrayList<>();
    public static List<Code> userLevelCodeList = new ArrayList<>();
    public static List<Code> starRatingCodeList = new ArrayList<>();
    public static Code phone;

}