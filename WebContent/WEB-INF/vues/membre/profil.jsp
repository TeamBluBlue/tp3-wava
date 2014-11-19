<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/sql" prefix="sql" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<section>
	<h2>Votre profil</h2>
	
	<%-- Requête SQL pour les informations du membre --%>
	<sql:query var="resInfoMem" dataSource="jdbc/twitface">
		SELECT *
		FROM membres
		WHERE MemNo = ?
		<sql:param value="${sessionScope['connBean'].noUtil}" />
	</sql:query>
	
	<%-- Affichage des informations du membre --%>
	<c:forEach var="infoMem" items="${resInfoMem.rows}" end="0">
		<p>
			${infoMem.MemNom}
			<c:choose>
				<c:when test="${infoMem.MemSexe == 'M'}">
					(homme)
				</c:when>
				<c:when test="${infoMem.MemSexe == 'F'}">
					(femme)
				</c:when>
			</c:choose>
		</p>
		<p>Date de naissance : ${infoMem.MemDateNaissance}</p>
		<p>Ville d'origine : ${infoMem.MemVilleOrigine}</p>
		<p>Ville actuelle : ${infoMem.MemVilleActuelle}</p>
		<p><a href="mailto:${infoMem.MemCourriel}">${infoMem.MemCourriel}</a></p>
	</c:forEach>
</section>

<section>
	<h2>Votre babillard</h2>
	
	<%-- Requête SQL pour les publications sur le babillard du membre --%>
	<sql:query var="resInfoPub" dataSource="jdbc/twitface">
		SELECT p.*, m.*
		FROM publications p
		INNER JOIN
		membres m
		ON
		p.MemNoCreateur = m.MemNo
		WHERE MemNoBabillard = ?
		ORDER BY PubDate DESC
		<sql:param value="${sessionScope['connBean'].noUtil}" />
	</sql:query>
	
	<c:choose>
		<c:when test="${resInfoPub.rowCount == 0}">
			<p>Aucune publication sur votre babillard</p>
		</c:when>
		<c:otherwise>
			<ul id="lst-pub-babillard">
				<%-- Parcours et affichage des publications --%>
				<c:forEach var="pub" items="${resInfoPub.rows}">
					<li>
						<%-- Affichage de la photo du membre qui a ajouté la publication --%>
						<fmt:formatNumber var="noFormate" value="${pub.MemNo}" pattern="000" />
						<img src="${pageContext.request.contextPath}/images/photos/membre-${noFormate}.jpg" class="photo-membre" alt="Photo de ${pub.MemNom}" />
						<p>Publié par ${pub.MemNom}</p>
						<p>${pub.PubDate}</p>
						<p>${pub.PubTexte}</p>
					</li>
				</c:forEach>
			</ul>
		</c:otherwise>
	</c:choose>
</section>
