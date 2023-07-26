package org.secwallet.generate.web;

import org.secwallet.generate.service.GenerateService;
import org.secwallet.core.model.Result;
import org.secwallet.core.util.file.FileUtil;
import org.secwallet.generate.vo.ConnParam;
import org.secwallet.generate.vo.FileVo;
import org.secwallet.generate.vo.GenerateModel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;



/**
 * code generation controller
 */
@RestController
@RequestMapping("/generate")
@Api(value = "codeGenerationController",description = "developer action",tags = "Code Generation Control Service Operation Interface")
public class GenerateController {

//    @Value("${cthl.file.outPutFolderPath}")
    private String outPutFolderPath;

	@Autowired
	private GenerateService generateService;
	
	@PostMapping("/testConnection.html")
	@ApiOperation(value = "Data source connection test")
	public Result testConnection(@RequestBody ConnParam connParam) {
		boolean success = generateService.testConnection(connParam);
		if(success) {
			return Result.ok(generateService.testConnection(connParam));
		}
		return Result.fail("Connection failed, please check the database and connection。");
	}

	@ApiOperation(value = "Reverse lookup data table, return Table list collection！")
	@PostMapping("/getTables.html")
	public Result getTables(@RequestBody @ApiParam(name = "Data source link information",value = "json",required = true) ConnParam connParam) {
		return Result.ok(generateService.getTables(connParam));
	}

	@ApiOperation(value = "Get the code generation data model, return GenerateModel!")
	@PostMapping("/getGenerateModel.html")
	public Result getGenerateModel(@RequestBody@ApiParam(name = "Data source link information",value = "json",required = true) GenerateModel generateModel) {
        generateModel.setOutPutFolderPath(outPutFolderPath);
		return Result.ok(generateService.getGenerateModel(generateModel));
	}

	@ApiOperation(value = "generate")
	@PostMapping("/generateModels.html")
	public Result generateModels(@RequestBody @ApiParam(name = "Data source link information",value = "json",required = true) GenerateModel generateModel) throws Exception {
		String res_value=generateService.generateModels(generateModel);
	    if (res_value.startsWith("fail")){
            return Result.fail("Failed to generate code file, please check the error log for details!");
		}
	    return Result.ok(res_value);
	}
    @ApiOperation("Download the generated code file compressed package, zip format")
    @PostMapping("/downCodeFile.html")
    public void downTaxFile(@RequestBody @ApiParam(name = "file info",value = "json",required = false) FileVo fileVo, HttpServletResponse response, HttpServletRequest request) {
        try {
            FileUtil.downLoadFile(request, response, fileVo.getFilePath(), fileVo.getZipFileName()+".zip");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
