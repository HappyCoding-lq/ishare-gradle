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
@Table ( name ="t_bank_info" )
public class BankInfo  implements Serializable {

	private static final long serialVersionUID =  7175486733830269935L;

	/**
	 * 银行编码
	 */
	@Id
   	@Column(name = "bank_code" )
	private String bankCode;

	/**
	 * 银行名称
	 */
   	@Column(name = "bank_name" )
	private String bankName;

	/**
	 * 服务开通时间
	 */
   	@Column(name = "service_date" )
	private String serviceDate;

	/**
	 * 服务端口
	 */
   	@Column(name = "service_port" )
	private Integer servicePort;

	/**
	 * 是否启用
	 */
   	@Column(name = "is_delete" )
	private String isDelete;

	/**
	 * 创建时间
	 */
   	@Column(name = "create_time" )
	private Date createTime;

}
