package dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import dao.AziendaDAOInterface;
import data.database.DBConnector;
import data.model.Azienda;
import data.model.Utente;
import data.model.enumeration.TipoUtente;
import framework.data.DataLayerException;

public class AziendaDAO implements AziendaDAOInterface {

	@Override
	public int insert(Azienda azienda) throws DataLayerException {
		String insertQuery = "INSERT INTO azienda VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
		PreparedStatement preparedStatement;
        int status = 0;
        
        Utente utente = new Utente(
        		azienda.getCodiceFiscale(),
        		azienda.getUsername(),
        		azienda.getEmail(),
        		azienda.getPassword(),
        		azienda.getTelefono(),
        		azienda.getTipoUtente());
        
        new UtenteDAO().insert(utente);
        
        try (Connection connectionection = DBConnector.getDatasource().getConnection()) {
            preparedStatement = connectionection.prepareStatement(insertQuery);

            preparedStatement.setString(1, azienda.getUtente());
            preparedStatement.setString(2, azienda.getNome());
            preparedStatement.setString(3, azienda.getRegione());
            preparedStatement.setString(4, azienda.getIndirizzoSedeLegale());
            preparedStatement.setString(5, azienda.getForoCompetente());
            preparedStatement.setString(6, azienda.getNomeRappresentante());
            preparedStatement.setString(7, azienda.getCognomeRappresentante());
            preparedStatement.setString(8, azienda.getNomeResponsabile());
            preparedStatement.setString(9, azienda.getCognomeResponsabile());
            preparedStatement.setBoolean(10, azienda.isConvenzionata());

            status = preparedStatement.executeUpdate();

            connectionection.close();
        } catch (SQLException e) {
        	throw new DataLayerException("Unable to insert company", e);
        }
		
		return status;
	}

	@Override
	public int update(Azienda azienda) throws DataLayerException {
		String updateQuery = "UPDATE azienda SET nome = ?, regione = ?, indirizzo_sede_legale = ?, " +
							 "foro_competente = ?, nome_rappresentante = ?, cognome_rappresentante = ?, " +
							 "nome_responsabile = ?, cognome_responsabile = ? WHERE utente = ?";
		PreparedStatement preparedStatement;
		int status = 0;
		
		Utente utente = new Utente(
			azienda.getCodiceFiscale(),
			azienda.getUsername(),
			azienda.getEmail(),
			azienda.getPassword(),
			azienda.getTelefono(),
			azienda.getTipoUtente());
		
		new UtenteDAO().update(utente);
		
		try (Connection connectionection = DBConnector.getDatasource().getConnection()) {
            preparedStatement = connectionection.prepareStatement(updateQuery);

            preparedStatement.setString(1, azienda.getNome());
            preparedStatement.setString(2, azienda.getRegione());
            preparedStatement.setString(3, azienda.getIndirizzoSedeLegale());
            preparedStatement.setString(4, azienda.getForoCompetente());
            preparedStatement.setString(5, azienda.getNomeRappresentante());
            preparedStatement.setString(6, azienda.getCognomeRappresentante());
            preparedStatement.setString(7, azienda.getNomeResponsabile());
            preparedStatement.setString(8, azienda.getCognomeResponsabile());
            preparedStatement.setString(9, azienda.getUtente());

            status = preparedStatement.executeUpdate();

            connectionection.close();
        } catch (SQLException e) {
        	throw new DataLayerException("Unable to update company", e);
        }
		
		return status;
	}

	@Override
	public boolean setConvenzione(Azienda azienda, boolean convezione) throws DataLayerException  {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Azienda getAziendaByCF(String codiceFiscale) throws DataLayerException {
		String query = "SELECT * FROM azienda JOIN utente ON utente = codice_fiscale WHERE utente = ?;";
        PreparedStatement preparedStatement;
        Azienda azienda = null;

        try (Connection connection = DBConnector.getDatasource().getConnection()) {
            preparedStatement = connection.prepareStatement(query);

            preparedStatement.setString(1, codiceFiscale);

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
        	throw new DataLayerException("Unable to get company by CF", e);
        }
        return azienda;
	}
	
	@Override
	public Azienda getAziendaByIDTirocinio(int id) throws DataLayerException {
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
        	throw new DataLayerException("Unable to get company by intership ID", e);
        }
        return azienda;
	}

	@Override
	public List<Azienda> allAziende() throws DataLayerException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Azienda> allAziendeAccordingToConvention(boolean convenzione) throws DataLayerException {
		List<Azienda> aziende = new ArrayList<>();
		PreparedStatement preparedStatement;
		String query = "SELECT * FROM azienda JOIN utente ON utente = codice_fiscale WHERE convenzionata = ?";
		
		try (Connection connection = DBConnector.getDatasource().getConnection()) {
            preparedStatement = connection.prepareStatement(query);

            preparedStatement.setInt(1, convenzione ? 1 : 0);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Azienda azienda = new Azienda(
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
                aziende.add(azienda);
            }

            connection.close();
        } catch (SQLException e) {
        	throw new DataLayerException("Unable to get all companies according to convention", e);
        }
		
		return aziende;
	}

	@Override
	public Azienda getAziendaByUtente(Utente utente) {
		// TODO Auto-generated method stub
		return null;
	}	
	
}
