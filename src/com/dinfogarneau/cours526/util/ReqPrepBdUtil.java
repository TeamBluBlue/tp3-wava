package com.dinfogarneau.cours526.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.naming.NamingException;

/**
 * Permet d'encapsuler les opérations sur une base de données
 * à l'aide de requêtes préparées et d'une source de données (JDNI).
 * @author Stéphane Lapointe
 */
public class ReqPrepBdUtil {
	
	/**
	 * Nom de la source de données.
	 */
	protected String nomDataSource;
	
	/**
	 * Connexion à la source de données.
	 */
	protected Connection conn;
	
	/**
	 * Représente la dernière requête qui a été préparée.
	 */
	protected PreparedStatement ps;
	
	/**
	 * Représente la dernière requête SQL sous forme de chaîne qui a été préparée.
	 */
	protected String sql;
	
	/**
	 * Jeu de résultats associé à la dernière requête qui a été exécutée.
	 */
	protected ResultSet rs;

	/**
	 * Permet d'initialiser la source de données.
	 * @param nomDataSource Nom de la source de données.
	 */
	public ReqPrepBdUtil(String nomDataSource) {
		this.nomDataSource = nomDataSource;
		this.conn = null;
		this.ps = null;
		this.rs = null;
	}
	
	/**
	 * Permet d'ouvrir la connexion à la source de données.
	 * @throws NamingException S'il est impossible de trouver la source de données.
	 * @throws SQLException S'il est impossible de se connecter à la source de données.
	 */
	public void ouvrirConnexion() throws NamingException, SQLException {
		try {
			javax.naming.Context ic = new javax.naming.InitialContext();
			javax.naming.Context ec = (javax.naming.Context) ic.lookup("java:comp/env");
			javax.sql.DataSource ds = (javax.sql.DataSource) ec.lookup(nomDataSource);
			this.conn = ds.getConnection();
		} catch (javax.naming.NamingException ne) {
			throw new javax.naming.NamingException("Impossible de trouver la source de données : "
					+ nomDataSource + " (" + ne.getMessage() + ")");
		} catch (SQLException se) {
			throw new SQLException(
					"Impossible de se connecter à la source de données : "
								+ nomDataSource + " (" + se.getMessage() + ")", se);
		}
	}
	
	/**
	 * Permet de fermer silencieusement les ressources utilisées.
	 */
	public void fermerConnexion() {
		
		// Fermeture du jeu de résultats, si nécessaire.
		try {
			if (rs != null)
				rs.close();
		} catch (Exception e) {} // Rien à faire.
		
		// Fermeture de la requête préparée, si nécessaire.
		try {
			if (this.ps != null)
				this.ps.close();
		} catch (Exception e) {} // Rien à faire.
		
		// Fermeture de la connexion, si nécessaire.	
		try {
			if (this.conn != null)
				this.conn.close();
		} catch (Exception e) {} // Rien à faire.
		
		this.rs= null;
		this.ps = null;
		this.conn = null;
	}

	/**
	 * Permet de préparer une requête SQL.
	 * @param req Une chaîne représentant la requête à préparer.
	 * @param accesCles Un booléen indiquant si les clés générées doivent être rendues accessibles.
	 * @throws IllegalStateException Si la connexion à la base de données n'a pas été établie.
	 * @throws SQLException S'il est impossible de préparer la requête SQL.
	 */
	public void preparerRequete(String req, boolean accesCles) throws SQLException, IllegalStateException  {
		
		// Vérification de l'existence de la connexion.
		if (this.conn == null) {
			fermerConnexion();
			throw new IllegalStateException("Impossible de préparer la requête car la connexion à la base de données n'a pas été établie");
		}
		
		// Préparation de la requête.
		try {
			this.ps = this.conn.prepareStatement(req, accesCles ? Statement.RETURN_GENERATED_KEYS : Statement.NO_GENERATED_KEYS);
		} catch (SQLException se) {
			fermerConnexion();
			throw new SQLException("Échec lors de la préparation de la requête SQL"
					+ " (" + se.getMessage() + ")", se);
		}
		
		this.sql = req;
	}

