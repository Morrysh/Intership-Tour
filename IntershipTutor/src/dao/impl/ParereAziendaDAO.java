package dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import dao.ParereAziendaDAOInterface;
import data.database.DBConnector;
import data.model.Azienda;
import data.model.ParereAzienda;
import data.model.Studente;
import framework.data.DataLayerException;

public class ParereAziendaDAO implements ParereAziendaDAOInterface {

	@Override
	public int insert(ParereAzienda parereAzienda) throws DataLayerException {
		String insertQuery = "INSERT INTO parereAzienda VALUES (?, ?, ?, ?);";
		PreparedStatement preparedStatement;
        int status = 0;
        
        try (Connection connection = DBConnector.getDatasource().getConnection()) {
            preparedStatement = connection.prepareStatement(insertQuery);

            preparedStatement.setString(1, parereAzienda.getStudente());
            preparedStatement.setString(2, parereAzienda.getAzienda());
            preparedStatement.setString(3, parereAzienda.getParere());
            preparedStatement.setInt(4, parereAzienda.getVoto());

            status = preparedStatement.executeUpdate();

            connection.close();
        } catch (SQLException e) {
        	throw new DataLayerException("Unable to insert company review", e);
        }
		
		return status;
	}

	@Override
	public int update(ParereAzienda parereAzienda) throws DataLayerException {
		String updateQuery = "UPDATE parereazienda SET parere = ?, voto = ? WHERE azienda = ? AND studente = ?;";
		PreparedStatement preparedStatement;
        int status = 0;
        
        try (Connection connection = DBConnector.getDatasource().getConnection()) {
            preparedStatement = connection.prepareStatement(updateQuery);

            preparedStatement.setString(1, parereAzienda.getParere());
            preparedStatement.setInt(2, parereAzienda.getVoto());
            preparedStatement.setString(3, parereAzienda.getAzienda());
            preparedStatement.setString(4, parereAzienda.getStudente());

            status = preparedStatement.executeUpdate();

            connection.close();
        } catch (SQLException e) {
        	throw new DataLayerException("Unable to update company review", e);
        }
		
		return status;
	}

	@Override
	public int delete(ParereAzienda parereAzienda) throws DataLayerException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Map<String, String> getPareriAzienda(Azienda azienda) throws DataLayerException {
		Map<String, String> pareri = new HashMap<>();
		PreparedStatement preparedStatement;
		String query = "SELECT * FROM parereazienda JOIN studente ON studente = utente WHERE azienda = ?;";
		
		try (Connection connection = DBConnector.getDatasource().getConnection()) {
            preparedStatement = connection.prepareStatement(query);

            preparedStatement.setString(1, azienda.getUtente());

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
            	String nome = resultSet.getString(Studente.NOME);
            	String cognome = resultSet.getString(Studente.COGNOME);
                String parere = resultSet.getString(ParereAzienda.PARERE);
                pareri.put(nome + " " + cognome, parere);
            }

            connection.close();
        } catch (SQLException e) {
        	throw new DataLayerException("Unable to get reviews", e);
        }
		
		return pareri;
	}
	
	@Override
	public int getMediaVoto(Azienda azienda) throws DataLayerException {
		
		int voto = 0;
		PreparedStatement preparedStatement;
		String query = "SELECT AVG(voto) as voto FROM parereazienda WHERE azienda = ?;";
		
		try (Connection connection = DBConnector.getDatasource().getConnection()) {
            preparedStatement = connection.prepareStatement(query);

            preparedStatement.setString(1, azienda.getCodiceFiscale());

            ResultSet resultSet = preparedStatement.executeQuery();
            
            if(resultSet.next())
            	voto = resultSet.getInt("voto");
            
            connection.close();
        } catch (SQLException e) {
        	throw new DataLayerException("Unable to get rating average", e);
        }
		
		
		return voto*100/5;
	}

	@Override
	public ParereAzienda getParereStudente(Studente studente, Azienda azienda) throws DataLayerException {
		ParereAzienda parereAzienda = null;
		PreparedStatement preparedStatement;
		String query = "SELECT * FROM parereazienda WHERE studente = ? AND azienda = ?;";
		
		try (Connection connection = DBConnector.getDatasource().getConnection()) {
			preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, studente.getCodiceFiscale());
            preparedStatement.setString(2, azienda.getCodiceFiscale());

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
            	parereAzienda = new ParereAzienda(
                		resultSet.getString(ParereAzienda.STUDENTE),
                		resultSet.getString(ParereAzienda.AZIENDA),
		 				resultSet.getString(ParereAzienda.PARERE),
                        resultSet.getInt(ParereAzienda.VOTO));
            }

            connection.close();
        } catch (SQLException e) {
        	throw new DataLayerException("Unable to get company review according to student", e);
        }
		
		return parereAzienda;
	}
}
