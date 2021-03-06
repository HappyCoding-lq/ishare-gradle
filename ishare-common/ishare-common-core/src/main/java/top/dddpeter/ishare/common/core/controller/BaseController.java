package top.dddpeter.ishare.common.core.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import top.dddpeter.ishare.common.constant.Constants;
import top.dddpeter.ishare.common.core.page.PageDomain;
import top.dddpeter.ishare.common.core.page.TableDataInfo;
import top.dddpeter.ishare.common.core.page.TableSupport;
import top.dddpeter.ishare.common.dto.ResultDTO;
import top.dddpeter.ishare.common.utils.DateUtils;
import top.dddpeter.ishare.common.utils.ServletUtils;
import top.dddpeter.ishare.common.utils.sql.SqlUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.beans.PropertyEditorSupport;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * web层通用数据处理
 * 
 * @author ishare
 */
public class BaseController
{
    protected final Logger logger = LoggerFactory.getLogger(BaseController.class);

    /**
     * 将前台传递过来的日期格式的字符串，自动转化为Date类型
     */
    @InitBinder
    public void initBinder(WebDataBinder binder)
    {
        // Date 类型转换
        binder.registerCustomEditor(Date.class, new PropertyEditorSupport()
        {
            @Override
            public void setAsText(String text)
            {
                setValue(DateUtils.parseDate(text));
            }
        });
    }

    /**
     * 设置请求分页数据
     */
    protected void startPage()
    {
        PageDomain pageDomain = TableSupport.buildPageRequest();
        startPage(pageDomain);
    }

    /**
     * 设置请求分页数据(新)
     */
    protected void startPage(PageDomain pageDomain)
    {
        Integer pageNum = pageDomain.getPageNum();
        Integer pageSize = pageDomain.getPageSize();
        if (null != pageNum && null != pageSize)
        {
            String orderBy = SqlUtil.escapeOrderBySql(pageDomain.getOrderBy());
            PageHelper.startPage(pageNum, pageSize, orderBy);
        }
    }

    /**
     * 获取request
     */
    public HttpServletRequest getRequest()
    {
        return ServletUtils.getRequest();
    }

    /**
     * 获取response
     */
    public HttpServletResponse getResponse()
    {
        return ServletUtils.getResponse();
    }

    /**
     * 获取session
     */
    public HttpSession getSession()
    {
        return getRequest().getSession();
    }

    public long getCurrentUserId()
    {
        String currentId = getRequest().getHeader(Constants.CURRENT_ID);
        if (StringUtils.isNotBlank(currentId))
        {
            return Long.valueOf(currentId);
        }
        return 0L;
    }

    public String getLoginName()
    {
        return getRequest().getHeader(Constants.CURRENT_USERNAME);
    }

    /**
     * 响应请求分页数据
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    protected TableDataInfo getDataTable(List<?> list)
    {
        TableDataInfo rspData = new TableDataInfo();
        rspData.setCode(0);
        rspData.setRows(list);
        rspData.setTotal(new PageInfo(list).getTotal());
        return rspData;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    protected ResultDTO result(List<?> list)
    {
        PageInfo<?> pageInfo = new PageInfo(list);
        Map<String, Object> m = new HashMap<>();
        m.put("rows", list);
        m.put("pageNum", pageInfo.getPageNum());
        m.put("total", pageInfo.getTotal());
        return ResultDTO.success(m);
    }

    /**
     * 响应返回结果
     * 
     * @param rows 影响行数
     * @return 操作结果
     */
    protected ResultDTO toResultDTO(int rows)
    {
        return rows > 0 ? ResultDTO.success() : ResultDTO.failure();
    }

    /**
     * 响应返回结果
     * 
     * @param result 结果
     * @return 操作结果
     */
    protected ResultDTO toResultDTO(boolean result)
    {
        return result ? ResultDTO.success() : ResultDTO.failure();
    }
}
