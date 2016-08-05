package dto;

import org.primefaces.json.JSONArray;
import org.primefaces.json.JSONObject;

public class Converter {
	public static MenuDto[] toMenuArray(JSONArray array) {
		if (array == null || array.length() == 0)
			return null;

		MenuDto[] arr = new MenuDto[array.length()];
		for (int i = 0; i < arr.length; i++) {
			JSONObject obj = array.getJSONObject(i);
			MenuDto dto = toMenu(obj);
			arr[i] = dto;
		}
		return arr;
	}

	private static MenuDto toMenu(JSONObject obj) {
		MenuDto dto = new MenuDto();
		if (obj.has("description")) {
			dto.setDescription(obj.getString("description"));
		}
		if (obj.has("idItem")) {
			dto.setIdItem(obj.getLong("idItem"));
		}
		if (obj.has("idItemSup")) {
			dto.setIdItemSup(obj.getLong("idItemSup"));
		}
		if (obj.has("link")) {
			dto.setLink(obj.getString("link"));
		}
		return dto;
	}

	public static TimezoneDto[] toTimezoneArray(JSONArray array) {
		if (array == null || array.length() == 0)
			return new TimezoneDto[0];
		
		TimezoneDto[] arr = new TimezoneDto[array.length()];
		for (int i = 0; i < arr.length; i++) {
			JSONObject obj = array.getJSONObject(i);
			TimezoneDto dto = toTimezone(obj);
			arr[i] = dto;
		}
		return arr;
	}
	
	public static TimezoneDto toTimezone(JSONObject obj) {
		TimezoneDto dto = new TimezoneDto();
		if (obj.has("city")) {
			dto.setCity(obj.getString("city"));
		}
		if (obj.has("hourDiff")) {
			dto.setHourDiff(obj.getInt("hourDiff"));
		}
		if (obj.has("idTimezone")) {
			dto.setIdTimezone(obj.getLong("idTimezone"));
		}
		if (obj.has("name")) {
			dto.setName(obj.getString("name"));
		}
		if (obj.has("username")) {
			dto.setUsername(obj.getString("username"));
		}
		return dto;
	}
	
	public static UserDto[] toUserArray(JSONArray array) {
		if (array == null || array.length() == 0)
			return null;
		
		UserDto[] arr = new UserDto[array.length()];
		for (int i = 0; i < arr.length; i++) {
			JSONObject obj = array.getJSONObject(i);
			UserDto dto = toUser(obj);
			arr[i] = dto;
		}
		return arr;
	}
	
	public static UserDto toUser(JSONObject obj){
		UserDto dto = new UserDto();

		dto.setIdUser(obj.getLong("idUser"));
		if(obj.has("username")){
			dto.setUsername(obj.getString("username"));
		}
		if(obj.has("role")){
			dto.setRole(toRole(obj.getJSONObject("role")));
		}
		
		return dto;
	}
	
	public static RoleDto toRole(JSONObject obj){
		RoleDto dto = new RoleDto();
		
		dto.setIdRole(obj.getLong("idRole"));
		dto.setDescription(obj.getString("description"));
		return dto;
	}
}
