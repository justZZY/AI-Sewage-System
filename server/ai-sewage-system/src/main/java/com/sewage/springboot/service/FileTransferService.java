package com.sewage.springboot.service;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONObject;

/**
 * 文件上传/下载
 * <br><br>
 * 使用词接口上传文件成功会返回文件id，此时文件只是上传了，但是没有激活状态，
 * 需要在后续提交表单中调用{@link FileTransferService#updateFileStatus}方法更新文件状态
 *
 */
public interface FileTransferService {

	public JSONObject singleFileUpload(MultipartFile file);
	
	/**
	 * 多文件上传，文件过多时效率低，不建议使用
	 * @param files
	 * @return
	 */
	@Deprecated()
	public JSONObject multiFilesUpload(List<MultipartFile> files);
	
	public JSONObject queryFileInfo(Integer FileId);
	
	public ResponseEntity<FileSystemResource> getFileResponseEntity(Integer FileId) throws UnsupportedEncodingException;

	void updateFileStatus(Integer fileId);

	void updateFileStatus(List<Integer> fileList);
}
