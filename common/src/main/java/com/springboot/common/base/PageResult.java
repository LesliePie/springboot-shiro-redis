package com.springboot.common.base;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * @program: web
 * @description: 分页返回
 * @author: Leslie
 * @create: 2018-07-16 16:18
 **/
@ApiModel(value = "分页返回")
public class PageResult<T> {
    @ApiModelProperty(value = "总条数")
    private Integer total;
    @ApiModelProperty(value = "当前页码")
    private Integer pageIndex;
    @ApiModelProperty(value = "页码条数")
    private Integer pageSize;
    @ApiModelProperty(value = "总页数")
    private Integer totalPages;
    @ApiModelProperty(value = "剩余条数")
    private Integer resultNumber;
    @ApiModelProperty(value = "返回内容")
    private List<T> list;


    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    public PageResult() {
    }

    public Integer getResultNumber() {
        return resultNumber;
    }


    public PageResult(Integer total, Integer totalPages, Integer pageIndex, Integer pageSize, List<T> list) {
        this.total = total;
        this.pageIndex = pageIndex;
        this.pageSize = pageSize;
        this.list = list;
        this.totalPages=totalPages;
        int result=this.total-(this.pageIndex*this.pageSize);
        this.resultNumber=result>0?result:0;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(Integer pageIndex) {
        this.pageIndex = pageIndex;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }
}
