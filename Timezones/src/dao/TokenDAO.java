package dao;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;

import com.sun.jersey.spi.resource.Singleton;

import model.Token;
import model.User;

@Singleton
public class TokenDAO {

	private EntityManager em;

	public TokenDAO() {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("Timezones");
		em = emf.createEntityManager();
	}

	@SuppressWarnings("unchecked")
	public Token insertToken(User user, String generatedToken) {
		em.clear();
		Query query = em.createQuery("SELECT t FROM Token t WHERE t.user = :user");
		query.setParameter("user", user);
		List<Token> tokens = query.getResultList();
		Token token;
		if (tokens.size() > 0) {
			token = tokens.get(0);
		} else {
			token = new Token();
			token.setUser(user);
		}
		token.setToken(generatedToken);
		token.setExpirationTime(calculateTokenExpiration());

		EntityTransaction transaction = em.getTransaction();
		transaction.begin();
		token = em.merge(token);
		transaction.commit();
		
		return token;
	}

	@SuppressWarnings("unchecked")
	public Token getToken (String token) {
		em.clear();
		Query query = em.createQuery("SELECT t FROM Token t WHERE t.token = :token");
		query.setParameter("token", token);
		List<Token> tokens = query.getResultList();
		if(tokens.size() == 0){
			return null;
		}
		else{
			return tokens.get(0);
		}
	}
	
	// set token expiration 1 hour later
	public Date calculateTokenExpiration() {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(Calendar.HOUR_OF_DAY, 1);
		return cal.getTime();
	}
}
