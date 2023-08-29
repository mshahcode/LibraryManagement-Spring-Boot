package az.library.management;

import az.library.management.client.CashbackClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        ApplicationContext ctx =  SpringApplication.run(Application.class, args);
        CashbackClient cashbackClient = ctx.getBean(CashbackClient.class);
        System.out.println(cashbackClient.getCashBack(10.0));
    }

}
