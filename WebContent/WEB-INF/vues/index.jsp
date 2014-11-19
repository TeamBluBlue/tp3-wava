<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!DOCTYPE html>
<!-- Syntaxe: HTML5 Polyglot, Conformité: HTML5 et XML (XHTML5) -->

<html xmlns="http://www.w3.org/1999/xhtml" lang="fr" xml:lang="fr">
<head>
	<meta charset="utf-8" />
	<title>twitface :: L'ultime réseau social pour les étudiants</title>

	<meta name="description" content="twitface, votre réseau social préféré, restez en contact avec vos amis et votre famille (la solution ultime pour les étudiants)" />
	<meta name="keywords" content="twitface, réseau social, réseau, social, contact, lien, ami, famille, parenté, étudiant" />
	<meta name="author" content="Stéphane Lapointe" />
	<meta name="author" content="Étudiants du cours 420-526-FX" />

	<link rel="icon" href="${pageContext.request.contextPath}/favicon.ico" />

	<link rel="stylesheet" href="${pageContext.request.contextPath}/css/reset.css" />
	<link rel="stylesheet" href="${pageContext.request.contextPath}/css/twitface-index-erreur.css" />
</head>
<body>
	<div id="page">

		<header>
			<img src="${pageContext.request.contextPath}/images/logo-twitface.png" id="logo-twitface" alt="Logo twitface" />
			<h1 class="equivalent-image">twitface</h1>
		</header>

		<div id="contenu">

			<%-- Affichage du message confirmation la déconnexion, si nécessaire --%>
			<c:if test="${not empty requestScope.msgConfDeconn}">
				<p id="msg-conf-deconn">${requestScope.msgConfDeconn}</p>
			</c:if>

			<section id="inscription">
				<h2>Inscription</h2>
				<div id="form-inscription">
					<%-- Formulaire d'inscription --%>
					<img src="${pageContext.request.contextPath}/images/en-construction.png" alt="Section en construction" />
				</div>  <!-- Fin de la division "form-inscription" -->
			</section>  <!-- Fin de la section "inscription" -->

			<section id="connexion">
				<h2>Connexion</h2>

				<div id="form-connexion">
					<!-- Formulaire de connexion -->
					<form method="post" action="${pageContext.request.contextPath}/connexion">
						<p>
							<label for="nom-util">Nom d'utilisateur : </label>
							<input type="text" name="nom-util" id="nom-util" value="${fn:trim(param['nom-util'])}" />
						</p>
						<p>
							<label for="mot-passe">Mot de passe : </label>
							<input type="password" name="mot-passe" id="mot-passe" />
						</p>
						<input type="image" id="img-soumettre-connexion" src="${pageContext.request.contextPath}/images/icone-connexion.png" alt="Se connecter" />

						<%-- Affichage du message d'erreur, si nécessaire --%>
						<c:if test="${not empty requestScope.msgErrConn}">
							<p id="msg-err-conn">${requestScope.msgErrConn}</p>
						</c:if>
					</form>
				</div>  <!-- Fin de la division "form-connexion" -->
			</section>  <!-- Fin de la section "connexion" -->

			<section id="rechercher-amis">
				<h2>Rechercher des amis</h2>

				<div id="form-rechercher-amis">
					<!-- Formulaire de recherche d'amis -->
					<form method="get" action="${pageContext.request.contextPath}/rech-amis">
						<p>
							<input type="text" name="nom-ami" id="nom-ami" /><input type="image" name="rech-amis" id="img-soumettre-rech-amis" src="${pageContext.request.contextPath}/images/icone-recherche.png" alt="Rechercher des amis" />
						</p>
					</form>
				</div>  <!-- Fin de la division "form-rechercher-amis" -->
			</section>  <!-- Fin de la section "rechercher-amis" -->

		</div>  <!-- Fin de la division "contenu" -->

		<footer>
			<c:import url="/WEB-INF/vues/pied-page.jsp" />
		</footer>

	</div>  <!-- Fin de la division "page" -->

</body>
</html>