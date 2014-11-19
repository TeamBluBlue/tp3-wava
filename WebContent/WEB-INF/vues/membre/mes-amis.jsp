<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/sql" prefix="sql" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<h2>Vos demandes d'amitié</h2>
<c:choose>
	<c:when test="${requestScope.modAcceptDemAmi != null}">
		<%--Définition de l'id du message à afficher (confirmation ou erreur) --%>
		<p id="msg-
			<c:choose><c:when test="${requestScope.modAcceptDemAmi.demandeAcceptee}">
				conf
			</c:when><c:otherwise>
				err
			</c:otherwise></c:choose>
		-accept-dem-ami">
			${requestScope.modAcceptDemAmi.message}
		</p>
	</c:when>
</c:choose>
<sql:query var="resDemandesAmitie" dataSource="jdbc/twitface">
	SELECT MemNo, MemNom, MemSexe, DemAmiDate
	FROM membres
	INNER JOIN demandes_amis ON membres.MemNo = demandes_amis.MemNoDemandeur
	WHERE MemNoInvite=?
	ORDER BY MemNom
	<sql:param value="${sessionScope['connBean'].noUtil}" />
</sql:query>

<c:choose>
	<c:when test="${empty resDemandesAmitie.rows}">
		<p>Aucune demande d'amitié</p>
	</c:when>
	
	<c:otherwise>
		<ul id="lst-Demandes" class="lst-membres">
			<c:forEach var="da" items="${resDemandesAmitie.rows}">
				<li>
					<fmt:formatNumber var="noFormate" value="${da.MemNo}" pattern="000" />
					<div class="div-img">
						<img src="${pageContext.request.contextPath}/images/photos/membre-${noFormate}.jpg" class="photo-membre" alt="Photo de ${da.MemNom}" />
					</div>
					<p class="nom-ami">${da.MemNom}</p>
					<p>
						Demande: ${da.DemAmiDate }
					</p>
					<p>
						<a href="${pageContext.request.contextPath}/membre/accept-dem-ami?no-ami=${da.MemNo}">Ajouter comme ami<c:if test="${da.MemSexe == 'F'}">e</c:if></a>
					</p>
				</li>
			</c:forEach>
		</ul>
	</c:otherwise>
</c:choose>

<h2>Vos amis</h2>
<sql:query var="resAmis" dataSource="jdbc/twitface">
	SELECT MemNo, MemNom, MemSexe
	FROM membres
	WHERE MemNo IN
	(
		SELECT MemNo1 FROM amis WHERE MemNo2=? UNION
		SELECT MemNo2 FROM amis WHERE MemNo1=?
	)
	ORDER BY MemNom ASC
	<sql:param value="${sessionScope['connBean'].noUtil}" />
	<sql:param value="${sessionScope['connBean'].noUtil}" />
</sql:query>
<c:choose>
	<c:when test="${empty resAmis.rows}">
		<p>Aucun ami</p>
	</c:when>
	
	<c:otherwise>
		<ul id="lst-Amis" class="lst-membres">
			<c:forEach var="ami" items="${resAmis.rows}">
				<li>
					<fmt:formatNumber var="noFormate" value="${ami.MemNo}" pattern="000" />
					<div class="div-img">
						<img src="${pageContext.request.contextPath}/images/photos/membre-${noFormate}.jpg" class="photo-membre" alt="Photo de ${ami.MemNom}" />
					</div>
					<p class="nom-ami">${ami.MemNom}</p>
					<p>
						<a href="${pageContext.request.contextPath}/membre/supp-ami?no-ami=${ami.MemNo}">Supprimer l'ami<c:choose><c:when test="${ami.MemSexe == 'F'}">e</c:when></c:choose></a>
					</p>
				</li>
			</c:forEach>
		</ul>
	</c:otherwise>
</c:choose>