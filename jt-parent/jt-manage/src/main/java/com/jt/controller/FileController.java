package com.jt.controller;

import com.jt.VO.EasyUI_Image;
import com.jt.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Controller
public class FileController {
    @Autowired
    private FileService fileService;
    /**
     * 确定url请求路径
     * 用户提交的参数
     * @return
     */
    @RequestMapping("/file")
    public String file(MultipartFile fileImage) throws IOException {
        //获取图片名称
        String fileName = fileImage.getOriginalFilename();
        //判断文件夹是否存在
        File fileDir = new File("D:/JT/images/");
        if(!fileDir.exists()){
            fileDir.mkdirs();
        }
        File file = new File("D:/JT/images/"+fileName);
        //1.实现文件上传的api
        fileImage.transferTo(file);
        return "redirect:file.jsp";
    }

    /**
     * 实现用户文件上传
     */
    @RequestMapping("/pic/upload")
    @ResponseBody
    public EasyUI_Image fileUpload(MultipartFile uploadFile){
        return fileService.fileUpload(uploadFile);
    }
}
