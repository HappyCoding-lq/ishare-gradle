package top.dddpeter.ishare.gateway.bank.po;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * @Description  
 * @Author  Hunter
 * @Date 2022-04-07 
 */

@Setter
@Getter
@ToString
@Entity
@Table ( name ="t_bank_service" )
public class BankService  implements Serializable {

	private static final long serialVersionUID =  5534896855523624624L;

	/**
	 * 服务ID
	 */
	@Id
   	@Column(name = "service_id" )
	private Long serviceId;

	/**
	 * 服务编码
	 */
   	@Column(name = "service_code" )
	private String serviceCode;

	/**
	 * 服务名称
	 */
   	@Column(name = "service_name" )
	private String serviceName;

	/**
	 * 服务处理类
	 */
   	@Column(name = "service_bean" )
	private String serviceBean;

	/**
	 * 银行代码
	 */
   	@Column(name = "bank_code" )
	private String bankCode;

	/**
	 * 创建时间
	 */
   	@Column(name = "create_time" )
	private Date createTime;

}
