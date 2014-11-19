<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<!-- Syntaxe: HTML5 Polyglot, Conformité: HTML5 et XML (XHTML5) -->

<html xmlns="http://www.w3.org/1999/xhtml" lang="fr" xml:lang="fr">
<head>
	<meta charset="utf-8" />
	<title>twitface :: Erreur :: Méthode HTTP non permise (HTTP 405)</title>

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
			<img src="${pageContext.request.contextPath}/images/logo-twitface-erreur.png" id="logo-twitface" alt="Logo twitface (erreur)" />
			<h1 class="equivalent-image">twitface</h1>
		</header>

		<section id="contenu">
			<h2>Erreur</h2>

			<p id="msg-err-http-et-exception">Méthode HTTP non permise (HTTP 405)</p>

			<%-- Affichage d'un lien approprié en fonction du mode de connection --%>
			<c:choose>
				<c:when test="${sessionScope['connBean'].modeConn == 'MEMBRE'}">
					<a href="${pageContext.request.contextPath}/membre">Retour à votre page personnelle</a>
				</c:when>
				<c:when test="${sessionScope['connBean'].modeConn == 'ADMIN'}">
					<a href="${pageContext.request.contextPath}/admin">Retour à la page d'administration du site Web</a>
				</c:when>
				<c:otherwise>
					<a href="${pageContext.request.contextPath}/">Retour à l'accueil</a>
				</c:otherwise>
			</c:choose>
		</section>

		<footer>
			<c:import url="/WEB-INF/vues/pied-page.jsp" />
		</footer>

	</div>  <!-- Fin de la division "page" -->

</body>
</html>