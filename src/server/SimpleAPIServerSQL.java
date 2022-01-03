package server;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TimeZone;
import java.util.TreeSet;

import global.Agents;
import global.Etat;
import global.Fil;
import global.Groupe;
import global.Message;
import global.MessageNotFoundException;
import global.UserNotFoundException;
import global.Utilisateur;
import global.UtilisateurCampus;

public class SimpleAPIServerSQL implements APIServerSQL {

	private Connection connection;

	public final static String TECHNICIEN_TEXT = "TECHNICIEN";
	public final static String UTILISATEUR_TEXT = "USER";

	private final static SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public SimpleAPIServerSQL(Connection c) {
		formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
		connection = c;
		try {
			Statement stmt = c.createStatement();

			// Creation des tables si elles n'existent pas

			// Les tables de base (utilisateurs, groupes, fils, messages)
			stmt.execute(
					"CREATE TABLE IF NOT EXISTS Utilisateurs (id VARCHAR(50) NOT NULL PRIMARY KEY, prenom VARCHAR(50) NOT NULL, nom VARCHAR(50) NOT NULL, type VARCHAR(50) NOT NULL, password VARCHAR(255) NOT NULL)");
			stmt.execute("CREATE TABLE IF NOT EXISTS Groupes (nomG VARCHAR(50) NOT NULL PRIMARY KEY)");
			stmt.execute(
					"CREATE TABLE IF NOT EXISTS Fils (idF int NOT NULL AUTO_INCREMENT PRIMARY KEY, titre VARCHAR(255) NOT NULL, f_g_id VARCHAR(50), f_u_id VARCHAR(50), FOREIGN KEY (f_g_id) REFERENCES Groupes(nomG) ON DELETE CASCADE, FOREIGN KEY (f_u_id) REFERENCES Utilisateurs(id) ON DELETE CASCADE)");
			stmt.execute(
					"CREATE TABLE IF NOT EXISTS Messages (idM int NOT NULL AUTO_INCREMENT PRIMARY KEY, contenu VARCHAR(255) NOT NULL, date TIMESTAMP NOT NULL, m_u_id VARCHAR(50), m_f_id int, FOREIGN KEY (m_f_id) REFERENCES Fils(idF) ON DELETE CASCADE, FOREIGN KEY (m_u_id) REFERENCES Utilisateurs(id) ON DELETE CASCADE)");

			// Les tables de relation
			stmt.execute(
					"CREATE TABLE IF NOT EXISTS LinkUtilisateurGroupe (lug_u_id VARCHAR(50), lug_g_id VARCHAR(50), PRIMARY KEY (lug_u_id, lug_g_id), FOREIGN KEY (lug_u_id) REFERENCES Utilisateurs(id) ON DELETE CASCADE, FOREIGN KEY (lug_g_id) REFERENCES Groupes(nomG) ON DELETE CASCADE)");
			stmt.execute(
					"CREATE TABLE IF NOT EXISTS Lectures (l_u_id VARCHAR(50), l_m_id int, PRIMARY KEY (l_u_id, l_m_id), state VARCHAR(10) DEFAULT 'NONE', FOREIGN KEY (l_u_id) REFERENCES Utilisateurs(id) ON DELETE CASCADE, FOREIGN KEY (l_m_id) REFERENCES Messages(idM) ON DELETE CASCADE)");
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			return;
		}
	}

	@Override
	public Utilisateur getUtilisateur(String nom, String prenom) {
		Utilisateur returned = null;
		try {
			Statement stmt = connection.createStatement();
			ResultSet rst = stmt.executeQuery("SELECT * FROM Utilisateurs WHERE nom='" + toSQLString(nom)
					+ "' AND prenom='" + toSQLString(prenom) + "'");
			returned = createUtilisateurFromResultSet(rst);
			stmt.close();
		} catch (SQLException | UserNotFoundException e) {
			e.printStackTrace();
		}
		return returned;
	}

	@Override
	public Utilisateur getUtilisateur(String identifiant) {
		Utilisateur returned = null;
		try {
			Statement stmt = connection.createStatement();
			ResultSet rst = stmt.executeQuery("SELECT * FROM Utilisateurs WHERE id='" + toSQLString(identifiant) + "'");
			returned = createUtilisateurFromResultSet(rst);
			stmt.close();
		} catch (SQLException | UserNotFoundException e) {
			e.printStackTrace();
		}
		return returned;
	}

