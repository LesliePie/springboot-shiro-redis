package com.springboot.common.base;

import com.google.common.base.CaseFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.util.CollectionUtils;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @program: education-parent
 * @description: 分页请求
 * @author: Leslie
 * @create: 2018-07-20 17:07
 **/
@ApiModel(value = "分页请求")
public class PageRequest implements Serializable {
    private static final long serialVersionUID = 5819502601299420051L;
    /**
     * 页数大小
     */
    @NotNull
    @ApiModelProperty(value = "页数大小")
    private Integer pageSize=10;
    /**
     * 当前页
     */
    @NotNull
    @ApiModelProperty(value = "当前页")
    private Integer nowPage=1;
    /**
     * 查询条件
     */
    @ApiModelProperty(value = "查询条件")
    private Map<String,Object> condition;
    /**
     * 排序条件
     */
    @ApiModelProperty(value = "排序条件")
    private List<String> orderCondition;
    /**
     * 升序or降序排列，
     */
    @ApiModelProperty(value = "排序方式，升序Or降序")
    private Boolean isDesc=false;

    public void setDesc(Boolean desc) {
        isDesc = desc;
    }

    public List<String> getOrderCondition() {
        if (!CollectionUtils.isEmpty(orderCondition)){
            for (String item :orderCondition){
                item=CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE,item);
            }
        }
        return orderCondition;
    }

    public void setOrderCondition(List<String> orderCondition) {
        this.orderCondition = orderCondition;
    }


    public Boolean getDesc() {
        return isDesc;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getNowPage() {
        return nowPage;
    }

    public void setNowPage(Integer nowPage) {
        this.nowPage = nowPage;
    }

    public Map<String, Object> getCondition() {
        return condition;
    }

    public void setCondition(Map<String, Object> condition) {
        this.condition = condition;
    }

    @Override
    public String toString() {
        return "PageRequest{" +
                "pageSize=" + pageSize +
                ", nowPage=" + nowPage +
                ", condition=" + condition +
                '}';
    }
}
