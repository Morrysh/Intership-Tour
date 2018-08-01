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

public class StudenteDAO implements StudenteDAOInterface {

	@Override
	public int insert(Studente studente) {
		String insertQuery = "INSERT INTO studente VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
		PreparedStatement preparedStatement;
        int status = 0;
        
        try (Connection connection = DBConnector.getDatasource().getConnection()) {
            preparedStatement = connection.prepareStatement(insertQuery);

            preparedStatement.setString(1, studente.getUtente());
            preparedStatement.setString(3, studente.getNome());
            preparedStatement.setString(4, studente.getCognome());
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
            e.printStackTrace();
        }
		
		return status;
	}

	@Override
	public int update(Studente studente) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int delete(Studente studente) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Studente getStudenteByCF(String codiceFiscale) {
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
            e.printStackTrace();
        }
        return studente;
	}

}
