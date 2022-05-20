package top.dddpeter.ishare.system.domain.message;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class CommonSendSms {

    private String templateNo;

    private List<String> phonesList;

    private Map<String, String> param;

}
