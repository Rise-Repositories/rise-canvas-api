package school.sptech.crudrisecanvas.utils;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.stereotype.Component;
import javax.sql.DataSource;

@Component
public class DatabaseInitialization {

    @Autowired
    DataSource dataSource;
    @PostConstruct
    public void createProcedure() {
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScript(new ClassPathResource("data.sql"));
        populator.setSeparator("$$");
        populator.execute(this.dataSource);

    }
}