	/**
	 * Permet d'exécuter la dernière requête SQL qui a été préparée;
	 * cette requête doit être de type SELECT.
	 * @param params Paramètres à utiliser pour exécuter la requête.
	 * @return Le jeu de résultats correspondant à la requête.
	 * @throws IllegalStateException Si la connexion à la base de données n'a pas été établie ou si la requête n'a pas été préparée.
	 * @throws SQLException S'il est impossible de passer les paramètres à la requête ou de l'exécuter.
	 */
	public ResultSet executerRequeteSelect(Object... params) throws SQLException, IllegalStateException {
		
		// Vérification de l'existence de la connexion.
		if (this.conn == null) {
			fermerConnexion();
			throw new IllegalStateException("Impossible d'exécuter la requête car la connexion à la base de données n'a pas été établie");
		}

		// Vérification de l'existence de la requête préparée.
		if (this.ps == null) {
			fermerConnexion();
			throw new IllegalStateException("Impossible d'exécuter une requête qui n'a pas été préparée");
		}
		
		// Passage des paramètres à la requête préparée.
		try {
			for (int i=0; i < params.length; i++)
				this.ps.setObject(i+1, params[i]);
		} catch (SQLException se) {
			fermerConnexion();
			throw new SQLException("Échec lors du passage des paramètres à la requête SQL"
					+ " (" + se.getMessage() + ")", se);
		}
		
		// Exécution de la requête préparée.
		try {
			this.rs = this.ps.executeQuery();
		} catch (SQLException se) {
			fermerConnexion();
			throw new SQLException("Échec lors de l'exécution de la requête préparée : " + this.sql
					+ " (" + se.getMessage() + ")", se);
		}
		
		return this.rs;
	}
	
	/**
	 * Permet d'exécuter la dernière requête SQL qui a été préparée;
	 * cette requête doit être de type mise à jour (INSERT, UPDATE ou DELETE).
	 * @param params Paramètres à utiliser pour exécuter la requête.
	 * @return Le nombre d'enregistrements affectés par la requête.
	 * @throws IllegalStateException Si la connexion à la base de données n'a pas été établie ou si la requête n'a pas été préparée.
	 * @throws SQLException S'il est impossible de passer les paramètres à la requête ou de l'exécuter.
	 */
	public int executerRequeteMaj(Object... params) throws SQLException, IllegalStateException {
		
		// Vérification de l'existence de la connexion.
		if (this.conn == null) {
			fermerConnexion();
			throw new IllegalStateException("Impossible d'exécuter la requête car la connexion à la base de données n'a pas été établie");
		}
		
		// Vérification de l'existence de la requête préparée.
		if (this.ps == null) {
			fermerConnexion();
			throw new IllegalStateException("Impossible d'exécuter une requête qui n'a pas été préparée");
		}
		
		// Passage des paramètres à la requête préparée.
		try {
			for (int i=0; i < params.length; i++)
				this.ps.setObject(i+1, params[i]);
		} catch (SQLException se) {
			fermerConnexion();
			throw new SQLException("Échec lors du passage des paramètres à la requête SQL"
					+ " (" + se.getMessage() + ")", se);
		}
		
		// Nombre d'enregistrements affectés par la requête.
		int nbEnreg;
		// Exécution de la requête préparée.
		try {
			nbEnreg = this.ps.executeUpdate();
		} catch (SQLException se) {
			fermerConnexion();
			throw new SQLException("Échec lors de l'exécution de la requête préparée : " + this.sql
					+ " (" + se.getMessage() + ")", se);
		}
		
		return nbEnreg;
	}
	
	/**
	 * Permet de récupérer les clés qui ont été générées par la dernière requête exécutée.
	 * @return La liste de clés générées.
	 * @throws SQLException S'il est impossible de récupérer les clés.
	 * @throws IllegalStateException Si la connexion à la base de données n'a pas été établie ou si la requête n'a pas été préparée.
	 */
	public List<Long> obtenirClesGenerees() throws SQLException, IllegalStateException {
		
		// Vérification de l'existence de la connexion.
		if (this.conn == null) {
			fermerConnexion();
			throw new IllegalStateException("Impossible de récupérer les clés générées car la connexion à la base de données n'a pas été établie");
		}
		
		// Vérification de l'existence de la requête préparée.
		if (this.ps == null) {
			fermerConnexion();
			throw new IllegalStateException("Impossible de récupérer les clés générées car aucun requête n'a été préparée");
		}
		
		// Récupération des clés générées.
		ResultSet rsCles;
		try {
			rsCles = this.ps.getGeneratedKeys();		
		} catch (SQLException se) {
			fermerConnexion();
			throw new SQLException("Échec lors de la récupération des clés générées "
					+ " (" + se.getMessage() + ")", se);
		}
		
		// Création d'un liste de clés qui sera retournée.
		List<Long> listeCles = new ArrayList<Long>();
		while (rsCles.next())
			listeCles.add(rsCles.getLong(1));
		
		// Fermeture du ResultSet.
		rsCles.close();
		
		return listeCles;
	}


}
