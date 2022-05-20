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
@Table ( name ="t_bank_branch" )
public class BankBranch  implements Serializable {

	private static final long serialVersionUID =  1655624684121972730L;

	/**
	 * 网点ID
	 */
	@Id
   	@Column(name = "branch_id" )
	private Long branchId;

	/**
	 * 网点代码
	 */
   	@Column(name = "branch_code" )
	private String branchCode;

	/**
	 * 银行代码
	 */
   	@Column(name = "bank_code" )
	private String bankCode;

	/**
	 * 网点名称
	 */
   	@Column(name = "branch_name" )
	private String branchName;

	/**
	 * 创建时间
	 */
   	@Column(name = "create_time" )
	private Date createTime;

}
