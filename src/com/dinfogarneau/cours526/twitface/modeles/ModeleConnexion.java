package com.dinfogarneau.cours526.twitface.modeles;

import java.sql.ResultSet;
import java.sql.SQLException;
import javax.naming.NamingException;

import com.dinfogarneau.cours526.twitface.beans.ConnexionBean;
import com.dinfogarneau.cours526.twitface.classes.ConnexionMode;
import com.dinfogarneau.cours526.util.ReqPrepBdUtil;

/**
 * Modèle permettant de gérer la connexion d'un membre
 * @author Éric Bonin
 * @author Charles-André Beaudry
 */
public class ModeleConnexion {
	
	// Constantes
	// ==========
	/**
	 * Constante représentant le DataSource utilisé
	 */
	private static final String NOM_DATA_SOURCE = "jdbc/twitface";
	
	// Attributs
	// ========
	/**
	 * Utilisateur connecté
	 */
	private ConnexionBean utilisateurConnecte;
	
	private String msgErreur;
	
	// Constructeur
	// ============
	/**
	 * Initialise l'utilisateur connecté à null
	 */
	public ModeleConnexion(){
		utilisateurConnecte = null;
		msgErreur = null;
	}
	
	/**
	 * Retourne le Java Bean de l'utilisateur connecté
	 * @return Le Java Bean de l'utilisateur connecté
	 */
	public ConnexionBean getUtilisateurConnecte() {
		return utilisateurConnecte;
	}
	
	/**
	 * Modifie l'utilisateur connecté
	 * @param utilisateurConnecte
	 */
	public void setUtilisateurConnecte(ConnexionBean utilisateurConnecte) {
		this.utilisateurConnecte = utilisateurConnecte;
	}
	
	/**
	 * Retourne le message d'erreur enregistré
	 * @return Le message d'erreur enregistré
	 */
	public String getMsgErreur() {
		return msgErreur;
	}
	
	/**
	 * Modifie le message d'erreur enregistré
	 * @param msgErreur
	 */
	public void setMsgErreur(String msgErreur) {
		this.msgErreur = msgErreur;
	}
	
	// Méthodes
	// ========
	/**
	 * Tente de connecter l'utilisateur
	 * @param nomUtil Nom d'utilisateur utilisé lors de la connexion
	 * @param motDePasse Mot de passe utilisé lors de la connexion
	 * @throws SQLException 
	 * @throws NamingException 
	 */
	public void connecterUtilisateur(String nomUtil, String motDePasse) throws NamingException, SQLException{
		String trimNomUtil = "";
		String trimMotDePasse = "";
		
		if (nomUtil != null && motDePasse != null){
			trimNomUtil = nomUtil.trim();
			trimMotDePasse = motDePasse.trim();
		}
		
		if (trimNomUtil != "" && trimMotDePasse != ""){
			// Chercher l'utilisateur dans la table des membres
			this.setUtilisateurConnecte(this.trouverMembre(ConnexionMode.MEMBRE, trimNomUtil, trimMotDePasse));
			
			// Aller chercher l'utilisateur dans la table des admins
			// si un membre n'a pas été trouvé
			if (this.getUtilisateurConnecte() == null){
				this.setUtilisateurConnecte(this.trouverMembre(ConnexionMode.ADMIN, trimNomUtil, trimMotDePasse));
			}
			
			// Si aucun utilisateur n'a été trouvé après avoir regardé dans les deux tables
			// d'utilisateurs, les informations de connexion ne sont pas valides
			if (this.getUtilisateurConnecte() == null){
				this.setMsgErreur("Les informations de connexion sont incorrectes");
			}
			
		}
		else{
			this.setMsgErreur("Les champs ne doivent pas être vides");
		}
	}
	
	/**
	 * 
	 * @param modeConnexion Mode de connexion qui correspond au type de membre voulu
	 * @param nomUtil Nom d'utilisateur recherché
	 * @param motDePasse Mot de passe recherché
	 * @return Un ArrayList de ConnexionBean. Celui-ci de vrait contenir seulement un seul ConnexionBean
	 * @throws SQLException 
	 * @throws NamingException 
	 */
	private ConnexionBean trouverMembre(ConnexionMode modeConnexion,
			String nomUtil, String motDePasse) throws NamingException, SQLException{
		String reqSqlConnexion;
		String[] champsBd = new String[4];
		ConnexionBean utilisateur = null;
		
		// Définir dans quelle table la recherche se fera
		if (modeConnexion == ConnexionMode.ADMIN){
			reqSqlConnexion = "SELECT AdminNo, AdminNom, AdminNomUtil "
					+ "FROM administrateurs "
					+ "WHERE AdminNomUtil = ? AND AdminMotPasse = SHA2(?, 256)";
			champsBd[0] = "AdminNo";
			champsBd[1] = "AdminNom";
			champsBd[2] = "AdminNomUtil";
			champsBd[3] = "Admin";
		}
		else{
			reqSqlConnexion = "SELECT MemNo, MemNom, MemNomUtil "
					+ "FROM membres "
					+ "WHERE MemNomUtil = ? AND MemMotPasse = SHA2(?, 256)";
			champsBd[0] = "MemNo";
			champsBd[1] = "MemNom";
			champsBd[2] = "MemNomUtil";
			champsBd[3] = "Mem";
		}
		
		ReqPrepBdUtil utilBd = new ReqPrepBdUtil(NOM_DATA_SOURCE);
		
		utilBd.ouvrirConnexion();
		
		// Préparer et exécuter la requête
		utilBd.preparerRequete(reqSqlConnexion, false);
		ResultSet rs = utilBd.executerRequeteSelect(nomUtil, motDePasse);
		
		if (rs.first()){
			ConnexionMode typeMembre;
			utilisateur = new ConnexionBean();
			
			// Trouver le type de connexion de l'utilisateur
			if (champsBd[3] == "Admin"){
				typeMembre = ConnexionMode.ADMIN;
			}
			else{
				typeMembre = ConnexionMode.MEMBRE;
			}
			
			// Récupérer les valeurs trouvées dans un ConnexionBean
			utilisateur.setNoUtil(rs.getShort(champsBd[0]));
			utilisateur.setNom(rs.getString(champsBd[1]));
			utilisateur.setNomUtil(rs.getString(champsBd[2]));
			utilisateur.setModeConn(typeMembre);
		}
		
		rs.close();
		utilBd.fermerConnexion();
		
		return utilisateur;
	}
	
	/**
	 * Vérifie si la connexion est en état d'erreur
	 * @return true si une erreur de connexion s'est produite, sinon false
	 */
	public Boolean erreurConnexion(){
		if (this.getMsgErreur() != null){
			return true;
		}
		else{
			return false;
		}
	}
}
