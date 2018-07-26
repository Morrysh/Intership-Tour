package dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import dao.UtenteDAOInterface;
import database.DBConnector;
import model.Utente;
import model.enumeration.TipoUtente;

public class UtenteDAO implements UtenteDAOInterface {

	@Override
	public int insert(Utente utente) {
		String insertQuery = "INSERT INTO utente VALUES (?, ?, ?, ?, ?);";
		PreparedStatement preparedStatement;
        int status = 0;
        
        try (Connection connection = DBConnector.getDatasource().getConnection()) {
            preparedStatement = connection.prepareStatement(insertQuery);

            preparedStatement.setString(1, utente.getCodiceFiscale());
            preparedStatement.setString(2, utente.getEmail());
            preparedStatement.setString(3, utente.getPassword());
            preparedStatement.setString(4, utente.getTelefono());
            preparedStatement.setString(5, utente.getTipoUtente().name());

            status = preparedStatement.executeUpdate();

            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
		
		return status;
	}

	@Override
	public int update(Utente utente) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int delete(Utente utente) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Utente getUtenteByUsernameAndPassword(String username, String password) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Utente getUtenteByCF(String codiceFiscale) {
		String query = "SELECT * FROM utente WHERE id = ?;";
        PreparedStatement preparedStatement;
        Utente utente = null;

        try (Connection connection = DBConnector.getDatasource().getConnection()) {
            preparedStatement = connection.prepareStatement(query);

            preparedStatement.setString(1, codiceFiscale);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                utente = new Utente(
                		resultSet.getString(Utente.CODICE_FISCALE),
                        resultSet.getString(Utente.EMAIL),
                        resultSet.getString(Utente.PASSWORD),
                        resultSet.getString(Utente.TELEFONO),
                        TipoUtente.valueOf(resultSet.getString(Utente.TIPO_UTENTE)));
            }

            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return utente;
	}

}
