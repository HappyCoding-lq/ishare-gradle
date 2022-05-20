package top.dddpeter.ishare.tool.sequence.test;


import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import top.dddpeter.ishare.tool.ToolApplication;
import top.dddpeter.ishare.tool.sequence.service.MaxNoPubFunService;

import javax.annotation.Resource;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = ToolApplication.class)
@Slf4j
public class MaxNoPubFunServiceImplTest {
    @Resource
    MaxNoPubFunService maxNoPubFunService;
    @Test
    public void createMaxNo() {
        for(int i=0 ;i<1000;i++){
             new Runnable() {
                 @Override
                 public void run() {
                     long start = System.currentTimeMillis();
                     String result = maxNoPubFunService.createMaxNo("test",15);
                     long end = System.currentTimeMillis();
                     log.info(String.valueOf(this));
                     log.info(((end-start)*1.000)+"ms");
                 }
             }.run();

        }

    }
}