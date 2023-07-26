package org.secwallet.generate.web;


import com.github.pagehelper.util.StringUtil;
import org.secwallet.generate.model.SysDataSource;
import org.secwallet.generate.service.SysDataSourceService;
import org.secwallet.core.controller.BaseController;
import org.secwallet.core.exception.CTException;
import org.secwallet.core.model.Result;
import org.secwallet.core.util.date.DateUtil;
import org.secwallet.core.util.string.UniqueIdUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@Api(value = "Data Source Configuration Management Service",description = "developer action",tags = "Data source configuration management interface")
@RestController
@Slf4j
@RequestMapping("/sysDataSource")
public class SysDataSourceController extends BaseController {

    @Autowired
    private SysDataSourceService sysDataSourceService;

    @ApiOperation(value = "Add data source configuration information, return success or failure！")
    @PostMapping("/add.html")
    public Result add(@RequestBody @ApiParam(name = "System menu information", value = "json", required = true) SysDataSource dataSource){
        try{
            dataSource.setId(UniqueIdUtil.getGuid());
            dataSource.setCreateTime(DateUtil.format(new Date(), DateUtil.DATE_FORMAT_TIMESTAMP));
            dataSource.setCreateUser(getUser().getUsername());
            dataSource.setCreateUserId(getUser().getId().toString());
            dataSource.setUpdateTime(DateUtil.format(new Date(), DateUtil.DATE_FORMAT_TIMESTAMP));
            if (sysDataSourceService.insert(dataSource)>0){
                return Result.ok();
            }
        }catch (CTException e){
            log.error("Failed to add data source configuration information："+e.getMessage());
        }
        return Result.fail("Failed to add data source configuration information！");
    }
    @ApiOperation(value = "Update data source configuration information, return success or failure！")
    @PostMapping("/update.html")
    public Result update(@RequestBody @ApiParam(name = "System menu information", value = "json", required = true)SysDataSource dataSource){
        try {
            if (sysDataSourceService.updateByPrimaryKey(dataSource)>0){
                return Result.ok();
            }
        }catch (CTException e){
            log.error("Failed to update data source configuration information："+e.getMessage());
        }
        return Result.fail("Failed to update data source configuration information!");
    }

    @ApiOperation(value = "Delete data source configuration information by id！")
    @GetMapping("/deleteById.html")
    public Result deleteById(@ApiParam(name = "id",value = "id",required = true) @RequestParam String id){
        try{
            if (StringUtil.isEmpty(id)){
                return Result.fail("id is null！");
            }
            if (sysDataSourceService.deleteByPrimaryKey(id)>0){
                return Result.ok();
            }
        }catch (CTException e){
            log.error("Failed to delete data source configuration information through data source configuration id："+e.getMessage());
        }
        return Result.fail("Failed to delete data source configuration information by id!");
    }

    @ApiOperation(value = "Through the data source configuration id, get the data source details")
    @GetMapping("/findById.html")
    public Result findById(@ApiParam(name = "id", value = "Configure id by data source", required = true) @RequestParam String id){
        try{
            if(StringUtil.isEmpty(id)){
                return Result.fail("Configure id by data source！");
            }
            return Result.ok(sysDataSourceService.findById(id));
        }catch (CTException e){
            log.error("Failed to get data source details through data source configuration id："+e.getMessage());
        }
        return Result.fail("Failed to get data source details through data source configuration id！");
    }
    @ApiOperation(value = "Get data source information, support paging!")
    @PostMapping("/findAllToPage.html")
    public Result findAllToPage(@RequestBody @ApiParam(name = "Data source name name, pagination parameters",value = "json",required = true) SysDataSource sysDataSource){
        return Result.ok(sysDataSourceService.findAllToPage(sysDataSource));
    }
    @ApiOperation(value = "Get data source information without paging!")
    @PostMapping("/findAllDataSource.html")
    public Result findAllDataSource(@RequestBody @ApiParam(name = "data source name",value = "json",required = true) SysDataSource sysDataSource){
        return Result.ok(sysDataSourceService.findAllDataSource(sysDataSource));
    }

}
