package com.jt.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data  //安装lombok插件
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class EasyUI_Tree {
    private Long id;	//分类Id号
    private String text;//分类名称
    private String state; //open 节点打开  closed 节点关闭

}
