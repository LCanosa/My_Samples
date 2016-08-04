package model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the USERS database table.
 * 
 */
@Entity
@Table(name="USERS")
@NamedQuery(name="User.findAll", query="SELECT u FROM User u")
public class User implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3069575191101415368L;

	@Id
	@SequenceGenerator(name="USERS_IDUSER_GENERATOR", sequenceName="USERS_SEQ", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="USERS_IDUSER_GENERATOR")
	@Column(name="ID_USER")
	private Long idUser;

	@Column(name="FLAG_DELETED")
	private Boolean flagDeleted;

	private String password;

	private String username;

	//bi-directional many-to-one association to Timezone
	@OneToMany(mappedBy="user")
	private List<Timezone> timezones;

	//bi-directional many-to-one association to Role
	@ManyToOne
	@JoinColumn(name="ID_ROLE")
	private Role role;

	public User() {
	}

	public Long getIdUser() {
		return this.idUser;
	}

	public void setIdUser(Long idUser) {
		this.idUser = idUser;
	}

	public Boolean getFlagDeleted() {
		return this.flagDeleted;
	}

	public void setFlagDeleted(Boolean flagDeleted) {
		this.flagDeleted = flagDeleted;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public List<Timezone> getTimezones() {
		return this.timezones;
	}

	public void setTimezones(List<Timezone> timezones) {
		this.timezones = timezones;
	}

	public Timezone addTimezone(Timezone timezone) {
		getTimezones().add(timezone);
		timezone.setUser(this);

		return timezone;
	}

	public Timezone removeTimezone(Timezone timezone) {
		getTimezones().remove(timezone);
		timezone.setUser(null);

		return timezone;
	}

	public Role getRole() {
		return this.role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

}