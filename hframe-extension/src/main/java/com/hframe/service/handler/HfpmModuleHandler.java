package com.hframe.service.handler;

import com.hframe.domain.model.HfpmModule;
import com.hframe.domain.model.HfpmProgram;
import com.hframe.service.interfaces.IHfpmModuleSV;
import com.hframe.service.interfaces.IHfpmProgramSV;
import com.hframework.base.bean.AbstractBusinessHandler;
import com.hframework.common.annotation.extension.AfterCreateHandler;
import com.hframework.common.annotation.extension.AfterDeleteHandler;
import com.hframework.common.util.FileUtils;
import com.hframework.common.util.message.XmlUtils;
import com.hframework.generator.util.CreatorUtil;
import com.hframework.web.config.bean.Program;
import com.hframework.web.config.bean.program.Module;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by zhangquanhong on 2016/10/14.
 */
@Service
public class HfpmModuleHandler extends AbstractBusinessHandler<HfpmModule> {
    @Resource
    private IHfpmProgramSV hfpmProgramSV;

    @Resource
    private IHfpmModuleSV hfpmModuleSV;


    @AfterCreateHandler
    public boolean createModule(HfpmModule module) throws Exception {
        String companyCode = "hframe";
        HfpmProgram hfpmProgram = hfpmProgramSV.getHfpmProgramByPK(module.getHfpmProgramId());
        String programCode = hfpmProgram.getHfpmProgramCode();
        String projectBasePath = CreatorUtil.getTargetProjectBasePath(companyCode,
                "hframe".equals(programCode) ? "trunk" : programCode, null);


        Program program = XmlUtils.readValueFromAbsoluteFilePath(projectBasePath + "/hframe-web/src/main/resources/program/program.xml", Program.class);

        List<Module> moduleList = program.getModules().getModuleList();
        if(moduleList == null) {
            moduleList = new ArrayList<Module>();
            program.getModules().setModuleList(moduleList);
        }

        for (Module module1 : moduleList) {
            if(module1.getCode().equals(module.getHfpmModuleCode())) {
                return true;
            }
        }

        com.hframework.web.config.bean.program.Module module1 = new com.hframework.web.config.bean.program.Module();
        module1.setCode(module.getHfpmModuleCode());
        module1.setName(module.getHfpmModuleName());
        moduleList.add(module1);

        String xml = XmlUtils.writeValueAsString(program);
        FileUtils.writeFile(projectBasePath + "/hframe-web/src/main/resources/program/program.xml", xml);

        return true;
    }


    @AfterDeleteHandler
    public boolean deleteModule(HfpmModule module) throws Exception {
        String companyCode = "hframe";
        HfpmProgram hfpmProgram = hfpmProgramSV.getHfpmProgramByPK(module.getHfpmProgramId());
        String programCode = hfpmProgram.getHfpmProgramCode();
        String projectBasePath = CreatorUtil.getTargetProjectBasePath(companyCode,
                "hframe".equals(programCode) ? "trunk" : programCode, null);


        Program program = XmlUtils.readValueFromAbsoluteFilePath(projectBasePath + "/hframe-web/src/main/resources/program/program.xml", Program.class);

        List<Module> moduleList = program.getModules().getModuleList();
        if(moduleList == null) {
            moduleList = new ArrayList<Module>();
            program.getModules().setModuleList(moduleList);
        }
        Iterator<Module> iterator = moduleList.iterator();
        while (iterator.hasNext()) {
            Module module1 = iterator.next();
            if(module1.getCode().equals(module.getHfpmModuleCode())) {
                iterator.remove();
            }
        }

        String xml = XmlUtils.writeValueAsString(program);
        FileUtils.writeFile(projectBasePath + "/hframe-web/src/main/resources/program/program.xml", xml);

        return true;
    }
}
