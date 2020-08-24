package com.jt.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@TableName("tb_item")
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
//如果get,set方法不全,添加该属性忽略转换
@JsonIgnoreProperties(ignoreUnknown = true)
public class Item extends BasePojo{
    @TableId(type = IdType.AUTO)
    private Long id;//定义主键
    private String title;//标题
    private String sellPoint;//卖点
    private Long price;//这个精度比较好,,,为了表示小数,库内的价格都会扩大100倍;
    private Integer num;//数量
    private String barcode;//二维码
    private String image;//图片
    private Long cid;//商品的分类信息
    private Integer status;//商品的状态

    public String[] getImages(){
        return image.split(",");
    }
}
