package com.dinfogarneau.cours526.twitface.controleurs;

import java.io.IOException;
import java.sql.SQLException;

import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dinfogarneau.cours526.twitface.classes.ConnexionMode;
import com.dinfogarneau.cours526.twitface.modeles.ModeleGestionAmis;
import com.dinfogarneau.cours526.twitface.beans.ConnexionBean;

/**
 * Contrôleur pour les ressources des membres.
 * @author Stéphane Lapointe
 * @author VOS NOMS COMPLETS ICI
 */
public class ControleurMembre extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	// Attributs
	// =========
	/**
	 * URI sans le context path.
	 */
	protected String uri;
	
	/**
	 * Vue à afficher (chemin du fichier sur le serveur).
	 */
	protected String vue;
	
	/**
	 * Fragment de la vue (chemin du fichier sur le serveur) à charger
	 * dans la zone de contenu si la vue est créée à partir du gabarit.
	 */
	protected String vueContenu;
	
	/**
	 * Sous-titre de la vue si la vue est créée à partir du gabarit.
	 */
	protected String vueSousTitre;
	
	/**
	 * Permet d'effectuer les traitements avant de gérer la ressource demandée.
	 * @param request La requête HTTP.
	 * @param response La réponse HTTP.
	 * @return true si les opérations se sont bien déroulées; false, autrement.
	 * @throws IOException 
	 */
	protected boolean preTraitement(HttpServletRequest request, HttpServletResponse response) throws IOException {
		// Récupération de l'URI sans le context path.
		this.uri = request.getRequestURI().substring(request.getContextPath().length());
		this.vue = null;
		this.vueContenu = null;
		this.vueSousTitre = null;
		
		// Expiration de la cache pour les pages de cette section.		
		response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");  // HTTP 1.1.
		response.setHeader("Pragma", "no-cache");  // HTTP 1.0.
		response.setDateHeader("Expires", 0);  // Proxies.
		
		// Récupération du ConnexionBean dans la session de l'utilisateur
		ConnexionBean util = (ConnexionBean) request.getSession().getAttribute("connBean");

		// Contrôle d'accès à la section pour les clients.
		if (util.getModeConn() == null || util.getModeConn() != ConnexionMode.MEMBRE) {
			// Non connecté en tant que membre; on retourne une code d'erreur
			// HTTP 401 qui sera intercepté par la page d'erreur "erreur-401.jsp".
			response.sendError(401);
			return false;
		}
		else
			return true;
	}

	/**
	 * Permet de gérer les ressources GET suivantes :
	 * 		"/membre/" ou "/membre"		:	Accueil pour les membres
	 *		"/membre/profil"			:	Profil - Babillard
	 *		"/membre/mes-amis"			:	Amis et mes demandes d'amitié
	 *		"/membre/sugg-ami"			:	Suggérer des amis
	 *		"/membre/accept-dem-ami"	:	Accepter une demande d'amitié
	 *		"/membre/supp-ami"			:	Supprimer un ami
	 *		"/membre/dem-ami"			:	Effectuer une demande d'amitié
	 * 
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		// Opérations pré-traitement et suite des opérations, si nécessaire.
		if (preTraitement(request, response)) {

			// ================================
			// Gestion de la ressource demandée
			// ================================
			
			ConnexionBean util = (ConnexionBean) request.getSession().getAttribute("connBean");
			
			// Accueil - Membres
			// =================
			if (uri.equals("/membre/") || uri.equals("/membre")) {
				// Paramètres pour la vue créée à partir du gabarit.
				vue = "/WEB-INF/vues/gabarit-vues.jsp";
				vueContenu = "/WEB-INF/vues/membre/accueil-membre.jsp";
				
				vueSousTitre = "Page personnelle de " + util.getNom() + " (" + util.getNomUtil() + ")";

			// Profil - Babillard
			// ==================
			} else if (uri.equals("/membre/profil")) {
				// Paramètres pour la vue créée à partir du gabarit.
				vue = "/WEB-INF/vues/gabarit-vues.jsp";
				vueContenu = "/WEB-INF/vues/membre/profil.jsp";
				vueSousTitre = "Mon profil et mon babillard";
				
			// Suggérer des amis
			// =================
			} else if (uri.equals("/membre/sugg-amis")) {

				// Création du modèle pour suggérer des amis.
				ModeleGestionAmis mga = new ModeleGestionAmis();
				// Appel de la méthode du modèle qui suggère des amis.
				// La liste des amis suggérés est conservée dans le modèle.
				try {
					mga.suggererAmis(
							(int) request.getSession().getAttribute("noUtil"),
							request.getParameter("indice-prem"),
							request.getParameter("nb-amis-sugg")
							);
				} catch (NamingException | SQLException e) {
					throw new ServletException(e);
				}

				// Conservation du modèle dans un attribut de la requête.
				request.setAttribute("modSuggAmis", mga);

				// Paramètres pour la vue créée à partir du gabarit.
				vue = "/WEB-INF/vues/gabarit-vues.jsp";
				vueContenu = "/WEB-INF/vues/membre/sugg-amis.jsp";
				vueSousTitre = "Suggestions d'amis";


			// Supprimer un ami
			// ================
			// *** NOTE : Devrait utiliser la méthode POST ***
			} else if (uri.equals("/membre/supp-ami")) {

				// *** En construction ***
				vue = "/WEB-INF/vues/gabarit-vues.jsp";
				vueContenu = "/WEB-INF/vues/en-construction.jsp";
				vueSousTitre = "Suppression d'un ami";

			// Effectuer une demande d'amitié
			// ==============================
			// *** NOTE : Devrait utiliser la méthode POST ***
			} else if (uri.equals("/membre/dem-ami")) {

				// *** En construction ***
				vue = "/WEB-INF/vues/gabarit-vues.jsp";
				vueContenu = "/WEB-INF/vues/en-construction.jsp";
				vueSousTitre = "Demande d'amitié";
				
			// Ressource non disponible
			// ========================
			} else {
				response.sendError(404);

			} // Fin du branchement en fonction de la ressource demandée.
			
		}

		// Opérations post-traitement.
		postTraitement(request, response);

	}

	/**
	 * Permet de gérer les ressources POST suivantes :
	 * 		Aucune ressource.
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		// Opérations pré-traitement et suite des opérations, si nécessaire.
		if (preTraitement(request, response)) {
			
			// ================================
			// Gestion de la ressource demandée
			// ================================
	
			// Méthode HTTP non permise
			// ========================
			if (uri.equals("/membre/") || uri.equals("/membre")
					|| uri.equals("/membre/profil")	|| uri.equals("/membre/sugg-amis")
					|| uri.equals("/membre/supp-ami") || uri.equals("/membre/dem-ami")) {
				response.sendError(405);
				
			// Ressource non disponible
			// ========================
			} else {
				response.sendError(404);
	
			} // Fin du branchement en fonction de la ressource demandée.
			
		}

		// Opérations post-traitement.
		postTraitement(request, response);

	}
	
	/**
	 * Permet d'effectuer les traitements suite à la gestion de la ressource demandée.
	 * @param request La requête HTTP.
	 * @param response La réponse HTTP.
	 */
	protected void postTraitement(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Doit-on transférer le contrôle vers une vue ?
		if (this.vue != null) {
			// Doit-on conserver les informations pour la production d'une vue à partir du gabarit ?
			if (this.vueContenu != null || this.vueSousTitre != null) {
				// On conserve le chemin du fichier du fragment de la vue ainsi que le
				// sous-titre de la vue dans les attributs de la requête;
				// ces informations serviront à générer la vue à partir du gabarit.
				request.setAttribute("vueContenu", this.vueContenu);
				request.setAttribute("vueSousTitre", this.vueSousTitre);
			}
			// Transfert du contrôle de l'exécution à la vue.
			request.getRequestDispatcher(this.vue).forward(request, response);
		}		
	}	

}
