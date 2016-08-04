package model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the MENU database table.
 * 
 */
@Entity
@NamedQuery(name="Menu.findAll", query="SELECT m FROM Menu m")
public class Menu implements Serializable {
	

	/**
	 * 
	 */
	private static final long serialVersionUID = -4196037449229150318L;

	@Id
	@Column(name="ID_ITEM")
	private Long idItem;

	private String description;

	@Column(name="\"LINK\"")
	private String link;

	@Column(name="ID_ITEM_SUP")
	private Long idItemSup;

	//bi-directional many-to-many association to Role
	@ManyToMany
	@JoinTable(
		name="MENU_ROLES"
		, joinColumns={
			@JoinColumn(name="ID_ITEM")
			}
		, inverseJoinColumns={
			@JoinColumn(name="ID_ROLE")
			}
		)
	private List<Role> roles;

	public Menu() {
	}

	public Long getIdItem() {
		return this.idItem;
	}

	public void setIdItem(Long idItem) {
		this.idItem = idItem;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getLink() {
		return this.link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public List<Role> getRoles() {
		return this.roles;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}

	public Long getIdItemSup() {
		return idItemSup;
	}

	public void setIdItemSup(Long idItemSup) {
		this.idItemSup = idItemSup;
	}

}