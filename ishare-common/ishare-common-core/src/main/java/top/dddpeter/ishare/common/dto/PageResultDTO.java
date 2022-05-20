package top.dddpeter.ishare.common.dto;

import io.swagger.annotations.ApiModelProperty;
import top.dddpeter.ishare.common.core.page.PageDomain;
import top.dddpeter.ishare.common.enums.ResponseCodeEnum;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName: PageResultDTO
 * @Description: 分页通用响应结果DTO
 * @Author: 徐
 * @Date: 2020/1/2 3:50 下午
 * @Version: 1.0
 */
public class PageResultDTO implements Serializable {

    private static final long serialVersionUID = 5065009887439497089L;


    @ApiModelProperty("响应是否成功")
    private boolean success;

    @ApiModelProperty("响应消息")
    private String message;

    @ApiModelProperty("响应编码")
    private String code;

    @ApiModelProperty("返回页数据")
    private List rows;

    @ApiModelProperty("总条数")
    private Integer total;

    @ApiModelProperty("当前页")
    private Integer pageNum;

    @ApiModelProperty("每页显示数量")
    private Integer pageSize;

    @ApiModelProperty("总页数")
    private Integer totalPage;


    public PageResultDTO() {
    }


    public static PageResultDTO success(List rows, Integer total, PageDomain pageDomain) {
        return new PageResultDTO(true, ResponseCodeEnum.SUCCESS.getCode(), ResponseCodeEnum.SUCCESS.getMessage(),
                rows, total, pageDomain.getPageNum(), pageDomain.getPageSize());
    }

    public static PageResultDTO success(List rows, Integer total, Integer pageNum, Integer pageSize) {
        return new PageResultDTO(true, ResponseCodeEnum.SUCCESS.getCode(), ResponseCodeEnum.SUCCESS.getMessage(),
                rows, total, pageNum, pageSize);
    }

    public static PageResultDTO success(String code, String massage, List rows, Integer total, PageDomain pageDomain) {
        return new PageResultDTO(true, code, massage, rows, total, pageDomain.getPageNum(), pageDomain.getPageSize());
    }

    public static PageResultDTO success(String code, String massage, List rows, Integer total, Integer pageNum, Integer pageSize) {
        return new PageResultDTO(true, code, massage, rows, total, pageNum, pageSize);
    }


    public static PageResultDTO failure() {
        return new PageResultDTO(false, ResponseCodeEnum.ERROR.getCode(), ResponseCodeEnum.ERROR.getMessage());
    }

    public static PageResultDTO failure(String message) {
        return new PageResultDTO(false, ResponseCodeEnum.ERROR.getCode(), message);
    }

    public static PageResultDTO failure(String code, String message) {
        return new PageResultDTO(false, code, message);
    }


    public PageResultDTO(boolean success, String code, String message) {
        this.success = success;
        this.message = message;
        this.code = code;
    }

    public PageResultDTO(boolean success, String code, String message, List rows, Integer total, Integer pageNum, Integer pageSize) {
        this.success = success;
        this.code = code;
        this.message = message;
        this.rows = rows;
        this.total = total;
        this.pageNum = pageNum;
        this.pageSize = pageSize;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List getRows() {
        return rows;
    }

    public void setRows(List rows) {
        this.rows = rows;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getTotalPage() {
        if (this.total != null && this.pageSize != null) {
            totalPage = total % pageSize != 0 ? total / pageSize + 1 : total / pageSize;
        }
        return totalPage;
    }

    public void setTotalPage(Integer totalPage) {
        this.totalPage = totalPage;
    }

}