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

import dao.TokenDAO;
import dao.UserDAO;
import dto.Converters;
import dto.UserDto;
import model.Token;
import model.User;

@Path("user")
public class CrudUser {
	@InjectParam
	private UserDAO userDAO;
	@InjectParam
	private TokenDAO tokenDAO;

	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces("application/json")
	public Response createUser(@FormParam("username") String username, @FormParam("password") String password) {
		try {
			JSONObject jsonObject = new JSONObject();
			if (username == null ||  username.isEmpty() || password == null || password.isEmpty()) {
				jsonObject.put("Response", "missing or incorrect parameters");
				return Response.status(Response.Status.BAD_REQUEST).entity(String.valueOf(jsonObject)).build();
			}
			String result = userDAO.createUser(username, password);
			jsonObject.put("Response", result);
			if (result.equals("Existing")) {
				return Response.status(Response.Status.CONFLICT).entity(String.valueOf(jsonObject)).build();
			}
			return Response.status(Response.Status.CREATED).entity(String.valueOf(jsonObject)).build();
		} catch (Exception ex) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		}
	}

	@GET
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces("application/json")
	public Response listUsers(@HeaderParam("token") String tokenString) {
		try {
			JSONObject jsonObject = new JSONObject();
			String tokenVerified = verifyToken(tokenString);
			if (tokenVerified == null) {
				return Response.status(Response.Status.UNAUTHORIZED).build();
			} else if (!tokenVerified.equals("OK")) {
				jsonObject.put("Response", tokenVerified);
				return Response.status(Response.Status.UNAUTHORIZED).entity(String.valueOf(jsonObject)).build();
			} else {
				List<User> userList = userDAO.listUser();
				UserDto[] users = Converters.listUserToDto(userList);
				jsonObject.put("users", users);
			}

			return Response.status(Response.Status.OK).entity(String.valueOf(jsonObject)).build();
		} catch (Exception ex) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		}
	}

	@DELETE
	@Path("/{idUser}")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces("application/json")
	public Response deleteUser(@HeaderParam("token") String tokenString, @PathParam("idUser") Long idUser) {
		try {
			JSONObject jsonObject = new JSONObject();
			String tokenVerified = verifyToken(tokenString);
			if (tokenVerified == null) {
				return Response.status(Response.Status.UNAUTHORIZED).build();
			} else if (!tokenVerified.equals("OK")) {
				jsonObject.put("Response", tokenVerified);
				return Response.status(Response.Status.UNAUTHORIZED).entity(String.valueOf(jsonObject)).build();
			} else {
				String result = userDAO.deleteUser(idUser);
				jsonObject.put("Response", result);

				if (result.equals("Non existent user")) {
					return Response.status(Response.Status.NOT_FOUND).entity(String.valueOf(jsonObject)).build();
				}
			}
			return Response.status(Response.Status.OK).entity(String.valueOf(jsonObject)).build();
		} catch (Exception ex) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		}
	}

	@PUT
	@Path("/{idUser}")
	@Produces("application/json")
	public Response updateUser(@HeaderParam("token") String tokenString, @PathParam("idUser") Long idUser,
			@FormParam("username") String username, @FormParam("password") String password,
			@FormParam("idRole") Long idRole) {
		try {
			JSONObject jsonObject = new JSONObject();

			String tokenVerified = verifyToken(tokenString);
			if (tokenVerified == null) {
				return Response.status(Response.Status.UNAUTHORIZED).build();
			} else if (!tokenVerified.equals("OK")) {
				jsonObject.put("Response", tokenVerified);
				return Response.status(Response.Status.UNAUTHORIZED).entity(String.valueOf(jsonObject)).build();
			} else {
				String result = userDAO.updateUser(idUser, username, password, idRole);
				jsonObject.put("Response", result);
				
				if (result.equals("Existing")) {
					return Response.status(Response.Status.CONFLICT).entity(String.valueOf(jsonObject)).build();
				} else if (result.equals("role not found")) {
					return Response.status(Response.Status.BAD_REQUEST).entity(String.valueOf(jsonObject)).build();
				} else if (result.equals("user not found")) {
					return Response.status(Response.Status.NOT_FOUND).entity(String.valueOf(jsonObject)).build();
				} else {
					return Response.status(Response.Status.OK).entity(String.valueOf(jsonObject)).build();
				}
			}
		} catch (Exception ex) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		}
	}

	// only administrator (role 1) and user managers (role 2) are allowed to operate
	public String verifyToken(String tokenString) {
		if (tokenString == null) {
		}
		Token token = tokenDAO.getToken(tokenString);
		if (token == null) {
			return null;
		} else if (token.getUser().getRole().getIdRole() != 1 && token.getUser().getRole().getIdRole() != 2) {
			return "unauthorized operation";
		} else {
			long diffTime = token.getExpirationTime().getTime() - new Date().getTime();
			if (diffTime < 0) {
				return "token expired, please login again!";
			}
		}
		return "OK";
	}
}
