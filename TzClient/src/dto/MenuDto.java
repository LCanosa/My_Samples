package dto;

public class MenuDto {
	private Long idItem;
	private String description;
	private String link;
	private Long idItemSup;
	public Long getIdItem() {
		return idItem;
	}
	public void setIdItem(Long idItem) {
		this.idItem = idItem;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	public Long getIdItemSup() {
		return idItemSup;
	}
	public void setIdItemSup(Long idItemSup) {
		this.idItemSup = idItemSup;
	}
}
