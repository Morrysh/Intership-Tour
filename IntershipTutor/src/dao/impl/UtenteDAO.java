package dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import dao.UtenteDAOInterface;
import data.database.DBConnector;
import data.model.Utente;
import data.model.enumeration.TipoUtente;
import framework.data.DataLayerException;

public class UtenteDAO implements UtenteDAOInterface {

	@Override
	public int insert(Utente utente) throws DataLayerException {
		String insertQuery = "INSERT INTO utente VALUES (?, ?, ?, ?, ?, ?);";
		PreparedStatement preparedStatement;
        int status = 0;
        
        try (Connection connection = DBConnector.getDatasource().getConnection()) {
            preparedStatement = connection.prepareStatement(insertQuery);

            preparedStatement.setString(1, utente.getCodiceFiscale());
            preparedStatement.setString(2, utente.getUsername());
            preparedStatement.setString(3, utente.getEmail());
            preparedStatement.setString(4, utente.getPassword());
            preparedStatement.setString(5, utente.getTelefono());
            preparedStatement.setString(6, utente.getTipoUtente().name());

            status = preparedStatement.executeUpdate();

            connection.close();
        } catch (SQLException e) {
        	throw new DataLayerException("Unable to insert user", e);
        }
		
		return status;
	}

	@Override
	public int update(Utente utente) throws DataLayerException {
		String insertQuery = "UPDATE utente SET username = ?, email = ?, password = ?, telefono = ? " +
							 "WHERE codice_fiscale = ?";
		PreparedStatement preparedStatement;
        int status = 0;
        
        try (Connection connection = DBConnector.getDatasource().getConnection()) {
            preparedStatement = connection.prepareStatement(insertQuery);

            preparedStatement.setString(1, utente.getUsername());
            preparedStatement.setString(2, utente.getEmail());
            preparedStatement.setString(3, utente.getPassword());
            preparedStatement.setString(4, utente.getTelefono());
            preparedStatement.setString(5, utente.getCodiceFiscale());

            status = preparedStatement.executeUpdate();

            connection.close();
        } catch (SQLException e) {
        	throw new DataLayerException("Unable to update user", e);
        }
		
		return status;
	}

	@Override
	public int delete(Utente utente) throws DataLayerException {
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override
	public boolean checkEmailDisponibile(String email) throws DataLayerException {
		return this.checkCampoDisponibile(Utente.EMAIL, email);
	}

	@Override
	public boolean checkUsernameDisponibile(String username) throws DataLayerException {
		return this.checkCampoDisponibile(Utente.USERNAME, username);
	}

	@Override
	public boolean checkTelefonoDisponibile(String telefono) throws DataLayerException {
		return this.checkCampoDisponibile(Utente.TELEFONO, telefono);
	}
	
	private boolean checkCampoDisponibile(String campo, String valore) throws DataLayerException {
		// campo is set inside the code, 
		// it is not inserted by the user(No risk of SQL injection)
		String query = "SELECT COUNT(*) AS count FROM utente WHERE " + campo + " = ?;";
        PreparedStatement preparedStatement;
        int numeroUtilizzo = 0;
        

        try (Connection conn = DBConnector.getDatasource().getConnection()) {
            preparedStatement = conn.prepareStatement(query);

            preparedStatement.setString(1, valore);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                numeroUtilizzo = resultSet.getInt("count");
            }
            conn.close();
        } catch (SQLException e) {
        	throw new DataLayerException("Unable to check field availability", e);
        }
		return numeroUtilizzo == 0;
	}

	@Override
	public Utente getUtenteByUsernameAndPassword(String username, String password) throws DataLayerException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Utente getUtenteByCF(String codiceFiscale) throws DataLayerException {
		String query = "SELECT * FROM utente WHERE codice_fiscale = ?;";
        PreparedStatement preparedStatement;
        Utente utente = null;

        try (Connection connection = DBConnector.getDatasource().getConnection()) {
            preparedStatement = connection.prepareStatement(query);

            preparedStatement.setString(1, codiceFiscale);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                utente = new Utente(
                		resultSet.getString(Utente.CODICE_FISCALE),
                        resultSet.getString(Utente.USERNAME),
                        resultSet.getString(Utente.EMAIL),
                        resultSet.getString(Utente.PASSWORD),
                        resultSet.getString(Utente.TELEFONO),
                        TipoUtente.valueOf(resultSet.getString(Utente.TIPO_UTENTE)));
            }

            connection.close();
        } catch (SQLException e) {
        	throw new DataLayerException("Unable to get user by CF", e);
        }
        return utente;
	}

	@Override
	public Utente getLogged(String username, String password) throws DataLayerException {
		String queryUtente = "SELECT * FROM utente WHERE username = ? AND password = ?;";
        PreparedStatement preparedStatement;
        Utente utente = null;

        try (Connection connection = DBConnector.getDatasource().getConnection()) {
            preparedStatement = connection.prepareStatement(queryUtente);

            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);

            ResultSet resultSet = preparedStatement.executeQuery();
            	if (resultSet.next()) {
            		utente = new Utente(
                		resultSet.getString(Utente.CODICE_FISCALE),
                        resultSet.getString(Utente.USERNAME),
                        resultSet.getString(Utente.EMAIL),
                        resultSet.getString(Utente.PASSWORD),
                        resultSet.getString(Utente.TELEFONO),
                        TipoUtente.valueOf(resultSet.getString(Utente.TIPO_UTENTE)));
            }
            
            connection.close();
        } catch (SQLException e) {
        	throw new DataLayerException("Unable to check user credentials", e);
        }
        return utente;
	}

}
