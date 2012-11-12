package fr.sewatech.devoxx.vaadin;

import com.vaadin.data.util.sqlcontainer.SQLContainer;
import com.vaadin.data.util.sqlcontainer.connection.SimpleJDBCConnectionPool;
import com.vaadin.data.util.sqlcontainer.query.TableQuery;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServiceSession;
import com.vaadin.ui.*;

import java.io.Serializable;
import java.sql.SQLException;

public class MainView extends CustomComponent {

    public MainView() {
        Layout layout = new VerticalLayout();

        Table table = new Table();
        layout.addComponent(table);

        try {
            SimpleJDBCConnectionPool connectionPool = new SimpleJDBCConnectionPool("org.h2.Driver", "jdbc:h2:~/test", "sa", "");
            TableQuery tableQuery = new TableQuery("Person", connectionPool);
            SQLContainer dataSource = new SQLContainer(tableQuery);
            table.setContainerDataSource(dataSource);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        setCompositionRoot(layout);
    }
}