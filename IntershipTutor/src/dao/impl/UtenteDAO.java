package dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import dao.UtenteDAOInterface;
import database.DBConnector;
import model.Utente;
import model.enumeration.TipoUtente;

public class UtenteDAO implements UtenteDAOInterface {

	@Override
	public int insert(Utente utente) {
		String insertQuery = "INSERT INTO utente VALUES (?, ?, ?, ?, ?);";
		PreparedStatement preparedStatement;
        int status = 0;
        
        try (Connection connection = DBConnector.getDatasource().getConnection()) {
            preparedStatement = connection.prepareStatement(insertQuery);

            preparedStatement.setString(1, utente.getCodiceFiscale());
            preparedStatement.setString(2, utente.getEmail());
            preparedStatement.setString(3, utente.getPassword());
            preparedStatement.setString(4, utente.getTelefono());
            preparedStatement.setString(5, utente.getTipoUtente().name());

            status = preparedStatement.executeUpdate();

            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
		
		return status;
	}

	@Override
	public int update(Utente utente) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int delete(Utente utente) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override
	public boolean checkEmailDisponibile(String email) {
		return this.checkCampoDisponibile(Utente.EMAIL, email);
	}

	@Override
	public boolean checkUsernameDisponibile(String username) {
		return this.checkCampoDisponibile(Utente.USERNAME, username);
	}

	@Override
	public boolean checkTelefonoDisponibile(String telefono) {
		return this.checkCampoDisponibile(Utente.TELEFONO, telefono);
	}
	
	private boolean checkCampoDisponibile(String campo, String valore) {
		// campo is set inside the code, 
		// it is not inserted by the user(No risk of SQL injection)
		String query = "SELECT COUNT(*) AS count FROM utente WHERE " + campo + " = ?;";
        PreparedStatement preparedStatement;
        int numeroUtilizzo = 0;
        

        try (Connection conn = DBConnector.getDatasource().getConnection()) {
            preparedStatement = conn.prepareStatement(query);

            preparedStatement.setString(1, valore);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                numeroUtilizzo = resultSet.getInt("count");
            }
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
		return numeroUtilizzo == 0;
	}

	@Override
	public Utente getUtenteByUsernameAndPassword(String username, String password) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Utente getUtenteByCF(String codiceFiscale) {
		String query = "SELECT * FROM utente WHERE codice_fiscale = ?;";
        PreparedStatement preparedStatement;
        Utente utente = null;

        try (Connection connection = DBConnector.getDatasource().getConnection()) {
            preparedStatement = connection.prepareStatement(query);

            preparedStatement.setString(1, codiceFiscale);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                utente = new Utente(
                		resultSet.getString(Utente.CODICE_FISCALE),
                        resultSet.getString(Utente.EMAIL),
                        resultSet.getString(Utente.USERNAME),
                        resultSet.getString(Utente.PASSWORD),
                        resultSet.getString(Utente.TELEFONO),
                        TipoUtente.valueOf(resultSet.getString(Utente.TIPO_UTENTE)));
            }

            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return utente;
	}

	@Override
	public Object getLogged(String username, String password) {
		String queryUtente = "SELECT tipo, codice_fiscale FROM utente WHERE username = ? AND password = ?;";
        PreparedStatement preparedStatement;
        Object utente = null;
        String codiceFiscale = "";
        TipoUtente tipoUtente = null;

        try (Connection connection = DBConnector.getDatasource().getConnection()) {
            preparedStatement = connection.prepareStatement(queryUtente);

            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                codiceFiscale = resultSet.getString(Utente.CODICE_FISCALE);
                tipoUtente = TipoUtente.valueOf(resultSet.getString(Utente.TIPO_UTENTE));
                
                switch(tipoUtente) {
	            	case studente:
	            		utente = new StudenteDAO().getStudenteByCF(codiceFiscale);
	            		break;
	            	case azienda:
	            		utente = new AziendaDAO().getAziendaByCF(codiceFiscale);
	            		break;
	            	case amministratore:
	            		utente = new AmministratoreDAO().getAmministratoreByCF(codiceFiscale);
	           }

            }
            
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return utente;
	}

}
