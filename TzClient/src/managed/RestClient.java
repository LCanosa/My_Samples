package managed;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.MediaType;

import org.primefaces.json.JSONArray;
import org.primefaces.json.JSONObject;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.representation.Form;

import dto.Converter;
import dto.MenuDto;
import dto.TimezoneDto;
import dto.UserDto;

@ManagedBean(name = "restClient")
@ViewScoped
public class RestClient {
	private String username;
	private String newUsr;
	private String password;
	private String newPwd;
	private String password2;
	private String name;
	private String city;
	private Integer hourDiff;
	private String errorMessage;
	private String page;
	private Integer myIdUser;
	private Integer idUser;
	private Client client;
	private WebResource loginResource;
	private WebResource listUserResource;
	private WebResource createUserResource;
	private WebResource updateUserResource;
	private WebResource deleteUserResource;
	private WebResource listTimezoneResource;
	private WebResource createTimezoneResource;
	private WebResource updateTimezoneResource;
	private WebResource deleteTimezoneResource;
	private static final String SERVICE_URL = "http://localhost:7001/Timezones/rest/";
	private String token;
	private MenuDto[] menus;
	private TimezoneDto[] timezones;
	private TimezoneDto[] userTimezones;
	private UserDto[] users;
	private TimeZone tz = TimeZone.getTimeZone("GMT");

	@ManagedProperty("#{navMenuBean}")
	private NavMenuBean navMenuBean;
	private Integer myRole;

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

	public String getPassword2() {
		return password2;
	}

