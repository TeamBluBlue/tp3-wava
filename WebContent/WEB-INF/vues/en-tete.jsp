<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<img src="${pageContext.request.contextPath}/images/logo-twitface-tf.png" id="logo-twitface-tf" alt="Logo twitface" />
<h1 class="equivalent-image">twitface</h1>
<img src="${pageContext.request.contextPath}/images/oiseau-twitter.png" id="oiseau-twitface" alt="Oiseau twitface" />

<div id="connexion">
	<c:choose>
		<c:when test="${empty sessionScope['connBean']}">
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
	
				<%-- Champ caché pour indiquer une tentative de connexion à partir de la recherche d'amis --%>
				<input type="hidden" name="source" value="rech-amis" />
	
				<%-- Affichage du message d'erreur, si nécessaire --%>
				<c:if test="${not empty requestScope.msgErrConn}">
					<p id="msg-err-conn">${requestScope.msgErrConn}</p>
				</c:if>
			</form>
		</div>  <!-- Fin de la division "form-connexion" -->
		</c:when>
		<c:otherwise>
			<%-- Utilisateur connecté; on affiche de l'information sur l'utilisateur --%>
			<%-- Affichage de la photo si c'est un membre (pas un administrateur) --%>
			<c:if test="${sessionScope['connBean'].modeConn == 'MEMBRE'}">	
				<fmt:formatNumber var="noFormate" value="${sessionScope['connBean'].noUtil}" pattern="000" />
				<img src="${pageContext.request.contextPath}/images/photos/membre-${noFormate}.jpg" id="photo-membre-conn" alt="Photo de ${sessionScope['connBean'].nom}" />
			</c:if>	
			<div id="info-util">
				<p>
					${sessionScope['connBean'].nom} (${sessionScope['connBean'].nomUtil})
				</p>
				<c:if test="${sessionScope['connBean'].modeConn == 'ADMIN'}">
					<p>Administrateur du site Web</p>
				</c:if>
				
				<p>
					<a href="${pageContext.request.contextPath}/deconnexion">Déconnexion</a>
				</p>
			</div>
		</c:otherwise>
	</c:choose>
</div>  <!-- Fin de la division "connexion" -->
