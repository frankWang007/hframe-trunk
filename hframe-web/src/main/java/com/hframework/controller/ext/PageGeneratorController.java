package com.hframework.controller.ext;

import com.alibaba.fastjson.JSON;
import com.hframe.domain.model.*;
import com.hframe.service.interfaces.*;
import com.hframework.base.service.CommonDataService;
import com.hframework.base.service.DataSetLoaderService;
import com.hframework.base.service.ModelLoaderService;
import com.hframework.beans.controller.ResultCode;
import com.hframework.beans.controller.ResultData;
import com.hframework.common.ext.CollectionUtils;
import com.hframework.common.ext.Fetcher;
import com.hframework.common.ext.Mapper;
import com.hframework.common.util.FileUtils;
import com.hframework.common.util.StringUtils;
import com.hframework.common.util.message.XmlUtils;
import com.hframework.controller.DefaultController;
import com.hframework.ext.datasoucce.DataSourceContextHolder;
import com.hframework.web.bean.WebContext;
import com.hframework.web.bean.WebContextHelper;
import com.hframework.web.config.bean.Module;
import com.hframework.web.config.bean.dataset.Entity;
import com.hframework.web.config.bean.module.Component;
import com.hframework.web.config.bean.module.Page;
import org.apache.commons.collections.map.HashedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Created by zhangquanhong on 2016/12/11.
 */
@Controller
@RequestMapping(value = "/extend")
public class PageGeneratorController extends ExtBaseController {
    private static final Logger logger = LoggerFactory.getLogger(PageGeneratorController.class);
    @Resource
    private DataSetLoaderService dataSetLoaderService;

    @Resource
    private ModelLoaderService modelLoaderService;
    @Resource
    private CommonDataService commonDataService;

    @Resource
    private IHfpmProgramSV hfpmProgramSV;

    @Resource
    private IHfsecMenuSV hfsecMenuSV;

    @Resource
    private IHfsecUserSV hfsecUserSV;


    @Resource
    private IHfpmModuleSV hfpmModuleSV;

    @Resource
    private IHfcfgDbConnectSV iHfcfgDbConnectSV;
    @Resource
    private IHfpmProgramCfgSV iHfpmProgramCfgSV;

    @Resource
    private IHfusEntityStoreSV hfusEntityStoreSV;

    @Resource
    private IHfsecMenuSV iHfsecMenuSV;

    @Resource
    private IHfmdEntitySV iHfmdEntitySV;

    @Resource
    private IHfmdEntityAttrSV hfmdEntityAttrSV;

