package co.edu.uco.easy.victusresidencias.victus_api.dao.impl.postgresql;

import java.sql.Connection;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import jakarta.annotation.PostConstruct;
import co.edu.uco.easy.victusresidencias.victus_api.crosscutting.helpers.SqlConnectionHelper;
import co.edu.uco.easy.victusresidencias.victus_api.dao.*;

@Component
public final class PostgreSqlDAOFactory extends DAOFactory {

    private Connection connection;

    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.username}")
    private String user;

    @Value("${spring.datasource.password:NOT_FOUND}")
    private String password;

    public PostgreSqlDAOFactory(Environment environment) {
        super(environment);
    }

    @PostConstruct
    private void init() {
        System.out.println("🔍 Verificando variables inyectadas desde Vault:");
        System.out.println("URL: " + url);
        System.out.println("Usuario: " + user);
        System.out.println("Password: " + (password.equals("NOT_FOUND") ? "❌ NO ENCONTRADA" : "********"));

        openConnection();
    }

    @Override
    protected void openConnection() {
        SqlConnectionHelper.validateIfConnectionIsOpen(connection);
        connection = SqlConnectionHelper.openConnectionPostgreSQL(url, user, password);
        System.out.println("✅ Conectado correctamente a la base de datos PostgreSQL (credenciales Vault)");
    }

    @Override
    public void initTransaction() {
        SqlConnectionHelper.initTransaction(connection);
    }

    @Override
    public void commitTransaction() {
        SqlConnectionHelper.commitTransaction(connection);
    }

    @Override
    public void rollbackTransaction() {
        SqlConnectionHelper.rollbackTransaction(connection);
    }

    @Override
    public void closeConnection() {
        SqlConnectionHelper.closeConnection(connection);
        System.out.println("🔒 Desconectado de la base de datos");
    }

    @Override
    public AdministratorDAO getAdministratorDAO() {
        return new AdministratorPostgreSQLDAO(connection);
    }
}
