package com.sewage.springboot.service.impl;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sewage.springboot.handle.exception.response.BussinessException;
import com.sewage.springboot.handle.exception.response.FileUploadException;
import com.sewage.springboot.mapper.impl.FileMapper;
import com.sewage.springboot.service.FileTransferService;
import com.sewage.springboot.util.CommonUtil;
import com.sewage.springboot.util.UserInfoUtils;

import tk.mybatis.mapper.entity.Example;


@Service
@Transactional
public class FileTransferServiceImpl implements FileTransferService{
	
	@Value("${upload-root-path}") 
	private String uploadRootPath;
	@Autowired 
	FileMapper fileMapper;
	static final Random random =new Random();
	static final String SEP = java.io.File.separator;
	
	@PostConstruct
	private void initUploadDir() {
		if(uploadRootPath==null || uploadRootPath.trim().isEmpty()) {
			ApplicationHome h = new ApplicationHome(getClass());
			File jarF = h.getSource();
			uploadRootPath = jarF.getParentFile().toString() + SEP + "file";
		}
		System.out.println(uploadRootPath);
	}
	
	@Override
	public JSONObject singleFileUpload(@RequestParam org.springframework.web.multipart.MultipartFile file) {
		if(file==null || file.isEmpty()) throw new FileUploadException("文件为空！");
        String originalFilename = file.getOriginalFilename();
	    String suffix = originalFilename.lastIndexOf(".")>0?originalFilename.substring(originalFilename.lastIndexOf(".")):"";
	    String saveFileName =  UUID.randomUUID().toString().replaceAll("-","");
	    // 文件存储的相对路径
	    String relativeSavePath =  new SimpleDateFormat("yyyy.MM").format(new Date()) + SEP + saveFileName + suffix;
	    File saveFile = new File(uploadRootPath + SEP + relativeSavePath);
	    if(!saveFile.getParentFile().exists()) {
	    	saveFile.getParentFile().mkdirs();
	    }
	    // 1.保存文件
	    try {
			file.transferTo(saveFile);
		} catch (IllegalStateException | IOException e) {
			throw new FileUploadException(0,"文件上传失败[服务器存储失败]！");
		}
	    
	    // 2.记录到数据库
	    com.sewage.springboot.entity.po.File fileEntity = new com.sewage.springboot.entity.po.File(
	    		null, originalFilename, relativeSavePath, "0", UserInfoUtils.getUserInfo().getUsername(), new Date(), new Date());
	    int i = fileMapper.insertSelective(fileEntity);
	    if(i<=0) throw new FileUploadException(0, "文件上传失败[记录失败]");
		return CommonUtil.jsonResult(1, "上传成功", fileEntity.getId());  
	}

	@Override
	public JSONObject multiFilesUpload(List<MultipartFile> files) {
		JSONArray ja = new JSONArray();
		files.forEach(file-> ja.add(singleFileUpload(file).get("data")));
		return CommonUtil.jsonResult(1, "上传成功",ja);  
	}

	@Override
	public JSONObject queryFileInfo(Integer FileId) {
		com.sewage.springboot.entity.po.File model = new com.sewage.springboot.entity.po.File();
		model.setId(FileId);
		model.setStatus("1");
		com.sewage.springboot.entity.po.File fileEntity = fileMapper.selectOne(model);
		if(fileEntity==null) throw new BussinessException(0,"文件不存在！");
		fileEntity.setFilepath(null); // 隐藏文件路径
		return CommonUtil.jsonResult(1, "ok", fileEntity);
	}

	@Override
	public ResponseEntity<FileSystemResource> getFileResponseEntity(Integer FileId) throws UnsupportedEncodingException {
		com.sewage.springboot.entity.po.File model = new com.sewage.springboot.entity.po.File();
		model.setId(FileId);
//		model.setStatus("1"); // 客户端拍照上传回显，还没更新status
		com.sewage.springboot.entity.po.File fileEntity = fileMapper.selectOne(model);
		String relativePath = fileEntity.getFilepath();
		File file = new File(uploadRootPath + SEP + relativePath);
		HttpHeaders headers = new HttpHeaders(); 
		headers.add("Content-Disposition", "attachment;filename=" +   java.net.URLEncoder.encode(fileEntity.getOriginalFilename(),"UTF-8"));
		return ResponseEntity 
				.ok() 
				.headers(headers) 
				.contentLength(file.length())
				.body(new FileSystemResource(file));
	}
	
	@Override
	public void updateFileStatus(Integer fileId) throws BussinessException {
		if(fileId==null) return ;
		com.sewage.springboot.entity.po.File entity = new com.sewage.springboot.entity.po.File();
		entity.setId(fileId);
		entity.setStatus("1");
		entity.setUpdateTime(new Date());
		int i = fileMapper.updateByPrimaryKeySelective(entity);
		if(i<=0) throw new BussinessException(0, "文件状态修改失败",entity);
	}

	@Override
	public void updateFileStatus(List<Integer> fileList) throws BussinessException{
		if(fileList==null || fileList.isEmpty()) return ;
		com.sewage.springboot.entity.po.File entity = new com.sewage.springboot.entity.po.File();
		entity.setStatus("1");
		entity.setUpdateTime(new Date());
		Example ex = new Example(com.sewage.springboot.entity.po.File.class);
		ex.and().andIn("id", fileList);
		int i = fileMapper.updateByExampleSelective(entity, ex);
		if(i!=fileList.size()) throw new BussinessException(0, "文件状态修改失败",entity);
	}
	

}
