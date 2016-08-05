package managed;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.DefaultSubMenu;
import org.primefaces.model.menu.MenuModel;

import dto.MenuDto;

@ManagedBean(name = "navMenuBean")
@ViewScoped
public class NavMenuBean {

	private MenuModel model;

	public void createMenu(MenuDto[] menus) {
		model = new DefaultMenuModel();
		DefaultMenuItem home = new DefaultMenuItem("HOME");
		home.setCommand("#{restClient.setPage('home')}");
		home.setUpdate(":container");
		model.addElement(home);

		if (menus != null) {
			for (MenuDto root : menus) {
				if (root.getIdItemSup() == 0) {
					DefaultSubMenu menu = new DefaultSubMenu(root.getDescription());
					for (MenuDto leaf : menus) {
						if (leaf.getIdItemSup() == root.getIdItem()) {
							DefaultMenuItem menuItem = new DefaultMenuItem(leaf.getDescription());
							menuItem.setCommand("#{restClient.setPage('" + leaf.getLink() + "')}");
							menuItem.setUpdate(":container");
							menu.addElement(menuItem);
						}
					}
					model.addElement(menu);
				}
			}
		}

		DefaultMenuItem logout = new DefaultMenuItem("LOGOUT");
		logout.setCommand("#{restClient.logout}");
		logout.setUpdate("@all");
		model.addElement(logout);
	}

	public MenuModel getModel() {
		return model;
	}

	public void setModel(MenuModel model) {
		this.model = model;
	}
}
