package com.zlkj.admin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zlkj.admin.dao.CodeMapper;
import com.zlkj.admin.entity.Code;
import com.zlkj.admin.service.ICodeService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author sunny
 */
@Service
public class CodeServiceImpl extends ServiceImpl<CodeMapper, Code> implements ICodeService {

    @Resource
    private CodeMapper codeMapper;

    @Override
    public List<Code> getCodeType(Code codeType) {
        return codeMapper.getCodeType(codeType);
    }
}