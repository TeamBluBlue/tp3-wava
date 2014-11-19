package com.dinfogarneau.cours526.twitface.controleurs;

import java.io.IOException;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.naming.NamingException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dinfogarneau.cours526.twitface.classes.ConnexionMode;
import com.dinfogarneau.cours526.twitface.modeles.ModeleConnexion;

/**
 * Contrôleur général pour les ressources publiques.
 * @author Stéphane Lapointe
 * @author Éric Bonin
 * @author Charles-André Beaudry
 */
public class ControleurGeneral extends HttpServlet {
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
	 */
	protected void preTraitement(HttpServletRequest request, HttpServletResponse response) {
		// Récupération de l'URI sans le context path.
		this.uri = request.getRequestURI().substring(request.getContextPath().length());
		this.vue = null;
		this.vueContenu = null;
		this.vueSousTitre = null;		
	}

	/**
	 * Permet de gérer les ressources GET suivantes :
	 * 		"/" ou ""		:	Accueil
	 * 		"/deconnexion"	:	Déconnexion
	 * 		"/rech-amis"	:	Recherche d'amis
	 * 
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		// Opérations pré-traitement.
		preTraitement(request, response);

		// ================================
		// Gestion de la ressource demandée
		// ================================

		// Accueil
		// =======
		if (uri.equals("/") || uri.equals("")) {
			// Si on demande la page d'accueil, il n'y a rien à faire.
			// On affiche le vue "index.jsp".
			vue = "/WEB-INF/vues/index.jsp";

		// Rechercher des amis
		// ===================
		} else if (uri.equals("/rech-amis")) {

			// *******************
			// *** À COMPLÉTER ***
			// *******************

			// Paramètres pour la vue créée à partir du gabarit.
			vue = "/WEB-INF/vues/gabarit-vues.jsp";
			vueContenu = "/WEB-INF/vues/general/rech-amis.jsp";
			vueSousTitre = "Rechercher des amis";

		// Méthode HTTP non permise
		// ========================
		} else if (uri.equals("/connexion")) {
			// On retourne immédiatement le code d'erreur HTTP 405;
			// la réponse sera interceptée par la page d'erreur "erreur-405.jsp".
			response.sendError(405);
			
		// Ressource non disponible
		// ========================
		} else {
			// On retourne immédiatement le code d'erreur HTTP 404;
			// la réponse sera interceptée par la page d'erreur "erreur-404.jsp".
			response.sendError(404);

		} // Fin du branchement en fonction de la ressource demandée.

		// Opérations post-traitement.
		postTraitement(request, response);
	}
	
	/**
	 * Permet de gérer les ressources POST suivantes :
	 * 		"/connexion"	:	Connexion
	 * 
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		// Opérations pré-traitement.
		preTraitement(request, response);

		// ================================
		// Gestion de la ressource demandée
		// ================================

		// Connexion
		// =========
		if (this.uri.equals("/connexion")) {
			String nomUtil = request.getParameter("nom-util");
			String mdp = request.getParameter("mot-passe");
			
			ModeleConnexion modConnexion = new ModeleConnexion();
			
			try{
				modConnexion.connecterUtilisateur(nomUtil, mdp);
			}
			catch(NamingException | SQLException e){
				throw new ServletException(e);
			}
			
			// Si une erreur s'est produite durant la connexion,
			// afficher la page d'accueil
			if (modConnexion.erreurConnexion()){
				request.setAttribute("msgErrConn", modConnexion.getMsgErreur());
				String source = (String)request.getParameter("source");
				
				if (source != null && source.contains("rech-amis")){
					this.vue = "/WEB-INF/vues/gabarit-vues.jsp";
					this.vueContenu = "/WEB-INF/vues/general/rech-amis.jsp";
					this.vueSousTitre = "Rechercher des amis";
				}
				else{
					this.vue = "/WEB-INF/vues/index.jsp";
				}
			}
			else{
				// Si l'utilisateur est connecté, rediriger vers la bonne section
				request.getSession().setAttribute("connBean", modConnexion.getUtilisateurConnecte());
				
				if (modConnexion.getUtilisateurConnecte().getModeConn() == ConnexionMode.ADMIN)
					response.sendRedirect("admin");
				else{
					response.sendRedirect("membre");
				}
			}

		// Méthode HTTP non permise
		// ========================
		} else if (uri.equals("/") || uri.equals("") || uri.equals("/rech-amis") ) {
			// On retourne immédiatement le code d'erreur HTTP 405;
			// la réponse sera interceptée par la page d'erreur "erreur-405.jsp".
			response.sendError(405);

		// Ressource non disponible
		// ========================
		} else {
			// On retourne immédiatement le code d'erreur HTTP 404;
			// la réponse sera interceptée par la page d'erreur "erreur-404.jsp".
			response.sendError(404);

		} // Fin du branchement en fonction de la ressource demandée.

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
