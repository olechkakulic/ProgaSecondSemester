package olechka.lab8.server;

import olechka.lab8.server.models.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Optional;

//класс для того чтобы проверять пользователей
public class UserManager {
    private final SessionFactory sessionFactory;
    private final MessageDigest digest;

    public UserManager(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;

        try {
            digest = MessageDigest.getInstance("SHA-384");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public User checkUser(String login, String password) {
        // login = "user; drop table User;"
        // from User where login=user; drop table User;
        try (Session s = sessionFactory.openSession()) {
            Optional<User> userOpt = s.createQuery("from User u where u.login = :login", User.class)
                    .setParameter("login", login)
                    .getResultStream()
                    .findFirst();
            if (userOpt.isEmpty()) {
                return null;
            }
            if (userOpt.get().getPassword().equals(hashPassword(password))) {
                return userOpt.get();
            }
            return null;
        }

    }

    public User register(String login, String password) {
        if (login.isEmpty()) {
            return null;
        }
        try (Session s = sessionFactory.openSession()) {
            Transaction tx = s.beginTransaction();
            User user = new User();
            user.setLogin(login);
            user.setPassword(hashPassword(password));
            s.persist(user);
            tx.commit();
            return user;
        } catch (ConstraintViolationException e) {
            return null;
        }
    }

    private String hashPassword(String password) {
        byte[] hash = digest.digest(password.getBytes());
        return Base64.getEncoder().encodeToString(hash);
    }
}
