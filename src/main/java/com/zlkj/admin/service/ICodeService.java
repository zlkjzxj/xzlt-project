package com.zlkj.admin.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.zlkj.admin.entity.Code;

import java.util.List;

/**
 * @author sunny
 * @since 2018-12-25
 */
public interface ICodeService extends IService<Code> {
    List<Code> getCodeType(Code codeType);
}