	public void setPassword2(String password2) {
		this.password2 = password2;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getPage() {
		return page;
	}

	public void setPage(String page) {
		this.page = page;
		errorMessage = "";
	}

	public NavMenuBean getNavMenuBean() {
		return navMenuBean;
	}

	public void setNavMenuBean(NavMenuBean navMenuBean) {
		this.navMenuBean = navMenuBean;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public UserDto[] getUsers() {
		if (users == null) {
			listUsers();
		}
		return users;
	}

	public void setUsers(UserDto[] users) {
		this.users = users;
	}

	public TimezoneDto[] getTimezones() {
		if (timezones == null) {
			listTimezones();
		}
		return timezones;
	}

	public void setTimezones(TimezoneDto[] timezones) {
		this.timezones = timezones;
	}

	public String getNewUsr() {
		return newUsr;
	}

	public void setNewUsr(String newUsr) {
		this.newUsr = newUsr;
	}

	public String getNewPwd() {
		return newPwd;
	}

	public void setNewPwd(String newPwd) {
		this.newPwd = newPwd;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

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

	public Integer getIdUser() {
		return idUser;
	}

	public void setIdUser(Integer idUser) {
		this.idUser = idUser;
	}

	public Integer getMyRole() {
		return myRole;
	}

	public void setMyRole(Integer myRole) {
		this.myRole = myRole;
	}

	public TimezoneDto[] getUserTimezones() {
		for (TimezoneDto timezoneDto : userTimezones) {
			timezoneDto.setTime(getTime(timezoneDto.getHourDiff()));
		}
		return userTimezones;
	}

	public void setUserTimezones(TimezoneDto[] userTimezones) {
		this.userTimezones = userTimezones;
	}

	@PostConstruct
	public void init() {
		client = Client.create();
		page = "login";
	}

	public void login() {
		try {
			navMenuBean.setModel(null);
			errorMessage = "";
			if (loginResource == null) {
				loginResource = client.resource(SERVICE_URL + "login");
			}

			Form form = new Form();
			form.add("username", username);
			form.add("password", password);

			ClientResponse response = loginResource.accept("application/json")
					.type(MediaType.APPLICATION_FORM_URLENCODED).post(ClientResponse.class, form);
			switch (response.getStatus()) {
			case 200:
				String jsonString = response.getEntity(String.class);
				JSONObject jsonObject = new JSONObject(jsonString);
				token = (String) jsonObject.get("token");
				myIdUser = (Integer) jsonObject.get("idUser");
				myRole = (Integer) jsonObject.get("role");
				menus = Converter.toMenuArray((JSONArray) jsonObject.get("menus"));
				navMenuBean.createMenu(menus);
				userTimezones = Converter.toTimezoneArray((JSONArray) jsonObject.get("timezones"));
				setPage("home");
				break;
			case 401:
				errorMessage = "wrong username or password!";
				break;
			default:
				errorMessage = "Ooops, an error occourred!, please retry later!";
			}
		} catch (Exception ex) {
			errorMessage = "Ooops, an error occourred!, please retry later!";
			ex.printStackTrace();
		}
	}

	public void createUser() {
		try {
			errorMessage = "";
			if (createUserResource == null) {
				createUserResource = client.resource(SERVICE_URL + "user");
			}

			if (newUsr == null || newUsr.isEmpty() || newPwd == null || newPwd.isEmpty() || password2 == null
					|| password2.isEmpty()) {
				errorMessage = "Username and password are required!";
				return;
			}
			if (!newPwd.equals(password2)) {
				errorMessage = "Password does not match!";
				return;
			}

			Form form = new Form();
			form.add("username", newUsr);
			form.add("password", newPwd);

			ClientResponse response = createUserResource.accept("application/json")
					.type(MediaType.APPLICATION_FORM_URLENCODED).post(ClientResponse.class, form);

			switch (response.getStatus()) {
			case 201:
				if (token == null) {
					username = newUsr;
					password = newPwd;
					login();
				} else {
					listUsers();
					setPage("listUsers");
				}
				break;
			case 400:
				errorMessage = "Username and password are required!";
				break;
			case 409:
				errorMessage = "User alredy exists!";
				break;
			default:
				errorMessage = "Ooops, an error occourred!, please retry later!";
			}
		} catch (Exception ex) {
			errorMessage = "Ooops, an error occourred!, please retry later!";
			ex.printStackTrace();
		}
	}

	public void listUsers() {
		try {
			errorMessage = "";
			if (listUserResource == null) {
				listUserResource = client.resource(SERVICE_URL + "user");
			}
			ClientResponse response = listUserResource.accept("application/json").header("token", token)
					.type(MediaType.APPLICATION_FORM_URLENCODED).get(ClientResponse.class);
			switch (response.getStatus()) {
			case 200:
				String jsonString = response.getEntity(String.class);
				JSONObject jsonObject = new JSONObject(jsonString);
				users = Converter.toUserArray((JSONArray) jsonObject.get("users"));
				break;
			default:
				users = new UserDto[0];
			}
		} catch (Exception ex) {
			errorMessage = "Ooops, an error occourred!, please retry later!";
			ex.printStackTrace();
		}
	}

	public void updateUser(UserDto user) {
		try {
			errorMessage = "";
			updateUserResource = client.resource(SERVICE_URL + "user/" + user.getIdUser());

			Form form = new Form();
			form.add("username", user.getUsername());
			form.add("password", user.getPassword());
			form.add("idRole", user.getRole().getIdRole());

			ClientResponse response = updateUserResource.accept("application/json").header("token", token)
					.type(MediaType.APPLICATION_FORM_URLENCODED).put(ClientResponse.class, form);

			listUsers();
			switch (response.getStatus()) {
			case 200:
				break;
			case 409:
				errorMessage = "Username alredy exists!";
				break;
			case 400:
				errorMessage = "wrong parameters, please correct!";
				break;
			default:
				errorMessage = "Ooops, an error occourred!, please retry later!";
			}
		} catch (Exception ex) {
			errorMessage = "Ooops, an error occourred!, please retry later!";
			ex.printStackTrace();
		}
	}

	public void deleteUser(UserDto user) {
		try {
			errorMessage = "";
			deleteUserResource = client.resource(SERVICE_URL + "user/" + user.getIdUser());

			ClientResponse response = deleteUserResource.accept("application/json").header("token", token)
					.type(MediaType.APPLICATION_FORM_URLENCODED).delete(ClientResponse.class);

			listUsers();
			switch (response.getStatus()) {
			case 200:
				break;
			case 404:
				errorMessage = "non existing user";
				break;
			default:
				errorMessage = "Ooops, an error occourred!, please retry later!";
			}
		} catch (Exception ex) {
			errorMessage = "Ooops, an error occourred!, please retry later!";
			ex.printStackTrace();
		}
	}

	public void createTimezone() {
		try {
			if (idUser == null) {
				idUser = myIdUser;
			}
			errorMessage = "";
			if (createTimezoneResource == null) {
				createTimezoneResource = client.resource(SERVICE_URL + "timezone");
			}

			if (name == null || name.isEmpty() || city == null || city.isEmpty() || hourDiff == null) {
				errorMessage = "Required fields missing";
				return;
			}
			if (hourDiff < -12 || hourDiff > 13) {
				errorMessage = "Please set a correct value for difference to GMT";
				return;
			}

			Form form = new Form();
			form.add("idUser", idUser);
			form.add("name", name);
			form.add("city", city);
			form.add("hourDiff", hourDiff);

			ClientResponse response = createTimezoneResource.accept("application/json").header("token", token)
					.type(MediaType.APPLICATION_FORM_URLENCODED).post(ClientResponse.class, form);

			listTimezones();
			refreshUserTimezones();
			
			switch (response.getStatus()) {	
			case 201:
				setPage("listTimezones");
				break;
			case 400:
				errorMessage = "Required fields missing";
				break;
			case 409:
				errorMessage = "Timezone alredy exists!";
				break;
			case 401:
				errorMessage = "Operation not authorized";
				break;
			default:
				errorMessage = "Ooops, an error occourred!, please retry later!";
			}
		} catch (Exception ex) {
			errorMessage = "Ooops, an error occourred!, please retry later!";
			ex.printStackTrace();
		}
	}

	public void listTimezones() {
		try {
			errorMessage = "";
			if (listTimezoneResource == null) {
				listTimezoneResource = client.resource(SERVICE_URL + "timezone");
			}
			ClientResponse response = listTimezoneResource.accept("application/json").header("token", token)
					.type(MediaType.APPLICATION_FORM_URLENCODED).get(ClientResponse.class);
			switch (response.getStatus()) {
			case 200:
				String jsonString = response.getEntity(String.class);
				JSONObject jsonObject = new JSONObject(jsonString);
				timezones = Converter.toTimezoneArray((JSONArray) jsonObject.get("timezones"));
				break;
			default:
				timezones = new TimezoneDto[0];
			}
		} catch (Exception ex) {
			errorMessage = "Ooops, an error occourred!, please retry later!";
			ex.printStackTrace();
		}
	}

	public void updateTimezone(TimezoneDto timezone) {
		try {
			errorMessage = "";
			updateTimezoneResource = client.resource(SERVICE_URL + "timezone/" + timezone.getIdTimezone());

			Form form = new Form();
			form.add("name", timezone.getName());
			form.add("city", timezone.getCity());
			form.add("hourDiff", timezone.getHourDiff());

			ClientResponse response = updateTimezoneResource.accept("application/json").header("token", token)
					.type(MediaType.APPLICATION_FORM_URLENCODED).put(ClientResponse.class, form);

			listTimezones();
			refreshUserTimezones();
			switch (response.getStatus()) {
			case 200:
				break;
			case 404:
				errorMessage = "Non existent timezone";
				break;
			case 401:
				errorMessage = "Operation not authorized";
				break;
			default:
				errorMessage = "Ooops, an error occourred!, please retry later!";
			}
		} catch (Exception ex) {
			errorMessage = "Ooops, an error occourred!, please retry later!";
			ex.printStackTrace();
		}
	}

	public void deleteTimezone(TimezoneDto timezone) {
		try {
			errorMessage = "";
			deleteTimezoneResource = client.resource(SERVICE_URL + "timezone/" + timezone.getIdTimezone());

			ClientResponse response = deleteTimezoneResource.accept("application/json").header("token", token)
					.type(MediaType.APPLICATION_FORM_URLENCODED).delete(ClientResponse.class);

			listTimezones();
			refreshUserTimezones();
			switch (response.getStatus()) {
			case 200:
				break;
			case 404:
				errorMessage = "non existing timezone";
				break;
			case 401:
				errorMessage = "Operation not authorized";
				break;
			default:
				errorMessage = "Ooops, an error occourred!, please retry later!";
			}
		} catch (Exception ex) {
			errorMessage = "Ooops, an error occourred!, please retry later!";
			ex.printStackTrace();
		}
	}

	public void logout() {
		ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
		try {
			ec.redirect(((HttpServletRequest) ec.getRequest()).getRequestURI());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}	
	
	public String getTime(int hourDiff) {		
		Calendar now = Calendar.getInstance(tz);
		now.add(Calendar.HOUR_OF_DAY, hourDiff);
		return String.format("%02d:%02d", now.get(Calendar.HOUR_OF_DAY), now.get(Calendar.MINUTE));		
	}

	public void refreshUserTimezones(){
		ArrayList<TimezoneDto> tempList = new ArrayList<TimezoneDto>();
		for (TimezoneDto timezoneDto : timezones) {
			if(timezoneDto.getUsername().equals(username)){
				tempList.add(timezoneDto);
			}
		}
		userTimezones = tempList.toArray(new TimezoneDto[tempList.size()]);
	}
}
