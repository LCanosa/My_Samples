package model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the TIMEZONES database table.
 * 
 */
@Entity
@Table(name="TIMEZONES")
@NamedQuery(name="Timezone.findAll", query="SELECT t FROM Timezone t")
public class Timezone implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2665909536092639126L;

	@Id
	@SequenceGenerator(name="TIMEZONES_IDTIMEZONE_GENERATOR", sequenceName="TIMEZONES_SEQ", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TIMEZONES_IDTIMEZONE_GENERATOR")
	@Column(name="ID_TIMEZONE")
	private Long idTimezone;

	private String city;

	@Column(name="HOUR_DIFF")
	private Integer hourDiff;

	private String name;

	//bi-directional many-to-one association to User
	@ManyToOne
	@JoinColumn(name="ID_USER")
	private User user;

	public Timezone() {
	}

	public Long getIdTimezone() {
		return this.idTimezone;
	}

	public void setIdTimezone(Long idTimezone) {
		this.idTimezone = idTimezone;
	}

	public String getCity() {
		return this.city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public Integer getHourDiff() {
		return this.hourDiff;
	}

	public void setHourDiff(Integer hourDiff) {
		this.hourDiff = hourDiff;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}