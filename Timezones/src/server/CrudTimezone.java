package server;

import java.util.Date;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONObject;

import com.sun.jersey.api.core.InjectParam;

import dao.TimezoneDAO;
import dao.TokenDAO;
import dto.Converters;
import dto.TimezoneDto;
import dto.UserDto;
import model.Timezone;
import model.Token;
import model.User;

@Path("timezone")
public class CrudTimezone {
	@InjectParam
	private TokenDAO tokenDAO;
	@InjectParam
	private TimezoneDAO timezoneDAO;

	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces("application/json")
	public Response createTimezone(@HeaderParam("token") String tokenString, @FormParam("idUser") Long idUser,
			@FormParam("name") String name, @FormParam("city") String city, @FormParam("hourDiff") int hourDiff) {
		try {
			JSONObject jsonObject = new JSONObject();
			if (idUser == null || name == null || name.isEmpty() || city == null || city.isEmpty() || hourDiff < -12
					|| hourDiff > 13) {
				jsonObject.put("Response", "missing or incorrect parameters");
				return Response.status(Response.Status.BAD_REQUEST).entity(String.valueOf(jsonObject)).build();
			}
			String tokenVerified = verifyToken(tokenString, idUser);
			if (tokenVerified == null) {
				return Response.status(Response.Status.UNAUTHORIZED).build();
			} else if (!tokenVerified.equals("OK")) {
				jsonObject.put("Response", tokenVerified);
				return Response.status(Response.Status.UNAUTHORIZED).entity(String.valueOf(jsonObject)).build();
			} else {
				String result = timezoneDAO.createTimezone(idUser, name, city, hourDiff);
				jsonObject.put("Response", result);
				if (result.equals("Timezone alredy exists")) {
					return Response.status(Response.Status.CONFLICT).entity(String.valueOf(jsonObject)).build();
				} else if (result.equals("Non existent user")){
					return Response.status(Response.Status.BAD_REQUEST).entity(String.valueOf(jsonObject)).build();
				}
				return Response.status(Response.Status.CREATED).entity(String.valueOf(jsonObject)).build();
			}
		} catch (Exception ex) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		}
	}

	@GET
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces("application/json")
	public Response getAllTimezones(@HeaderParam("token") String tokenString) {
		try {
			JSONObject jsonObject = new JSONObject();
			User user = verifyToken(tokenString);
			if (user == null) {
				return Response.status(Response.Status.UNAUTHORIZED).build();
			} else if (user.getRole().getIdRole() == 1) {
				List<Timezone> timezoneList = timezoneDAO.listTimezones();
				TimezoneDto[] timezones = Converters.listTimezoneToDto(timezoneList);
				jsonObject.put("timezones", timezones);
			} else {
				List<Timezone> timezoneList = timezoneDAO.listTimezones(user);
				TimezoneDto[] timezones = Converters.listTimezoneToDto(timezoneList);
				jsonObject.put("timezones", timezones);
			}
			return Response.status(Response.Status.OK).entity(String.valueOf(jsonObject)).build();
		} catch (Exception ex) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		}
	}
	
	@DELETE
	@Path("/{idTimezone}")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces("application/json")
	public Response deleteTimezone(@HeaderParam("token") String tokenString, @PathParam("idTimezone") Long idTimezone) {
		try {
			JSONObject jsonObject = new JSONObject();
			
			Timezone timezone = timezoneDAO.getTimezone(idTimezone);
			if (timezone == null) {
				jsonObject.put("Response", "Non existent timezone");
				return Response.status(Response.Status.NOT_FOUND).entity(String.valueOf(jsonObject)).build();
			}

			Long idUser = timezone.getUser().getIdUser();			
			String tokenVerified = verifyToken(tokenString, idUser);
			
			if (tokenVerified == null) {
				return Response.status(Response.Status.UNAUTHORIZED).build();
			} else if (!tokenVerified.equals("OK")) {
				jsonObject.put("Response", tokenVerified);
				return Response.status(Response.Status.UNAUTHORIZED).entity(String.valueOf(jsonObject)).build();
			} else {
				String result = timezoneDAO.deleteTimezone(timezone);
				jsonObject.put("Response", result);
				return Response.status(Response.Status.OK).entity(String.valueOf(jsonObject)).build();
			}
		} catch (Exception ex) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		}
	}

	@PUT
	@Path("/{idTimezone}")
	@Produces("application/json")
	public Response updateTimezone(@HeaderParam("token") String tokenString, @PathParam("idTimezone") Long idTimezone, 
			@FormParam("name") String name, @FormParam("city") String city, @FormParam("hourDiff") Integer hourDiff) {
		try {
			JSONObject jsonObject = new JSONObject();
			
			Timezone timezone = timezoneDAO.getTimezone(idTimezone);
			if (timezone == null) {
				jsonObject.put("Response", "Non existent timezone");
				return Response.status(Response.Status.NOT_FOUND).entity(String.valueOf(jsonObject)).build();
			}

			Long idUser = timezone.getUser().getIdUser();			
			String tokenVerified = verifyToken(tokenString, idUser);
			
			if (tokenVerified == null) {
				return Response.status(Response.Status.UNAUTHORIZED).build();
			} else if (!tokenVerified.equals("OK")) {
				jsonObject.put("Response", tokenVerified);
				return Response.status(Response.Status.UNAUTHORIZED).entity(String.valueOf(jsonObject)).build();
			} else {
				String result = timezoneDAO.updateTimezone(timezone, name, city, hourDiff);
				jsonObject.put("Response", result);
				return Response.status(Response.Status.OK).entity(String.valueOf(jsonObject)).build();
			}
		} catch (Exception ex) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		}
	}

	// operations are allowed to administrators (role 1) and to the user
	// authenticated on his own records
	public String verifyToken(String tokenString, Long idUser) {
		if (tokenString == null) {
			return null;
		}
		Token token = tokenDAO.getToken(tokenString);
		if (token == null) {
			return null;
		} else if (token.getUser().getRole().getIdRole() != 1 && token.getUser().getIdUser() != idUser) {
			return "unauthorized operation";
		} else {
			long diffTime = token.getExpirationTime().getTime() - new Date().getTime();
			if (diffTime < 0) {
				return "token expired, please login again!";
			}
		}
		return "OK";
	}

	// only administrators (role 1) are allowed to operate
	public User verifyToken(String tokenString) {
		if (tokenString == null) {
			return null;
		}
		Token token = tokenDAO.getToken(tokenString);
		if (token == null) {
			return null;
		} else {
			long diffTime = token.getExpirationTime().getTime() - new Date().getTime();
			if (diffTime < 0) {
				return null;
			}
		}
		return token.getUser();
	}
}
