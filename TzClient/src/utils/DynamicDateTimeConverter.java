package utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@FacesConverter("dynamicDateTimeConverter")
public class DynamicDateTimeConverter implements Converter {

	String pattern;
	TimeZone timeZone;

	public String getPattern() {
		return pattern;
	}

	public void setPattern(String pattern) {
		this.pattern = pattern;
	}

	public TimeZone getTimeZone() {
		return timeZone;
	}

	public void setTimeZone(TimeZone timeZone) {
		this.timeZone = timeZone;
	}

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		setPattern((String) component.getAttributes().get("pattern"));
		setTimeZone(TimeZone.getTimeZone((String) component.getAttributes().get("timeZone")));
		SimpleDateFormat sdf = new SimpleDateFormat(getPattern());
		sdf.setTimeZone(getTimeZone());
		try {
			return sdf.parse(value);
		} catch (ParseException e) {
			return null;
		}
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		setPattern((String) component.getAttributes().get("pattern"));
		setTimeZone(TimeZone.getTimeZone((String) component.getAttributes().get("timeZone")));
		SimpleDateFormat sdf = new SimpleDateFormat(getPattern());
		sdf.setTimeZone(getTimeZone());
		return sdf.format(value);
	}

}