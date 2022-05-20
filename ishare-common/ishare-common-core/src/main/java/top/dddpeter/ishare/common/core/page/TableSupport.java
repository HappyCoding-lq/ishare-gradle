package top.dddpeter.ishare.common.core.page;

import top.dddpeter.ishare.common.constant.Constants;
import top.dddpeter.ishare.common.utils.ServletUtils;

/**
 * 表格数据处理
 * 
 * @author ishare
 */
public class TableSupport
{
    /**
     * 封装分页对象
     */
    public static PageDomain getPageDomain()
    {
        PageDomain pageDomain = new PageDomain();
        pageDomain.setPageNum(ServletUtils.getParameterToInt(Constants.PAGE_NUM));
        pageDomain.setPageSize(ServletUtils.getParameterToInt(Constants.PAGE_SIZE));
        pageDomain.setOrderBy(ServletUtils.getParameter(Constants.ORDER_BY));
        return pageDomain;
    }

    public static PageDomain buildPageRequest()
    {
        return getPageDomain();
    }
}
