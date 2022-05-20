package top.dddpeter.ishare.common.encryption.annotation;

import org.springframework.context.annotation.Import;
import top.dddpeter.ishare.common.encryption.config.EncryptAutoConfiguration;

import java.lang.annotation.*;

/**
 * 启用加密Starter
 *
 * <p>在Spring Boot启动类上加上此注解<p>
 *
 * <pre class="code">
 * &#064;SpringBootApplication
 * &#064;EnableEncrypt
 * public class App {
 *     public static void main(String[] args) {
 *         SpringApplication.run(App.class, args);
 *     }
 * }
 * <pre>
 *
 * @author : huangshuanbao
 * @version : V1.0
 * @date : 2020/2/24 1:14 下午t
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import({EncryptAutoConfiguration.class})
public @interface EnableEncrypt {

}
