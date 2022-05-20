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
@Table ( name ="x_sequence_global" )
public class SequenceGlobal  implements Serializable {

	private static final long serialVersionUID =  6890079709397027052L;

	/**
	 * 自增主键id
	 */
	@Id
   	@Column(name = "id" )
	private Long id;

	/**
	 * sequence当前值
	 */
   	@Column(name = "value" )
	private Long value;

	/**
	 * sequence对应的名称，通常是表名
	 */
   	@Column(name = "name" )
	private String name;

	/**
	 * 创建时间
	 */
   	@Column(name = "gmt_create" )
	private Date gmtCreate;

	/**
	 * 修改时间
	 */
   	@Column(name = "gmt_modified" )
	private Date gmtModified;

}
