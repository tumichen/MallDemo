package com.jt.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class EasyUI_Image {
    private Integer error=0;//表示上传文件时是否有错
    private String url;//图片的虚拟路径
    private Integer width;
    private Integer height;
    /**
     * 多系统之间对象直接传递时必须序列化
     */
}
