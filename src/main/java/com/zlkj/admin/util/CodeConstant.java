package com.zlkj.admin.util;


import com.zlkj.admin.dto.CodeDto;
import com.zlkj.admin.entity.Code;

import java.util.*;

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
    public static List<Code> starRatingCodeList = new ArrayList<>();
    public static Map<String, List<CodeDto>> GZQY_MAP = new HashMap<>();
    public static Map<String, List<Code>> PROGRESSCODE_MAP = new HashMap<>();
    public static Map<String, List<Code>> USER_LEVEL_MAP = new HashMap<>();
    public static Map<String, Code> PHONE_MAP = new HashMap<>();

}