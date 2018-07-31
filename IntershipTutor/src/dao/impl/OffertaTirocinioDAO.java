package dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import dao.OffertaTirocinioDAOInterface;
import database.DBConnector;
import model.Azienda;
import model.OffertaTirocinio;
import model.Utente;
import model.enumeration.CampoRicercaTirocinio;
import model.enumeration.TipoUtente;

public class OffertaTirocinioDAO implements OffertaTirocinioDAOInterface {

	@Override
	public int insert(OffertaTirocinio offertaTirocinio) {
		String insertQuery = "INSERT INTO offertatirocinio VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ,?);";
		PreparedStatement preparedStatement;
        int status = 0;
        
        try (Connection connection = DBConnector.getDatasource().getConnection()) {
            preparedStatement = connection.prepareStatement(insertQuery);

            preparedStatement.setInt(1, offertaTirocinio.getIdTirocinio());
            preparedStatement.setString(2, offertaTirocinio.getAzienda());
            preparedStatement.setString(3, offertaTirocinio.getTitolo());
            preparedStatement.setString(4, offertaTirocinio.getLuogo());
            preparedStatement.setString(5, offertaTirocinio.getObiettivi());
            preparedStatement.setString(6, offertaTirocinio.getModalita());
            preparedStatement.setString(7, offertaTirocinio.getRimborso());
            preparedStatement.setDate(8, offertaTirocinio.getDataInizio());
            preparedStatement.setDate(9, offertaTirocinio.getDataFine());
            preparedStatement.setTime(10, offertaTirocinio.getOraInizio());
            preparedStatement.setTime(11, offertaTirocinio.getOraFine());
            preparedStatement.setInt(12, offertaTirocinio.getNumeroOre());
            preparedStatement.setBoolean(13, offertaTirocinio.isVisibile());
            

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
	public OffertaTirocinio getOffertaByID(int id){
		OffertaTirocinio ot = null;
		PreparedStatement preparedStatement;
		String query = "SELECT * FROM offertatirocinio WHERE id_tirocinio = ?;";
		
		try (Connection connection = DBConnector.getDatasource().getConnection()) {
			preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
            	ot = new OffertaTirocinio(
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
            e.printStackTrace();
        }
		
		return ot;
	}
	
	@Override
	public Azienda getAziendaByIDTirocinio(int id){
		String query = "SELECT * FROM azienda JOIN utente ON azienda.utente = utente.codice_fiscale "
				+ "WHERE utente = (SELECT azienda FROM offertatirocinio WHERE id_tirocinio = ?);";
        PreparedStatement preparedStatement;
        Azienda azienda = null;

        try (Connection connection = DBConnector.getDatasource().getConnection()) {
            preparedStatement = connection.prepareStatement(query);

            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                azienda = new Azienda(
                		resultSet.getString(Utente.CODICE_FISCALE),
                		resultSet.getString(Utente.EMAIL),
                		resultSet.getString(Utente.USERNAME),
                		resultSet.getString(Utente.PASSWORD),
                		resultSet.getString(Utente.TELEFONO),
                		TipoUtente.valueOf(resultSet.getString(Utente.TIPO_UTENTE)),
                		resultSet.getString(Azienda.UTENTE),
                		resultSet.getString(Azienda.NOME),
                        resultSet.getString(Azienda.REGIONE),
                        resultSet.getString(Azienda.INDIRIZZO_SEDE_LEGALE),
                        resultSet.getString(Azienda.FORO_COMPETENTE),
                        resultSet.getString(Azienda.NOME_RAPPRESENTANTE),
                        resultSet.getString(Azienda.COGNOME_RAPPRESENTANTE),
                        resultSet.getString(Azienda.NOME_RESPONSABILE),
                        resultSet.getString(Azienda.COGNOME_RESPONSABILE),
                        resultSet.getBoolean(Azienda.CONVENZIONATA));
            }

            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return azienda;
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
                offerteTirocinio.add(offertaTirocinio);
            }

            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
		
		return offerteTirocinio;
	}
	
	@Override
	public List<OffertaTirocinio> allOfferteTirocinioAccordingToVisibilita(boolean visibile) {
		List<OffertaTirocinio> offerteTirocinio = new ArrayList<>();
		PreparedStatement preparedStatement;
		String query = "SELECT * FROM offertatirocinio WHERE visibile = ?;";
		
		try (Connection connection = DBConnector.getDatasource().getConnection()) {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, visibile ? 1 : 0);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                OffertaTirocinio offertaTirocinio = new OffertaTirocinio(
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
                offerteTirocinio.add(offertaTirocinio);
            }

            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
		
		return offerteTirocinio;
	}

	@Override
	// IllegalArgumentException management, not here
	public List<OffertaTirocinio> filtraPerCampo(Map<CampoRicercaTirocinio, String> campoRicerca) {
		List<OffertaTirocinio> offerteTirocinio = new ArrayList<>();
		PreparedStatement preparedStatement;
		String query = "SELECT * FROM offertatirocinio WHERE ";
		
		try (Connection connection = DBConnector.getDatasource().getConnection()) {
			
			int counter = 0;
			
			// Building query according to campoRicerca entries
			for(Map.Entry<CampoRicercaTirocinio, String> entry : campoRicerca.entrySet()) {
				if(counter != 0) query += " AND ";
				switch(entry.getKey()) {
					case durata: 
						query += "ROUND((data_fine - data_inizio)/60) = ?";
				        break;
		
					default:
						query += "UPPER(" + entry.getKey() + ") LIKE UPPER(?)";
				}
				counter++;
			}
			
			preparedStatement = connection.prepareStatement(query);
			
			// Preparing statement according to campoRicerca entries
			counter = 1;
			for(Map.Entry<CampoRicercaTirocinio, String> entry : campoRicerca.entrySet()) {
				switch(entry.getKey()) {
					case durata:
						preparedStatement.setString(counter++, entry.getValue());
						break;

					default:
						preparedStatement.setString(counter++, "%" + entry.getValue() + "%");
				}
			}

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                OffertaTirocinio offertaTirocinio = new OffertaTirocinio(
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
                offerteTirocinio.add(offertaTirocinio);
            }

            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
		
		return offerteTirocinio;
	}
	
	@Override
	public List<OffertaTirocinio> offerteTirocinioByIDAzienda(String utente){
		List<OffertaTirocinio> offerteTirocinio = new ArrayList<>();
		PreparedStatement preparedStatement;
		String query = "SELECT * FROM offertatirocinio where azienda = ?";
		
		try (Connection connection = DBConnector.getDatasource().getConnection()) {
			preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, utente);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                OffertaTirocinio offertaTirocinio = new OffertaTirocinio(
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
                offerteTirocinio.add(offertaTirocinio);
            }

            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
		
		return offerteTirocinio;
	}

}
