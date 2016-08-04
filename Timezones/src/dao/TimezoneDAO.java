package dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;

import com.sun.jersey.api.core.InjectParam;
import com.sun.jersey.spi.resource.Singleton;

import model.Timezone;
import model.User;

@Singleton
public class TimezoneDAO {
	@InjectParam
	private UserDAO userDAO;

	private EntityManager em;

	public TimezoneDAO() {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("Timezones");
		em = emf.createEntityManager();
	}

	@SuppressWarnings("unchecked")
	public String createTimezone(Long idUser, String name, String city, int hourDiff) {
		em.clear();
		// check if user exist
		User user = userDAO.getUser(idUser);
		if (user == null) {
			return "Non existent user";
		}

		// check if timezone exists
		Query query = em.createQuery("SELECT t FROM Timezone t where t.user = :user and upper(t.name) = :name");
		query.setParameter("user", user);
		query.setParameter("name", name.toUpperCase());
		List<Timezone> timezones = query.getResultList();

		// Existing timezone
		if (timezones.size() > 0) {
			return ("Timezone alredy exists");
		}

		// new timezone
		Timezone timezone = new Timezone();
		timezone.setUser(user);
		timezone.setName(name);
		timezone.setCity(city);
		timezone.setHourDiff(hourDiff);

		EntityTransaction transaction = em.getTransaction();
		transaction.begin();
		em.persist(timezone);
		transaction.commit();

		return String.valueOf(timezone.getIdTimezone());
	}

	@SuppressWarnings("unchecked")
	public List<Timezone> listTimezones() {
		em.clear();
		Query query = em.createQuery("SELECT t FROM Timezone t");
		List<Timezone> timezones = query.getResultList();
		return timezones;
	}

	@SuppressWarnings("unchecked")
	public List<Timezone> listTimezones(User user) {
		em.clear();
		Query query = em.createQuery("SELECT t FROM Timezone t where t.user = :user");
		query.setParameter("user", user);
		List<Timezone> timezones = query.getResultList();
		return timezones;
	}
	
	public String deleteTimezone(Timezone timezone) {
		EntityTransaction transaction = em.getTransaction();
		transaction.begin();
		em.remove(timezone);
		transaction.commit();

		return "timezone deleted";
	}

	public String updateTimezone(Timezone timezone, String name, String city, Integer hourDiff) {
		
		if (name != null && !name.isEmpty()) {
			timezone.setName(name);
		}
		if (city != null && !city.isEmpty()) {
			timezone.setCity(city);
		}
		if (hourDiff != null) {
			timezone.setHourDiff(hourDiff);
		}

		EntityTransaction transaction = em.getTransaction();
		transaction.begin();
		em.merge(timezone);
		transaction.commit();

		return "Updated";
	}
	
	@SuppressWarnings("unchecked")
	public Timezone getTimezone(Long idTimezone){
		em.clear();
		Query query = em.createQuery("SELECT t FROM Timezone t where t.idTimezone = :idTimezone");
		query.setParameter("idTimezone", idTimezone);
		List<Timezone> timezones = query.getResultList();
		if (timezones.isEmpty()) {
			return null;
		}

		return timezones.get(0);		
	}
}
