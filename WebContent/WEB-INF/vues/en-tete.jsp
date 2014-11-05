<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<img src="${pageContext.request.contextPath}/images/logo-twitface-tf.png" id="logo-twitface-tf" alt="Logo twitface" />
<h1 class="equivalent-image">twitface</h1>
<img src="${pageContext.request.contextPath}/images/oiseau-twitter.png" id="oiseau-twitface" alt="Oiseau twitface" />

<div id="connexion">

	<div id="form-connexion">
		<!-- Formulaire de connexion -->
		<form method="post" action="/connexion">
			<p>
				<label for="nom-util">Nom d'utilisateur : </label>
				<input type="text" name="nom-util" id="nom-util" />
			</p>
			<p>
				<label for="mot-passe">Mot de passe : </label>
				<input type="password" name="mot-passe" id="mot-passe" />
			</p>
			<input type="image" id="img-soumettre-connexion" src="${pageContext.request.contextPath}/images/icone-connexion.png" alt="Se connecter" />

			<%-- Champ caché pour indiquer une tentative de connexion à partir de la recherche d'amis --%>
			<input type="hidden" name="source" value="rech-amis" />

			<%-- Affichage du message d'erreur, si nécessaire --%>
			<p id="msg-err-conn">MESSAGE D'ERREUR, SI NÉCESSAIRE SEULEMENT</p>
		</form>
	</div>  <!-- Fin de la division "form-connexion" -->


	<%-- Utilisateur connecté; on affiche de l'information sur l'utilisateur --%>
	<c:if test="${not empty sessionScope['modeConn'] && sessionScope['modeConn'] != 'AUCUN'}">
		<%-- Affichage de la photo si c'est un membre (pas un administrateur) --%>
		<fmt:formatNumber var="noFormate" value="${sessionScope['noUtil']}" pattern="000" />
		<img src="${pageContext.request.contextPath}/images/photos/membre-${noFormate}.jpg" id="photo-membre-conn" alt="Photo de ${sessionScope['nom']}" />
			
		<div id="info-util">
			<p>
				${sessionScope['nom']} (${sessionScope['nomUtil']})
			</p>
			<p>
				<a href="${pageContext.request.contextPath}/deconnexion">Déconnexion</a>
			</p>
		</div>

	</c:if>  <%-- Fin de utilisateur connecté --%>


</div>  <!-- Fin de la division "connexion" -->
