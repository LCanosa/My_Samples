package model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the TOKEN database table.
 * 
 */
@Entity
public class Token implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5743106052414681741L;

	@Id
	@SequenceGenerator(name="TOKEN_IDTOKEN_GENERATOR", sequenceName="TOKEN_SEQ", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TOKEN_IDTOKEN_GENERATOR")
	@Column(name="ID_TOKEN")
	private Long idToken;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="EXPIRATION_TIME")
	private Date expirationTime;

	private String token;

	//bi-directional many-to-one association to User
	@ManyToOne
	@JoinColumn(name="ID_USER")
	private User user;

	public Token() {
	}

	public Long getIdToken() {
		return this.idToken;
	}

	public void setIdToken(Long idToken) {
		this.idToken = idToken;
	}

	public Date getExpirationTime() {
		return this.expirationTime;
	}

	public void setExpirationTime(Date expirationTime) {
		this.expirationTime = expirationTime;
	}

	public String getToken() {
		return this.token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}