	@Override
	public boolean setUtilisateur(Utilisateur utilisateur) {
		try {
			String type = TECHNICIEN_TEXT;
			if (utilisateur instanceof UtilisateurCampus) {
				type = UTILISATEUR_TEXT;
			}
			Statement stmt = connection.createStatement();
			stmt.execute("DELETE FROM LinkUtilisateurGroupe WHERE lug_u_id = '"
					+ toSQLString(utilisateur.getIdentifiant()) + "'");
			for (Groupe g : utilisateur.getGroupes()) {
				stmt.execute("INSERT INTO LinkUtilisateurGroupe (lug_u_id, lug_g_id) VALUES ('"
						+ toSQLString(utilisateur.getIdentifiant()) + "', '" + toSQLString(g.getNom()) + "')");
			}

			try {
				Utilisateur u = getUtilisateur(utilisateur.getIdentifiant());
				stmt.execute("UPDATE Utilisateurs SET id='" + toSQLString(utilisateur.getIdentifiant()) + "', prenom='"
						+ toSQLString(utilisateur.getPrenom()) + "', nom='" + toSQLString(utilisateur.getNom())
						+ "', password='" + toSQLString(utilisateur.getPassword()) + "', type='" + toSQLString(type)
						+ "' WHERE id='" + toSQLString(utilisateur.getIdentifiant()) + "'");
				stmt.close();
				return true;
			} catch (Exception e) {
				stmt.execute("INSERT INTO Utilisateurs (id, prenom, nom, password, type) VALUES ('"
						+ toSQLString(utilisateur.getIdentifiant()) + "', '" + toSQLString(utilisateur.getPrenom())
						+ "', '" + toSQLString(utilisateur.getNom()) + "', '" + toSQLString(utilisateur.getPassword())
						+ "', '" + toSQLString(type) + "')");
				stmt.close();
				return false;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public Fil getFil(String titre) {
		Fil returned = null;
		try {

			Statement stmt = connection.createStatement();
			ResultSet rst = stmt.executeQuery("SELECT * FROM Fils WHERE titre='" + titre + "'");
			if (rst.next()) {
				returned = new Fil(rst.getString("titre"), getGroupe(rst.getString("f_g_id")),
						getUtilisateur(rst.getString("f_u_id")));

				Statement stmt2 = connection.createStatement();
				ResultSet rst2 = stmt.executeQuery("SELECT * FROM Messages WHERE m_f_id=" + rst.getInt("idF"));
				while (rst2.next()) {
					returned.getMessages().add(new Message(rst2.getString("contenu"), rst2.getTimestamp("date"),
							getUtilisateur(rst2.getString("m_u_id")), returned));
				}
				stmt2.close();
			}
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return returned;
	}

	@Override
	public Fil getFil(int id) {
		Fil returned = null;
		try {

			Statement stmt = connection.createStatement();
			ResultSet rst = stmt.executeQuery("SELECT * FROM Fils WHERE idF=" + id);
			if (rst.next()) {
				returned = new Fil(rst.getString("titre"), getGroupe(rst.getString("f_g_id")),
						getUtilisateur(rst.getString("f_u_id")));

				Statement stmt2 = connection.createStatement();
				ResultSet rst2 = stmt.executeQuery("SELECT * FROM Messages WHERE m_f_id=" + id);
				while (rst2.next()) {
					returned.getMessages().add(new Message(rst2.getString("contenu"), rst2.getTimestamp("date"),
							getUtilisateur(rst2.getString("m_u_id")), returned));
				}

				stmt2.close();
			}
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return returned;
	}

	@Override
	public SortedSet<Fil> getFils(Groupe groupe) {
		SortedSet<Fil> returned = new TreeSet<>();
		try {

			Statement stmt = connection.createStatement();
			ResultSet rst = stmt.executeQuery("SELECT * FROM Fils WHERE f_g_id='" + toSQLString(groupe.getNom()) + "'");
			while (rst.next()) {
				returned.add(getFil(rst.getInt("idF")));
			}
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return returned;
	}

	@Override
	public boolean createFil(Fil f) {
		try {
			Statement stmt = connection.createStatement();
			if (getFil(f.getSujet()) != null) {
				stmt.close();
				return false;
			}
			stmt.execute("INSERT INTO Fils (titre, f_g_id, f_u_id) VALUES ('" + toSQLString(f.getSujet()) + "', '"
					+ toSQLString(f.getGroupe().getNom()) + "', '" + toSQLString(f.getCreateur().getIdentifiant())
					+ "')");
			stmt.close();
			return true;

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public Groupe getGroupe(String nom) {
		Groupe groupe = null;
		try {
			Statement stmt = connection.createStatement();
			ResultSet rst = stmt.executeQuery("SELECT * FROM Groupes WHERE nomG='" + toSQLString(nom) + "'");
			if (rst.next()) {
				groupe = new Groupe(nom);
				Statement stmt2 = connection.createStatement();
				ResultSet rst2 = stmt.executeQuery(
						"SELECT * FROM LinkUtilisateurGroupe WHERE lug_g_id='" + toSQLString(groupe.getNom()) + "'");
				while (rst2.next()) {
					groupe.addUtilisateurs(getUtilisateur(rst2.getString("lug_u_id")));
				}
				stmt2.close();
			}
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return groupe;
	}

	@Override
	public Set<Groupe> getGroupesFromUser(Utilisateur u) {
		Set<Groupe> returned = new HashSet<Groupe>();
		try {
			Statement stmt = connection.createStatement();
			ResultSet rst = stmt.executeQuery(
					"SELECT * FROM LinkUtilisateurGroupe WHERE lug_u_id='" + toSQLString(u.getIdentifiant()) + "'");
			while (rst.next()) {
				returned.add(getGroupe(rst.getString("lug_g_id")));
			}
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return returned;
	}

	@Override
	public boolean createGroupe(Groupe groupe) {
		try {
			if (getGroupe(groupe.getNom()) != null) {
				return false;
			}
			Statement stmt = connection.createStatement();
			stmt.execute("INSERT INTO Groupes (nomG) VALUES ('" + toSQLString(groupe.getNom()) + "')");
			stmt.close();
			return true;

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public void sendMessage(Message m) {
		try {
			if (getFil(m.getFil().getSujet()) != null) {
				createFil(m.getFil());
			}

			Statement stmt = connection.createStatement();
			ResultSet rst = stmt
					.executeQuery("SELECT * FROM Fils WHERE titre='" + toSQLString(m.getFil().getSujet()) + "'");
			int id = -1;
			if (rst.next()) {
				id = rst.getInt("idF");
			}
			stmt.close();
			stmt = connection.createStatement();

			String dateS = formatter.format(m.getDate());
			stmt.execute("INSERT INTO Messages (contenu, date, m_f_id, m_u_id) VALUES ('" + toSQLString(m.getTexte())
					+ "', '" + dateS + "', " + id + ", '" + m.getExpediteur().getIdentifiant() + "')");
			stmt.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void hasSentMessage(Message m, Utilisateur u) {
		try {
			Statement stmt = connection.createStatement();
			ResultSet rst = stmt.executeQuery("SELECT * FROM Messages WHERE contenu='" + toSQLString(m.getTexte())
					+ "' AND m_u_id='" + toSQLString(m.getExpediteur().getIdentifiant()) + "' AND date='"
					+ formatter.format(m.getDate()) + "'");
			int idM = -1;
			if (rst.next()) {
				idM = rst.getInt("idM");
			} else {
				throw new MessageNotFoundException();
			}
			stmt.close();
			stmt = connection.createStatement();
			rst = stmt.executeQuery(
					"SELECT * FROM Lectures WHERE l_u_id='" + toSQLString(u.getIdentifiant()) + "' AND l_m_id=" + idM);
			Statement stmt2 = connection.createStatement();
			if (rst.next()) {

				stmt2.execute("UPDATE Lectures SET state='SENT' WHERE l_u_id='" + toSQLString(u.getIdentifiant())
						+ "' AND l_m_id=" + idM);
			} else {
				stmt2.execute("INSERT INTO Lectures (l_u_id, l_m_id, state) VALUES ('" + toSQLString(u.getIdentifiant())
						+ "', " + idM + ", 'SENT')");
			}
			stmt2.close();
			stmt.close();

		} catch (SQLException | MessageNotFoundException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void hasReadMessage(Message m, Utilisateur u) {
		try {
			Statement stmt = connection.createStatement();
			ResultSet rst = stmt.executeQuery("SELECT * FROM Messages WHERE contenu='" + toSQLString(m.getTexte())
					+ "' AND m_u_id='" + toSQLString(m.getExpediteur().getIdentifiant()) + "' AND date='"
					+ formatter.format(m.getDate()) + "'");
			int idM = -1;
			if (rst.next()) {
				idM = rst.getInt("idM");
			} else {
				throw new MessageNotFoundException();
			}
			stmt.close();
			stmt = connection.createStatement();
			rst = stmt.executeQuery(
					"SELECT * FROM Lectures WHERE l_u_id='" + toSQLString(u.getIdentifiant()) + "' AND l_m_id=" + idM);
			Statement stmt2 = connection.createStatement();
			if (rst.next()) {

				stmt2.execute("UPDATE Lectures SET state='READ' WHERE l_u_id='" + toSQLString(u.getIdentifiant())
						+ "' AND l_m_id=" + idM);
			} else {
				stmt2.execute("INSERT INTO Lectures (l_u_id, l_m_id, state) VALUES ('" + toSQLString(u.getIdentifiant())
						+ "', " + idM + ", 'READ')");
			}
			stmt2.close();
			stmt.close();

		} catch (SQLException | MessageNotFoundException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Etat getMessageState(Message m) {
		Etat etat = Etat.LU;
		try {
			Statement stmt = connection.createStatement();
			ResultSet rst = stmt.executeQuery("SELECT * FROM Messages WHERE contenu='" + toSQLString(m.getTexte())
					+ "' AND m_u_id='" + toSQLString(m.getExpediteur().getIdentifiant()) + "' AND date='"
					+ formatter.format(m.getDate()) + "'");
			int idM = -1;
			if (rst.next()) {
				idM = rst.getInt("idM");
			} else {
				throw new MessageNotFoundException();
			}
			stmt.close();

			Utilisateur[] utilisateurs = getGroupe(m.getFil().getGroupe().getNom()).getUtilisateurs();
			for (Utilisateur u : utilisateurs) {
				stmt = connection.createStatement();
				rst = stmt.executeQuery("SELECT * FROM Lectures WHERE l_u_id='" + toSQLString(u.getIdentifiant())
						+ "' AND l_m_id=" + idM);
				if (rst.next()) {
					if (rst.getString("state").equals("SENT")) {
						etat = Etat.RECU;
					}
				} else {
					etat = Etat.EN_ATTENTE;
					stmt.close();
					break;
				}
				stmt.close();
			}
			return etat;
		} catch (SQLException | MessageNotFoundException e) {
			e.printStackTrace();
		}
		return Etat.EN_ATTENTE;
	}

	// ***************************************
	// ========= UTILS ====================
	// ***************************************

	/**
	 * Permet de cr�er un utilisateur � partir d'un ResultSet
	 * 
	 * @param rst : Le ResultSet en entr�e
	 * @return L'utilisateur
	 **/
	private Utilisateur createUtilisateurFromResultSet(ResultSet rst) throws SQLException, UserNotFoundException {
		if (rst.next()) {
			String type = rst.getString("type");
			if (type.equalsIgnoreCase(TECHNICIEN_TEXT)) {
				return new Agents(rst.getString("nom"), rst.getString("prenom"), rst.getString("id"),
						rst.getString("password"));
			} else {
				return new UtilisateurCampus(rst.getString("nom"), rst.getString("prenom"), rst.getString("id"),
						rst.getString("password"));
			}

		} else {
			throw new UserNotFoundException("Utilisateur introuvable dans la base de donn�es !");
		}
	}

	private String toSQLString(String s) {
		StringBuilder builder = new StringBuilder();
		for (char c : s.toCharArray()) {
			builder.append(c);
			if (c == '\'') {
				builder.append(c);
			}
		}
		return builder.toString();

	}

	@Override
	public void removeUtilisateur(Utilisateur utilisateur) {
		try {
			Statement stmt = connection.createStatement();
			stmt.execute("DELETE FROM Utilisateurs WHERE m_u_id = '" + toSQLString(utilisateur.getIdentifiant()) + "'");

			stmt.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void removeGroupe(Groupe groupe) {
		try {
			Statement stmt = connection.createStatement();
			stmt.execute("DELETE FROM Groupes WHERE nomG = '" + toSQLString(groupe.getNom()) + "'");
			stmt.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

}
