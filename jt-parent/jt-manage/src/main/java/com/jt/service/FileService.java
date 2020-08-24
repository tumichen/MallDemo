package com.jt.service;

import com.jt.VO.EasyUI_Image;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileService {
    EasyUI_Image fileUpload(MultipartFile uploadFile);
}
