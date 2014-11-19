<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!-- Menu principal du site Web -->
<ul>
	<c:if test="${sessionScope['connBean'].modeConn == 'MEMBRE'}">
		<li><a href="${pageContext.request.contextPath}/membre">Accueil - Nouvelles</a></li>
		<li><a href="${pageContext.request.contextPath}/membre/profil">Profil - Babillard</a></li>
		<li><a href="${pageContext.request.contextPath}/membre/mes-amis">Amis et demandes d'amitié</a></li>
		<li><a href="${pageContext.request.contextPath}/membre/sugg-amis">Suggérer des amis</a></li>
	</c:if>
	<c:if test="${empty sessionScope['connBean'].modeConn || sessionScope['connBean'].modeConn == 'AUCUN'}">
		<li><a href="${pageContext.request.contextPath}/">M'inscrire</a></li>
	</c:if>
	<c:choose>
		<c:when test="${sessionScope['connBean'].modeConn == 'ADMIN'}">
			<li><a href="${pageContext.request.contextPath}/admin">Gestion des publications</a></li>			
		</c:when>
		<c:otherwise>
			<li><a href="${pageContext.request.contextPath}/rech-amis">Rechercher des amis</a></li>
		</c:otherwise>
	</c:choose>
</ul>