    /**
     * 页面生成
     * @return
     */
    @RequestMapping(value = "/page_generate.json")
    @ResponseBody
    public ResultData pageGenerate(@RequestParam(value="dataIds[]",required=false) List<String> entityCodes, String moduleCode, HttpServletRequest request){
        logger.debug("request : {}", entityCodes, moduleCode);
        try{

            Map<String, String>  pageContextParams = DefaultController.getPageContextParams(request);
            WebContext.putContext(DefaultController.getPageContextRealyParams(pageContextParams));
            Map<String, String> pageFlowParams = WebContext.get(HashMap.class.getName());

            String companyCode = "hframe";
            Long programId = null;
            String programCode = "hframe";
            String programName = "框架";
            Long moduleId = null;
            String moduleName = "框架";
            if(pageFlowParams != null) {
                if(pageFlowParams.containsKey("hfpmProgramId") && StringUtils.isNotBlank(pageFlowParams.get("hfpmProgramId"))) {
                    HfpmProgram program = hfpmProgramSV.getHfpmProgramByPK(Long.parseLong(pageFlowParams.get("hfpmProgramId")));
                    programId = program.getHfpmProgramId();
                    programCode = program.getHfpmProgramCode();
                    programName = program.getHfpmProgramName();
                }
//                if(pageFlowParams.containsKey("hfpmModuleId") && StringUtils.isNotBlank(pageFlowParams.get("hfpmModuleId"))) {
//                    HfpmModule module = hfpmModuleSV.getHfpmModuleByPK(Long.parseLong(pageFlowParams.get("hfpmModuleId")));
//                    moduleCode = module.getHfpmModuleCode();
//                    moduleName = module.getHfpmModuleName();
//                }
            }

            HfmdEntity_Example example = new HfmdEntity_Example();
            HfmdEntity_Example.Criteria criteria = example.createCriteria();
            if(programId != null) criteria.andHfpmProgramIdEqualTo(programId);
            if(moduleId != null) criteria.andHfpmModuleIdEqualTo(moduleId);


            Map<String, Boolean> selfDependEntityCache =  new HashedMap();

            if(programId == null && moduleId == null) criteria.andHfmdEntityCodeLike("hf%");
            final List<HfmdEntity> hfmdEntityListByExample = iHfmdEntitySV.getHfmdEntityListByExample(example);

            Map<String, HfmdEntity> cache = CollectionUtils.convert(hfmdEntityListByExample, new Mapper<String, HfmdEntity>() {
                public <K> K getKey(HfmdEntity hfmdEntity) {
                    return (K) hfmdEntity.getHfmdEntityCode();
                }
            });

            Module module = new Module();
            module.setCode(moduleCode);
            List<Page> pageList = new ArrayList<Page>();
            module.setPageList(pageList);

            List<Page> menuList = new ArrayList<Page>();
            for (String entityCode : entityCodes) {
                if(StringUtils.isBlank(entityCode)) {
                    continue;
                }
                String[] lineEntityCodes = entityCode.substring(0,entityCode.length()-1).split("\\|");
                String rootEntityCode = lineEntityCodes[0];
                String rootEntityName = cache.get(rootEntityCode).getHfmdEntityName();

                String[] relEntityCodes = Arrays.copyOfRange(lineEntityCodes, 1, lineEntityCodes.length);

                boolean isSelfDepend = isSelfDependEntity(selfDependEntityCache, cache.get(rootEntityCode));



                List<Page> pages = getPages(moduleCode, rootEntityCode, rootEntityName, relEntityCodes, isSelfDepend);
                menuList.add(pages.get(0));
                pageList.addAll(pages);
            }

            WebContextHelper contextHelper = new WebContextHelper(companyCode, programCode,null, "default");
            String pageFilePath = contextHelper.programConfigRootDir + "/" + contextHelper.programConfigModuleDir + "/" + moduleCode + ".xml";

            String xml = XmlUtils.writeValueAsString(module);
//            String pageFilePath =  PropertyConfigurerUtils.getProperty(DataSetLoaderService.CreatorConst.PROJECT_BASE_FILE_PATH) +
//                    "/hframe-webtemplate/src/main/resources/program/hframe/module/" + moduleCode + ".xml";
            System.out.println(pageFilePath);
            System.out.println(xml);
            FileUtils.writeFile(pageFilePath, xml);

            //已经由具体的dataset变化时候动态加载到对应的xml文件中
            dataSetLoaderService.load(companyCode,programCode,moduleCode);

            startDynamicDataSource(pageFlowParams);
            List<HfsecMenu> hfsecMenuList = iHfsecMenuSV.getHfsecMenuListByExample(new HfsecMenu_Example());
            Iterator<Page> iterator = menuList.iterator();
            while(iterator.hasNext()) {
                Page page = iterator.next();
                for (HfsecMenu hfsecMenu : hfsecMenuList) {
                    if(page.getId().equals(hfsecMenu.getHfsecMenuCode())) {
                        iterator.remove();
                        break;
                    }
                }
            }

            for (Page page : menuList) {
                HfsecMenu hfsecMenu = new HfsecMenu();
                hfsecMenu.setHfsecMenuCode(page.getId());
                hfsecMenu.setHfsecMenuName(page.getName());
                hfsecMenu.setHfsecMenuDesc(page.getName());
                hfsecMenu.setMenuLevel(1);
                hfsecMenu.setParentHfsecMenuId(-2L);
                hfsecMenu.setIcon("");
                hfsecMenu.setUrl("/" + moduleCode + "/" + page.getId() + ".html");
                iHfsecMenuSV.create(hfsecMenu);
            }
            return ResultData.success();
        }catch (Exception e) {
            logger.error("error : ", e);

            return ResultData.error(ResultCode.ERROR);
        }finally {
            DataSourceContextHolder.clear();
        }
    }

