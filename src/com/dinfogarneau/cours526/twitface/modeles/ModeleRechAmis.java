package com.dinfogarneau.cours526.twitface.modeles;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.naming.NamingException;
import com.dinfogarneau.cours526.twitface.classes.Ami;
import com.dinfogarneau.cours526.util.ReqPrepBdUtil;

/**
 * Modèle permettant rechercher des amis
 * @author Éric Bonin
 * @author Charles-André Beaudry
 */
public class ModeleRechAmis {
		
	// Attributs
	// =========

	// Pour la recherche d'amis
	// ---------------------------
	
	/**
	 * Liste des amis recherchés.
	 */
	private ArrayList<Ami> lstRechAmis;
	
	/**
	 * Le nom (ou une partie) des amis que le jeu de résultats doit contenir pour la recherche.
	 */
	private String nomAmi;
	
	/**
	 * La ville d'origine des amis que le jeu de résultats doit contenir pour la recherche.
	 */
	private String villeOrigine;
	
	/**
	 * Le ville d'origine des amis que le jeu de résultats doit contenir pour la recherche.
	 */
	private String villeActuelle;
	
	/**
	 * Le sexe des amis que le jeu de résultats doit contenir pour la recherche.
	 */
	private String[] sexe;

	// Constructeur
	// ============
	/**
	 * Initialise les attributs du modèle de gestion d'amis.
	 */
	public ModeleRechAmis() {
		this.lstRechAmis = null;
		this.nomAmi = "";
		this.villeOrigine = "";
		this.villeActuelle = "";
		this.sexe = new String[2];
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
	
	/**
	 * @return the nomAmi
	 */
	public String getNomAmi() {
		return nomAmi;
	}


	/**
	 * @param nomAmi the nomAmi to set
	 */
	public void setNomAmi(String nomAmi) {
		this.nomAmi = nomAmi;
	}


	/**
	 * @return the villeOrigine
	 */
	public String getVilleOrigine() {
		return villeOrigine;
	}


	/**
	 * @param villeOrigine the villeOrigine to set
	 */
	public void setVilleOrigine(String villeOrigine) {
		this.villeOrigine = villeOrigine;
	}


	/**
	 * @return the villeActuelle
	 */
	public String getVilleActuelle() {
		return villeActuelle;
	}


	/**
	 * @param villeActuelle the villeActuelle to set
	 */
	public void setVilleActuelle(String villeActuelle) {
		this.villeActuelle = villeActuelle;
	}

	/**
	 * @return the sexe
	 */
	public String[] getSexe() {
		return sexe;
	}


	/**
	 * @param sexe the sexe to set
	 */
	public void setSexe(String[] sexe) {
		this.sexe = sexe;
	}

	// Méthodes
	// ========
	

	/**
	 * Permet de suggérer des amis.
	 * @param noUtil Le numéro de l'utilisateur connecté.
	 * @param indicePremChaine L'indice dans le jeu de résultats du premier ami à retourner pour les suggestions.
	 * @param nbAmisSuggChaine Le nombre d'amis suggérés que le jeu de résultats doit contenir pour les suggestions.
	 * @throws NamingException S'il est impossible de trouver la source de données.
	 * @throws SQLException S'il y a une erreur SQL quelconque.
	 */
	public void rechercherAmis(int noUtil, String indicePremChaine, String nbAmisSuggChaine) throws NamingException, SQLException {

		// Traitement du paramètre donnant l'indice dans le jeu
		// de résultats du premier ami à retourner (l'indice est en base 0).
		// La valeur par défaut est 0.
		int indicePrem = 0;
		// Est-ce que le paramètre est présent ?
		if (indicePremChaine != null) {
			// Est-ce que c'est un entier ?
			try {
				indicePrem = Integer.parseInt(indicePremChaine);
				if (indicePrem < 0) {
					indicePrem = 0;   // On reprend la valeur par défaut, si négatif.
				}
			} catch (NumberFormatException nfe) {
				// Rien à faire; la valeur par défaut sera utilisée.
			}
		}

		// Traitement du paramètre donnant le nombre
		// d'amis suggérés que le jeu de résultats doit contenir.
		// La valeur par défaut est NB_MAX_AMIS_SUGG.
		this.nbAmisSugg = NB_MAX_AMIS_SUGG;
		// Est-ce que le paramètre est présent ?
		if (nbAmisSuggChaine != null) {
			// Est-ce que c'est un entier ?
			try {
				this.nbAmisSugg = Integer.parseInt(nbAmisSuggChaine);
				if (this.nbAmisSugg <= 0) {
					this.nbAmisSugg = NB_MAX_AMIS_SUGG;   // On reprend la valeur par défaut, si 0 ou négatif.
				}
			} catch (NumberFormatException nfe) {
				// Rien à faire; la valeur par défaut sera utilisée.
			}
		}

		// Source de données (JNDI).
		String nomDataSource = "jdbc/twitface";

		// Création de l'objet pour l'accès à la BD.
		ReqPrepBdUtil utilBd = new ReqPrepBdUtil(nomDataSource);

		// Obtention de la connexion à la BD.
		utilBd.ouvrirConnexion();

		// Requête SQL permettant de suggérer des amis en fonction du nombre d'amis en commun (Ayoye !!!).
		String reqSQLSuggAmis =
				"SELECT m.MemNom, aCom.NoAmiPotentiel, COUNT(aCom.AmiEnCommun) AS NbAmisEnCom"
				+ " FROM"
				+ " ( "
				// Liste des amis qui ont des amis en commun avec le membre actuel (partie 1).
				+ " SELECT aPot.MemNo2 AS NoAmiPotentiel, aMem.MemNo1 AS AmiEnCommun "
				+ " FROM"
				+ " ("
				// Les amis du membre actuel. 
				+ " SELECT MemNo1 FROM amis WHERE MemNo2=?"
				+ " UNION "
				+ " SELECT MemNo2 FROM amis WHERE MemNo1=?"
				+ " ) AS aMem"
				+ " INNER JOIN"
				+ " amis aPot"
				+ " ON"
				+ " aMem.MemNo1 = aPot.MemNo1"
				+ " WHERE"
				// Ne doit pas être le membre lui-même.
				+ " aPot.MemNo2 <> ?"
				// Ne doit pas être déjà ami avec le membre.
				+ " AND"
				+ " aPot.MemNo2 NOT IN"
				+ " ("
				+ " SELECT MemNo1 FROM amis WHERE MemNo2=?"
				+ " UNION"
				+ " SELECT MemNo2 FROM amis WHERE MemNo1=?"
				+ " )"
				+ " UNION"
				// Liste des amis qui ont des amis en commun avec le membre actuel (partie 2).
				+ " SELECT aPot.MemNo1 AS NoAmiPotentiel, aMem.MemNo1 AS AmiEnCommun"
				+ " FROM"
				+ " ("
				// Les amis du membre actuel.
				+ " SELECT MemNo1 FROM amis WHERE MemNo2=?"
				+ " UNION"
				+ " SELECT MemNo2 FROM amis WHERE MemNo1=?"
				+ " ) AS aMem"
				+ " INNER JOIN"
				+ " amis aPot"
				+ " ON"
				+ " aMem.MemNo1 = aPot.MemNo2"
				+ " WHERE"
				// Ne doit pas être le membre lui-même.
				+ " aPot.MemNo1 <> ?"
				// Ne doit pas être déjà ami avec le membre.
				+ " AND"
				+ " aPot.MemNo1 NOT IN"
				+ " ("
				+ " SELECT MemNo1 FROM amis WHERE MemNo2=?"
				+ " UNION"
				+ " SELECT MemNo2 FROM amis WHERE MemNo1=?"
				+ " )"
				+ " ) AS aCom"
				// Récupération du nom de l'ami suggéré.
				+ " INNER JOIN"
				+ " membres m"
				+ " ON"
				+ " m.MemNo = aCom.NoAmiPotentiel"
				// Regroupements.
				+ " GROUP BY aCom.NoAmiPotentiel, m.MemNo"
				// Tri en fonction du nombre d'amis en commun (descendant) et du nom de l'ami suggéré.
				+ " ORDER BY NbAmisEnCom DESC, m.MemNom ASC"
				// Limiter la recherche à certaines suggestions seulement (page de résultats)
				+ " LIMIT ?, ?";
	
		// Préparation de la requête SQL.
		utilBd.preparerRequete(reqSQLSuggAmis, false);
		
		// Exécution de la requête tout en lui passant les paramètres pour l'exécution.
		ResultSet rs = utilBd.executerRequeteSelect(
				noUtil, noUtil, noUtil, noUtil, noUtil,
				noUtil, noUtil, noUtil, noUtil, noUtil,
				indicePrem, nbAmisSugg);

		// Création de liste de suggestions d'amis.
		this.lstRechAmis = new ArrayList<Ami>();
		// Objet pour conserver une suggestion d'ami.
		Ami ami;
		// Parcours des amis suggérés.
		while (rs.next()) {
			// Création de l'objet "Ami".
			ami = new Ami(rs.getString("MemNom"), rs.getInt("NoAmiPotentiel"), rs.getInt("NbAmisEnCom"));
			// Ajout de l'ami dans la liste.
			this.lstRechAmis.add(ami);
		}

		// Prochain indice du premier si on désire d'autres suggestions d'amis (les suggestions suivantes).
		this.indicePrem = indicePrem + nbAmisSugg;

		// Fermeture de la connexion à la BD.
		utilBd.fermerConnexion();
		
	}  // Fin de "suggererAmis"
	
	/**
	 * Permet d'accepter une demande d'amitié.
	 * @param noUtilDemChaine Le numéro de l'utilisateur qui a fait la demande d'amitié
	 * @param noUtilRep Le numéro de l'utilisateur connecté.
	 * @throws NamingException S'il est impossible de trouver la source de données.
	 * @throws SQLException S'il y a une erreur SQL quelconque.
	 */
	public void accepterDemande(String noUtilDemChaine, int noUtilRep) throws NamingException, SQLException {

		// Traitement du paramètre donnant le numéro de l'utilisateur qui a fait la demande
		int noUtilDem;
		
		// Est-ce que le paramètre est présent ?
		if (noUtilDemChaine != null) {
			// Est-ce que c'est un entier ?
			try {
				noUtilDem = Integer.parseInt(noUtilDemChaine);
			} catch (NumberFormatException nfe) {
				this.demandeAcceptee = false;
				this.message = "Le numéro de l'utilisateur demandant n'est pas un numéro. (°~°)";
				return;
			}
		// Le paramètre est absent
		} else {
			// La demande n'est pas acceptée
			this.demandeAcceptee = false;
			this.message = "Le numéro de l'utilisateur demandant ne doit pas être nul. ( -_-')";
			return;
		}
		
		// Source de données (JNDI).
		String nomDataSource = "jdbc/twitface";

		// Création de l'objet pour l'accès à la BD.
		ReqPrepBdUtil utilBd = new ReqPrepBdUtil(nomDataSource);

		// Obtention de la connexion à la BD.
		utilBd.ouvrirConnexion();

		// Requête SQL permettant de supprimer la demande d'amitié.
		String reqSQLSupprDem = "DELETE FROM demandes_amis WHERE MemNoDemandeur=? AND MemNoInvite=?";	

		// Préparation de la requête SQL.
		utilBd.preparerRequete(reqSQLSupprDem, false);

		// Exécution de la requête tout en lui passant les paramètres pour l'exécution.
		// La requete retourne le nombre de lignes affectées.
		int nbRangs = utilBd.executerRequeteMaj(noUtilDem, noUtilRep);
		
		// S'il y a au moins un rang qui a été supprimé, la demande existe, et elle peut être acceptée.
		this.demandeAcceptee = nbRangs > 0;
		
		// Si la demande est acceptée
		if (this.demandeAcceptee) {
			String reqSQLRendreAmis = "INSERT INTO amis (MemNo1, MemNo2, DateAmitie) VALUES (?, ?, NOW())";
			
			utilBd.preparerRequete(reqSQLRendreAmis, false);
			
			// La demande a été acceptée si un rang a été ajouté lors de
			// l'exécution de la requête (tout en lui passant les paramètres pour l'exécution).
			this.demandeAcceptee = utilBd.executerRequeteMaj(noUtilDem, noUtilRep) == 1;
			
			// Si la demande est acceptée
			if (this.demandeAcceptee) {
				this.message = "Demande acceptée! Vous avez un nouvel ami \\(^o^)/";
			} else {
				this.message = "Erreur lors de la création de l'amitié (-_-)";
			}
		} else {
			this.message = "La demande d'amitié n'existe pas! (Q_Q)";			
		}
		
		// Fermeture de la connexion à la BD.
		utilBd.fermerConnexion();
	}

}
