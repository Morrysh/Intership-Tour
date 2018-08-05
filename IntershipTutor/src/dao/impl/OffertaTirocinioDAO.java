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
import data.model.OffertaTirocinio;
import data.model.enumeration.CampoRicercaTirocinio;

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
	public int getCountAccordingToConvention(boolean visibile) throws SQLException{
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
            e.printStackTrace();
        }
		
		return count;
	}
	
	@Override
	public int getCountAccordingToRicerca(Map<CampoRicercaTirocinio, String> campoRicerca) throws SQLException {
		String query = "SELECT COUNT(*) as count FROM offertatirocinio JOIN " +
					   "azienda ON azienda = utente WHERE visibile = 1 AND " + 
				       "UPPER(nome) LIKE UPPER(?) AND " +
				       "UPPER(luogo) LIKE UPPER(?) AND " +
				       "UPPER(obiettivi) LIKE UPPER(?) AND " +
				       "UPPER(modalita) LIKE UPPER(?) AND " +
				       "ROUND((data_fine - data_inizio)/60) LIKE ?;";
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
            e.printStackTrace();
        }
		
		return count;
	}

	@Override
	public OffertaTirocinio getOffertaByID(int id){
		OffertaTirocinio offertaTirocinio = null;
		PreparedStatement preparedStatement;
		String query = "SELECT * FROM offertatirocinio WHERE id_tirocinio = ?;";
		
		try (Connection connection = DBConnector.getDatasource().getConnection()) {
			preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, id);

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
            e.printStackTrace();
        }
		
		return offertaTirocinio;
	}
	
	@Override
	public List<OffertaTirocinio> allOfferteInRange(int paginaCorrente) throws SQLException{
		List<OffertaTirocinio> offerteTirocinio = new ArrayList<>();
		PreparedStatement preparedStatement;
		// Il limite di offerte visibili per pagina è 5
		String query = "SELECT * FROM offertatirocinio WHERE visibile = 1 LIMIT ?, 5;";
		
		try (Connection connection = DBConnector.getDatasource().getConnection()) {
            preparedStatement = connection.prepareStatement(query);
            // Se la pagina è la prima si parte da 5, se è la seconda da 10 etc.
            preparedStatement.setInt(1, paginaCorrente * 5);

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
	public List<OffertaTirocinio> filtraPerCampo(Map<CampoRicercaTirocinio, String> campoRicerca, int paginaCorrente) {
		List<OffertaTirocinio> offerteTirocinio = new ArrayList<>();
		PreparedStatement preparedStatement;
		String query = "SELECT * FROM offertatirocinio JOIN azienda ON azienda = utente WHERE visibile = 1 AND " + 
					   "UPPER(nome) LIKE UPPER(?) AND " +
					   "UPPER(luogo) LIKE UPPER(?) AND " +
					   "UPPER(obiettivi) LIKE UPPER(?) AND " +
					   "UPPER(modalita) LIKE UPPER(?) AND " +
					   "ROUND((data_fine - data_inizio)/60) LIKE ? LIMIT ?, 5;";
		
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
			preparedStatement.setInt(6, paginaCorrente * 5);
			
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
