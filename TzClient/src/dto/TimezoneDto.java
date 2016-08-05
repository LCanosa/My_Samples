package dto;

public class TimezoneDto {

	private Long idTimezone;
	private String city;
	private Integer hourDiff;
	private String name;
	private String username;
	private String time;
	
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public Integer getHourDiff() {
		return hourDiff;
	}
	public void setHourDiff(Integer hourDiff) {
		this.hourDiff = hourDiff;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public Long getIdTimezone() {
		return idTimezone;
	}
	public void setIdTimezone(Long idTimezone) {
		this.idTimezone = idTimezone;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	
}
