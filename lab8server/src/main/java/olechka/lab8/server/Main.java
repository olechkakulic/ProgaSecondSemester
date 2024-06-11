package olechka.lab8.server;

import olechka.lab8.models.StudyGroup;
import olechka.lab8.server.models.User;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class Main {
    public static void main(String[] args) throws Exception {
//        для использования логгера
        System.setProperty("org.jboss.logging.provider", "slf4j");

//        получение реестра сервисов
        StandardServiceRegistry registry = new StandardServiceRegistryBuilder().build();
        try (SessionFactory sessionFactory = new MetadataSources(registry)
//                В данном коде используется метод цепочки вызовов (addAnnotatedClass) для добавления классов сущностей (StudyGroup и User)
//                к метаданным. Эти классы являются POJO (Plain Old Java Objects),
//                которые аннотированы аннотациями Hibernate для указания их связи с таблицами базы данных
//                используется аннотация Enity
                .addAnnotatedClass(StudyGroup.class)
                .addAnnotatedClass(User.class)
                //          теперь сесионфэктори будет знать как соед с бд какие классы мы связали с таблицей и как с ними работать
                .buildMetadata()
                .buildSessionFactory()) {
            try (Server server = new Server(sessionFactory)) {
                server.run();
                server.waitForever();
            }
        }
    }
}
