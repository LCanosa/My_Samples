package server;

import java.math.BigInteger;
import java.security.SecureRandom;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONObject;

import com.sun.jersey.api.core.InjectParam;

import dao.TokenDAO;
import dao.UserDAO;
import dto.Converters;
import dto.MenuDto;
import dto.TimezoneDto;
import model.Token;
import model.User;

@Path("login")
public class Login {
	@InjectParam
	private UserDAO userDAO;
	@InjectParam
	private TokenDAO tokenDAO;

	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces("application/json")
	public Response authenticate(@FormParam("username") String username, @FormParam("password") String password) {
		try {
			JSONObject jsonObject = new JSONObject();
			if(username == null || password == null){
				jsonObject.put("authentication", "failed");
				return Response.status(Response.Status.UNAUTHORIZED).entity(String.valueOf(jsonObject)).build();
			}
			User user = userDAO.getUser(username, password);
			// user does not exist
			if (user == null) {
				jsonObject.put("authentication", "failed");
				return Response.status(Response.Status.UNAUTHORIZED).entity(String.valueOf(jsonObject)).build();
			} else {
				// existing user. Generate token
				SecureRandom random = new SecureRandom();
				String generatedToken = new BigInteger(130, random).toString(32);
				Token token = tokenDAO.insertToken(user, generatedToken);

				// generate response object
				jsonObject.put("authentication", "OK");
				jsonObject.put("token", token.getToken());
				MenuDto[] menus = Converters.listMenuToDto(user.getRole().getMenus());
				jsonObject.put("menus", menus);
				TimezoneDto[] timezones = Converters.listTimezoneToDto(user.getTimezones());
				jsonObject.put("timezones", timezones);
				jsonObject.put("idUser", user.getIdUser());
				jsonObject.put("role", user.getRole().getIdRole());
				return Response.status(Response.Status.OK).entity(String.valueOf(jsonObject)).build();
			}
		} catch (Exception ex) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		}
	}
}
