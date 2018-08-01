package dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import dao.AmministratoreDAOInterface;
import data.database.DBConnector;
import data.model.Amministratore;
import data.model.Utente;
import data.model.enumeration.TipoUtente;

public class AmministratoreDAO implements AmministratoreDAOInterface {

	@Override
	public Amministratore getAmministratoreByUtente(Utente utente) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Amministratore getAmministratoreByCF(String codiceFiscale) {
		String query = "SELECT * FROM azienda JOIN utente ON utente = codice_fiscale WHERE utente = ?;";
        PreparedStatement preparedStatement;
        Amministratore amministratore = null;

        try (Connection connection = DBConnector.getDatasource().getConnection()) {
            preparedStatement = connection.prepareStatement(query);

            preparedStatement.setString(1, codiceFiscale);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                amministratore = new Amministratore(
                		resultSet.getString(Utente.CODICE_FISCALE),
                		resultSet.getString(Utente.EMAIL),
                		resultSet.getString(Utente.USERNAME),
                		resultSet.getString(Utente.PASSWORD),
                		resultSet.getString(Utente.TELEFONO),
                		TipoUtente.valueOf(resultSet.getString(Utente.TIPO_UTENTE)),
                		resultSet.getString(Amministratore.UTENTE),
                		resultSet.getString(Amministratore.NOME),
                		resultSet.getString(Amministratore.COGNOME));
            }

            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return amministratore;
	}

}
