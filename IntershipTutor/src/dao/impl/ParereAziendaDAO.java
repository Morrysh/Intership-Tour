package dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import dao.ParereAziendaDAOInterface;
import data.database.DBConnector;
import data.model.Azienda;
import data.model.ParereAzienda;

public class ParereAziendaDAO implements ParereAziendaDAOInterface {

	@Override
	public int insert(ParereAzienda parereAzienda) {
		String insertQuery = "INSERT INTO parereAzienda VALUES (?, ?, ?);";
		PreparedStatement preparedStatement;
        int status = 0;
        
        try (Connection connection = DBConnector.getDatasource().getConnection()) {
            preparedStatement = connection.prepareStatement(insertQuery);

            preparedStatement.setString(1, parereAzienda.getStudente());
            preparedStatement.setString(2, parereAzienda.getAzienda());
            preparedStatement.setString(3, parereAzienda.getParere());

            status = preparedStatement.executeUpdate();

            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
		
		return status;
	}

	@Override
	public int update(ParereAzienda parereAzienda) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int delete(ParereAzienda parereAzienda) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<String> getPareriAzienda(Azienda azienda) {
		List<String> pareri = new ArrayList<>();
		PreparedStatement preparedStatement;
		String query = "SELECT * FROM parereazienda WHERE azienda = ?;";
		
		try (Connection connection = DBConnector.getDatasource().getConnection()) {
            preparedStatement = connection.prepareStatement(query);

            preparedStatement.setString(1, azienda.getUtente());

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String parere = resultSet.getString(ParereAzienda.PARERE);
                pareri.add(parere);
            }

            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
		
		return pareri;
	}
	
	@Override
	public int getMediaVoto(Azienda azienda){
		
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
            e.printStackTrace();
        }
		
		
		return voto*100/5;
	}
}
