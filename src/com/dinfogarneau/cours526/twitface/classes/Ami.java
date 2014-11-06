package com.dinfogarneau.cours526.twitface.classes;

/**
 * Classe permettant de conserver de l'information sur un ami d'un membre ou bien sur un ami suggéré à un membre.
 * @author Stéphane Lapointe
 */
public class Ami {
	
	// Attributs
	// =========

	/**
	 * Nom de l'ami.
	 */
	private String nomAmi;
	
	/**
	 * Numéro de l'ami.
	 */
	private int noAmi;
	
	/**
	 * Ville d'origine de l'ami.
	 */
	private String villeOrigine;
	
	/**
	 * Ville actuelle de l'ami.
	 */
	private String villeActuelle;
	
	/**
	 * Nombre d'amis en commun d'un certain membre avec cet ami qui est suggéré; -1 si inutilisé.
	 */
	private int nbAmisEnCommun;
	
	
	// Constructeurs
	// =============
	/**
	 * Initialise l'ami avec les paramètres spécifiés; utile pour les suggestions d'amis.
	 * @param nomAmi Le nom de l'ami.
	 * @param noAmi Le numéro de l'ami.
	 * @param nbAmisEnCommun Le nombre d'ami en commun avec un certain membre.
	 */
	public Ami(String nomAmi, int noAmi, int nbAmisEnCommun) {
		this.nomAmi = nomAmi;
		this.noAmi = noAmi;
		this.villeOrigine = null;
		this.villeActuelle = null;
		this.nbAmisEnCommun = nbAmisEnCommun;
	}

	/**
	 * Initialise l'ami avec les paramètres spécifiés.
	 * @param nomAmi Le nom de l'ami.
	 * @param noAmi Le numéro de l'ami.
	 * @param villeOrigine Ville d'origine de l'ami.
	 * @param villeActuelle Ville actuelle de l'ami.
	 */
	public Ami(String nomAmi, int noAmi, String villeOrigine, String villeActuelle) {
		this.nomAmi = nomAmi;
		this.noAmi = noAmi;
		this.villeOrigine = villeOrigine;
		this.villeActuelle = villeActuelle;
		this.nbAmisEnCommun = -1;
	}

	
	// Getters et Setters
	// ==================

	/**
	 * Retourne le nom de l'ami.
	 * @return Le nom de l'ami.
	 */
	public String getNomAmi() {
		return this.nomAmi;
	}

	/**
	 * Modifie le nom de l'ami.
	 * @param nomAmi Le nouveau nom de l'ami.
	 */
	public void setNomAmi(String nomAmi) {
		this.nomAmi = nomAmi;
	}

	/**
	 * Retourne le numéro de l'ami.
	 * @return Le numéro de l'ami.
	 */
	public int getNoAmi() {
		return this.noAmi;
	}

	/**
	 * Modifie le numéro de l'ami.
	 * @param noAmi Le nouveau numéro de l'ami.
	 */
	public void setNoAmi(int noAmi) {
		this.noAmi = noAmi;
	}

	/**
	 * Retourne la ville d'origine de l'ami.
	 * @return La ville d'origine de l'ami.
	 */
	public String getVilleOrigine() {
		return this.villeOrigine;
	}

	/**
	 * Modifie la ville d'origine de l'ami.
	 * @param villeOrigine La nouvelle ville d'origine de l'ami.
	 */
	public void setVilleOrigine(String villeOrigine) {
		this.villeOrigine = villeOrigine;
	}

	/**
	 * Retourne la ville actuelle de l'ami.
	 * @return La ville actuelle de l'ami.
	 */
	public String getVilleActuelle() {
		return this.villeActuelle;
	}

	/**
	 * Modifie la ville actuelle de l'ami.
	 * @param villeActuelle La nouvelle ville actuelle de l'ami.
	 */
	public void setVilleActuelle(String villeActuelle) {
		this.villeActuelle = villeActuelle;
	}

	/**
	 * Retourne le nombre d'amis en commun d'un certain membre avec cet ami qui est suggéré.
	 * @return Le nombre d'amis en commun.
	 */
	public int getNbAmisEnCommun() {
		return this.nbAmisEnCommun;
	}

	/**
	 * Modifie le nombre d'amis en commun d'un certain membre avec cet ami qui est suggéré.
	 * @param nbAmisEnCommun Le nouveau nombre d'amis en commun.
	 */
	public void setNbAmisEnCommun(int nbAmisEnCommun) {
		this.nbAmisEnCommun = nbAmisEnCommun;
	}

}
