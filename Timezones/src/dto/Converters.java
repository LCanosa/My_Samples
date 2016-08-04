package dto;

import java.util.List;

import model.Menu;
import model.Role;
import model.Timezone;
import model.User;

public class Converters {

	public static MenuDto menuToDto(Menu menu){
		MenuDto dto = new MenuDto();
		dto.setIdItem(menu.getIdItem());
		dto.setDescription(menu.getDescription());
		dto.setLink(menu.getLink());
		dto.setIdItemSup(menu.getIdItemSup());
		return dto;
	}
	
	public static MenuDto[] listMenuToDto(List<Menu> menuList){
		MenuDto[] dtoArray = new MenuDto[menuList.size()];
		int i = 0;
		for (Menu menu : menuList) {
			dtoArray[i] = menuToDto(menu);
			i++;
		}
		return dtoArray;
	}
	
	public static TimezoneDto timezoneToDto(Timezone timezone){
		TimezoneDto dto = new TimezoneDto();
		dto.setIdTimezone(timezone.getIdTimezone());
		dto.setCity(timezone.getCity());
		dto.setHourDiff(timezone.getHourDiff());
		dto.setName(timezone.getName());
		dto.setUsername(timezone.getUser().getUsername());
		return dto;
	}
	
	public static TimezoneDto[] listTimezoneToDto(List<Timezone> timezoneList){
		TimezoneDto[] dtoArray = new TimezoneDto[timezoneList.size()];
		int i = 0;
		for (Timezone timezone : timezoneList) {
			dtoArray[i] = timezoneToDto(timezone);
			i++;
		}
		return dtoArray;
	}
	
	public static RoleDto roleToDto(Role role){
		RoleDto dto = new RoleDto();
		dto.setIdRole(role.getIdRole());
		dto.setDescription(role.getRoleDescription());
		return dto;
	}
	
	public static UserDto userToDto (User user){
		UserDto dto = new UserDto();
		dto.setIdUser(user.getIdUser());
		dto.setUsername(user.getUsername());
		dto.setTimezones(listTimezoneToDto(user.getTimezones()));
		dto.setRole(roleToDto(user.getRole()));
		return dto;
	}
	
	public static UserDto[] listUserToDto(List<User> userList){
		UserDto[] dtoArray = new UserDto[userList.size()];
		int i = 0;
		for (User user : userList) {
			dtoArray[i] = userToDto(user);
			i++;
		}
		return dtoArray;
	}
}
