package dto;

public class UserDto {
	private Long idUser;
	private String username;
	private String password;
	private TimezoneDto[] timezones;
	private RoleDto role;
	
	public Long getIdUser() {
		return idUser;
	}
	public void setIdUser(Long idUser) {
		this.idUser = idUser;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public TimezoneDto[] getTimezones() {
		return timezones;
	}
	public void setTimezones(TimezoneDto[] timezones) {
		this.timezones = timezones;
	}
	public RoleDto getRole() {
		return role;
	}
	public void setRole(RoleDto role) {
		this.role = role;
	}
}