    private boolean isSelfDependEntity(Map<String, Boolean> selfDependEntityCache, HfmdEntity hfmdEntity) throws Exception {
        if(selfDependEntityCache.containsKey(hfmdEntity.getHfmdEntityCode())) {
            return selfDependEntityCache.get(hfmdEntity.getHfmdEntityCode());
        }

        HfmdEntityAttr_Example hfmdEntityAttrExample = new HfmdEntityAttr_Example();
        hfmdEntityAttrExample.createCriteria().andHfmdEntityIdEqualTo(hfmdEntity.getHfmdEntityId());
        List<HfmdEntityAttr> hfmdEntityAttrList = hfmdEntityAttrSV.getHfmdEntityAttrListByExample(hfmdEntityAttrExample);
        List<Long> hfmdEntityAttrIds = CollectionUtils.fetch(hfmdEntityAttrList, new Fetcher<HfmdEntityAttr, Long>() {
            public Long fetch(HfmdEntityAttr hfmdEntityAttr) {
                return hfmdEntityAttr.getHfmdEntityAttrId();
            }
        });

        List<Long> relHfmdEntityAttrIds = CollectionUtils.fetch(hfmdEntityAttrList, new Fetcher<HfmdEntityAttr, Long>() {
            public Long fetch(HfmdEntityAttr hfmdEntityAttr) {
                return hfmdEntityAttr.getRelHfmdEntityAttrId();
            }
        });
        hfmdEntityAttrIds.retainAll(relHfmdEntityAttrIds);
        if(hfmdEntityAttrIds.size() > 0) {
            selfDependEntityCache.put(hfmdEntity.getHfmdEntityCode(), true);
        }else {
            selfDependEntityCache.put(hfmdEntity.getHfmdEntityCode(), false);
        }
        return selfDependEntityCache.get(hfmdEntity.getHfmdEntityCode());
    }

    private List<Page> getPages(String moduleCode, String rootEntityCode, String rootEntityName, String[] relEntityCodes, boolean isSelfDepend) {

        List<Page> pages = new ArrayList<Page>();


        if(relEntityCodes == null || relEntityCodes.length == 0) {
            if(isSelfDepend) {
                pages.add(getPage(EntityPageSet.CATEGORY_MGR, moduleCode, rootEntityCode, rootEntityName,null, null));
            }else {
                pages.add(getPage(EntityPageSet.SINGLE_MGR, moduleCode, rootEntityCode, rootEntityName,null, null));
            }
            pages.add(getPage(EntityPageSet.SINGLE_CREATE, moduleCode, rootEntityCode, rootEntityName,null, rootEntityCode + EntityPageSet.SINGLE_MGR.id));
            pages.add(getPage(EntityPageSet.SINGLE_EDIT, moduleCode, rootEntityCode, rootEntityName,null, rootEntityCode + EntityPageSet.SINGLE_MGR.id));
            pages.add(getPage(EntityPageSet.SINGLE_DETAIL, moduleCode, rootEntityCode, rootEntityName, null, rootEntityCode + EntityPageSet.SINGLE_MGR.id));

        }else {
            if(isSelfDepend) {
                pages.add(getPage(EntityPageSet.CATEGORY_MGR, moduleCode, rootEntityCode, rootEntityName,null, null));
            }else {
                pages.add(getPage(EntityPageSet.COMPLEX_MGR, moduleCode, rootEntityCode, rootEntityName,relEntityCodes, null));
            }
            pages.add(getPage(EntityPageSet.COMPLEX_CREATE, moduleCode, rootEntityCode, rootEntityName, relEntityCodes, rootEntityCode + EntityPageSet.COMPLEX_MGR.id));
            pages.add(getPage(EntityPageSet.COMPLEX_EDIT, moduleCode, rootEntityCode, rootEntityName, relEntityCodes, rootEntityCode + EntityPageSet.COMPLEX_MGR.id));
            pages.add(getPage(EntityPageSet.COMPLEX_DETAIL, moduleCode, rootEntityCode, rootEntityName, relEntityCodes, rootEntityCode + EntityPageSet.COMPLEX_MGR.id));
        }

        return pages;
    }

    private Page getPage(EntityPageSet entityPage, String moduleCode, String rootEntityCode, String rootEntityName, String[] relEntityCodes, String relPageCode) {
        Page page = new Page();

        page.setId(rootEntityCode + entityPage.id);
        page.setName(rootEntityName + entityPage.name);
        page.setPageTemplate(entityPage.pageTemplate);
        page.setDataSet(moduleCode + "/" + rootEntityCode);
        page.setRelPage(relPageCode);
        page.setComponentList(new ArrayList<Component>());
        String[] components = entityPage.component;
        for (String componentId : components) {
            Component component = new Component();
            component.setId(componentId);
            if(componentId.equals("qForm")) {
                component.setDataSet(moduleCode + "/" + rootEntityCode + "_DS4Q");
            }else if(componentId.equals("cList") && relEntityCodes != null) {
                component.setDataSet(moduleCode + "/" + relEntityCodes[0]);
            }else if(componentId.equals("eList") && relEntityCodes != null) {
                component.setDataSet(moduleCode + "/" + relEntityCodes[0]);
            }else if(componentId.equals("qList") && relEntityCodes != null) {
                component.setDataSet(moduleCode + "/" + relEntityCodes[0]);
            }else {
                component.setDataSet(moduleCode + "/" + rootEntityCode);
            }
            page.getComponentList().add(component);
        }

        return page;
    }




