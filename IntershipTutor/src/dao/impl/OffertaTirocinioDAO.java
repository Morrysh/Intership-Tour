package dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import dao.OffertaTirocinioDAOInterface;
import database.DBConnector;
import model.OffertaTirocinio;

public class OffertaTirocinioDAO implements OffertaTirocinioDAOInterface {

	@Override
	public int insert(OffertaTirocinio offertaTirocinio) {
		String insertQuery = "INSERT INTO offertatirocinio VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ,?);";
		PreparedStatement preparedStatement;
        int status = 0;
        
        try (Connection connection = DBConnector.getDatasource().getConnection()) {
            preparedStatement = connection.prepareStatement(insertQuery);

            preparedStatement.setInt(1, offertaTirocinio.getIdTirocinio());
            preparedStatement.setString(2, offertaTirocinio.getAzienda());
            preparedStatement.setString(3, offertaTirocinio.getLuogo());
            preparedStatement.setString(4, offertaTirocinio.getObiettivi());
            preparedStatement.setString(5, offertaTirocinio.getModalita());
            preparedStatement.setString(6, offertaTirocinio.getRimborso());
            preparedStatement.setDate(7, offertaTirocinio.getDataInizio());
            preparedStatement.setDate(8, offertaTirocinio.getDataFine());
            preparedStatement.setTime(9, offertaTirocinio.getOraInizio());
            preparedStatement.setTime(10, offertaTirocinio.getOraFine());
            preparedStatement.setInt(11, offertaTirocinio.getNumeroOre());
            preparedStatement.setBoolean(12, offertaTirocinio.isVisibile());
            

            status = preparedStatement.executeUpdate();

            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
		
		return status;
	}

	@Override
	public int update(OffertaTirocinio offertaTirocinio) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int delete(OffertaTirocinio offertaTirocinio) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int setVisibilita(OffertaTirocinio offertaTirocinio, boolean visibilita) {
		String insertQuery = "UPDATE offertatirocinio SET visibile = ? WHERE id_tirocinio = ?;";
		PreparedStatement preparedStatement;
        int status = 0;
        
        try (Connection connection = DBConnector.getDatasource().getConnection()) {
            preparedStatement = connection.prepareStatement(insertQuery);

            preparedStatement.setInt(1, offertaTirocinio.getIdTirocinio());
            preparedStatement.setInt(2, visibilita ? 1 : 0);

            status = preparedStatement.executeUpdate();

            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
		
		return status;
	}

	@Override
	public List<OffertaTirocinio> allOfferteTirocinio() {
		List<OffertaTirocinio> offerteTirocinio = new ArrayList<>();
		PreparedStatement preparedStatement;
		String query = "SELECT * FROM offertatirocinio;";
		
		try (Connection connection = DBConnector.getDatasource().getConnection()) {
            preparedStatement = connection.prepareStatement(query);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                OffertaTirocinio offertaTirocinio = new OffertaTirocinio(
                		resultSet.getInt(OffertaTirocinio.ID_TIROCINIO),
                		resultSet.getString(OffertaTirocinio.AZIENDA),
		 				resultSet.getString(OffertaTirocinio.LUOGO),
                        resultSet.getString(OffertaTirocinio.OBIETTIVI),
                        resultSet.getString(OffertaTirocinio.MODALITA),
                        resultSet.getString(OffertaTirocinio.RIMBORSO),
                        resultSet.getDate(OffertaTirocinio.DATA_INIZIO),
                        resultSet.getDate(OffertaTirocinio.DATA_FINE),
                        resultSet.getTime(OffertaTirocinio.ORA_INIZIO),
                        resultSet.getTime(OffertaTirocinio.ORA_FINE),
                        resultSet.getInt(OffertaTirocinio.NUMERO_ORE),
                        resultSet.getBoolean(OffertaTirocinio.VISIBILE));
            offerteTirocinio.add(offertaTirocinio);
            }

            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
		
		return offerteTirocinio;
	}

}
