package top.dddpeter.ishare.gateway.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.dddpeter.ishare.common.dto.ResultDTO;
import top.dddpeter.ishare.common.utils.StringUtils;

import java.lang.reflect.Method;
import java.util.Map;

@RestController
@RequestMapping("/common")
@Slf4j
public class CommonController {

    @GetMapping("/enum/list")
    public ResultDTO getEnumList(@RequestParam("class") String className, @RequestParam(value = "method", required = false) String methodName) {
        try {
            Class clazz = Class.forName(className);
            if(!clazz.isEnum()){
                return ResultDTO.failure("参数异常");
            }
            Method method = clazz.getDeclaredMethod(StringUtils.isEmpty(methodName)?"getCodeValueMap":methodName);
            if(method == null){
                return ResultDTO.failure("参数异常");
            }
            Map result = (Map)method.invoke(null);
            return ResultDTO.success(result);
        } catch (Exception ex) {
            log.error("枚举获取异常:", ex);
            return ResultDTO.failure("获取异常");
        }
    }

}
