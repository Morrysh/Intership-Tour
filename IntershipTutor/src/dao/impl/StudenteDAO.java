package dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import dao.StudenteDAOInterface;
import data.database.DBConnector;
import data.model.Studente;
import data.model.Utente;
import data.model.enumeration.TipoUtente;
import framework.data.DataLayerException;

public class StudenteDAO implements StudenteDAOInterface {

	@Override
	public int insert(Studente studente) throws DataLayerException {
		String insertQuery = "INSERT INTO studente VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
		PreparedStatement preparedStatement;
        int status = 0;
        
        Utente utente = new Utente(
        		studente.getCodiceFiscale(),
        		studente.getUsername(),
        		studente.getEmail(),
        		studente.getPassword(),
        		studente.getTelefono(),
        		studente.getTipoUtente());
        
        new UtenteDAO().insert(utente);
        
        try (Connection connection = DBConnector.getDatasource().getConnection()) {
            preparedStatement = connection.prepareStatement(insertQuery);

            preparedStatement.setString(1, studente.getUtente());
            preparedStatement.setString(2, studente.getNome());
            preparedStatement.setString(3, studente.getCognome());
            preparedStatement.setDate(4, studente.getDataNascita());
            preparedStatement.setString(5, studente.getLuogoNascita());
            preparedStatement.setString(6, studente.getProvinciaNascita());
            preparedStatement.setString(7, studente.getResidenza());
            preparedStatement.setString(8, studente.getProvinciaResidenza());
            preparedStatement.setString(9, studente.getTipoLaurea());
            preparedStatement.setString(10, studente.getCorsoLaurea());
            preparedStatement.setBoolean(11, studente.isHandicap());

            status = preparedStatement.executeUpdate();

            connection.close();
        } catch (SQLException e) {
        	throw new DataLayerException("Unable to insert student", e);
        }
		
		return status;
	}

	@Override
	public int update(Studente studente) throws DataLayerException {
		String updateQuery = "UPDATE studente SET nome = ?, cognome = ?, data_nascita = ?, luogo_nascita = ?, " +
				 			 "provincia_nascita = ?, residenza = ?, provincia_residenza = ?, " + 
				             "tipo_laurea = ?, corso_laurea = ? WHERE utente = ?";
		PreparedStatement preparedStatement;
		int status = 0;
		
		Utente utente = new Utente(
        		studente.getCodiceFiscale(),
        		studente.getUsername(),
        		studente.getEmail(),
        		studente.getPassword(),
        		studente.getTelefono(),
        		studente.getTipoUtente());
        
        new UtenteDAO().update(utente);
		
		try (Connection connection = DBConnector.getDatasource().getConnection()) {
			preparedStatement = connection.prepareStatement(updateQuery);
			
			preparedStatement.setString(1, studente.getNome());
			preparedStatement.setString(2, studente.getCognome());
			preparedStatement.setDate(3, studente.getDataNascita());
			preparedStatement.setString(4, studente.getLuogoNascita());
			preparedStatement.setString(5, studente.getProvinciaNascita());
			preparedStatement.setString(6, studente.getResidenza());
			preparedStatement.setString(7, studente.getProvinciaResidenza());
			preparedStatement.setString(8, studente.getTipoLaurea());
			preparedStatement.setString(9, studente.getCorsoLaurea());
			preparedStatement.setString(10, studente.getCodiceFiscale());
			
			status = preparedStatement.executeUpdate();
			
			connection.close();
		
		} catch (SQLException e) {
			throw new DataLayerException("Unable to update student", e);
		}
		
		return status;
	}

	@Override
	public int delete(Studente studente) throws DataLayerException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Studente getStudenteByCF(String codiceFiscale) throws DataLayerException {
		String query = "SELECT * FROM studente JOIN utente ON utente = codice_fiscale WHERE utente = ?;";
        PreparedStatement preparedStatement;
        Studente studente = null;

        try (Connection connection = DBConnector.getDatasource().getConnection()) {
            preparedStatement = connection.prepareStatement(query);

            preparedStatement.setString(1, codiceFiscale);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                studente = new Studente(
                		resultSet.getString(Utente.CODICE_FISCALE),
                		resultSet.getString(Utente.EMAIL),
                		resultSet.getString(Utente.USERNAME),
                		resultSet.getString(Utente.PASSWORD),
                		resultSet.getString(Utente.TELEFONO),
                		TipoUtente.valueOf(resultSet.getString(Utente.TIPO_UTENTE)),
                		resultSet.getString(Studente.UTENTE),
						resultSet.getString(Studente.NOME),
                        resultSet.getString(Studente.COGNOME),
                        resultSet.getDate(Studente.DATA_NASCITA),
                        resultSet.getString(Studente.LUOGO_NASCITA),
                        resultSet.getString(Studente.PROVINCIA_NASCITA),
                        resultSet.getString(Studente.RESIDENZA),
                        resultSet.getString(Studente.PROVINCIA_RESIDENZA),
                        resultSet.getString(Studente.TIPO_LAUREA),
                        resultSet.getString(Studente.CORSO_LAUREA),
                        resultSet.getBoolean(Studente.HANDICAP));
            }

            connection.close();
        } catch (SQLException e) {
        	throw new DataLayerException("Unable to get student by CF", e);
        }
        return studente;
	}

}
