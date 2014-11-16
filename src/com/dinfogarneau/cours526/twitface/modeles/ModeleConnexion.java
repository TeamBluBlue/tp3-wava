package com.dinfogarneau.cours526.twitface.modeles;

import java.sql.ResultSet;
import java.util.ArrayList;
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
	private final String NOM_DATA_SOURCE = "jdbc/twitface";
	
	// Attributs
	// ========
	/**
	 * Utilisateur connecté
	 */
	public ConnexionBean utilisateurConnecte;
	
	public String msgErreur;
	
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
	 */
	public void connecterUtilisateur(String nomUtil, String motDePasse){
		String trimNomUtil = nomUtil.trim();
		String trimMotDePasse = motDePasse.trim();
		ConnexionBean utilisateurTrouve = null;
		ArrayList<ConnexionBean> utilsTrouves = null;
		
		if (trimNomUtil != "" && trimMotDePasse != ""){
			
			// Chercher le membre dans les administrateurs
			utilsTrouves = this.trouverMembre(ConnexionMode.ADMIN, trimNomUtil, trimMotDePasse);
			
			// Arrêter la méthode s'il y a eu une erreur
			if (this.erreurConnexion()){
				return;
			}
			
			if (utilsTrouves.size() > 1){
				this.setMsgErreur("Trop de correspondances ont été trouvées. Vérifier l'état de la base de données.");
			}
			else{
				if (utilsTrouves.size() == 1){
					utilisateurTrouve = utilsTrouves.get(0);
				}
				else{
					// Si un admin n'a pas été trouvé, chercher dans les utilisateurs standards
					utilsTrouves = null;
					
					// Chercher le membre dans les membres
					utilsTrouves = this.trouverMembre(ConnexionMode.MEMBRE, trimNomUtil, trimMotDePasse);
					
					// Arrêter la méthode s'il y a eu une erreur
					if (this.erreurConnexion()){
						return;
					}
					
					if (utilsTrouves.size() > 1){
						this.setMsgErreur("Trop de correspondances ont été trouvées. Vérifier l'état de la base de données.");
					}
					else{
						if (utilsTrouves.size() == 1){
							utilisateurTrouve = utilsTrouves.get(0);
						}
						else{
							this.setMsgErreur("Le nom d'utilisateur ou le mot de passe sont invalides");
						}
					}
				}
			}
			
			if (this.erreurConnexion()){
				return;
			}
		}
		else{
			this.setMsgErreur("Les champs ne doivent pas être vides");
		}
		
		this.utilisateurConnecte = utilisateurTrouve;
	}
	
	/**
	 * 
	 * @param modeConnexion Mode de connexion qui correspond au type de membre voulu
	 * @param nomUtil Nom d'utilisateur recherché
	 * @param motDePasse Mot de passe recherché
	 * @return Un ArrayList de ConnexionBean. Celui-ci de vrait contenir seulement un seul ConnexionBean
	 */
	private ArrayList<ConnexionBean> trouverMembre(ConnexionMode modeConnexion,
			String nomUtil, String motDePasse){
		String reqSqlConnexion;
		String[] champsBd = new String[4];
		
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
		
		try{
			utilBd.ouvrirConnexion();
		}
		catch(NamingException ne){
			this.setMsgErreur(ne.getMessage());
		}
		catch(SQLException se){
			this.setMsgErreur(se.getMessage());
		}
		finally{
			if (this.erreurConnexion()){
				return null;
			}
		}
		
		ArrayList<ConnexionBean> utilsTrouves = new ArrayList<ConnexionBean>();
		
		// Préparer la requête, exécuter la requête et enregistrer les résultats
		// dans un ArrayList de ConnexionBean
		try{
			utilBd.preparerRequete(reqSqlConnexion, false);
			ResultSet rs = utilBd.executerRequeteSelect(nomUtil, motDePasse);
			
			while(rs.next()){
				ConnexionBean utilisateur = new ConnexionBean();
				
				ConnexionMode typeMembre;
				if (champsBd[3] == "Admin"){
					typeMembre = ConnexionMode.ADMIN;
				}
				else{
					typeMembre = ConnexionMode.MEMBRE;
				}
				
				utilisateur.setNoUtil(rs.getShort(champsBd[0]));
				utilisateur.setNom(rs.getString(champsBd[1]));
				utilisateur.setNomUtil(rs.getString(champsBd[2]));
				utilisateur.setModeConn(typeMembre);
				
				utilsTrouves.add(utilisateur);
			}
			rs.close();
		}
		catch(IllegalStateException ise){
			this.setMsgErreur(ise.getMessage());
		}
		catch(SQLException se){
			this.setMsgErreur(se.getMessage());
		}
		finally{
			if (this.erreurConnexion()){
				return null;
			}
		}
		
		return utilsTrouves;
	}
	
	/**
	 * Vérifie si la connexion est en état d'erreur
	 * @return true si une erreur de connexion s'est produite, sinon false
	 */
	public Boolean erreurConnexion(){
		if (this.getMsgErreur().trim() != null){
			return true;
		}
		else{
			return false;
		}
	}
}
