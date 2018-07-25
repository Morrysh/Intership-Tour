package dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import dao.TirocinioStudenteDAOInterface;
import database.DBConnector;
import model.OffertaTirocinio;
import model.Studente;
import model.TirocinioStudente;

public class TirocinioStudenteDAO implements TirocinioStudenteDAOInterface {

	@Override
	public int insert(TirocinioStudente tirocinioStudente) {
		String insertQuery = "INSERT INTO tirociniostudente VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
		PreparedStatement preparedStatement;
        int status = 0;
        
        try (Connection connection = DBConnector.getDatasource().getConnection()) {
            preparedStatement = connection.prepareStatement(insertQuery);

            preparedStatement.setString(1, tirocinioStudente.getStudente());
            preparedStatement.setInt(2, tirocinioStudente.getTirocinio());
            preparedStatement.setString(3, tirocinioStudente.getParere());
            preparedStatement.setInt(4, tirocinioStudente.getCfu());
            preparedStatement.setString(5, tirocinioStudente.getTutoreUniversitario());
            preparedStatement.setString(6, tirocinioStudente.getEmailTutoreUniversitario());
            preparedStatement.setInt(7, tirocinioStudente.getOreSvolte());
            preparedStatement.setString(8, tirocinioStudente.getDescrizioneDettagliata());
            preparedStatement.setString(9, tirocinioStudente.getGiudizioFinale());
            preparedStatement.setString(10, tirocinioStudente.getStato().name());

            status = preparedStatement.executeUpdate();

            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
		
		return status;
	}

	@Override
	public int update(TirocinioStudente tirocinioStudente) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int delete(TirocinioStudente tirocinioStudente) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<Studente> getStudentiPerTirocinio(OffertaTirocinio offertaTirocinio) {
		List<Studente> studenti = new ArrayList<>();
		PreparedStatement preparedStatement;
		String query = "SELECT * FROM (tirociniostudente JOIN studente ON studente.utente=tirociniostudente.studente)" + 
					   "JOIN utente ON utente.codice_fiscale=studente.utente WHERE tirocinio = ?;";
		
		try (Connection connection = DBConnector.getDatasource().getConnection()) {
            preparedStatement = connection.prepareStatement(query);

            preparedStatement.setInt(1, offertaTirocinio.getIdTirocinio());

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Studente studente = new Studente(
                		resultSet.getString(Studente.UTENTE),
						resultSet.getString(Studente.NOME),
                        resultSet.getString(Studente.COGNOME),
                        resultSet.getDate(Studente.DATA_NASCITA),
                        resultSet.getString(Studente.LUOGO_NASCITA),
                        resultSet.getString(Studente.RESIDENZA),
                        resultSet.getString(Studente.PROVINCIA),
                        resultSet.getString(Studente.TIPO_LAUREA),
                        resultSet.getString(Studente.CORSO_LAUREA),
                        resultSet.getBoolean(Studente.HANDICAP));
                studenti.add(studente);
            }

            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
		
		return studenti;
	}

}
