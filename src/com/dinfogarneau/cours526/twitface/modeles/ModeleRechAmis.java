package com.dinfogarneau.cours526.twitface.modeles;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.naming.NamingException;

import com.dinfogarneau.cours526.twitface.classes.Ami;
import com.dinfogarneau.cours526.util.ReqPrepBdUtil;

/**
 * Modèle permettant de rechercher des amis
 * @author Éric Bonin
 * @author Charles-André Beaudry
 */
public class ModeleRechAmis {
	
	// Constantes
	// ==========
	/**
	 * Constante représentant le DataSource utilisé
	 */
	private static final String NOM_DATA_SOURCE = "jdbc/twitface";
	
	// Attributs
	// =========

	// Pour la recherche d'amis
	// ---------------------------
	
	/**
	 * Liste des amis recherchés.
	 */
	private ArrayList<Ami> lstRechAmis;

	// Constructeur
	// ============
	/**
	 * Initialise les attributs du modèle de gestion d'amis.
	 */
	public ModeleRechAmis() {
		this.lstRechAmis = null;
	}

	
	// Getters et Setters
	// ==================

	/**
	 * Retourne la liste de suggestions d'amis.
	 * @return La liste de suggestions d'amis.
	 */
	public ArrayList<Ami> getLstRechAmis() {
		return this.lstRechAmis;
	}

	/**
	 * Modifie la liste de suggestions d'amis.
	 * @param lstRechAmis La nouvelle liste de suggestions d'amis.
	 */
	public void setLstRechAmis(ArrayList<Ami> lstRechAmis) {
		this.lstRechAmis = lstRechAmis;
	}

	// Méthodes
	// ========
	
	/**
	 * Permet de rechercher des amis.
	 * @param nomAmi Nom de l'ami à chercher
	 * @param villeOrigine Ville d'origine de l'ami à chercher
	 * @param villeActuelle Ville actuelle de l'ami à chercher
	 * @param sexe Sexe de l'ami à chercher
	 * @param utilConnecte Numéro de l'utilisateur connecté. Valeur négative si utilisateur non connecté.
	 * @throws NamingException
	 * @throws SQLException
	 */
	public void rechercherAmis(String nomAmi, String villeOrigine, String villeActuelle, String sexe, int utilConnecte) throws NamingException, SQLException {
		String trimNomAmi = "";
		String trimVilleOrigine = "";
		String trimVilleActuelle = "";
		String trimSexe = "";
		int noUtil = -1;
		
		ReqPrepBdUtil utilBd = new ReqPrepBdUtil(NOM_DATA_SOURCE);
		
		// Liste des critères à appliquer dans la requête SQL ainsi que ses paramètres
		ArrayList<String> criteresRequete = new ArrayList<String>();
		ArrayList<Object> paramsRequete = new ArrayList<Object>();
		
		// Validation des paramètres reçus et ajouter les critères appropriés
		if (nomAmi != null){
			trimNomAmi = nomAmi.trim();
			
			if (trimNomAmi != ""){
				criteresRequete.add("MemNom LIKE '%?%' ");
				paramsRequete.add(trimNomAmi);
			}
		}
		
		if (villeOrigine != null){
			trimVilleOrigine = villeOrigine.trim();
			
			if (trimVilleOrigine != ""){
				criteresRequete.add("MemVilleOrigine LIKE '%?%' ");
				paramsRequete.add(trimVilleOrigine);
			}
		}
		
		if (villeActuelle != null){
			trimVilleActuelle = villeActuelle.trim();
			
			if (trimVilleActuelle != ""){
				criteresRequete.add("MemVilleActuelle LIKE '%?%' ");
				paramsRequete.add(trimVilleActuelle);
			}
		}
		
		if (sexe != null){
			trimSexe = sexe.trim();
			
			if (trimSexe != ""){
				criteresRequete.add("MemSexe = ? ");
				paramsRequete.add(trimSexe);
			}
		}
		
		if (utilConnecte >= 0){
			noUtil = utilConnecte;
			
			if (noUtil >= 0){
				criteresRequete.add("MemNo NOT IN (SELECT MemNo2 FROM amis "
						+ "WHERE MemNo1 = ?) ");
				criteresRequete.add("MemNo != ? ");
				paramsRequete.add(noUtil);
				paramsRequete.add(noUtil);
			}
		}
		
		// Début de la construction de la requête SQL
		String requeteSQL = "SELECT MemNom, MemNo, MemVilleOrigine, MemVilleActuelle "
				+ "FROM membres ";
		
		// Ajout des critères appropriés dans la requête SQL
		if (criteresRequete.size() > 0){
			requeteSQL += "WHERE ";
			
			// Parcourir la liste des critères trouvés et ajouter les chaînes à la requête
			for (int i = 0; i < criteresRequete.size(); i++){
				if (i == 0){
					requeteSQL += criteresRequete.get(i) + " ";
				}
				else{
					requeteSQL += "AND " + criteresRequete.get(i) + " ";
				}
			}
		}
		
		requeteSQL += "ORDER BY MemNom";
		
		// Commencer à récupérer les amis correspondant aux critères de recherche
		utilBd.ouvrirConnexion();
		utilBd.preparerRequete(requeteSQL, false);
		ResultSet rs = utilBd.executerRequeteSelect(paramsRequete);
		
		// Enregistrer les amis trouvés dans l'ArrayList d'Amis du modèle
		while (rs.next()) {
			// Ajout de l'ami dans la liste
			this.lstRechAmis.add(new Ami(rs.getString("MemNom"), rs.getInt("MemNo"), rs.getString("MemVilleOrigine"), rs.getString("MemVilleActuelle")));
		}
		
		// Fermer le ResultSet et la connexion à la BD
		rs.close();
		utilBd.fermerConnexion();
	}
}
