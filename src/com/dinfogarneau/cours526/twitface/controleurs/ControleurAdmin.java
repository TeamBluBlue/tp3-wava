package com.dinfogarneau.cours526.twitface.controleurs;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dinfogarneau.cours526.twitface.classes.ConnexionMode;

/**
 * Contrôleur pour les ressources des administrateurs.
 * @author Éric Bonin
 * @author Charles-André Beaudry
 */
public class ControleurAdmin extends HttpServlet {
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
		
		// Récupération du mode de connexion dans la session utilisateur.
		// *** À MODIFIER (UTILISATION DU BEAN DE CONNEXION) ***
		ConnexionMode modeConn = (ConnexionMode) request.getSession().getAttribute("modeConn");

		// Contrôle d'accès à la section pour les clients.
		if (modeConn == null || modeConn != ConnexionMode.ADMIN) {
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
	 * 		"/admin/" ou "/admin"		:	Accueil pour les administrateurs
	 *		"/membre/supp-pub"			:	Supprimer une publication
	 * 
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		// Opérations pré-traitement et suite des opérations, si nécessaire.
		if (preTraitement(request, response)) {

			// ================================
			// Gestion de la ressource demandée
			// ================================

			// Accueil - Admins
			// =================
			if (uri.equals("/admin/") || uri.equals("/admin")) {
				// Paramètres pour la vue créée à partir du gabarit.
				vue = "/WEB-INF/vues/gabarit-vues.jsp";
				vueContenu = "/WEB-INF/vues/admin/accueil-admin.jsp";
				vueSousTitre = "Administration";

			// Accueil - Membres
			// =================	
			} else if (uri.equals("/admin/supp-pub")) {
				// Paramètres pour la vue créée à partir du gabarit.
				vue = "/WEB-INF/vues/gabarit-vues.jsp";
				vueContenu = "/WEB-INF/vues/admin/en-construction.jsp";
				vueSousTitre = "Administration";

				
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
			if (uri.equals("/admin/") || uri.equals("/admin")
					|| uri.equals("/admin/supp-pub")) {
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
