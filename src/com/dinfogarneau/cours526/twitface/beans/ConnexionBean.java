package com.dinfogarneau.cours526.twitface.beans;

import com.dinfogarneau.cours526.twitface.classes.ConnexionMode;

/**
 * Bean permettant de conserver de l'information sur l'utilisateur connecté sur le site Web.
 * Il y a deux types d'utilisateur : les membres et les administrateurs.
 * @author Stéphane Lapointe
 */
public class ConnexionBean {
	
	// Attributs
	// =========
	
	/**
	 * Nom d'utilisateur.
	 */
	private String nomUtil;
	
	/**
	 * Nom complet.
	 */
	private String nom;
	
	/**
	 * Numéro d'utilisateur.
	 */
	private int noUtil;
	
	/**
	 * Mode de connexion (membre ou administrateur).
	 */
	private ConnexionMode modeConn;

	
	// Constructeur
	// ============
	/**
	 * Initialise le bean de connexion avec des valeurs par défaut.
	 */
	public ConnexionBean() {
		this.nomUtil = null;
		this.nom = null;
		this.noUtil = 0;
		this.modeConn = ConnexionMode.AUCUN;
	}

	
	// Getters et Setters
	// ==================
	
	/**
	 * Retourne le nom d'utilisateur.
	 * @return Le nom d'utilisateur.
	 */
	public String getNomUtil() {
		return this.nomUtil;
	}

	/**
	 * Modifie le nom d'utilisateur.
	 * @param nomUtil Le nouveau nom d'utilisateur.
	 */
	public void setNomUtil(String nomUtil) {
		this.nomUtil = nomUtil;
	}

	/**
	 * Retourne le nom complet.
	 * @return Le nom complet.
	 */
	public String getNom() {
		return this.nom;
	}

	/**
	 * Modifie le nom complet.
	 * @param nom Le nouveau nom complet.
	 */
	public void setNom(String nom) {
		this.nom = nom;
	}

	/**
	 * Retourne le numéro d'utilisateur.
	 * @return Le numéro d'utilisateur.
	 */
	public int getNoUtil() {
		return this.noUtil;
	}

	/**
	 * Modifie le numéro d'utilisateur.
	 * @param noUtil Le nouveau numéro d'utilisateur.
	 */
	public void setNoUtil(int noUtil) {
		this.noUtil = noUtil;
	}

	/**
	 * Retourne le mode de connexion.
	 * @return Le mode de connexion.
	 */
	public ConnexionMode getModeConn() {
		return this.modeConn;
	}

	/**
	 * Modifie le mode de connexion.
	 * @param modeConn Le nouveau mode de connexion.
	 */
	public void setModeConn(ConnexionMode modeConn) {
		this.modeConn = modeConn;
	}

}
