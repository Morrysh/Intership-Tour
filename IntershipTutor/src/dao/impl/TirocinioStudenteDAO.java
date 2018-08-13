package dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import dao.TirocinioStudenteDAOInterface;
import data.database.DBConnector;
import data.model.OffertaTirocinio;
import data.model.Studente;
import data.model.TirocinioStudente;
import data.model.Utente;
import data.model.enumeration.StatoRichiestaTirocinio;
import data.model.enumeration.TipoUtente;
import framework.data.DataLayerException;

public class TirocinioStudenteDAO implements TirocinioStudenteDAOInterface {

	@Override
	public int insert(TirocinioStudente tirocinioStudente) throws DataLayerException {
		String insertQuery = "INSERT INTO tirociniostudente VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
		PreparedStatement preparedStatement;
        int status = 0;
        
        try (Connection connection = DBConnector.getDatasource().getConnection()) {
            preparedStatement = connection.prepareStatement(insertQuery);

            preparedStatement.setString(1, tirocinioStudente.getStudente());
            preparedStatement.setInt(2, tirocinioStudente.getTirocinio());
            preparedStatement.setInt(3, tirocinioStudente.getCfu());
            preparedStatement.setString(4, tirocinioStudente.getTutoreUniversitario());
            preparedStatement.setString(5, tirocinioStudente.getTelefonoTutoreUniversitario());
            preparedStatement.setString(6, tirocinioStudente.getEmailTutoreUniversitario());
            preparedStatement.setString(7, tirocinioStudente.getDescrizioneDettagliata());
            preparedStatement.setInt(8, tirocinioStudente.getOreSvolte());
            preparedStatement.setString(9, tirocinioStudente.getGiudizioFinale());
            preparedStatement.setString(10, tirocinioStudente.getParere());            
            preparedStatement.setString(11, tirocinioStudente.getStato().name());

            status = preparedStatement.executeUpdate();

            connection.close();
        } catch (SQLException e) {
        	throw new DataLayerException("Unable to insert student intership", e);
        }
		
		return status;
	}

	@Override
	public int update(TirocinioStudente tirocinioStudente) throws DataLayerException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int delete(Studente studente) throws DataLayerException {
		String deleteQuery = "DELETE FROM tirociniostudente WHERE studente = ?;";
		PreparedStatement preparedStatement;
		int status = 0;
		
		try (Connection connection = DBConnector.getDatasource().getConnection()) {
		preparedStatement = connection.prepareStatement(deleteQuery);
		
		preparedStatement.setString(1, studente.getCodiceFiscale());
		
		status = preparedStatement.executeUpdate();
		
		connection.close();
		} catch (SQLException e) {
			throw new DataLayerException("Unable to delete student intership", e);
		}
		
		return status;
	}
	
	@Override
	public int updateStato(String codiceFiscale, StatoRichiestaTirocinio statoRichiestaTirocinio) throws DataLayerException {
		String updateQuery = "UPDATE tirociniostudente SET stato = ? WHERE studente = ?;";
		PreparedStatement preparedStatement;
        int status = 0;
        
        try (Connection connection = DBConnector.getDatasource().getConnection()) {
            preparedStatement = connection.prepareStatement(updateQuery);

            preparedStatement.setString(1, statoRichiestaTirocinio.name());
            preparedStatement.setString(2, codiceFiscale);

            status = preparedStatement.executeUpdate();

            connection.close();
        } catch (SQLException e) {
        	throw new DataLayerException("Unable to set state of student intership", e);
        }
		
		return status;
	}
	
	@Override
	public TirocinioStudente getTirocinioStudenteByStudente(Studente studente) throws DataLayerException {
		TirocinioStudente tirocinioStudente = null;
		PreparedStatement preparedStatement;
		String query = "SELECT * FROM tirociniostudente WHERE studente = ?;";
		
		try (Connection connection = DBConnector.getDatasource().getConnection()) {
			preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, studente.getCodiceFiscale());

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
            	tirocinioStudente = new TirocinioStudente(
                		resultSet.getString(TirocinioStudente.STUDENTE),
                		resultSet.getInt(TirocinioStudente.ID_TIROCINIO),
                		resultSet.getInt(TirocinioStudente.CFU),
		 				resultSet.getString(TirocinioStudente.TUTORE_UNIVERSITARIO),
                        resultSet.getString(TirocinioStudente.TELEFONO_TUTORE),
                        resultSet.getString(TirocinioStudente.EMAIL_TUTORE),
                        resultSet.getString(TirocinioStudente.DESCRIZIONE_DETTAGLIATA),
                        resultSet.getInt(TirocinioStudente.ORE_SVOLTE),
                        resultSet.getString(TirocinioStudente.GIUDIZIO_FINALE),
                        resultSet.getString(TirocinioStudente.PARERE),
                        StatoRichiestaTirocinio.valueOf(resultSet.getString(TirocinioStudente.STATO)));
            }

            connection.close();
        } catch (SQLException e) {
        	throw new DataLayerException("Unable to get student intership by student", e);
        }
		
		return tirocinioStudente;
	}
	
	@Override
	public OffertaTirocinio getOffertaTirocinioByStudente(Studente studente) throws DataLayerException {
		OffertaTirocinio offertaTirocinio = null;
		PreparedStatement preparedStatement;
		String query = "SELECT * FROM tirociniostudente JOIN offertatirocinio ON " +
				 	  " tirociniostudente.id_tirocinio = offertatirocinio.id_tirocinio WHERE studente = ?;";
		
		try (Connection connection = DBConnector.getDatasource().getConnection()) {
			preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, studente.getCodiceFiscale());

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
            	offertaTirocinio = new OffertaTirocinio(
                		resultSet.getInt(OffertaTirocinio.ID_TIROCINIO),
                		resultSet.getString(OffertaTirocinio.AZIENDA),
                		resultSet.getString(OffertaTirocinio.TITOLO),
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
            }

            connection.close();
        } catch (SQLException e) {
        	throw new DataLayerException("Unable to get intership by student", e);
        }
		
		return offertaTirocinio;
	}

	@Override
	public List<Studente> getStudentiByOffertaTirocinio(OffertaTirocinio offertaTirocinio) throws DataLayerException {
		List<Studente> studenti = new ArrayList<>();
		PreparedStatement preparedStatement;
		String query = "SELECT * FROM (tirociniostudente JOIN studente ON utente = studente)" + 
					   "JOIN utente ON utente.codice_fiscale=studente.utente WHERE id_tirocinio = ?;";
		
		try (Connection connection = DBConnector.getDatasource().getConnection()) {
            preparedStatement = connection.prepareStatement(query);

            preparedStatement.setInt(1, offertaTirocinio.getIdTirocinio());

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Studente studente = new Studente(
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
                studenti.add(studente);
            }

            connection.close();
        } catch (SQLException e) {
        	throw new DataLayerException("Unable to get students", e);
        }
		
		return studenti;
	}

}
