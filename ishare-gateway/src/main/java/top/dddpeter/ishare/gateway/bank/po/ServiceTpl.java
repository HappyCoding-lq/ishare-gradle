package top.dddpeter.ishare.gateway.bank.po;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * @Description  
 * @Author  Hunter
 * @Date 2022-04-07 
 */

@Setter
@Getter
@ToString
@Entity
@Table ( name ="t_service_tpl" )
public class ServiceTpl  implements Serializable {

	private static final long serialVersionUID =  2988421885617999238L;

	/**
	 * 模板id
	 */
	@Id
   	@Column(name = "tpl_id" )
	private Long tplId;

	/**
	 * 服务编码
	 */
   	@Column(name = "service_code" )
	private String serviceCode;

	/**
	 * 银行编码
	 */
   	@Column(name = "bank_code" )
	private String bankCode;

	/**
	 * 模板名称(带相对路径)
	 */
   	@Column(name = "tpl_name" )
	private String tplName;

}