    /**
     * 代码差异比对
     * @return
     */
    @RequestMapping(value = "/page_load.json")
    @ResponseBody
    public ResultData getPageLoad(HttpServletRequest request){
        logger.debug("request : {}");
        try{
            Map<String, String>  pageContextParams = DefaultController.getPageContextParams(request);
            WebContext.putContext(DefaultController.getPageContextRealyParams(pageContextParams));
            Map<String, String> pageFlowParams = WebContext.get(HashMap.class.getName());

            String companyCode = "hframe";
            Long programId = null;
            String programCode = "hframe";
            String programName = "框架";
            Long moduleId = null;
            String moduleCode = "hframe";
            String moduleName = "框架";
            if(pageFlowParams != null) {
                if(pageFlowParams.containsKey("hfpmProgramId") && StringUtils.isNotBlank(pageFlowParams.get("hfpmProgramId"))) {
                    HfpmProgram program = hfpmProgramSV.getHfpmProgramByPK(Long.parseLong(pageFlowParams.get("hfpmProgramId")));
                    programId = program.getHfpmProgramId();
                    programCode = program.getHfpmProgramCode();
                    programName = program.getHfpmProgramName();
                }
                if(pageFlowParams.containsKey("hfpmModuleId") && StringUtils.isNotBlank(pageFlowParams.get("hfpmModuleId"))) {
                    HfpmModule module = hfpmModuleSV.getHfpmModuleByPK(Long.parseLong(pageFlowParams.get("hfpmModuleId")));
                    moduleId = module.getHfpmModuleId();
                    moduleCode = module.getHfpmModuleCode();
                    moduleName = module.getHfpmModuleName();

                }
            }
            HfmdEntity_Example example = new HfmdEntity_Example();
            HfmdEntity_Example.Criteria criteria = example.createCriteria();
            if(programId != null) criteria.andHfpmProgramIdEqualTo(programId);
            if(moduleId != null) criteria.andHfpmModuleIdEqualTo(moduleId);

            if(programId == null && moduleId == null) criteria.andHfmdEntityCodeLike("hf%");


            final List<HfmdEntity> hfmdEntityListByExample = iHfmdEntitySV.getHfmdEntityListByExample(example);

            Map<Module, List<List<Entity>>> entityRelats = WebContext.get(companyCode,programCode,"default").getEntityRelats();
            final Map<Module, List<List<HfmdEntity>>> todoList = new HashMap<Module, List<List<HfmdEntity>>>();
            for (Module module : entityRelats.keySet()) {
                todoList.put(module, new ArrayList<List<HfmdEntity>>());
                List<List<HfmdEntity>> targetList = todoList.get(module);
                List<List<Entity>> originList = entityRelats.get(module);
                for (List<Entity> entities : originList) {
                    List<HfmdEntity> target = new ArrayList<HfmdEntity>();
                    for (Entity entity : entities) {
                        if(StringUtils.isNotBlank(entity.getText())) {
                            target.add(getHfmdEntity(hfmdEntityListByExample, entity));
                        }
                    }
                    targetList.add(target);
                }
            }


            System.out.println("==>" + JSON.toJSONString(entityRelats));
            return ResultData.success(new HashMap<String,Object>(){{
                put("TodoList", hfmdEntityListByExample);
                put("DownList", todoList);
            }});
        }catch (Exception e) {
            logger.error("error : ", e);
            return ResultData.error(ResultCode.ERROR);
        }
    }



    private HfmdEntity getHfmdEntity(List<HfmdEntity> hfmdEntityListByExample, Entity entity) {
        Iterator<HfmdEntity> iterator = hfmdEntityListByExample.iterator();
        while (iterator.hasNext()) {
            HfmdEntity hfmdEntity = iterator.next();
            if(entity.getText().equals(hfmdEntity.getHfmdEntityCode())) {
                iterator.remove();
                return hfmdEntity;
            }
        }
        return null;
    }
}
