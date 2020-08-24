package com.jt.service.impl;

import com.jt.VO.EasyUI_Image;
import com.jt.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@Service
@Slf4j
@PropertySource(value = "classpath:/properties/image.properties")
public class FileServiceImpl implements FileService {
    //定义本地的磁盘路径,写在配置文件中
    @Value("${image.localDirPath}")
    private String localDirPath;
    @Value("${image.urlDirPath}")
    private String urlDirPath;
    /**
     * 1.校验文件类型
     * 2.防止恶意文件上传,将文件交给工具api校验高和宽;
     * 3.众多图片如何保存
     * 分文件存储,按照年月日
     * 4.文件如果重名怎么处理
     * 生成唯一的文件名
     *
     * @param uploadFile
     * @return
     */
    @Override
    public EasyUI_Image fileUpload(MultipartFile uploadFile) {

        EasyUI_Image image = new EasyUI_Image();
        //获取文件名
        String fileName = uploadFile.getOriginalFilename();
        //校验文件名称,正则表达式
        fileName = fileName.toLowerCase();
        if (!fileName.matches("^.+(.JPEG|.jpeg|.JPG|.jpg|.GIF|.gif|.BMP|.bmp|.PNG|.png)$")) {
            return image.setError(1);//上传的文件有误
        }
        //防止恶意文件
        try {
            BufferedImage bufferedImage = ImageIO.read(uploadFile.getInputStream());
            int height = bufferedImage.getHeight();
            int width = bufferedImage.getWidth();
            if (height == 0 || width == 0){
                image.setError(1);
                return image;
            }
            image.setHeight(height);
            image.setWidth(width);
            String dataPathDir = new SimpleDateFormat("yyyy/MM/dd").format(new Date());
            String realDirPath = localDirPath+dataPathDir;
            //判断文件夹是否存在
            File dirFile = new File(realDirPath);
            if (!dirFile.exists()){
                dirFile.mkdirs();
            }
            //采用uuid,32位的16进制数
            String uuid = UUID.randomUUID().toString().replace("-", "");
            String fileType = fileName.substring(fileName.lastIndexOf("."));
            String realName = uuid+fileType;
            //实现文件上传
            String realFilePath = realDirPath+"/"+realName;
            uploadFile.transferTo(new File(realFilePath));
            //编辑虚拟路径
            String realUrlPath = urlDirPath+dataPathDir+"/"+realName;
            image.setUrl(realUrlPath);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            image.setError(1);
            return image;
        }
        return image;
    }
}
