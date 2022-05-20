package top.dddpeter.ishare.system.domain.message;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class CommonSendEmail {

    private String templateNo;

    private List<String> emailList;

    private Map<String, String> param;

}
