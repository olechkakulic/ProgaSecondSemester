package olechka.lab7.server;

import olechka.lab7.models.StudyGroup;
import olechka.lab7.server.models.User;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class Main {
    public static void main(String[] args) throws Exception {
//        для использования логгера
        System.setProperty("org.jboss.logging.provider", "slf4j");

        StandardServiceRegistry registry = new StandardServiceRegistryBuilder().build();
        try (SessionFactory sessionFactory = new MetadataSources(registry)
                .addAnnotatedClass(StudyGroup.class)
                .addAnnotatedClass(User.class)
                .buildMetadata()
                .buildSessionFactory()) {
            try (Server server = new Server(sessionFactory)) {
                server.run();
            }
        }
    }
}
