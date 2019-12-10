package com.sewage.springboot.controller;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONObject;
import com.sewage.springboot.service.FileTransferService;

/**
 * 文件传输公共接口
 * @author sc
 *
 */
@RestController
@RequestMapping("/file")
@CrossOrigin
public class FileTransferController {
	
	@Autowired FileTransferService fileService;
	
	/** 单文件上传 */
    @RequestMapping(value="singleupload",method=RequestMethod.POST)
    public JSONObject fileUpload(@RequestParam MultipartFile file){
		return fileService.singleFileUpload(file);
    }
    
    /** 实现多文件上传
     * <br><br>
     * 注：一个事务
     */
    @RequestMapping(value="multifileUpload",method=RequestMethod.POST) 
    public JSONObject multifileUpload(@RequestParam List<MultipartFile> files){
        return fileService.multiFilesUpload(files);
    }
    
    @RequestMapping("/download/{fileId}")
    public ResponseEntity<FileSystemResource> downLoad(@PathVariable("fileId") Integer fileId) throws UnsupportedEncodingException {
    	return fileService.getFileResponseEntity(fileId);    
    }
}
