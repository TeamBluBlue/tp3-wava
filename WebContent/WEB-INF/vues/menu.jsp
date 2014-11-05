<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!-- Menu principal du site Web -->
<ul>
	<c:if test="${sessionScope['modeConn'] == 'MEMBRE'}">
		<li><a href="${pageContext.request.contextPath}/membre">Accueil - Nouvelles</a></li>
		<li><a href="${pageContext.request.contextPath}/membre/profil">Profil - Babillard</a></li>
		<li><a href="${pageContext.request.contextPath}/membre/sugg-amis">Suggérer des amis</a></li>
	</c:if>
	<c:if test="${empty sessionScope['modeConn'] || sessionScope['modeConn'] == 'AUCUN'}">
		<li><a href="${pageContext.request.contextPath}/">M'inscrire</a></li>
	</c:if>
	
	<li><a href="${pageContext.request.contextPath}/rech-amis">Rechercher des amis</a></li>
</ul>
