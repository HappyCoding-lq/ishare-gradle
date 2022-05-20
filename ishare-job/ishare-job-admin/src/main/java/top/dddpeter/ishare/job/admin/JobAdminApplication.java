package top.dddpeter.ishare.job.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import top.dddpeter.ishare.common.annotation.EnableIShareFeignClients;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

/**
 * @author hqins 2019-12-10
 */
@SpringBootApplication(scanBasePackages = {"top.dddpeter.ishare.job.admin", "top.dddpeter.ishare.system.feign"})
@EnableIShareFeignClients
public class JobAdminApplication {

	public static void main(String[] args) {
        SpringApplication.run(JobAdminApplication.class, args);
	}
	@PostConstruct
	public void started() {
		TimeZone.setDefault(TimeZone.getTimeZone("Asia/Shanghai"));
	}
}