package fr.sewatech.devoxx.vaadin;

import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.util.sqlcontainer.SQLContainer;
import com.vaadin.data.util.sqlcontainer.connection.SimpleJDBCConnectionPool;
import com.vaadin.data.util.sqlcontainer.query.TableQuery;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServiceSession;
import com.vaadin.ui.*;

import java.io.Serializable;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainView extends CustomComponent {

    private SimpleDateFormat dateFormat =  new SimpleDateFormat("dd/MM/yyyy");

    public MainView() {
        Layout layout = new VerticalLayout();

        Table table = new Table();
        layout.addComponent(table);

        try {
            SimpleJDBCConnectionPool connectionPool = new SimpleJDBCConnectionPool("org.h2.Driver", "jdbc:h2:~/test", "sa", "");
            TableQuery tableQuery = new TableQuery("Person", connectionPool);
            SQLContainer dataSource = new SQLContainer(tableQuery);
            table.setContainerDataSource(dataSource);

            table.setVisibleColumns(new String[] {"LAST_NAME", "FIRST_NAME", "BIRTHDATE", "MAIL"});
            table.setColumnHeader("LAST_NAME", "Nom");
            table.setColumnHeader("FIRST_NAME", "Prénom");
            table.setColumnHeader("BIRTHDATE", "naissance");
            table.setColumnHeader("MAIL", "mail");
            table.addGeneratedColumn("BIRTHDATE", new Table.ColumnGenerator() {
                @Override
                public Object generateCell(Table source, Object itemId, Object columnId) {
                    Item item = source.getItem(itemId);
                    Property<Date> property= item.getItemProperty(columnId);
                    Date date = property.getValue();
                    Label label = new Label(dateFormat.format(date));
                    return label;
                }
            });
            table.addGeneratedColumn("MAIL", new Table.ColumnGenerator() {
                @Override
                public Object generateCell(Table source, Object itemId, Object columnId) {
                    Item item = source.getItem(itemId);
                    Property<String> property= item.getItemProperty(columnId);
                    String value = property.getValue();
                    Link link = new Link(value, new ExternalResource("mailto:" + value));
                    return link;
                }
            });
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        setCompositionRoot(layout);
    }
}