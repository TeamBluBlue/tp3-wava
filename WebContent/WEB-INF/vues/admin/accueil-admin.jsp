<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/sql" prefix="sql" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<section>
	<h2>Liste des publications</h2>
	
	<%-- Requête SQL pour les publications sur le babillard du membre --%>
	<sql:query var="resInfoPub" dataSource="jdbc/twitface">
		SELECT p.*, m.*
		FROM publications p
		INNER JOIN
		membres m
		ON
		p.MemNoCreateur = m.MemNo
		ORDER BY PubDate DESC
	</sql:query>
	
	<c:choose>
		<c:when test="${resInfoPub.rowCount == 0}">
			<p>Aucune publication sur twitface</p>
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
						<p>
							<a class="lien-supp" href="${pageContext.request.contextPath}/admin/supp-pub?no-pub=${pub.PubNo}"><img src="${pageContext.request.contextPath}/images/detruire.png" alt="Supprimer la publication" /></a>
						</p>
					</li>
				</c:forEach>
			</ul>
		</c:otherwise>
	</c:choose>
</section>
