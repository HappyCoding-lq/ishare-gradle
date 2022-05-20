package top.dddpeter.ishare.common.exception;

import org.springframework.boot.autoconfigure.web.servlet.error.AbstractErrorController;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.dddpeter.ishare.common.dto.ResultDTO;

import javax.servlet.http.HttpServletRequest;

/**
 * @author zmr
 * @author lucas
 */
@RestController
public class GlobalErrorController extends AbstractErrorController
{
    private static final String ERROR_PATH = "/error";

    public GlobalErrorController(ErrorAttributes errorAttributes)
    {
        super(errorAttributes);
    }

    @RequestMapping(value = ERROR_PATH)
    public ResultDTO error(HttpServletRequest request)
    {
        HttpStatus status = getStatus(request);
        return ResultDTO.failure(String.valueOf(status.value()), status.getReasonPhrase());
    }

    @Override
    public String getErrorPath()
    {
        return ERROR_PATH;
    }
}