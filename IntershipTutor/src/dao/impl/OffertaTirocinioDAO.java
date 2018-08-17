package dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import dao.OffertaTirocinioDAOInterface;
import data.database.DBConnector;
import data.model.Azienda;
import data.model.OffertaTirocinio;
import data.model.enumeration.CampoRicercaTirocinio;
import framework.data.DataLayerException;

public class OffertaTirocinioDAO implements OffertaTirocinioDAOInterface {

	// The value is a double becouse in the homepage we need a double for the pagination
	// due to Java casting rules
	public static final double OFFERTE_PER_PAGINA = 5.0;
	
	@Override
	public int insert(OffertaTirocinio offertaTirocinio) throws DataLayerException {
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
            preparedStatement.setBoolean(13, true);
            

            status = preparedStatement.executeUpdate();

            connection.close();
        } catch (SQLException e) {
        	throw new DataLayerException("Unable to insert intership", e);
        }
		
		return status;
	}

	@Override
	public int update(OffertaTirocinio offertaTirocinio) throws DataLayerException {
		String updateQuery = "UPDATE offertatirocinio SET titolo = ?, luogo = ?, obiettivi = ?, " + 
							 "modalita = ?, rimborso = ?, data_inizio = ?, data_fine = ?, " + 
							 "ora_inizio = ?, ora_fine = ?, numero_ore = ? WHERE id_tirocinio = ?";
		PreparedStatement preparedStatement;
        int status = 0;
        
        try (Connection connection = DBConnector.getDatasource().getConnection()) {
            preparedStatement = connection.prepareStatement(updateQuery);

            preparedStatement.setString(1, offertaTirocinio.getTitolo());
            preparedStatement.setString(2, offertaTirocinio.getLuogo());
            preparedStatement.setString(3, offertaTirocinio.getObiettivi());
            preparedStatement.setString(4, offertaTirocinio.getModalita());
            preparedStatement.setString(5, offertaTirocinio.getRimborso());
            preparedStatement.setDate(6, offertaTirocinio.getDataInizio());
            preparedStatement.setDate(7, offertaTirocinio.getDataFine());
            preparedStatement.setTime(8, offertaTirocinio.getOraInizio());
            preparedStatement.setTime(9, offertaTirocinio.getOraFine());
            preparedStatement.setInt(10, offertaTirocinio.getNumeroOre());
            preparedStatement.setInt(11, offertaTirocinio.getIdTirocinio());

            status = preparedStatement.executeUpdate();

            connection.close();
        } catch (SQLException e) {
        	throw new DataLayerException("Unable to update intership", e);
        }
		
		return status;
	}

	@Override
	public int delete(int idTirocinio) throws DataLayerException {
		String deleteQuery = "DELETE FROM offertatirocinio WHERE id_tirocinio = ?;";
		PreparedStatement preparedStatement;
		int status = 0;
		
		try (Connection connection = DBConnector.getDatasource().getConnection()) {
		preparedStatement = connection.prepareStatement(deleteQuery);
		
		preparedStatement.setInt(1, idTirocinio);
		
		status = preparedStatement.executeUpdate();
		
		connection.close();
		} catch (SQLException e) {
			throw new DataLayerException("Unable to delete intership", e);
		}
		
		return status;
	}

	@Override
	public int setVisibilita(int idOffertaTirocinio, boolean visibilita) throws DataLayerException {
		String updateQuery = "UPDATE offertatirocinio SET visibile = ? WHERE id_tirocinio = ?;";
		PreparedStatement preparedStatement;
        int status = 0;
        
        try (Connection connection = DBConnector.getDatasource().getConnection()) {
            preparedStatement = connection.prepareStatement(updateQuery);

            preparedStatement.setInt(1, visibilita ? 1 : 0);
            preparedStatement.setInt(2, idOffertaTirocinio);

            status = preparedStatement.executeUpdate();

            connection.close();
        } catch (SQLException e) {
        	throw new DataLayerException("Unable to set visibility of intership", e);
        }
		
		return status;
	}
	
	@Override
	public int getCountAccordingToVisibilita(boolean visibile) throws DataLayerException {
		String insertQuery = "SELECT COUNT(*) AS count FROM offertatirocinio WHERE visibile = ?";
		PreparedStatement preparedStatement;
        int count = 0;
        
        try (Connection connection = DBConnector.getDatasource().getConnection()) {
            preparedStatement = connection.prepareStatement(insertQuery);
            preparedStatement.setInt(1, visibile ? 1 : 0);

            ResultSet resultSet = preparedStatement.executeQuery();
            
            if(resultSet.next()) {
            	count = resultSet.getInt("count");
            }

            connection.close();
        } catch (SQLException e) {
        	throw new DataLayerException("Unable to get count of interships according to visibility", e);
        }
		
		return count;
	}
	
	@Override
	public int getCountAccordingToRicerca(Map<CampoRicercaTirocinio, String> campoRicerca) throws DataLayerException {
		String query = "SELECT COUNT(*) as count FROM offertatirocinio JOIN " +
					   "azienda ON azienda = utente WHERE visibile = 1 AND " + 
				       "UPPER(nome) LIKE UPPER(?) AND " +
				       "UPPER(luogo) LIKE UPPER(?) AND " +
				       "UPPER(obiettivi) LIKE UPPER(?) AND " +
				       "UPPER(modalita) LIKE UPPER(?) AND " +
					   "(ROUND((data_fine - data_inizio)/60) LIKE ? or data_fine IS NULL OR data_inizio IS NULL);";
		PreparedStatement preparedStatement;
        int count = 0;
        
        try (Connection connection = DBConnector.getDatasource().getConnection()) {
            preparedStatement = connection.prepareStatement(query);
        	
        	preparedStatement.setString(1, campoRicerca.get(CampoRicercaTirocinio.azienda) == null ? "%" 
        			: "%" + campoRicerca.get(CampoRicercaTirocinio.azienda) + "%");
        	preparedStatement.setString(2, campoRicerca.get(CampoRicercaTirocinio.luogo) == null ? "%" 
        			: "%" + campoRicerca.get(CampoRicercaTirocinio.luogo) + "%");
        	preparedStatement.setString(3, campoRicerca.get(CampoRicercaTirocinio.obiettivi) == null ? "%" 
        			: "%" + campoRicerca.get(CampoRicercaTirocinio.obiettivi) + "%");
        	preparedStatement.setString(4, campoRicerca.get(CampoRicercaTirocinio.modalita) == null ? "%" 
        			: "%" + campoRicerca.get(CampoRicercaTirocinio.modalita) + "%");
        	preparedStatement.setString(5, campoRicerca.get(CampoRicercaTirocinio.durata) == null ? "%" 
        			:       campoRicerca.get(CampoRicercaTirocinio.durata));

            ResultSet resultSet = preparedStatement.executeQuery();
            
            if(resultSet.next()) {
            	count = resultSet.getInt("count");
            }

            connection.close();
        } catch (SQLException e) {
        	throw new DataLayerException("Unable to get count of filtered interships", e);
        }
		
		return count;
	}
	
	@Override
	public int getCountAccordingToAzienda(Azienda azienda) throws DataLayerException {
		String insertQuery = "SELECT COUNT(*) AS count FROM offertatirocinio WHERE azienda = ?";
		PreparedStatement preparedStatement;
        int count = 0;
        
        try (Connection connection = DBConnector.getDatasource().getConnection()) {
            preparedStatement = connection.prepareStatement(insertQuery);
            preparedStatement.setString(1, azienda.getCodiceFiscale());

            ResultSet resultSet = preparedStatement.executeQuery();
            
            if(resultSet.next()) {
            	count = resultSet.getInt("count");
            }

            connection.close();
        } catch (SQLException e) {
        	throw new DataLayerException("Unable to get intership count according to company", e);
        }
		
		return count;
	}
	
	@Override
	public int getCountAccordingToAziendaAndVisibilita(Azienda azienda, boolean visibile) throws DataLayerException {
		String insertQuery = "SELECT COUNT(*) AS count FROM offertatirocinio WHERE azienda = ? and visibile = ?";
		PreparedStatement preparedStatement;
        int count = 0;
        
        try (Connection connection = DBConnector.getDatasource().getConnection()) {
            preparedStatement = connection.prepareStatement(insertQuery);
            preparedStatement.setString(1, azienda.getCodiceFiscale());
            preparedStatement.setInt(2, visibile ? 1 : 0);

            ResultSet resultSet = preparedStatement.executeQuery();
            
            if(resultSet.next()) {
            	count = resultSet.getInt("count");
            }

            connection.close();
        } catch (SQLException e) {
        	throw new DataLayerException("Unable to get intership count according to company", e);
        }
		
		return count;
	}

	@Override
	public OffertaTirocinio getOffertaByID(int idTirocinio) throws DataLayerException {
		OffertaTirocinio offertaTirocinio = null;
		PreparedStatement preparedStatement;
		String query = "SELECT * FROM offertatirocinio WHERE id_tirocinio = ?;";
		
		try (Connection connection = DBConnector.getDatasource().getConnection()) {
			preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, idTirocinio);

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
        	throw new DataLayerException("Unable to get intership by id", e);
        }
		
		return offertaTirocinio;
	}
	
	@Override
	public OffertaTirocinio getBestOfferta() throws DataLayerException{
		OffertaTirocinio offertaTirocinio = null;
		PreparedStatement preparedStatement;
		String query = "SELECT offertatirocinio.*, COUNT(tirociniostudente.id_tirocinio) as conto "
				+ "FROM offertatirocinio JOIN tirociniostudente "
				+ "ON tirociniostudente.id_tirocinio = offertatirocinio.id_tirocinio "
				+ "GROUP BY offertatirocinio.id_tirocinio "
				+ "ORDER BY conto DESC "
				+ "LIMIT 1;";
		
		try (Connection connection = DBConnector.getDatasource().getConnection()) {
			preparedStatement = connection.prepareStatement(query);

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
        	throw new DataLayerException("Unable to get intership by id", e);
        }
		
		return offertaTirocinio;
	}
	
	@Override
	public List<OffertaTirocinio> allOfferte() throws DataLayerException {
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
        	throw new DataLayerException("Unable to get all interships", e);
        }
		
		return offerteTirocinio;
	}
	
	@Override
	public List<OffertaTirocinio> allOfferteInRangeAccordingToVisibilita(boolean visibile, int paginaCorrente) 
			throws DataLayerException {
		List<OffertaTirocinio> offerteTirocinio = new ArrayList<>();
		PreparedStatement preparedStatement;
		// Il limite di offerte visibili per pagina è 5
		String query = "SELECT * FROM offertatirocinio WHERE visibile = ? LIMIT ?, ?;";
		
		try (Connection connection = DBConnector.getDatasource().getConnection()) {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, visibile ? 1: 0);
            // Se la pagina è la prima si parte da 5, se è la seconda da 10 etc.
            preparedStatement.setInt(2, paginaCorrente * (int) OFFERTE_PER_PAGINA);
            preparedStatement.setInt(3, (int) OFFERTE_PER_PAGINA);

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
        	throw new DataLayerException("Unable to get interships in range", e);
        }
		
		return offerteTirocinio;
	}
	
	@Override
	public List<OffertaTirocinio> allOfferteInRangePerCampo(Map<CampoRicercaTirocinio, String> campoRicerca, int paginaCorrente) throws DataLayerException {
		List<OffertaTirocinio> offerteTirocinio = new ArrayList<>();
		PreparedStatement preparedStatement;
		String query = "SELECT * FROM offertatirocinio JOIN azienda ON azienda = utente WHERE visibile = 1 AND " + 
					   "UPPER(nome) LIKE UPPER(?) AND " +
					   "UPPER(luogo) LIKE UPPER(?) AND " +
					   "UPPER(obiettivi) LIKE UPPER(?) AND " +
					   "UPPER(modalita) LIKE UPPER(?) AND " +
					   "(ROUND((data_fine - data_inizio)/60) LIKE ? or data_fine IS NULL OR data_inizio IS NULL) " +
					   "LIMIT ?, ?;";
		
		try (Connection connection = DBConnector.getDatasource().getConnection()) {
			
			
			preparedStatement = connection.prepareStatement(query);
			
			preparedStatement.setString(1, campoRicerca.get(CampoRicercaTirocinio.azienda) == null ? "%" 
					: "%" + campoRicerca.get(CampoRicercaTirocinio.azienda) + "%");
			preparedStatement.setString(2, campoRicerca.get(CampoRicercaTirocinio.luogo) == null ? "%" 
					: "%" + campoRicerca.get(CampoRicercaTirocinio.luogo) + "%");
			preparedStatement.setString(3, campoRicerca.get(CampoRicercaTirocinio.obiettivi) == null ? "%" 
					: "%" + campoRicerca.get(CampoRicercaTirocinio.obiettivi) + "%");
			preparedStatement.setString(4, campoRicerca.get(CampoRicercaTirocinio.modalita) == null ? "%" 
					: "%" + campoRicerca.get(CampoRicercaTirocinio.modalita) + "%");
			preparedStatement.setString(5, campoRicerca.get(CampoRicercaTirocinio.durata) == null ? "%" 
					:       campoRicerca.get(CampoRicercaTirocinio.durata));
			preparedStatement.setInt(6, paginaCorrente * (int) OFFERTE_PER_PAGINA);
			preparedStatement.setInt(7, (int) OFFERTE_PER_PAGINA);
			
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
        	throw new DataLayerException("Unable to filter according to fields", e);
        }
		
		return offerteTirocinio;
	}
	
	@Override
	public List<OffertaTirocinio> allOfferteInRangeAccordingToAzienda(Azienda azienda, int paginaCorrente)
			throws DataLayerException {
		List<OffertaTirocinio> offerteTirocinio = new ArrayList<>();
		PreparedStatement preparedStatement;
		String query = "SELECT * FROM offertatirocinio WHERE azienda = ? LIMIT ?, ?;";
		
		try (Connection connection = DBConnector.getDatasource().getConnection()) {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, azienda.getCodiceFiscale());
            preparedStatement.setInt(2, paginaCorrente * (int) OFFERTE_PER_PAGINA);
            preparedStatement.setInt(3, (int) OFFERTE_PER_PAGINA);

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
        	throw new DataLayerException("Unable get all interships according to visibility", e);
        }
		
		return offerteTirocinio;
	}
	
	@Override
	public List<OffertaTirocinio> allOfferteInRangeAccordingToAziendaAndVisibilita(Azienda azienda, boolean visibile, int paginaCorrente) 
			throws DataLayerException {
		List<OffertaTirocinio> offerteTirocinio = new ArrayList<>();
		PreparedStatement preparedStatement;
		String query = "SELECT * FROM offertatirocinio WHERE azienda = ? AND visibile = ? LIMIT ?, ?";
		
		try (Connection connection = DBConnector.getDatasource().getConnection()) {
			preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, azienda.getCodiceFiscale());
            preparedStatement.setInt(2, visibile ? 1 : 0);
            preparedStatement.setInt(3, paginaCorrente * (int) OFFERTE_PER_PAGINA);
            preparedStatement.setInt(4, (int) OFFERTE_PER_PAGINA);

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
        	throw new DataLayerException("Unable to get intership according to company", e);
        }
		
		return offerteTirocinio;
	}

}
