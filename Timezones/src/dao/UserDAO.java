package dao;

import java.security.MessageDigest;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.Query;

import com.sun.jersey.spi.resource.Singleton;

import model.Role;
import model.User;

@Singleton
public class UserDAO {

	private EntityManager em;

	public UserDAO() {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("Timezones");
		em = emf.createEntityManager();
	}

	@SuppressWarnings("unchecked")
	public User getUser(String username, String password) {
		em.clear();
		Query query = em.createQuery(
				"SELECT u FROM User u where upper(u.username) = :username and u.password = :password and u.flagDeleted = false");
		query.setParameter("username", username.toUpperCase());
		query.setParameter("password", MD5(password));
		List<User> users = query.getResultList();
		if (users.isEmpty()) {
			return null;
		}
		return users.get(0);
	}

	@SuppressWarnings("unchecked")
	public User getUser(Long idUser) {
		em.clear();
		Query query = em.createQuery("SELECT u FROM User u where u.idUser = :idUser and u.flagDeleted = false");
		query.setParameter("idUser", idUser);
		List<User> users = query.getResultList();
		if (users.isEmpty()) {
			return null;
		}
		return users.get(0);
	}

	@SuppressWarnings("unchecked")
	public String createUser(String username, String password) {
		// check if user exists
		em.clear();
		Query query = em
				.createQuery("SELECT u FROM User u where upper(u.username) = :username and u.flagDeleted = false");
		query.setParameter("username", username.toUpperCase());
		List<User> users = query.getResultList();

		// Existing user
		if (users.size() > 0) {
			return ("Existing");
		}

		// new user
		User user = new User();
		user.setUsername(username);
		user.setPassword(MD5(password));

		// the user is always created with role "user" (3)
		Query queryRole = em.createQuery("SELECT r FROM Role r WHERE r.idRole = 3");
		Role userRole = (Role) queryRole.getSingleResult();
		user.setRole(userRole);
		user.setFlagDeleted(false);

		EntityTransaction transaction = em.getTransaction();
		transaction.begin();
		em.persist(user);
		transaction.commit();

		return String.valueOf(user.getIdUser());
	}

	@SuppressWarnings("unchecked")
	public List<User> listUser() {
		em.clear();
		Query query = em.createQuery("SELECT u FROM User u where u.flagDeleted = false");
		List<User> users = query.getResultList();
		return users;
	}

	@SuppressWarnings("unchecked")
	public String deleteUser(Long idUser) {
		em.clear();
		Query query = em.createQuery("SELECT u FROM User u WHERE u.idUser = :idUser AND u.flagDeleted = false");
		query.setParameter("idUser", idUser);
		List<User> users = query.getResultList();
		if (users.isEmpty()) {
			return "Non existent user";
		}
		User user = users.get(0);
		user.setFlagDeleted(true);

		EntityTransaction transaction = em.getTransaction();
		transaction.begin();
		em.merge(user);
		transaction.commit();

		return "User deleted";
	}

	@SuppressWarnings("unchecked")
	public String updateUser(Long idUser, String username, String password, Long idRole) {
		em.clear();

		if (username != null && !username.isEmpty()) {
			Query queryVerify = em.createQuery(
					"SELECT u FROM User u where upper(u.username) = :username and u.idUser != :idUser and u.flagDeleted = false");
			queryVerify.setParameter("username", username.toUpperCase());
			queryVerify.setParameter("idUser", idUser);
			List<User> users = queryVerify.getResultList();
			// Existing user
			if (users.size() > 0) {
				return ("Existing");
			}
		}

		Query query = em.createQuery("SELECT u FROM User u where u.idUser = :idUser");
		query.setParameter("idUser", idUser);
		try {
			User user = (User) query.getSingleResult();
			if (username != null && !username.isEmpty()) {
				user.setUsername(username);
			}
			if (password != null && !password.isEmpty()) {
				user.setPassword(MD5(password));
			}
			if (idRole != null) {
				try {
					Query queryRole = em.createQuery("SELECT r FROM Role r WHERE r.idRole = :idRole");
					queryRole.setParameter("idRole", idRole);
					Role role = (Role) queryRole.getSingleResult();
					user.setRole(role);
				} catch (NoResultException ex) {
					return "role not found";
				}
			}

			EntityTransaction transaction = em.getTransaction();
			transaction.begin();
			em.merge(user);
			transaction.commit();

			return "Updated";
		} catch (NoResultException ex) {
			return "user not found";
		}
	}

	public String MD5(String md5) {
		try {
			MessageDigest md = java.security.MessageDigest.getInstance("MD5");
			byte[] array = md.digest(md5.getBytes());
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < array.length; ++i) {
				sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1, 3));
			}
			return sb.toString();
		} catch (java.security.NoSuchAlgorithmException e) {
		}
		return null;
	}
}
